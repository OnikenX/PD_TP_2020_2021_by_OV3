package pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos;

import java.io.Serializable;

public class ListaCanais implements Serializable, Pedido {

    private String user;

    public ListaCanais(String user) {
        this.user = user;
    }
}
