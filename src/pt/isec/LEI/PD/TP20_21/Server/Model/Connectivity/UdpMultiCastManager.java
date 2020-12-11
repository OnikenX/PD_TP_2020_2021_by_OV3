package pt.isec.LEI.PD.TP20_21.Server.Model.Connectivity;

import pt.isec.LEI.PD.TP20_21.Server.Model.Server;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Respostas;
import pt.isec.LEI.PD.TP20_21.shared.FileTransfer.FilePacket;
import pt.isec.LEI.PD.TP20_21.shared.IpPort;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedido;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

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
    VerificaServers verificaServers;
    Collection<FicheiroSender> fileSenders;
    Collection<FicheiroReceiver> fileReceivers;


    public UdpMultiCastManager(Server server) throws IOException {
        fileSenders = Collections.synchronizedCollection(new LinkedList<FicheiroSender>());
        fileReceivers = Collections.synchronizedCollection(new LinkedList<FicheiroReceiver>());
        servidores = (Servidores) Collections.synchronizedCollection(new Servidores());
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
        Object mensagem;
        Class<?> classType = null;
        DatagramPacket packet;
        if (Utils.Consts.DEBUG)
            System.out.println("UdpMultiCast receiver iniciado...");
        try {
            byte[] bytes = new byte[Utils.Consts.MAX_SIZE_PER_PACKET];
            while (true) {
                packet = new DatagramPacket(bytes, bytes.length);
                multicastSocket.receive(packet);
                if (DEBUG)
                    System.out.println("Mensagem recebida por " + packet.getAddress() + ":" + packet.getPort() + " com tamanho de " + packet.getLength() + " bytes");
                mensagem = bytesToObject(packet.getData());
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
                } else if (classType == Pedido.Ping.class) {
                    Pedido.Ping ping = (Pedido.Ping) mensagem;

                    if (Utils.Consts.DEBUG)
                        System.out.println("[Ping] recebido ... ; locacao: " + ping.getLotacao());
                    servidores.add(servidores.new ServidorExterno(packet.getAddress().toString(), packet.getPort(), ping.getLotacao()));

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
     * @throws Exception se ouve problemas com o objeto
     */
    synchronized public void enviaMulticast(Object mensagem, boolean recebeResposta) throws Exception {
        InetAddress group = InetAddress.getByName(UDP_MULTICAST_GROUP);
        var buf = objectToBytes(mensagem);
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
    private class Servidores extends LinkedList<Servidores.ServidorExterno> implements Serializable {
        public void removeTimedOut() {
            var it = this.listIterator();
            while (it.hasNext()) {
                if ((Utils.getTimeStamp() - it.next().getActualizado()) > TIMEOUT_PINGS)
                    it.remove();
                it.next();
            }
        }

        @Override
        public boolean add(ServidorExterno servidorExterno) {
            for (var i : this)
                if (i.equals(servidorExterno)) {
                    i.setLotacao(servidorExterno.getLotacao());
                    return true;
                }
            return super.add(servidorExterno);
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
        }
    }

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
                        enviaMulticast(new Respostas.FileOutOfSync(fileId, fileType, read), false);
                    } else if (filePacket.getStart() < read) {//quer dizer começou antes
                        if (read < 0) {//wtf
                            if (DEBUG) {
                                new Exception("Recebi um read menor que 0?").printStackTrace();
                            }
                            enviaMulticast(new Respostas.FileOutOfSync(fileId, fileType, read), false);

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
            try {
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
