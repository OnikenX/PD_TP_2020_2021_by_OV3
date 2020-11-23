package pt.isec.LEI.PD.TP20_21.shared;

import java.io.Serializable;

public class IpServidor implements Serializable, Cloneable{
    private final String ip;
    private final int port;

    public IpServidor(String ip, int port) {
        this.port = port;
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
