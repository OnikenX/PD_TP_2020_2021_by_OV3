package pt.isec.LEI.PD.TP20_21.Server.Model.Connectivity.Sockets;

import pt.isec.LEI.PD.TP20_21.Server.Model.Data.ServerDB;
import pt.isec.LEI.PD.TP20_21.Server.Model.Server;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.MulticastPacket;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos.Ping;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos.PingPai;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos.TabelaCorrecao;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Respostas.FileOutOfSync;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Respostas.TabelaCorreta;
import pt.isec.LEI.PD.TP20_21.shared.FileTransfer.FilePacket;
import pt.isec.LEI.PD.TP20_21.shared.IpPort;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.*;
import java.util.function.UnaryOperator;


/**
 * Trata das mensagens entre servidores
 */
public class UdpMultiCastManager extends Thread {
    private Servidores servidores;
    //    protected String username;
    protected MulticastSocket multicastSocket;
    protected Server server;
    PingSender pingSender;
    /**
     * o id aleatorio que identifica o servidor quando faz chamadas multicast
     */
    final int serverMulticastId;
    VerificaServers verificaServers;
    /**
     * Lista de @{@link FicheiroSender}s
     */
    List<FicheiroSender> fileSenders;
    /**
     * Lista de @{@link FicheiroReceiver}s
     */
    List<FicheiroReceiver> fileReceivers;
    MulticastPacket multicastPacketToSend;
    public final long serverStartTimestamp = System.nanoTime();


    /**
     * Um server esta updated quando tem os mesmos a mesma checksum que o server pai
     */
    private boolean serverUpdated = false;

    /**
     * e um server pai quando e' o server mais antigo
     */
    private boolean serverDad = false;

    public UdpMultiCastManager(Server server) throws IOException {
        fileSenders = Collections.synchronizedList(new LinkedList<>());
        fileReceivers = Collections.synchronizedList(new LinkedList<>());
        servidores = new Servidores();
        serverMulticastId = new Random(serverStartTimestamp).nextInt();
        multicastPacketToSend = new MulticastPacket(serverMulticastId);
        this.multicastSocket = new MulticastSocket();
        multicastSocket.joinGroup(InetAddress.getByName(Utils.Consts.UDP_MULTICAST_GROUP));
        this.server = server;

        //threads
        setDaemon(true);
        start();
        pingSender = new PingSender();
        verificaServers = new VerificaServers();
    }


    public Servidores getServidores() {
        return servidores;
    }


    public boolean isServerDad() {
        return serverDad;
    }

    public void setServerDad(boolean serverDad) {
        this.serverDad = serverDad;
    }


    public boolean isServerUpdated() {
        return serverUpdated;
    }

