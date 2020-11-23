package pt.isec.LEI.PD.TP20_21.Server.Model;


import java.util.LinkedList;

/**
 * Faz manegamento dos objectos na memoria, toma conta dos dados e mexe na database
 */
 public class ServerData {
    private LinkedList<IpServidorServidor> servers;

    public ServerData() {
        this.servers = new LinkedList<>();
    }

    public synchronized LinkedList<IpServidorServidor> getServers() {
        return servers;
    }
}


