package pt.isec.LEI.PD.TP20_21.Server.Model;

import pt.isec.LEI.PD.TP20_21.Server.Connectivity.TcpServerClientAccepter;
import pt.isec.LEI.PD.TP20_21.Server.Connectivity.UdpServerClientPreConnection;
import pt.isec.LEI.PD.TP20_21.shared.IpPort;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Main class do servidor, vai guardar tudo o que o servidor precisa e gerar, da se a referencia desta class para as outras classes para chamar funções para que seja possivel fazer varias funções simultaniamente.
 * Tem sobre responsablidade direta todas as coneções do servidor
 */
public class Server {
    private LinkedList<TcpServerClientAccepter> clientConnections;
    private TcpServerClientAccepter tcpServerClient;
    private final UdpServerClientPreConnection udpServerClientPreConnection;
    private final ServerData serverData;
    private LinkedList<Socket> clientes;//é uma lista de sockets dos clientes
    private LinkedList<ClientReceiver> clientReceivers;//é uma lista de threads que fica a receber mensagens

    public synchronized LinkedList<Socket> getClientes() {
        return clientes;
    }

    public int getTcpPort() {
        return tcpServerClient.getPort();
    }

    public Server() {


        // onde vai seorganizar as coisas da memoria
        serverData = new ServerData();

        //criar thread para as ligações tcp
        tcpServerClient = new TcpServerClientAccepter(this);
        tcpServerClient.start();

        //implementar uma forma de parar o isto
        //synchronized ()
        //criação da thread que aceita clientes
        udpServerClientPreConnection = new UdpServerClientPreConnection(this, Utils.Consts.UDPClientRequestPort);
        udpServerClientPreConnection.start();



        //thread que fica a receber mensagens de outros servidores

    }

    private synchronized UdpServerClientPreConnection getUdpServerClientPreConnection() {
        return udpServerClientPreConnection;
    }

    private synchronized LinkedList<TcpServerClientAccepter> getClientConnections() {
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
        if (serverData.getServers().size() < 300)
            max = serverData.getServers().size();
        var linkedList = new LinkedList<IpPort>();
        for (int i = 0; i < max; i++)
            linkedList.add(serverData.getServers().get(i).getForClient());
        return linkedList;
    }
}
