package pt.isec.lei.pd.tp20_21.shared.Data.Canais;

import pt.isec.lei.pd.tp20_21.shared.Data.DataBase;
public class CanalDM extends Canal  implements DataBase {
    private final int pessoaDest;
    public CanalDM(int id, int pessoaCria, int pessoaDest) {
        super(id, pessoaCria);
        this.pessoaDest = pessoaDest;
    }
    public int getPessoaDest() {
        return pessoaDest;
    }

}
