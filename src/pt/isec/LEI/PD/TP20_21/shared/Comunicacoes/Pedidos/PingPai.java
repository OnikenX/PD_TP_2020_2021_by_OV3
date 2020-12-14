package pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos;

public class PingPai extends Ping {

    private final long canaisDMChecksum;
    private final long canaisGroupoChecksum;
    private final long mensagensChecksum;
    private final long utilizadoresChecksum;


    public PingPai(int lotacao, long serverStartTimestamp,  long canaisChecksum , long canaisDMChecksum, long canaisGroupoChecksum, long mensagensChecksum, long utilizadoresChecksum) {
        super(lotacao, serverStartTimestamp);
        this.canaisDMChecksum = canaisDMChecksum;
        this.canaisGroupoChecksum = canaisGroupoChecksum;
        this.mensagensChecksum = mensagensChecksum;
        this.utilizadoresChecksum = utilizadoresChecksum;
    }


    public long getCanaisDMChecksum() {
        return canaisDMChecksum;
    }

    public long getCanaisGroupoChecksum() {
        return canaisGroupoChecksum;
    }

    public long getMensagensChecksum() {
        return mensagensChecksum;
    }

    public long getUtilizadoresChecksum() {
        return utilizadoresChecksum;
    }
}
