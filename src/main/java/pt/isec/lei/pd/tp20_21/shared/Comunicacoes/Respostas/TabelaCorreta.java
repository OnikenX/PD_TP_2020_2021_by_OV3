package pt.isec.lei.pd.tp20_21.shared.Comunicacoes.Respostas;

import java.util.ArrayList;

/**
 * Tem uma lista de elementos que foram pedidos
 */
public class TabelaCorreta implements Respostas{
    private final String tabela;
    private final ArrayList<Object> items;

    public ArrayList<Object> getItems() {
        return items;
    }

    public String getTabela() {
        return tabela;
    }

    public TabelaCorreta(String tabela, ArrayList<Object> items) {
        this.tabela = tabela;
        this.items = items;
    }
}
