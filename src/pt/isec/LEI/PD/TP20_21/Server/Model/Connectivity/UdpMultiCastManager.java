package pt.isec.LEI.PD.TP20_21.Server.Model.Connectivity;

import pt.isec.LEI.PD.TP20_21.Server.Model.Server;
import pt.isec.LEI.PD.TP20_21.shared.IpPort;
import pt.isec.LEI.PD.TP20_21.shared.Pedido;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.LinkedList;

import static pt.isec.LEI.PD.TP20_21.shared.Utils.Consts.*;
import static pt.isec.LEI.PD.TP20_21.shared.Utils.bytesToObject;
import static pt.isec.LEI.PD.TP20_21.shared.Utils.getTimeStamp;


/**
 * Trata das mensagens entre servidores
 */
public class UdpMultiCastManager extends Thread {
    private Servidores servidores;
    //    protected String username;
    protected MulticastSocket multicastSocket;
    protected Server server;
    int port;
    PingSender ping;
    VerificaServers verificaServers;


    public UdpMultiCastManager(Server server) throws IOException {
        servidores = new Servidores();
        this.multicastSocket = new MulticastSocket();
        multicastSocket.joinGroup(InetAddress.getByName(Utils.Consts.UDP_MULTICAST_GROUP));

        this.server = server;
        setDaemon(true);

        //threads
        start();
        ping = new PingSender();
        verificaServers = new VerificaServers();
    }


    public Servidores getServidores() {
        return servidores;
    }

    //recebe uma mensagem multicast
    @Override
    public void run() {
        super.run();
        Object mensagem;
        Class<?> classType = null;
        DatagramPacket packet;
        if (Utils.Consts.DEBUG)
            System.out.println("UdpMultiCast receiver iniciado...");
        try {

            while (true) {
                packet = new DatagramPacket(new byte[Utils.Consts.MAX_SIZE_PER_PACKET], Utils.Consts.MAX_SIZE_PER_PACKET);
                multicastSocket.receive(packet);
                if (DEBUG)
                    System.out.println("Mensagem recebida por " + packet.getAddress() + ":" + packet.getPort() + " com tamanho de " + packet.getLength() + " bytes");
                mensagem = bytesToObject(packet.getData());
                if (mensagem == null) {
                    if (DEBUG)
                        System.out.println("Mensagem receb");
                    continue;
                }
                classType = mensagem.getClass();
                if (classType == FicheiroSender.class) {
                    //TODO: file receiver
                } else if (classType == Pedido.Ping.class) {
                    Pedido.Ping ping = (Pedido.Ping) mensagem;
                    if(Utils.Consts.DEBUG)
                        System.out.println("[Ping] recebido...");
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("O porto de escuta deve ser um inteiro positivo.");
        } catch (SocketException e) {
            System.err.println("Ocorreu um erro alive templateo nivel do socket UDP:\n\t" + e);
        } catch (IOException e) {
            System.err.println("Ocorreu um erro no acesso ao socket:\n\t" + e);
        } finally {
            if (multicastSocket != null) {
                multicastSocket.close();
            }
        }

    }


    /**
     * Envia uma mensagem por multicast
     *
     * @param mensagem       a enviar
     * @param recebeResposta se deve esperar por uma resposta ou não, returna null se nao receber uma.
     */
    synchronized public Object enviaMulticast(Object mensagem, boolean recebeResposta) {
        //TODO: nao sei o que isto faz mas pode ser preciso mais tarde
        return null;
    }

    /**
     * @param halfedConnections da a condição minima
     * @return se o servidor pode receber novos clientes
     */
    public boolean verifyServerAvailability(int halfedConnections) {
        for (var s : getServidores()) {
            if (s.getLotacao() < halfedConnections)
                return false;
        }
        return true;
    }

    /**
     * Verificação se os servidores estão activos.
     * <p>
     * Esta é uma thread auto enicializada e do tipo daemon.
     */
    private class VerificaServers extends Thread {
        public VerificaServers() {
            this.setDaemon(true);
            this.start();
        }

        @Override
        public void run() {
            while (true) {
                servidores.removeTimedOut();
                try {
                    sleep(SERVER_VERIFY_SERVERS_TIMER * 1000);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    /**
     * Pinga os outros servidor para dizer que está activo, começa automaticamente
     */
    private class PingSender extends Thread {
        public PingSender() throws IOException {
            setDaemon(true);
            start();
        }

        @Override
        public void run() {
            if (Utils.Consts.DEBUG)
                System.out.println("UdpMultiCast activado iniciado...");

            Pedido.Ping ping = new Pedido.Ping();
            while (true) {
                ping.setLotacao(server.getTcpConnections_size());
                enviaMulticast((Object) ping, false);
                if (DEBUG)
                    System.out.println("[Ping] enviado...");
                try {
                    Thread.sleep(Utils.Consts.PING_INTERVAL * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public LinkedList<IpPort> getServidoresForClient() {
        return servidores.getServidoresForClient();
    }

    /**
     * Guarda a lista de servidores para o Server
     */
    private class Servidores extends LinkedList<Servidores.ServidorExterno> implements Serializable {

        public void removeTimedOut() {
            var it = this.listIterator();
            while (it.hasNext()) {
                if ((Utils.getTimeStamp() - it.next().getActualizado()) > TIMEOUT_PINGS)
                    it.remove();
                it.next();
            }
        }

        public LinkedList<IpPort> getServidoresForClient() {
            LinkedList<IpPort> toReturn = new LinkedList<>();
            int max;
            this.sort(ServidorExterno::compareTo);
            max = Math.min(size(), 300);
            for (int i = 0; i < max; ++i)
                toReturn.add(this.get(i));
            return toReturn;
        }


        /**
         * Guarda as informações de um servidor externo
         */
        public class ServidorExterno extends IpPort implements Comparable<ServidorExterno>, Serializable, Cloneable {

            private int lotacao;

            /**
             * ultima vez que o server foi actualizado
             */
            private long actualizado;

            ServidorExterno(String ip, int port, int lotacao) {
                super(ip, port);
                this.setLotacao(lotacao);
                setActualizado();
            }

            public IpPort getForClient() {
                try {
                    return (IpPort) super.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                return null;
            }


            public int getLotacao() {
                return lotacao;
            }

            public void setLotacao(int lotacao) {
                if ((lotacao >= 0) && (lotacao <= MAX_LOTACAO))
                    this.lotacao = lotacao;
                setActualizado();
            }

            public long getActualizado() {
                return actualizado;
            }

            public void setActualizado() {
                this.actualizado = getTimeStamp();
            }

            /**
             * @param ipss do tipo {@link ServidorExterno}
             * @return diferença de locação
             */
            @Override
            public int compareTo(ServidorExterno ipss) {
                return lotacao - ipss.getLotacao();
            }
        }
    }

    private class FicheiroSender {

    }
}
