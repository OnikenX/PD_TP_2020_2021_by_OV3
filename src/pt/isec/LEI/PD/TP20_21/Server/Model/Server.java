package pt.isec.LEI.PD.TP20_21.Server.Model;

import pt.isec.LEI.PD.TP20_21.Server.Model.Connectivity.TcpManager;
import pt.isec.LEI.PD.TP20_21.Server.Model.Connectivity.UdpMultiCastManager;
import pt.isec.LEI.PD.TP20_21.Server.Model.Connectivity.UdpServerClientPreConnection;
import pt.isec.LEI.PD.TP20_21.Server.Model.Data.ServerDB;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Main class do servidor, vai guardar tudo o que o servidor precisa e gerar, da se a referencia desta class para as outras classes para chamar funções para que seja possivel fazer varias funções simultaniamente.
 * Tem sobre responsablidade direta todas as coneções do servidor
 */
public class Server {
//    private final LinkedList<TcpManager> clientConnections;
    private TcpManager tcpManager;
    public UdpMultiCastManager udpMultiCastManager;
    private UdpServerClientPreConnection udpServerClientPreConnection;
    private ServerDB serverDB;
    public final int server_number;

    public String getServerName(){
        return "server_"+server_number;
    }

    public int getTcpPort() { return tcpManager.getPort(); }
    public int getTcpConnections_size(){
        return tcpManager.getTcpServerClientConnections().size();
    }
    public Server(int server_number) throws SQLException, ClassNotFoundException, IOException, InterruptedException {
        this.server_number = server_number;
        //criar a lista de servidores

        //data
        serverDB = new ServerDB(this, server_number);

        //criar thread para as ligações tcp
        tcpManager = new TcpManager(this);

        //multicast
        udpMultiCastManager = new UdpMultiCastManager(this);

        //implementar uma forma de parar o isto


        //criação da thread que aceita clientes
        udpServerClientPreConnection = new UdpServerClientPreConnection(this, Utils.Consts.UDP_CLIENT_REQUEST_PORT);
        udpServerClientPreConnection.start();


        //thread que fica a receber mensagens de outros servidores

//        tcpManager. = new LinkedList<>();
        udpMultiCastManager.join();

    }

    private synchronized UdpServerClientPreConnection getUdpServerClientPreConnection() {
        return udpServerClientPreConnection;
    }

    //returna uma lista de servidores por lotação, começando por aqueles
    //que estao mais vazios ate aqueles que estao mais cheios



    public ServerDB getServerDB() {
        return serverDB;
    }
}
