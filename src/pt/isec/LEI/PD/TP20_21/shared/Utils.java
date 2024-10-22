package pt.isec.LEI.PD.TP20_21.shared;

import java.io.*;
import java.time.Instant;
import java.util.Objects;

public  class Utils {

    static int getObjectSize(Object obj){
        return Objects.requireNonNull(Utils.objectToBytes(obj)).length;
    }

    public static byte[] objectToBytes(Object object) {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutput oo;
        try {
            oo = new ObjectOutputStream(bStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        try {
            oo.writeObject(object);
            oo.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        try {
            oo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] resposta = bStream.toByteArray();
        try {
            bStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resposta;
    }

    public static Object bytesToObject(byte[] bytes) {
        ObjectInputStream iStream = null;
        try {
            iStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Object object = null;
        try {
            object = iStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        try {
            iStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return object;
    }

    public static long getTimeStamp() {
        return Instant.now().getEpochSecond();
    }


    /**
     * Mensagem de pedir a coneção
     */

    public static class Consts {
        /**
         * Error sent quando um user com o mesmo nome existe
         */
        public static final int ERROR_USER_ALREADY_EXISTS = -2;
        /**
         * Erro quando a informação do utilizador nao coincide com o que esta no server
         */
        public static final int ERROR_USER_INFO_NOT_MATCH = -3;
        /**
         * Erro quando existem outras servers menos lotados do que este
         */
        public static final int ERROR_SERVER_FULL = -4;



        /**
         * Mensagem a enviar para fazer um pedido de conecao do cliente ao servidor
         */
        public static final String PEDIR_CONECCAO = "1";


        //multicast
        /**
         * Porta a ser partilhada a todos os server para fazer multicast
         */
        public static final int UDP_MULTICAST_PORT = 8888;

        /**
         * grupo de multicast para se ligar
         */
        public static final String UDP_MULTICAST_GROUP = "228.0.0.1";
        //ainda nao tenho nocao se vou precisar disto
        /**
         * porta para enviar ficheiros e receber
         */
        public final static int FILE_SENDER_PORT = 45567;

        /**
         *  groupo para o gerenciador de ficheiros
         */
        public final static String FILE_SENDER_GROUP ="225";


        /**
         * nome do servidor principal
         */
        public static final String SERVER_ADDRESS = "localhost";

//        /**
//         * maximo de users existente num servidor
//         */
//        public static final int MAX_LOTACAO = 5;


        /**
         * tamanho maximo pedido pelo enunciado para os envios de tcp(em bytes)
         */
        public static final int MAX_SIZE_PER_PACKET = 5 * 1024;


        /**
         * Tempos em que o server vai verificando valores(em segundos)
         */
        public static final int SERVER_VERIFY_SERVERS_TIMER = 15;

        /**
         * Timeout para um servidor ser removido da lista(em segundos)
         */
        public static final int TIMEOUT_PINGS = 50;


        /**
         * Intervalo entre pings
         */
        public static final int PING_INTERVAL = 5;

        public static final int PIPE_BUFFER_SIZE_LIMIT = 1000;

        /**
         * Variavel para um modo de debug
         */
        public static boolean DEBUG = true;

        /**
         * Porta e endereço do servidor par coneção de default para o client se ligar ao servidor
         */
        public static int UDP_CLIENT_REQUEST_PORT = 5555;


        //public static final String USER = System.getenv("PDTP_SERVER_DB_USER") != null ? System.getenv("PDTP_SERVER_DB_USER") : "root";
        //public static final String PASS = System.getenv("PDTP_SERVER_DB_PASS") != null ? System.getenv("PDTP_SERVER_DB_PASS") : "P4ssword@";
    }
}
