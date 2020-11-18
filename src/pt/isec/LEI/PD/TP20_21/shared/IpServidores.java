package pt.isec.LEI.PD.TP20_21.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Guarda a resposta a enviar a um cliente
 */
public class IpServidores implements Serializable {
    public final boolean resposta;
    public final LinkedList<IpServidor> servidoresDisponiveis;
    public final int port;
    /**
     * @param resposta diz se o actual servidor se encontra disponivel
     * @param servidoresDisponiveis recebe uma lista de servidores disponiveis
     * @param port diz a port em que o client deve se ligar ao tcp deste servidor
     */
    public IpServidores(boolean resposta,LinkedList<IpServidor> servidoresDisponiveis, int port ) {
        this.resposta = resposta;
        this.port  = port;
        this.servidoresDisponiveis = servidoresDisponiveis;
    }


}
