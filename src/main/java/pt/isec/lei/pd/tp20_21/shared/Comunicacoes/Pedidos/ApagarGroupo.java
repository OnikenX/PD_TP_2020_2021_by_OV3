package pt.isec.lei.pd.tp20_21.shared.Comunicacoes.Pedidos;

public class ApagarGroupo implements Pedido {
    private final int id;
    public ApagarGroupo(int id){
        this.id = id ;
    }

    public int getId() {
        return id;
    }
}
