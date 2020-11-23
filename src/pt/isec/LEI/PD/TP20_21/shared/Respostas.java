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
        public final LinkedList<IpServidor> servers;
        public final int TcpPort;

        public RUdpClientServerPreConnection(int TcpPort, LinkedList<IpServidor> servers) {
            this.TcpPort = TcpPort;
            this.servers = servers;
        }
    }
}
