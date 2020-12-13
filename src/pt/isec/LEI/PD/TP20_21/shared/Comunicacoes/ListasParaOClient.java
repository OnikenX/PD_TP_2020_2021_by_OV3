package pt.isec.LEI.PD.TP20_21.shared.Comunicacoes;

import java.util.List;

public class ListasParaOClient {
    private final List<Object> list;
    private final String tabela;

    public ListasParaOClient(List<Object> list, String tabela) {
        this.list = list;
        this.tabela = tabela;
    }

    public List<Object> getList() {
        return list;
    }

    public String getTabela() {
        return tabela;
    }
}
