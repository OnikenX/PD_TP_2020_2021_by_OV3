package pt.isec.LEI.PD.TP20_21.shared;

import java.io.*;
import java.time.Instant;

public class Utils {
    public static byte[] objectToBytes(Object object)  {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutput oo = null;
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

    public static long getTimeStamp(){
        return Instant.now().getEpochSecond();
    }


    /**
     * Mensagem de pedir a coneção
     */

    public static class Consts {

        /**
         * Mensagem a enviar para fazer um pedido de conecao do cliente ao servidor
         */
        public static final String PEDIR_CONECCAO = "1";
        /**
         * Porta a ser partilhada a todos os server para fazer multicast
         */
        public static final int UDP_MulticastServerPort = 5432;
        public static final String ServerAddress = "localhost";
        /**
         * maximo de users existente num servidor
         */
        public static final int MAX_LOTACAO = 5;


        /**
         * tamanho maximo pedido pelo enunciado para os envios de tcp(em bytes)
         */
        public static final int MAX_SIZE_PER_PACKET = 5000;




        /**
         * Tempos em que o cliente vai verificando valores(em segundos)
         */
        public static final int VERIFICA_SERVERS = 15;
        /**
         * Timeout para um servidor ser removido da lista(em segundos)
         */
        public static final int TIMEOUT_PINGS = 30;

        public static boolean DEBUG = true;
        /**
         * Porta e endereço do servidor par coneção de default para o client se ligar ao servidor
         */
        public static int UDPClientRequestPort = 6969 ;



        //DATABASE STUFF
        public static final  String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        public static final int DB_PORT = 3306;
        public static final String DB_ADDRESS = "localhost";
        public static final String DB_TABLE = "messager_db";
        public static final String DB_URL = "jdbc:mysql://" + DB_ADDRESS + ":"+DB_PORT+"/" + DB_TABLE;
        public static final String USER = System.getenv("PDTP_SERVER_DB_USER")!= null ? System.getenv("PDTP_SERVER_DB_USER") :"root";
        public static final String PASS = System.getenv("PDTP_SERVER_DB_PASS")!= null ? System.getenv("PDTP_SERVER_DB_PASS") :"P4ssword@";
    }
}
