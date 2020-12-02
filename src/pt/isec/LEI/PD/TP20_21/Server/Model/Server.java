package pt.isec.LEI.PD.TP20_21.Server.Model;

import pt.isec.LEI.PD.TP20_21.Server.Connectivity.TcpManager;
import pt.isec.LEI.PD.TP20_21.Server.Connectivity.UdpMultiCast;
import pt.isec.LEI.PD.TP20_21.Server.Connectivity.UdpServerClientPreConnection;
import pt.isec.LEI.PD.TP20_21.shared.IpPort;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.net.*;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Main class do servidor, vai guardar tudo o que o servidor precisa e gerar, da se a referencia desta class para as outras classes para chamar funções para que seja possivel fazer varias funções simultaniamente.
 * Tem sobre responsablidade direta todas as coneções do servidor
 */
public class Server {
    private LinkedList<TcpManager> clientConnections;
    private TcpManager tcpManager;
    private final UdpServerClientPreConnection udpServerClientPreConnection;
    private final ServerData serverData;
    private final Servidores servidores;

    public synchronized LinkedList<Socket> getClientes() {
        return clientes;
    }

    public int getTcpPort() {
        return tcpManager.getPort();
    }

    public Server() throws SQLException {

        //multicast
        new UdpMultiCast();

        servidores = new Servidores();

        // onde vai seorganizar as coisas da memoria
        serverData = new ServerData();

        //criar thread para as ligações tcp
        tcpManager = new TcpManager(this);
        tcpManager.start();

        //implementar uma forma de parar o isto
        //synchronized ()
        //criação da thread que aceita clientes
        udpServerClientPreConnection = new UdpServerClientPreConnection(this, Utils.Consts.UDP_CLIENT_REQUEST_PORT);
        udpServerClientPreConnection.start();


        //thread que fica a receber mensagens de outros servidores

    }

    private synchronized UdpServerClientPreConnection getUdpServerClientPreConnection() {
        return udpServerClientPreConnection;
    }

    private synchronized LinkedList<TcpManager> getClientConnections() {
        return clientConnections;
    }

    public boolean verifyServerAvailability() {
        for (ServidorExterno s : serverData.getServers()) {
            if (s.getLotacao() < clientes.size() / 2)
                return false;
        }
        return true;
    }

    //returna uma lista de servidores por lotação, começando por aqueles que estao mais vazios ate aqueles que estao mais cheios


    public LinkedList<IpPort> getServersForClient() {
        Collections.sort(serverData.getServers());
        int max = 5;
        if (serverData.getServers().ize() < 300)
            max = serverData.getServers().size();
        var linkedList = new LinkedList<IpPort>();
        for (int i = 0; i < max; i++)
            linkedList.add(serverData.getServers().get(i).getForClient());
        return linkedList;
    }


    public Servidores getServidores() {
        return servidores;
    }
}
