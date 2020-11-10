package pt.isec.LEI.PD.TP20_21.shared;

import java.util.ArrayList;

public class IpServidores {
    public final boolean resposta;
    private ArrayList<IpServidor> servidoresDisponiveis;
    private final int port;

    public IpServidores(boolean resposta,ArrayList<IpServidor> servidoresDisponiveis, int port ) {
        this.resposta = resposta;
        this.port  = port;
        this.servidoresDisponiveis = servidoresDisponiveis;
    }


}
