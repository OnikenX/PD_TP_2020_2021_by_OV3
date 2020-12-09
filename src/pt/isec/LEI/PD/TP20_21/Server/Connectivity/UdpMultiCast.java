package pt.isec.LEI.PD.TP20_21.Server.Connectivity;

import pt.isec.LEI.PD.TP20_21.Server.Model.Server;
import pt.isec.LEI.PD.TP20_21.shared.Mensagens;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.Objects;

import static pt.isec.LEI.PD.TP20_21.shared.Utils.bytesToObject;
import static pt.isec.LEI.PD.TP20_21.shared.Utils.objectToBytes;


 public  class UdpMultiCast extends Thread {

    //    protected String username;
    protected MulticastSocket multicastSocket;
    protected boolean running;
    protected Server server;
    int port;

    public UdpMultiCast(Server server) throws IOException {
        this.multicastSocket = new MulticastSocket(Utils.Consts.UDP_MULTICAST_PORT);
        multicastSocket.joinGroup(InetAddress.getByName(Utils.Consts.UDP_MULTICAST_GROUP));

        this.server = server;
        this.running = false;
        setDaemon(true);

        start();
    }

    public synchronized void terminate() {
        running = false;
    }

    //recebe uma mensagem multicast
    @Override
    public void run() {
        Class<?> classType = null;


        DatagramPacket packet; //para receber os pedidos e enviar as respostas
        if (multicastSocket == null || !running)
            return;

        if (Utils.Consts.DEBUG)
            System.out.println("UdpMultiCast activado iniciado...");
        try {
            Mensagens.Ping ping = new Mensagens.Ping();
            while (running) {
                ping.setLotacao(server.getTcpConnections_size());
                byte[] pingBytes = objectToBytes(ping);
                packet = new DatagramPacket(pingBytes, 0, pingBytes.length);
                multicastSocket.receive(packet);
                Thread.sleep(Utils.Consts.PING_INTERVAL);
            }
        } catch (NumberFormatException e) {
            System.err.println("O porto de escuta deve ser um inteiro positivo.");
        } catch (
                SocketException e) {
            System.err.println("Ocorreu um erro ao nivel do socket UDP:\n\t" + e);
        } catch (
                IOException e) {
            System.err.println("Ocorreu um erro no acesso ao socket:\n\t" + e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (multicastSocket != null) {
                multicastSocket.close();
            }
        }

    }


    //enviar mensagem

    /**
     * Envia uma mensagem por mulClass<?>ticast
     *
     * @param mensagem       a enviar
     * @param recebeResposta se deve esperar por uma resposta ou não, returna null se nao receber uma.
     */
    synchronized public Object enviaMulticast(Object mensagem, boolean recebeResposta) {
        //TODO: nao sei o que isto faz mas pode ser preciso mais tarde
        return null;
    }
}