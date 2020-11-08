package pt.isec.LEI.PD.TP20_21.shared;

import java.util.ArrayList;

public class IpServidores {
    public final boolean resposta;
    private ArrayList<IpServidor> servidoresDisponiveis = new ArrayList<IpServidor>();

    IpServidores(boolean resposta) {
        this.resposta = resposta;
    }


}
