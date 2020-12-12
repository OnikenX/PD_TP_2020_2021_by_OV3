package pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos;

import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos.Pedido;

import java.io.Serializable;

public class Ping implements Serializable, Pedido {
    private int lotacao;
//        public static int SIZE = (Objects.requireNonNull(objectToBytes(new Ping())).length);

    public Ping(int lotacao) {
        this.lotacao = lotacao;
    }

    public Ping() {
    }

    public int getLotacao() {
        return lotacao;
    }

    public void setLotacao(int lotacao) {
        this.lotacao = lotacao;
    }
}
