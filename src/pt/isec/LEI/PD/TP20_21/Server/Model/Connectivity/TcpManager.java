package pt.isec.LEI.PD.TP20_21.Server.Model.Connectivity;

import pt.isec.LEI.PD.TP20_21.Server.Model.Server;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos.Conectar;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos.MensagemDM;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos.MensagemGrupo;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos.Pedido;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Objects;

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
        var socket = serverSocket.accept();
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
            oS.write(Objects.requireNonNull(Utils.objectToBytes(o)));
        }


        /**
         * Começa a thread que recebe mensagens
         */
        public void receiveMessages() {
            this.start();
        }

        //recebe mensagens
        @Override
        public void run() {
            super.run();
            byte[] bytes;
            Object input;
            try {
                Connection conn = null;//connecao à base de dados
                Statement stmt = null;//mesagem a enviar
                ResultSet rs = null;//resultado
                s = serverSocket.accept();
                iS = s.getInputStream();
                oS = s.getOutputStream();
                var serverDB = server.getServerData();
                while (!stop) {
                    input = Utils.bytesToObject(iS.readAllBytes());
                    if(input.getClass() == Conectar.class){
                        var mensagem = (Conectar)input;
                        //adicinar coluna do canal normal e dm if not exist


                        try {
                            //

                            server.getServerData().executeUpdate("");
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                    if(input.getClass() == MensagemDM.class) {
                        MensagemDM msg = (MensagemDM) input;

                        //serverDB.addUser(msg.getUserEnvia())
                        Object res = null; //que vem da bd
                        oS.write(Utils.objectToBytes(res));
                    }
                    else if(input.getClass() == MensagemGrupo.class) {
                        MensagemGrupo msg = (MensagemGrupo) input;
                        // cenas
                        Object res = null; //que vem da bd
                        oS.write(Utils.objectToBytes(res));
                    }
                }
            } catch (IOException /*SQLException*/ e) {
                e.printStackTrace();
                try {
                    s.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }
}