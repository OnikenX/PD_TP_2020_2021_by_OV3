package pt.isec.LEI.PD.TP20_21.Server.Model;

import pt.isec.LEI.PD.TP20_21.Server.Connectivity.TcpManager;
import pt.isec.LEI.PD.TP20_21.Server.Connectivity.UdpMultiCast;
import pt.isec.LEI.PD.TP20_21.Server.Connectivity.UdpServerClientPreConnection;
import pt.isec.LEI.PD.TP20_21.shared.IpPort;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Main class do servidor, vai guardar tudo o que o servidor precisa e gerar, da se a referencia desta class para as outras classes para chamar funções para que seja possivel fazer varias funções simultaniamente.
 * Tem sobre responsablidade direta todas as coneções do servidor
 */
public class Server {
//    private final LinkedList<TcpManager> clientConnections;
    private TcpManager tcpManager;
    private UdpMultiCast udpMultiCast;
    private UdpServerClientPreConnection udpServerClientPreConnection;
    private ServerData serverData;
    private Servidores servidores;

    public int getTcpPort() { return tcpManager.getPort(); }
    public int getTcpConnections_size(){
        return tcpManager.getTcpServerClientConnections().size();
    }
    public Server(int server_number) throws SQLException, ClassNotFoundException, IOException {

        //criar a lista de servidores
        servidores = new Servidores();

        //data
        serverData = new ServerData();

        //multicast
         udpMultiCast = new UdpMultiCast(this);

        //criar thread para as ligações tcp
        tcpManager = new TcpManager(this);

        //implementar uma forma de parar o isto


        //criação da thread que aceita clientes
        udpServerClientPreConnection = new UdpServerClientPreConnection(this, Utils.Consts.UDP_CLIENT_REQUEST_PORT);
        udpServerClientPreConnection.start();


        //thread que fica a receber mensagens de outros servidores

//        tcpManager. = new LinkedList<>();
    }

    private synchronized UdpServerClientPreConnection getUdpServerClientPreConnection() {
        return udpServerClientPreConnection;
    }

    public boolean verifyServerAvailability() {
        for (ServidorExterno s : serverData.getServidores()) {
            if (s.getLotacao() < tcpManager.getTcpServerClientConnections().size() /2)
                return false;
        }
        return true;
    }

    //returna uma lista de servidores por lotação, começando por aqueles
    //que estao mais vazios ate aqueles que estao mais cheios

    public LinkedList<IpPort> getServersForClient() {
        Collections.sort(serverData.getServidores());
        int max = 5;
        if (serverData.getServidores().size() < 300)
            max = serverData.getServidores().size();
        var linkedList = new LinkedList<IpPort>();
        for (int i = 0; i < max; i++)
            linkedList.add(serverData.getServidores().get(i).getForClient());
        return linkedList;
    }

    public ServerData getServerData() {
        return serverData;
    }

    public Servidores getServidores() {
        return servidores;
    }
}
