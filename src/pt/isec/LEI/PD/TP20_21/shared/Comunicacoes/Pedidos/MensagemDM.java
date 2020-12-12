package pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos;

import java.io.Serializable;

public class MensagemDM implements Serializable, Pedido {
    private final String userEnvia;
    private final String userRecebe;
    private final String conteudo;

    public String getUserEnvia() {
        return userEnvia;
    }

    public String getUserRecebe() {
        return userRecebe;
    }

    public String getConteudo() {
        return conteudo;
    }

    public MensagemDM(String userEnvia, String userRecebe, String conteudo) {
        this.userEnvia = userEnvia;
        this.userRecebe = userRecebe;
        this.conteudo = conteudo;
    }
}
