package pt.isec.LEI.PD.TP20_21.Server.Connectivity;

import pt.isec.LEI.PD.TP20_21.Server.Model.Server;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
    private static final int TIMEOUT = 3;//segungos

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
    public void ligarCliente() throws IOException {
        var socket = serverSocket.accept();

        /**
         * identifica a que user este client
         */
        int userId =-1;
        tcpServerClientConnections.add(new TcpServerClientConnection(socket));
    }

    public synchronized int getPort() {
        return port;
    }

    /**
     * Representa uma coneção do server para o cliente
     */
    static class TcpServerClientConnection extends Thread {
        private int port = -1;
        private boolean stop = false;
        private final Socket s;
        private final InputStream iS;
        private final OutputStream oS;

        public TcpServerClientConnection(Socket socket) throws IOException {
            this.s = socket;
            iS = s.getInputStream();
            oS = s.getOutputStream();
        }


        //TODO: ver a sincronização de threads e proteção de dados em multithreads
        public boolean isStop() {
            return stop;
        }

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
            Object object;
            try {
                while (!stop) {
                    Utils.bytesToObject(iS.readAllBytes());

                }
            } catch (IOException e) {
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