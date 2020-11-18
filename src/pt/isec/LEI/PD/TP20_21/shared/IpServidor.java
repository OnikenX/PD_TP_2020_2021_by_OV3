package pt.isec.LEI.PD.TP20_21.shared;

import java.io.Serializable;

import static pt.isec.LEI.PD.TP20_21.shared.Consts.MAX_LOTACAO;

public class IpServidor implements Serializable {

    private int lotacao;
    //    private final int maxlotacao = MAX_LOTACAO;
    private final String ip;
    private final int port;

    IpServidor(String ip, int port, int lotacao) {

        this.ip = ip;
        this.port = port;
        this.setLotacao(0);
//        this.lotacao = lotacao; // nao me lembro se era necessario dar a lotacao
    }

    public double getLotacao() {
        return lotacao;
    }

    public void setLotacao(int lotacao) {
        if ((lotacao >= 0) && (lotacao <= MAX_LOTACAO))
            this.lotacao = lotacao;
    }

    public String getIp() {
        return ip;
    }


    public int getPort() {
        return port;
    }

}
