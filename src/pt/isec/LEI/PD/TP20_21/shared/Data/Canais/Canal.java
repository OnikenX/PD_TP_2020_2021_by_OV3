package pt.isec.LEI.PD.TP20_21.shared.Data.Canais;

public abstract class Canal {
    private final int id;
    private final String nome;
    private final String descricao;

    public Canal(int id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }
}
