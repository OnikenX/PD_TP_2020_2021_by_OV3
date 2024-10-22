package pt.isec.LEI.PD.TP20_21.shared.Data;

import javax.xml.crypto.Data;
import java.sql.Timestamp;

public class Mensagem implements DataBase {
    private final int id ;
    private final Timestamp dataHoraEnvio ;
    private final int authorId;
    private final int canalId;
    private final boolean isAFile;
    private final String mensagem;

    public Mensagem(int id, Timestamp dataHoraEnvio, int authorId, int canalId, boolean isAFile, String mensagem) {
        this.id = id;
        this.dataHoraEnvio = dataHoraEnvio;
        this.authorId = authorId;
        this.canalId = canalId;
        this.isAFile = isAFile;
        this.mensagem = mensagem;
    }
    @Override
    public int getId() {
        return id;
    }

    public Timestamp getDataHoraEnvio() {
        return dataHoraEnvio;
    }

    public int getAuthorId() {
        return authorId;
    }

    public int getCanalId() {
        return canalId;
    }

    public boolean isAFile() {
        return isAFile;
    }

    public String getMensagem() {
        return mensagem;
    }
}



