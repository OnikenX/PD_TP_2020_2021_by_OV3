package pt.isec.LEI.PD.TP20_21.shared;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Classes que vão ser enviadas em resposta de algo
 */
public class Respostas implements Serializable {
    /**
     * Resposta para para o cliente quando se esta a pedir coneção tcp ao servidor.
     * Se a variavel TcpPort -1 quer dizer que o servidor não aceitou a ligação.
     */
    public static class RUdpClientServerPreConnection implements Serializable{
        public final LinkedList<IpPort> servers;
        public final int TcpPort;

        public RUdpClientServerPreConnection(int TcpPort, LinkedList<IpPort> servers) {
            this.TcpPort = TcpPort;
            this.servers = servers;
        }
    }

    public static class RUdpMulticast implements Serializable {
        public final IpPort server;

        public RUdpMulticast(IpPort server) {
            this.server = server;
        }
    }
}
