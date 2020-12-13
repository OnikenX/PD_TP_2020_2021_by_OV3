package pt.isec.LEI.PD.TP20_21.shared.Data.Utilizador;

public class UtilizadorServer extends Utilizador {
    private final String hash;

    public String getHash() {
        return hash;
    }

    public UtilizadorServer(int id, String username, String nome, String hash) {
        super(id, username, nome);
        this.hash = hash;
    }

}
