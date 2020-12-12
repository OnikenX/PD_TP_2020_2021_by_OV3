package pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Respostas;

import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Respostas.Respostas;
import pt.isec.LEI.PD.TP20_21.shared.IpPort;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Resposta para para o cliente quando se esta a pedir coneção tcp ao servidor.
 * Se a variavel TcpPort -1 quer dizer que o servidor não aceitou a ligação.
 * Verificar a tcpport para saber a resposta do servidor.
 */
public class PedidoDeLigar implements Serializable, Respostas {
    public final LinkedList<IpPort> servers;
    /**
     * Returns the port for the tcp or an error:<br/>
     * <b>ERROR_USER_INFO_NOT_MATCH</b><br/>
     * or<br/>
     * <b>ERROR_SERVER_FULL</b><br/>
     */
    public final int TcpPort;

    public PedidoDeLigar(int TcpPort, LinkedList<IpPort> servers) {
        this.TcpPort = TcpPort;
        this.servers = servers;
    }

    public PedidoDeLigar() {
        this.TcpPort = -1;
        this.servers = null;
    }

}
