package pt.isec.lei.pd.tp20_21.shared.Comunicacoes.Pedidos;

public class Mensagem {
    private final int Id ;
    private final int pessoaCriar;
    public Mensagem (int Id , int pessoaCriar){
        this.pessoaCriar = pessoaCriar;
        this.Id = Id;
    }

    public int getId() {
        return Id;
    }

    public int getPessoaCriar() {
        return pessoaCriar;
    }
}
