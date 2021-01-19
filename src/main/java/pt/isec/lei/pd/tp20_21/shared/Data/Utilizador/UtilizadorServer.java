package pt.isec.lei.pd.tp20_21.shared.Data.Utilizador;
import pt.isec.lei.pd.tp20_21.shared.Data.DataBase;

public class UtilizadorServer extends Utilizador  implements DataBase {
    private final String hash;

    public String getHash() {
        return hash;
    }

    public UtilizadorServer(int id, String username, String nome, String hash) {
        super(id, username, nome);
        this.hash = hash;
    }

}
