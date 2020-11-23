package pt.isec.LEI.PD.TP20_21.Server.Model;

import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.io.Serializable;
import java.util.LinkedList;

import static pt.isec.LEI.PD.TP20_21.shared.Utils.Consts.TIMEOUT_PINGS;

/**
 * Guarda a lista de servidores para o Server
 */
public class IpServidores extends LinkedList<IpServidorServidor> implements Serializable {

    public void removeTimedOut() {
        var it = this.listIterator();
        if ((Utils.getTimeStamp() - it.next().getActualizado()) > TIMEOUT_PINGS)
            it.remove();
        while (it.hasNext()) {
            if ((Utils.getTimeStamp() - it.next().getActualizado()) > TIMEOUT_PINGS)
                it.remove();
            it.next();
        }
    }
}