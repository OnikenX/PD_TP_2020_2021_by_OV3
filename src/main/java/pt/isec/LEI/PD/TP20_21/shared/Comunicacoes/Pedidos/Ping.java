package pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos;

import java.io.Serializable;

public class Ping implements Serializable, Pedido {
    private final int lotacao;
//        public static int SIZE = (Objects.requireNonNull(objectToBytes(new Ping())).length);
    private final long serverStartTimestamp;

    public Ping(int lotacao,long serverStartTimestamp) {
        this.lotacao = lotacao;this.serverStartTimestamp = serverStartTimestamp;
    }

    public int getLotacao() {
        return lotacao;
    }
    public long getServerStartTimestamp() {
        return serverStartTimestamp;
    }

}
