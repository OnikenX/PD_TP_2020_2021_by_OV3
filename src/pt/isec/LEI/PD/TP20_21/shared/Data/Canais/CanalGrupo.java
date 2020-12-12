package pt.isec.LEI.PD.TP20_21.shared.Data.Canais;

public class CanalGrupo extends Canal {
    private final int moderadorId;
    private final  String password;

    public CanalGrupo(int id, String nome, String descricao, int moderadorId, String password) {
        super(id, nome, descricao);
        this.moderadorId = moderadorId;
        this.password = password;
    }

    public int getModeradorId() {
        return moderadorId;
    }

    public String getPassword() {
        return password;
    }
}
