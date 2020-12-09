package pt.isec.LEI.PD.TP20_21.shared;

import java.util.Calendar;

abstract public class Mensagem {
    final public int destinatarioId;   //Quem recebe
    final public int remetenteId;      //Quem manda
    final public Calendar dataHoraEnvio; //Data e hora que a mensagem foi enviada
    final public String conteudo;

    protected Mensagem(int destinatarioId, int remetenteId, Calendar dataHoraEnvio, String conteudo) {
        this.destinatarioId = destinatarioId;
        this.remetenteId = remetenteId;
        this.dataHoraEnvio = dataHoraEnvio;
        if(conteudo.length() > 255)
            throw new StringIndexOutOfBoundsException("String demasiado grande, escreve mensagens com menos characters.");
        this.conteudo = conteudo;
    }

    public int getDestinatarioId() {
        return destinatarioId;
    }

    public int getRemetenteId() {
        return remetenteId;
    }

    public Calendar getDataHoraEnvio() {
        return dataHoraEnvio;
    }

    public String getConteudo() {
        return conteudo;
    }
}



