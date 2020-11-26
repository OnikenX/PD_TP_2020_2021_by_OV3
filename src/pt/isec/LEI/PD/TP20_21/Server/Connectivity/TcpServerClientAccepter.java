package pt.isec.LEI.PD.TP20_21.Server.Connectivity;

import pt.isec.LEI.PD.TP20_21.Server.Model.Server;
import pt.isec.LEI.PD.TP20_21.shared.Respostas;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Faz a connecção inicial do client
 */
public class TcpManager {
    private Server server;
    private int port;
    private ServerSocket serverSocket;


    public TcpManager(Server server){
        this.server = server;
        try {
            serverSocket = new ServerSocket(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        port = serverSocket.getLocalPort();
    }

     static class TcpServerClientAccepter extends Thread {
        private int port = -1;
        private boolean stop = false;


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

        @Override
        public void run() {
            super.run();
            try {

                while (!stop) {
                    Socket s = serverSocket.accept();
                    InputStream iS = s.getInputStream();
                    OutputStream oS = s.getOutputStream();

                    byte[] bufStr = new byte[256];

                    int nBytes = iS.read(bufStr);
                    String msgRecebida = new String(bufStr, 0, nBytes);
                    String msgResposta = "olaaaaaa";

                    bufStr = msgResposta.getBytes();
                    oS.write(bufStr);
                    s.close();
                }

                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}