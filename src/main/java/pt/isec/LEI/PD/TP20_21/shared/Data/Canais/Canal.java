package pt.isec.LEI.PD.TP20_21.shared.Data.Canais;

import pt.isec.LEI.PD.TP20_21.shared.Data.DataBase;

public abstract class Canal  implements DataBase {
    private final int id;
    private final int pessoaCria;

    public Canal(int id, int pessoaCria) {
        this.id = id;
        this.pessoaCria = pessoaCria;
    }


    public int getId() {
        return id;
    }

    public int getPessoaCria() {
        return pessoaCria;
    }

}
