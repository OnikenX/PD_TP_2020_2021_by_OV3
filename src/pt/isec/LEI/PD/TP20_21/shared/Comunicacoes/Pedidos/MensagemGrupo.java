package pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos;

import java.io.Serializable;

public class MensagemGrupo implements Serializable, Pedido {
    private final String userEnvia;
    private final String canalNome;
    private final String conteudo;

    public String getUserEnvia() {
        return userEnvia;
    }

    public String getCanalNome() {
        return canalNome;
    }

    public String getConteudo() {
        return conteudo;
    }

    public MensagemGrupo(String userEnvia, String canalNome, String conteudo) {
        this.userEnvia = userEnvia;
        this.canalNome = canalNome;
        this.conteudo = conteudo;
    }
}
