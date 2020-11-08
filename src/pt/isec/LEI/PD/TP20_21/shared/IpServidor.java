package pt.isec.LEI.PD.TP20_21.shared;

public class IpServidor {
    public final String ip;
    public final int port;

    IpServidor(String ip, int port) throws Exception {
        this.ip = ip;
        this.port = port;
    }
}
