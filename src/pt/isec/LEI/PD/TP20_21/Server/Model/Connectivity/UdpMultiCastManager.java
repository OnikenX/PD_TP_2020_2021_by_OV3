package pt.isec.LEI.PD.TP20_21.Server.Model.Connectivity;

import pt.isec.LEI.PD.TP20_21.Server.Model.Server;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos.PingPai;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Respostas.FileOutOfSync;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.MulticastPacket;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos.Ping;
import pt.isec.LEI.PD.TP20_21.shared.FileTransfer.FilePacket;
import pt.isec.LEI.PD.TP20_21.shared.IpPort;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedido;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.*;

import static pt.isec.LEI.PD.TP20_21.shared.Utils.Consts.*;
import static pt.isec.LEI.PD.TP20_21.shared.Utils.*;


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
    final int serverMulticastId;
    VerificaServers verificaServers;
    Collection<FicheiroSender> fileSenders;
    Collection<FicheiroReceiver> fileReceivers;
    MulticastPacket multicastPacketToSend;
    public final long serverStartTimestamp = System.nanoTime();

    public UdpMultiCastManager(Server server) throws IOException {
        fileSenders = Collections.synchronizedCollection(new LinkedList<FicheiroSender>());
        fileReceivers = Collections.synchronizedCollection(new LinkedList<FicheiroReceiver>());
        servidores = new Servidores();
        serverMulticastId = new Random(serverStartTimestamp).nextInt();
        multicastPacketToSend = new MulticastPacket(serverMulticastId);
        this.multicastSocket = new MulticastSocket(UDP_MULTICAST_PORT);
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
        MulticastSocket multicastSocketReceiver = null;
        Object mensagem = null;
        MulticastPacket multicastPacket = null;
        Class<?> classType = null;
        DatagramPacket packet;
        try {
            multicastSocketReceiver = new MulticastSocket(UDP_MULTICAST_PORT);
            multicastSocketReceiver.joinGroup(InetAddress.getByName(Utils.Consts.UDP_MULTICAST_GROUP));
            if (Utils.Consts.DEBUG)
                System.out.println("UdpMultiCast receiver iniciado...");
            byte[] bytes = new byte[Utils.Consts.MAX_SIZE_PER_PACKET];
            while (true) {
                packet = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(UDP_MULTICAST_GROUP), UDP_MULTICAST_PORT);
                multicastSocketReceiver.receive(packet);
                if (DEBUG)
                    System.out.println("Mensagem recebida por " + packet.getAddress() + ":" + packet.getPort() + ": server_"+server.server_number+ ": com tamanho de " + packet.getLength() + " bytes");
                multicastPacket =(MulticastPacket) bytesToObject(packet.getData());
                if(multicastPacket.getMulticastId() == serverMulticastId){
                    if(DEBUG)
                        System.out.println("E do mesmo servidor...");
                    continue;
                }else{
                    if(DEBUG)
                        System.out.println("sao diferentes servidores");
                }
                mensagem = multicastPacket.getData();
                if (mensagem == null) {
                    if (DEBUG)
                        System.out.println("Mensagem corrupta recebida...");
                    continue;
                }
                classType = mensagem.getClass();
                if (classType == FilePacket.class) {
                    FilePacket m = (FilePacket) mensagem;
                    for (var i : fileSenders) {
                        if (i.fileId == m.fileId) {
                            i.getPipeOut().write(m.getContent());
                            break;
                        }
                    }
                } else if (classType == Ping.class) {
                    Ping ping = (Ping) mensagem;
                    if (Utils.Consts.DEBUG)
                        System.out.println("[Ping] recebido ... ; locacao: " + ping.getLotacao());

                    servidores.verifyPing((Pedido.Ping)mensagem, packet);
                } else {
                    System.err.println("Undefined object detected");
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("O porto de escuta deve ser um inteiro positivo.");
        } catch (SocketException e) {
            System.err.println("Ocorreu um erro alive templateo nivel do socket UDP:\n\t" + e);
        } catch (IOException e) {
            System.err.println("Ocorreu um erro no acesso ao socket:\n\t" + e);
        } finally {
            if (multicastSocketReceiver != null) {
                multicastSocketReceiver.close();
            }
        }
    }


    /**
     * Envia uma mensagem por multicast
     *
     * @param mensagem       a enviar
     * @param recebeResposta se deve esperar por uma resposta ou não, returna null se nao receber uma.
     * @throws Exception se ouve problemas com o objeto
     */
    synchronized public void enviaMulticast(Object mensagem, boolean recebeResposta) throws Exception {
        InetAddress group = InetAddress.getByName(UDP_MULTICAST_GROUP);
        multicastPacketToSend.setData(mensagem);
        var buf = objectToBytes(multicastPacketToSend);
        if (buf == null) {
            throw new Exception("Class com problemas.");
        }
        var packet = new DatagramPacket(buf, buf.length, group, UDP_MULTICAST_PORT);
        multicastSocket.send(packet);
        //TODO: nao sei o que isto faz mas pode ser preciso mais tarde
//        return null;
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
                } catch (InterruptedException ignored) {}
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

            Ping ping = new Ping();
            while (true) {
                try {
                    ping = new Pedido.Ping(server.getTcpConnections_size(),server.getServerData().getChecksum("canais") ,server.getServerData().getChecksum("canaisDM"),server.getServerData().getChecksum("canaisGrupo"),server.getServerData().getChecksum("mensagens"),server.getServerData().getChecksum("utilizadores"), server.getServerData().isUpdated());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    if(DEBUG)
                        System.err.println("erro no criar um ping");
                    continue;
                }
                try {
                    enviaMulticast(ping, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
    private class Servidores implements Serializable, Iterable<Servidores.ServidorExterno> {
        List<ServidorExterno> servidoresList;

        Servidores(){
            servidoresList = Collections.synchronizedList(new LinkedList<ServidorExterno>());
        }

        public void removeTimedOut() {
            var it = this.iterator();
            while (it.hasNext()) {
                it.next();
                if ((Utils.getTimeStamp() - it.next().getActualizado()) > TIMEOUT_PINGS)
                    it.remove();
            }
        }

        public LinkedList<IpPort> getServidoresForClient() {
            LinkedList<IpPort> toReturn = new LinkedList<>();
            int max;

            Collections.sort(servidoresList);
            max = Math.min(servidoresList.size(), 300);
            for (int i = 0; i < max; ++i)
                toReturn.add(servidoresList.get(i));
            return toReturn;
        }

        public ServidorExterno get(int index) {
            return servidoresList.get(index);
        }


        @Override
        public Iterator<ServidorExterno> iterator() {
            return servidoresList.iterator();
        }

        //TODO: completar a verificicaçao de ping
        public void verifyPing(Ping mensagem, DatagramPacket packet) {
            if(mensagem instanceof PingPai){
                //verifica checksums
            }
            try {
                if(mensagem.)
                if (mensagem.getCanaisChecksum() != server.getServerData().getChecksum(server.table_canais))

                  if(mensagem.getMensagensChecksum() != server.getServerData().getChecksum(server.table_mensagens)

                )


            }catch(Exception e){
                e.printStackTrace();
            }

//            new ServidorExterno(packet.getAddress(), packet.getPort(), ((Pedido.Ping)mensagem).getLotacao());
            for (var i : this)
                if (i.equals(servidorExterno)) {
                    i.setLotacao(servidorExterno.getLotacao());
                    return true;
                }
            return servidoresList.add(servidorExterno);
        }


        /**
         * Guarda as informações de um servidor externo
         */
        public class ServidorExterno extends IpPort implements Comparable<ServidorExterno>, Serializable, Cloneable {

            private int lotacao;
            private long canaisChecksum;
            private long canaisDMChecksum;
            private long canaisGroupoChecksum;
            private long mensagensChecksum;
            private long utilizadoresChecksum;

            /**
             * ultima vez que o server foi actualizado
             */
            private long actualizado;

            ServidorExterno(String ip, int port, int lotacao) {
                super(ip, port);
                this.lotacao = lotacao;
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
                if (lotacao >= 0)
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



            public long getCanaisChecksum() {
                return canaisChecksum;
            }

            public void setCanaisChecksum(long canaisChecksum) {
                this.canaisChecksum = canaisChecksum;
            }

            public long getCanaisDMChecksum() {
                return canaisDMChecksum;
            }

            public void setCanaisDMChecksum(long canaisDMChecksum) {
                this.canaisDMChecksum = canaisDMChecksum;
            }

            public long getCanaisGroupoChecksum() {
                return canaisGroupoChecksum;
            }

            public void setCanaisGroupoChecksum(long canaisGroupoChecksum) {
                this.canaisGroupoChecksum = canaisGroupoChecksum;
            }

            public long getMensagensChecksum() {
                return mensagensChecksum;
            }

            public void setMensagensChecksum(long mensagensChecksum) {
                this.mensagensChecksum = mensagensChecksum;
            }

            public long getUtilizadoresChecksum() {
                return utilizadoresChecksum;
            }

            public void setUtilizadoresChecksum(long utilizadoresChecksum) {
                this.utilizadoresChecksum = utilizadoresChecksum;
            }

            public void setActualizado(long actualizado) {
                this.actualizado = actualizado;
            }

        }

    }
    //TODO: testar a transferencia de ficheiros
    /**
     * Recebe fichieros por udp
     */
    public class FicheiroReceiver extends Thread {
        public final int fileId;
        final PipedOutputStream pipeOut;
        final PipedInputStream pipeIn;
        FileOutputStream file;
        long read = 0;
        FilePacket.FileType fileType;
        long lastUpdate;
        String filename;
        private Thread killer;
        private FicheiroReceiver mypointer = this;

        public FicheiroReceiver(int fileId, FilePacket.FileType fileType) throws IOException {
            this.fileId = fileId;
            this.fileType = fileType;
            this.lastUpdate = System.currentTimeMillis();
            String filedir = server.getServerName() + File.separator + fileType;
            new File(filedir).mkdirs();
            filename = filedir + File.separator + fileId;
            file = new FileOutputStream(filename);
            this.pipeIn = new PipedInputStream();
            this.pipeOut = new PipedOutputStream(pipeIn);
            start();
            //thread que se auto mata quando nao recebe mensagem depois de 1 minuto e apaga o ficheiro se for preciso
            new Thread(() -> {
                try {
                    sleep(60000);
                    if (!mypointer.isAlive()) {
                        interrupt();
                    }
                    if (lastUpdate + 60000 > System.nanoTime()) {
                        file.close();
                        new File(filename).delete();
                        pipeOut.close();
                        pipeIn.close();
                        fileReceivers.remove(this);
                        interrupt();
                    }
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }).start();

        }


        synchronized public PipedOutputStream getPipeIn() {
            synchronized (pipeIn) {
                pipeIn.notify();
                return pipeOut;
            }
        }

        @Override
        public void run() {
            super.run();
            try {
                byte[] bytesIn;
                while (true) {
                    synchronized (pipeIn) {
                        pipeIn.wait();
                    }
                    bytesIn = pipeIn.readAllBytes();
                    FilePacket filePacket = (FilePacket) bytesToObject(bytesIn);
                    if (filePacket == null) {
                        throw new Error("Not received a complete FilePacket to a receiver.");
                    }
                    if (filePacket.getStart() > read) {//quer dizer que se perdeu um pacote
                        enviaMulticast(new FileOutOfSync(fileId, fileType, read), false);
                    } else if (filePacket.getStart() < read) {//quer dizer começou antes
                        if (read < 0) {//wtf
                            if (DEBUG) {
                                new Exception("Recebi um read menor que 0?").printStackTrace();
                            }
                            enviaMulticast(new FileOutOfSync(fileId, fileType, read), false);

                        } else {//vai buscar o que falta
                            //getStarted() - read - finalConteudo |
                            //diferença entre os dois
                            int diferenca = Long.valueOf(read - filePacket.getStart()).intValue();
                            //copia so a diferença
                            file.write(filePacket.getContent(), Long.valueOf(read - (long) diferenca).intValue(), diferenca);
                            read += diferenca;
                        }
                    } else {//caso tenhao recebido exatamente onde deve começar
                        file.write(filePacket.getContent());
                        //if (filePacket.setComplete()) {
                            //completed();
                        //}
                    }


                }

            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                try {
                    pipeIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    pipeOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        private void completed() {
            try{
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                pipeIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                pipeOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            interrupt();
        }

        public FilePacket.FileType getFileType() {
            return fileType;
        }

        public int getFileId() {
            return fileId;
        }
    }

    /**
     * Envia ficheiros por udp
     */
    public class FicheiroSender extends Thread {

        private final int fileId;
        PipedOutputStream pipeOut;
        PipedInputStream pipeIn;
        FileInputStream file;
        long read = 0;
        FilePacket.FileType fileType;

        public FicheiroSender(int fileId, FilePacket.FileType fileType, long startReading) {
            String filedir = server.getServerName() + File.separator + fileType;
            new File(filedir).mkdirs();
            this.fileType = fileType;
            this.fileId = fileId;
            pipeOut = new PipedOutputStream();
            try {
                pipeIn = new PipedInputStream(pipeOut);
                file = new FileInputStream(filedir + File.separator + fileId);
            } catch (IOException e) {
                e.printStackTrace();
                throw new Error("Problemas com pipes.");
            }
        }

        /**
         * @return pipe to write to
         */
        public synchronized PipedOutputStream getPipeOut() {
            return pipeOut;
        }

        @Override
        public void run() {
            super.run();
            long resetTo = 0;
            long temp = 0;

            byte[] fileChunk = new byte[Utils.Consts.PIPE_BUFFER_SIZE_LIMIT];
            FilePacket filePacket = new FilePacket(fileId, read, fileType, fileChunk);
            while (true) {
                try {
                    if (file.available() == 0) break;
                    //dar tempo para se escrever para o disco e nao encher as mensagens de leitura de so para ficheiros
                    if (pipeIn.available() != read) {
                        resetTo = Long.parseLong(new String(pipeIn.readAllBytes()));
                        temp = file.skip(resetTo - read);
                        if (read - temp != read - resetTo)
                            throw new Error("Problema com o skip.");
                        read = resetTo;
                    }
                    filePacket.setStart(read);
                    int readed = file.read(fileChunk);
                    if (file.available() == 0) {
                        filePacket.setComplete();
                        byte[] tempBytes = new byte[readed];
                        if (readed >= 0) System.arraycopy(fileChunk, 0, tempBytes, 0, readed);
                        filePacket.setContent(tempBytes);
                    } else {
                        filePacket.setContent(fileChunk);
                    }
                    enviaMulticast(filePacket, false);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
