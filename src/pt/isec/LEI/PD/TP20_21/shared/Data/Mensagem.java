package pt.isec.LEI.PD.TP20_21.shared.Data;

import java.util.Calendar;

abstract public class Mensagem {


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