    public void setServerUpdated(boolean serverUpdated) {
        this.serverUpdated = serverUpdated;
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
            //ciar o socket para receber pedidos
            multicastSocketReceiver = new MulticastSocket(Utils.Consts.UDP_MULTICAST_PORT);
            multicastSocketReceiver.joinGroup(InetAddress.getByName(Utils.Consts.UDP_MULTICAST_GROUP));
            if (Utils.Consts.DEBUG)
                System.out.println("UdpMultiCast receiver iniciado...");
            byte[] bytes = new byte[Utils.Consts.MAX_SIZE_PER_PACKET];

            while (true) {
                packet = new DatagramPacket(
                        bytes,
                        bytes.length,
                        InetAddress.getByName(Utils.Consts.UDP_MULTICAST_GROUP),
                        Utils.Consts.UDP_MULTICAST_PORT
                );

                multicastSocketReceiver.receive(packet);
                if (Utils.Consts.DEBUG)
                    System.out.println("Mensagem recebida por " + packet.getAddress() + ":" +
                            packet.getPort() + ": server_" + server.server_number + ":" +
                            " com tamanho de " + packet.getLength() + " bytes");
                multicastPacket = (MulticastPacket) Utils.bytesToObject(packet.getData());

                //verificaçao de erros e author
                if (multicastPacket == null) {
                    if (Utils.Consts.DEBUG)
                        System.out.println("Mensagem corrupta recebida...");
                    continue;
                }
                if (multicastPacket.getMulticastId() == serverMulticastId) {
//                    if (DEBUG)
//                        System.out.println("E do mesmo servidor, a ignorar...");
                    continue;
                }
                mensagem = multicastPacket.getData();


                //verfica o que fazer com o pacote
                try {
                    respostaDoRecebido(mensagem, packet);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }catch(Exception e){
                    System.err.println("Exception na interpretacao da mensagem.");
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

    private void respostaDoRecebido(Object mensagem, DatagramPacket packet) throws IOException, SQLException {
        var classType = mensagem.getClass();
        if (classType == FilePacket.class) {
            FilePacket m = (FilePacket) mensagem;
            for (var i : fileSenders) {
                if (i.fileId == m.fileId) {
                    i.getPipeOut().write(m.getContent());
                    break;
                }
            }
        } else if (mensagem.getClass() == Ping.class || mensagem.getClass() == PingPai.class) {
            if(Utils.Consts.DEBUG)
                System.out.println("[]");
            Ping ping = (Ping) mensagem;
            if (Utils.Consts.DEBUG)
                System.out.println("[Ping] recebido ... ; locacao: " + ping.getLotacao());
            servidores.verifyPing((Ping) mensagem, packet);
        } else if (classType == TabelaCorrecao.class) {
            if(serverDad){
                new Thread(() -> {
                    List<Object> items = null;
                    try {
                        items = server.getServerDB().getListaTabela(((TabelaCorrecao) mensagem).getTabela());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (items != null) {
                        try {
                            enviaMulticast(items, false);
                        } catch (Exception e) {
                            System.err.println("Enviada copia.");
                            e.printStackTrace();
                        }
                    }

                }).start();
            }
        }else if (classType == TabelaCorreta.class){

            new Thread(()-> {
                try {
                    server.getServerDB().verificaMudancas( ((TabelaCorreta) mensagem).getItems() , ((TabelaCorreta) mensagem).getTabela());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
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
        InetAddress group = InetAddress.getByName(Utils.Consts.UDP_MULTICAST_GROUP);
        multicastPacketToSend.setData(mensagem);
        var buf = Utils.objectToBytes(multicastPacketToSend);
        if (buf == null) {
            throw new Exception("Class com problemas.");
        }
        var packet = new DatagramPacket(buf, buf.length, group, Utils.Consts.UDP_MULTICAST_PORT);
        multicastSocket.send(packet);
        //TODO: nao sei o que isto pode fazer mas pode ser preciso mais tarde rever
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
                sleep(Utils.Consts.SERVER_VERIFY_SERVERS_TIMER * 1000);
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

    private Ping createPing() throws SQLException {
        if (isServerDad())
            return new PingPai(server.getTcpConnections_size(), serverStartTimestamp,
                    server.getServerDB().getChecksum(ServerDB.table_canais),
                    server.getServerDB().getChecksum(ServerDB.table_canaisDM),
                    server.getServerDB().getChecksum(ServerDB.table_canaisGrupo),
                    server.getServerDB().getChecksum(ServerDB.table_mensagens),
                    server.getServerDB().getChecksum(ServerDB.table_utilizadores)
            );
        else
            return new Ping(server.getTcpConnections_size(), serverStartTimestamp);
    }

    @Override
    public void run() {
        if (Utils.Consts.DEBUG)
            System.out.println("UdpMultiCast activado iniciado...");

        Ping ping;
        while (true) {
            try {
                ping = createPing();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                if (Utils.Consts.DEBUG)
                    System.err.println("erro no criar um ping por causa da base de dados");
                continue;
            }
            try {
                enviaMulticast(ping, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (Utils.Consts.DEBUG)
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
private class Servidores implements Serializable, Iterable<Servidores.ServidorExterno>, List<Servidores.ServidorExterno> {
    List<ServidorExterno> servidoresList;


    public void verifyPing(Ping mensagem, DatagramPacket packet) throws SQLException {
        if (isServerDad())
            if (mensagem.getServerStartTimestamp() < serverStartTimestamp)
                setServerDad(false);
        if (!isServerDad())
            if (mensagem instanceof PingPai) {//verifica se esta updated
                var mensagemPai = (PingPai) mensagem;
                //verifica erros de checksum
                if (mensagemPai.getCanaisDMChecksum() != server.getServerDB().getChecksum(ServerDB.table_canaisDM))
                    notUpdated(ServerDB.table_canaisDM);
                else if (mensagemPai.getCanaisGroupoChecksum() != server.getServerDB().getChecksum(ServerDB.table_canaisGrupo))
                    notUpdated(ServerDB.table_canaisGrupo);
                else if (mensagemPai.getMensagensChecksum() != server.getServerDB().getChecksum(ServerDB.table_mensagens))
                    notUpdated(ServerDB.table_mensagens);
                else if (mensagemPai.getUtilizadoresChecksum() != server.getServerDB().getChecksum(ServerDB.table_utilizadores))
                    notUpdated(ServerDB.table_utilizadores);
                else setServerUpdated(true);
            }

        var servidorExterno = new ServidorExterno(packet.getAddress().toString(), packet.getPort(), mensagem.getLotacao(), mensagem.getServerStartTimestamp());

        boolean updated = false;
        for (ServidorExterno i : this) {
            if (i.equals(servidorExterno)) {
                i.setLotacao(servidorExterno.lotacao);
                updated = true;
                break;
            }
        }
        if (!updated)
            add(servidorExterno);
    }


    /**
     * Guarda as informações de um servidor externo
     */
    public class ServidorExterno extends IpPort implements Comparable<ServidorExterno>, Serializable, Cloneable {

        private int lotacao;

        /**
         * ultima vez que o server foi verificado
         */
        private long verificado;

        private final long serverStartTimestamp;

        public ServidorExterno(String ip, int port, int lotacao, long serverStartTimeStamp) {
            super(ip, port);
            this.lotacao = lotacao;
            this.serverStartTimestamp = serverStartTimeStamp;
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


        public long getServerStartTimestamp() {
            return serverStartTimestamp;
        }

        public int getLotacao() {
            return lotacao;
        }

        public void setLotacao(int lotacao) {
            if (lotacao >= 0)
                this.lotacao = lotacao;
            setActualizado();
        }

        public long getVerificado() {
            return verificado;
        }

        public void setActualizado() {
            this.verificado = Utils.getTimeStamp();
        }

        /**
         * @param ipss do tipo {@link ServidorExterno}
         * @return diferença de locação
         */
        @Override
        public int compareTo(ServidorExterno ipss) {
            return lotacao - ipss.getLotacao();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            ServidorExterno that = (ServidorExterno) o;
            return serverStartTimestamp == that.serverStartTimestamp;
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), lotacao, verificado, serverStartTimestamp);
        }
    }


    @Override
    public int size() {
        return servidoresList.size();
    }

    @Override
    public boolean isEmpty() {
        return servidoresList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return servidoresList.contains(o);
    }

    @Override
    public Object[] toArray() {
        return servidoresList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return servidoresList.toArray(a);
    }

    @Override
    public boolean add(ServidorExterno servidorExterno) {
        return servidoresList.add(servidorExterno);
    }

    @Override
    public boolean remove(Object o) {
        return servidoresList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return servidoresList.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends ServidorExterno> c) {
        return servidoresList.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends ServidorExterno> c) {
        return servidoresList.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return servidoresList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return servidoresList.retainAll(c);
    }

    @Override
    public void replaceAll(UnaryOperator<ServidorExterno> operator) {
        servidoresList.replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super ServidorExterno> c) {
        servidoresList.sort(c);
    }

    @Override
    public void clear() {
        servidoresList.clear();
    }

    @Override
    public boolean equals(Object o) {
        return servidoresList.equals(o);
    }

    @Override
    public int hashCode() {
        return servidoresList.hashCode();
    }

    @Override
    public ServidorExterno set(int index, ServidorExterno element) {
        return servidoresList.set(index, element);
    }

    @Override
    public void add(int index, ServidorExterno element) {
        servidoresList.add(index, element);
    }

    @Override
    public ServidorExterno remove(int index) {
        return servidoresList.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return servidoresList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return servidoresList.lastIndexOf(o);
    }

    @Override
    public ListIterator<ServidorExterno> listIterator() {
        return servidoresList.listIterator();
    }

    @Override
    public ListIterator<ServidorExterno> listIterator(int index) {
        return servidoresList.listIterator(index);
    }

    @Override
    public List<ServidorExterno> subList(int fromIndex, int toIndex) {
        return servidoresList.subList(fromIndex, toIndex);
    }

    Servidores() {
        servidoresList = Collections.synchronizedList(new LinkedList<ServidorExterno>());
    }

    /**
     * Verifica aqueles que estao em timeout e se este e' o server pai
     */
    public void removeTimedOut() {
        var it = this.iterator();
        ServidorExterno se;
        long lowestTime = serverStartTimestamp;
        while (it.hasNext()) {
            se = it.next();
            if ((Utils.getTimeStamp() - se.getVerificado()) > Utils.Consts.TIMEOUT_PINGS)
                it.remove();
            else {
                if (se.serverStartTimestamp < lowestTime)
                    lowestTime = se.serverStartTimestamp;
            }
        }
        //faz ser o pai
        if (lowestTime >= serverStartTimestamp)
            setServerDad(true);
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


}

    /**
     * Muda o estado para nao updated, o que quer dizer que os outros servidores tem de tirar lo da lista e so adicionam quando ele assim estiver updated
     *
     * @param tabela que nao esta actualizada
     */
    private void notUpdated(String tabela) {
        setServerUpdated(false);
        if (Utils.Consts.DEBUG)
            System.out.println("nao esta updetado no " + tabela);
        try {
            enviaMulticast(new TabelaCorrecao(tabela), false);
        } catch (Exception e) {
            System.err.println("Problemas a enviar mensagem multicast");
            e.printStackTrace();
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
                FilePacket filePacket = (FilePacket) Utils.bytesToObject(bytesIn);
                if (filePacket == null) {
                    throw new Error("Not received a complete FilePacket to a receiver.");
                }
                if (filePacket.getStart() > read) {//quer dizer que se perdeu um pacote
                    enviaMulticast(new FileOutOfSync(fileId, fileType, read), false);
                } else if (filePacket.getStart() < read) {//quer dizer começou antes
                    if (read < 0) {//wtf
                        if (Utils.Consts.DEBUG) {
                            new Exception("Recebi um read menor que 0?").printStackTrace();
                        }
                        enviaMulticast(new FileOutOfSync(fileId, fileType, read), false);

                    } else {
                        //vai buscar o que falta
                        //getStarted() - read - finalConteudo
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
