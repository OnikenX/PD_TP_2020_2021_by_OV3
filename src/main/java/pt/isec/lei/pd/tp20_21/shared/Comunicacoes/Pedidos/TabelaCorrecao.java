package pt.isec.lei.pd.tp20_21.shared.Comunicacoes.Pedidos;

/**
 * Faz um pedido de uma lista de items para corrigir uma tabela de elementos ao server pai
 */
public class TabelaCorrecao implements Pedido{
     public final String tabela;

    public TabelaCorrecao(String tabela) {
        this.tabela = tabela;
    }

    public String getTabela() {
        return tabela;
    }

}
