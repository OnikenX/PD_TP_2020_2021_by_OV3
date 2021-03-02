package pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos;

import java.io.Serializable;

public class MensagemDM implements Serializable, Pedido {
    private final int userEnvia;
    private final int userRecebe;
    private final String conteudo;
    private final boolean isAFile;

    public int getUserEnvia() {
        return userEnvia;
    }

    public int getUserRecebe() {
        return userRecebe;
    }

    public String getConteudo() {
        return conteudo;
    }

    public boolean isAFile(){return isAFile;}

    public MensagemDM(int userEnvia, int userRecebe, String conteudo, boolean isAFile) {
        this.userEnvia = userEnvia;
        this.userRecebe = userRecebe;
        this.conteudo = conteudo;
        this.isAFile = isAFile;
    }
}
