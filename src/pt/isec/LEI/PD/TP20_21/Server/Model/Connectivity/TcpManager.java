package pt.isec.LEI.PD.TP20_21.Server.Model.Connectivity;

import pt.isec.LEI.PD.TP20_21.Server.Model.Data.ServerDB;
import pt.isec.LEI.PD.TP20_21.Server.Model.Server;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.ListasParaOClient;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos.ApagarGroupo;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos.Conectar;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos.MensagemDM;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos.MensagemGrupo;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.LinkedList;
import java.util.Objects;

import static pt.isec.LEI.PD.TP20_21.shared.Utils.objectToBytes;

/**
 * Faz a connecção inicial do client
 */
public class TcpManager {
    private Server server;
    private int port;
    private ServerSocket serverSocket;

    public TcpServerClientConnections getTcpServerClientConnections() {
        return tcpServerClientConnections;
    }

    private final TcpServerClientConnections tcpServerClientConnections = new TcpServerClientConnections();
    static public class TcpServerClientConnections extends LinkedList<TcpServerClientConnection> {

    }



    public TcpManager(Server server) {
        this.server = server;
        try {
            serverSocket = new ServerSocket(0);
            serverSocket.setSoTimeout(3 * 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        port = serverSocket.getLocalPort();
    }

    //TODO: continuar a fazer esta parte e completar o TcpServerClientConnection
    public int ligarCliente(int user_id) throws IOException {
        //var socket = serverSocket.accept();
        tcpServerClientConnections.add(new TcpServerClientConnection(user_id));
        return getPort();
    }

    public synchronized int getPort() {
        return port;
    }

    /**
     * Representa uma coneção do server para o cliente
     */
    class TcpServerClientConnection extends Thread {
        private int port = -1;
        private boolean stop = false;
        private Socket s;
        private InputStream iS;
        private OutputStream oS;
        private final int user_id;

        public TcpServerClientConnection(int user_id) throws IOException {
            this.user_id = user_id;
            start();
        }


        //TODO: ver a sincronização de threads e proteção de dados em multithreads

        public void setStop(boolean stop) {
            this.stop = stop;
        }

        public int getPort() {
            return port;
        }



        //Message sender
        public void sendMessage(Object o) throws IOException {
            oS.write(Objects.requireNonNull(objectToBytes(o)));
        }


//        /**
//         * Começa a thread que recebe mensagens
//         */
//        public void receiveMessages() {
//            this.start();
//        }

        //recebe mensagens
        @Override
        public void run() {
            super.run();
            byte[] bytes = null;
            Object input = null;
            try {
                s = serverSocket.accept();
                iS = s.getInputStream();
                oS = s.getOutputStream();
                var serverDB = server.getServerDB();
                input = new ListasParaOClient(serverDB.getListaTabela(ServerDB.table_canaisDM), ServerDB.table_canaisDM);
                if(Utils.Consts.DEBUG)
                    bytes = Objects.requireNonNull(objectToBytes(input));
                    System.out.println("[Client] connected");
                oS.write(bytes);
if(Utils.Consts.DEBUG)
                    System.out.println("enviado uma lista");
                oS.write(Objects.requireNonNull(objectToBytes(new ListasParaOClient(serverDB.getListaTabela(ServerDB.table_canaisGrupo), ServerDB.table_canaisGrupo))));
                oS.write(Objects.requireNonNull(objectToBytes(new ListasParaOClient(serverDB.getListaTabela(ServerDB.table_utilizadores), ServerDB.table_utilizadores))));
                oS.write(Objects.requireNonNull(objectToBytes(new ListasParaOClient(serverDB.getListaTabela(ServerDB.table_mensagens), ServerDB.table_mensagens))));
                while (!stop) {
                    input = Utils.bytesToObject(iS.readAllBytes());
                    if(input.getClass() == Conectar.class){
                        var mensagem = (Conectar)input;
                        //adicinar coluna do canal normal e dm if not exist

                        server.getServerDB();
                    }
                    if(input.getClass() == MensagemDM.class) {
                        MensagemDM msg = (MensagemDM) input;

                        server.getServerDB().mensagemDM(Timestamp.from(Instant.now()),  ((MensagemDM) input).getUserEnvia(), ((MensagemDM) input).getUserRecebe() , ((MensagemDM) input).isAFile(), ((MensagemDM) input).getConteudo());
                        Object res = null; //que vem da bd
                        oS.write(objectToBytes(res));
                    }
                    else if(input.getClass() == MensagemGrupo.class) {
                        MensagemGrupo msg = (MensagemGrupo) input;
                        // cenas
                        Object res = null; //que vem da bd
                        oS.write(objectToBytes(res));
                    }else if (input.getClass() == ApagarGroupo.class){
                        server.getServerDB().deleteCanal(((ApagarGroupo) input).getId());
                    }

                }
            } catch (IOException /*SQLException*/ e) {
                e.printStackTrace();
                try {
                    s.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}