package pt.isec.LEI.PD.TP20_21.shared.Comunicacoes;

import pt.isec.LEI.PD.TP20_21.shared.FileTransfer.FilePacket;
import pt.isec.LEI.PD.TP20_21.shared.IpPort;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Classes que vão ser enviadas em resposta de algo
 */

public class Respostas implements Serializable {
    /**
     * Resposta para para o cliente quando se esta a pedir coneção tcp ao servidor.
     * Se a variavel TcpPort -1 quer dizer que o servidor não aceitou a ligação.
     */
    public static class PedidoDeLigar implements Serializable{
        public final LinkedList<IpPort> servers;
        public final int TcpPort;

        public PedidoDeLigar(int TcpPort, LinkedList<IpPort> servers) {
            this.TcpPort = TcpPort;
            this.servers = servers;
        }
        public PedidoDeLigar(){
            this.TcpPort = -1;
            this.servers = null;
        }

    }

    public static class FileOutOfSync implements Serializable{
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

}
