package pt.isec.LEI.PD.TP20_21.shared.FileTransfer;


public class FilePacket {


    public enum FileType {profilePic, message;}

    /**
     * where to start to write
     */
    private long start;
    private final FileType fileType;
    private boolean complete;
    public final int fileId;
    private byte[] content;


    public void setStart(long start) {
        this.start = start;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public boolean isComplete() {
        return complete;
    }

    public long getStart() {
        return start;
    }

    public void setComplete() {
        complete = true;
    }

    public FileType getFileType() {
        return fileType;
    }

    public byte[] getContent() {
        return content;
    }


    public FilePacket(int fileId,long start, FileType fileType, byte[] content) {
        this.fileId = fileId;
        this.complete = false;
        this.start = start;
        this.fileType = fileType;
        this.content = content;
    }
}
