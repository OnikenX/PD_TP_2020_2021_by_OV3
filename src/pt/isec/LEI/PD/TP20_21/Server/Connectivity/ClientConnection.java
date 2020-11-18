package pt.isec.LEI.PD.TP20_21.Server.Connectivity;

import pt.isec.LEI.PD.TP20_21.Server.Model.Server;

import java.io.IOException;
import java.net.ServerSocket;

public class ClientConnection extends Thread {
    private Server server;
    private int listeningPort;
    ServerSocket serverSocket;
    public ClientConnection(Server server, int listeningPort) {
        this.server = server;
        this.listeningPort = listeningPort;
    }

    @Override
    public void run() {
        super.run();
        try {
            serverSocket = new ServerSocket(listeningPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                var s = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            var iS = serverSocket.getInputStream();
            var oS = serverSocket.getOutputStream();



        }


    }
}
