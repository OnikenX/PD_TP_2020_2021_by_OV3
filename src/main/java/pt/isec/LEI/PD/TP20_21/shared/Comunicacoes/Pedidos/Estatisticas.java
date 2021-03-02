package pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos;

import java.io.Serializable;

public class Estatisticas implements Serializable, Pedido {
    private String idCanal;

    public Estatisticas(String idCanal) {
        this.idCanal = idCanal;
    }
}
