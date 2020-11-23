package pt.isec.LEI.PD.TP20_21.Server.Connectivity;

import pt.isec.LEI.PD.TP20_21.Server.Model.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Faz a connecção inicial do client
 */
public class TcpServerClientAccepter extends Thread {
    private int port = -1;
    private Server server;

    public TcpServerClientAccepter(Server server){
        this.server = server;
    }



    public int getPort() {
        return port;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(0);
            port = serverSocket.getLocalPort();
            boolean stop = false;
            while (!stop) {
                Socket s = serverSocket.accept();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}