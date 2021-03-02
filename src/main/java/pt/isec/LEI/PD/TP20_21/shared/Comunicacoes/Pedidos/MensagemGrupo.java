package pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos;

import java.io.Serializable;

public class MensagemGrupo implements Serializable, Pedido {
    private final int userEnvia;
    private final int canalId;
    private final boolean isAFile;
    private final String conteudo;

    public int getUserEnvia() {
        return userEnvia;
    }

    public int getCanalId() {
        return canalId;
    }

    public String getConteudo() {
        return conteudo;
    }

    public MensagemGrupo(int userEnvia, int canalId, String conteudo, boolean isAFile) {
        this.userEnvia = userEnvia;
        this.canalId = canalId;
        this.conteudo = conteudo;
        this.isAFile = isAFile;
    }
}
