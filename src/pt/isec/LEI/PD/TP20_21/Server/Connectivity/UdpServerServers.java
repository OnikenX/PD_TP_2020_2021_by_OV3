package pt.isec.LEI.PD.TP20_21.Server.Connectivity;

import java.net.MulticastSocket;

//thread aberta sempre aberta no porto para os outros servidores
//e depois responde sempre que recebe
public class UdpServerServers extends Thread {


    public UdpServerServers(String username, MulticastSocket s) {
        this.username = username;
        this.s = s;
        running = true;
    }

    public void terminate() {
        running = false;
    }

}

