package pt.isec.LEI.PD.TP20_21.shared;

public class IpServidor {
    public final double lotacao;
    public final String ip;
    public final int port;

    IpServidor(String ip, int port, double lotacao) {
        this.ip = ip;
        this.port = port;
        this.lotacao = lotacao; // nao me lembro se era necessario dar a lotacao
    }
}
