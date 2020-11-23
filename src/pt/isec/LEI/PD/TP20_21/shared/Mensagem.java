package pt.isec.LEI.PD.TP20_21.shared;

import java.util.Calendar;

abstract public class Mensagem {
    final public int destinatarioId;   //Quem recebe
    final public int remetenteId;      //Quem manda
    final public Calendar dataHoraEnvio; //Data e hora que a mensagem foi enviada

    protected Mensagem(int destinatarioId, int remetenteId, Calendar dataHoraEnvio) {
        this.destinatarioId = destinatarioId;
        this.remetenteId = remetenteId;
        this.dataHoraEnvio = dataHoraEnvio;
    }
}



