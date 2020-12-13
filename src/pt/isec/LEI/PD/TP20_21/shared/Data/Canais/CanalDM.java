package pt.isec.LEI.PD.TP20_21.shared.Data.Canais;

public class CanalDM extends Canal {
    private final int pessoaDest;
    public CanalDM(int id, int pessoaCria, int pessoaDest) {
        super(id, pessoaCria);
        this.pessoaDest = pessoaDest;
    }


    public int getPessoaDest() {
        return pessoaDest;
    }
}
