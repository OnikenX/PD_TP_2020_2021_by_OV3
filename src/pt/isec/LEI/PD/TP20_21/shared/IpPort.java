package pt.isec.LEI.PD.TP20_21.shared;

import java.io.Serializable;

/**
 * Guarda um par ip/port para depois serem usados
 */
public class IpPort implements Serializable, Cloneable{
    public final String ip;
    public final int port;

    public IpPort(String ip, int port) {
        this.port = port;
        this.ip = ip;
    }

}
