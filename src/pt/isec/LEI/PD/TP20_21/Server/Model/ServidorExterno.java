package pt.isec.LEI.PD.TP20_21.Server.Model;

import pt.isec.LEI.PD.TP20_21.shared.IpPort;

import java.io.Serializable;

import static pt.isec.LEI.PD.TP20_21.shared.Utils.getTimeStamp;
import static pt.isec.LEI.PD.TP20_21.shared.Utils.Consts.MAX_LOTACAO;

/**
 * Guarda as informações de um servidor externo
 */
public class ServidorExterno extends IpPort implements Comparable<ServidorExterno>, Serializable, Cloneable{

    private int lotacao;

    /**
     * ultima vez que o server foi actualizado
     */
    private long actualizado;

    ServidorExterno(String ip, int port, int lotacao) {
        super(ip, port);
        this.setLotacao(lotacao);
        setActualizado();
    }

    public IpPort getForClient()  {
        try {
            return (IpPort)super.clone();
        } catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return null;
    }


    public int getLotacao() {
        return lotacao;
    }

    public void setLotacao(int lotacao) {
        if ((lotacao >= 0) && (lotacao <= MAX_LOTACAO))
            this.lotacao = lotacao;
        setActualizado();
    }

    public long getActualizado() {
        return actualizado;
    }

    public void setActualizado() {
        this.actualizado = getTimeStamp();
    }

    /**
     * @param ipss do tipo {@link ServidorExterno}
     * @return diferença de locação
     */
    @Override
    public int compareTo(ServidorExterno ipss){
        return lotacao - ipss.getLotacao();
    }
}
