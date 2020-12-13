package pt.isec.LEI.PD.TP20_21.shared.Data.Canais;

public abstract class Canal {
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
