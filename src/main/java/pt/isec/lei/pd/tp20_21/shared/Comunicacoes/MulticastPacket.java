package pt.isec.lei.pd.tp20_21.shared.Comunicacoes;

import java.io.Serializable;

/**
 * Class que serve para enviar packotes em muliticast, serve como wrapper para mais informaçao
 */
public  class MulticastPacket implements Serializable {
    private int multicastId;
    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setMulticastId(int multicastId) {
        this.multicastId = multicastId;
    }

    public int getMulticastId() {
        return multicastId;
    }

    public MulticastPacket() {}

    public MulticastPacket(int multicastId) {
        this.multicastId = multicastId;
    }

}
