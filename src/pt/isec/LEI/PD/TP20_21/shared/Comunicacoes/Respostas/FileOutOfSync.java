package pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Respostas;

import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Respostas.Respostas;
import pt.isec.LEI.PD.TP20_21.shared.FileTransfer.FilePacket;

import java.io.Serializable;

public class FileOutOfSync implements Serializable, Respostas {
    private int fileId;
    private FilePacket.FileType fileType;
    private long resetTo;

    public FileOutOfSync(int fileId, FilePacket.FileType fileType, long resetTo) {
        this.fileId = fileId;
        this.fileType = fileType;
        this.resetTo = resetTo;
    }

    public int getFileId() {
        return fileId;
    }

    public FilePacket.FileType getFileType() {
        return fileType;
    }

    public long getResetTo() {
        return resetTo;
    }

}
