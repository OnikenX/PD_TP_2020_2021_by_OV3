package pt.isec.LEI.PD.TP20_21.shared.Data.Canais;

public class CanalGrupo extends Canal {
    private String nome;
    private String descricao;
    private String password;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CanalGrupo(int id, int pessoaCria, String nome, String descricao, String password) {
        super(id, pessoaCria);
        this.nome = nome;
        this.descricao = descricao;
        this.password = password;
    }
}
