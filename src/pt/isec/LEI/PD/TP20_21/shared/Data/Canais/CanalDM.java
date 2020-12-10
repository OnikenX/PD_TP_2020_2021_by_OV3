package pt.isec.LEI.PD.TP20_21.shared.Data.Canais;

public class CanalDM extends Canal {
    private final int pessoa1;


    private final int pessoa2;

    public CanalDM(int id, String nome, String descricao, int pessoa1, int pessoa2) {
        super(id, nome, descricao);
        this.pessoa1 = pessoa1;
        this.pessoa2 = pessoa2;
    }

    public int getPessoa1() {
        return pessoa1;
    }

    public int getPessoa2() {
        return pessoa2;
    }
}
