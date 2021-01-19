package pt.isec.lei.pd.tp20_21.shared.Comunicacoes;

import pt.isec.lei.pd.tp20_21.shared.Utils;

import java.io.Serializable;
import java.util.List;

public class ListasParaOClient implements Serializable {
    private final List<Object> list;
    private final String tabela;

    public ListasParaOClient(List<Object> list, String tabela) {
        if(Utils.Consts.DEBUG)
            System.out.println("[Construtor LIstasparaOClient]");
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
