package pt.isec.LEI.PD.TP20_21.Server.Model;

import pt.isec.LEI.PD.TP20_21.Server.Connectivity.ClientConnection;
import pt.isec.LEI.PD.TP20_21.Server.Connectivity.UdpServerClient;
import pt.isec.LEI.PD.TP20_21.shared.IpServidor;

import java.util.ArrayList;
import java.util.LinkedList;

import static pt.isec.LEI.PD.TP20_21.shared.Consts.*;
//Main class do servidor, vai guardar tudo o que o servidor precisa e gerar, da se a referencia desta class para as outras classes para chamar funções para que seja possivel fazer varias funções simultaniamente
public class Server {
    ServerData serverData;
    LinkedList<ClientConnection> clientConnections;
    LinkedList<IpServidor> servers;
    UdpServerClient udpServer;
    public Server(){
        this.serverData =new ServerData();
        udpServer = new UdpServerClient(this, UDPClientRequestPort);
        udpServer.start();
    }

    public int createTCPClientConnection() {

    }


    public boolean verificarServidor() {
        return true;
    }

    //returna uma lista de servidores por lotação, começando por aqueles que estao mais vazios ate aqueles que estao mais cheios
    public ArrayList<IpServidor> getServerOrderedByLotation(){
        servers.sort((e1,e2) ->{
            getLotacion()
        });
    }

}
