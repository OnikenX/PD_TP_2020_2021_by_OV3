package pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos;

import java.io.Serializable;

public class Ping implements Serializable, Pedido {
    private int lotacao;
//        public static int SIZE = (Objects.requireNonNull(objectToBytes(new Ping())).length);
    private boolean updated;

    public Ping(int lotacao, boolean updated) {
        this.lotacao = lotacao;this.updated = updated;
    }

    public int getLotacao() {
        return lotacao;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public void setLotacao(int lotacao) {
        this.lotacao = lotacao;
    }
}
