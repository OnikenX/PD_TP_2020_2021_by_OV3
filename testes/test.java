import pt.isec.LEI.PD.TP20_21.Server.Model.Data.ServerDB;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos.Conectar;
import pt.isec.LEI.PD.TP20_21.shared.Password;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.sql.*;
import java.util.Objects;

import static pt.isec.LEI.PD.TP20_21.Server.Model.Data.ServerDB.DB_URL;
import static pt.isec.LEI.PD.TP20_21.Server.Model.Data.ServerDB.JDBC_DRIVER;
import static pt.isec.LEI.PD.TP20_21.shared.Utils.objectToBytes;

class supertest implements Serializable{}


public class test extends supertest {
    public static void main(String[] args) {
        System.out.println(test.class instanceof Serializable);

    }



    static void someSqlTest() throws SQLException, InterruptedException {
        Connection conn = conn = DriverManager.getConnection(DB_URL + 1, "server_1", "W-pass-123");
        conn.setAutoCommit(true);
        var stmt = conn.createStatement();
        var rs = stmt.executeQuery("checksum table " + ServerDB.DB_TABLE + 1 + ".utilizadores;");
        Thread.sleep(10);
        System.out.println("vou imprimir:");
        if (rs.next())
            System.out.println(rs.getLong(2));
        long returned=-1;
        rs = stmt.executeQuery("checksum table " + ServerDB.DB_TABLE + 1 + ".mensagens;");
        if(rs.next())
            returned = rs.getLong(2);
        rs.close();
//        return returned;
    }

    static void testSender() throws IOException {
        new Thread(() -> {
            try {//envia coisas
                InetAddress group = InetAddress.getByName("228.0.0.1");
                MulticastSocket so = new MulticastSocket(2345);
                so.joinGroup(group);
                var str = new String("lol");
                var buff = str.getBytes();
                while (true) {
                    Thread.sleep(1000);
                    so.send(new DatagramPacket(buff, buff.length, group, 8888));
                    System.out.println("Sent \"lol\"...");
                }
            } catch (Exception ignored) {
            }
        }).start();
    }

    static void testReceive() throws IOException {
        new Thread(() -> {
            try {//recebe mensagens
                InetAddress group = InetAddress.getByName("228.0.0.1");
                MulticastSocket s = new MulticastSocket(8888);
                s.joinGroup(group);
                var buff = new byte[100];
                while (true) {
                    var pack = new DatagramPacket(buff, buff.length, group, 8888);
                    s.receive(pack);
                    System.out.println("Received " + new String(pack.getData()));
                }
            } catch (Exception ignored) {
            }
        }).start();

    }

    static void testFileTypes() {
        try (FileInputStream file = new FileInputStream("/home/onikenx/IdeaProjects/PD_TP_2020_2021_by_OV3/files_server/testfile")) {
            byte[] bytes = new byte[1];
            int nbytes;
            System.out.println("what");
            System.out.println(file.available());
            System.out.println("what?");
            if (file.available() > 3) {
                file.skip(2);
                nbytes = file.read(bytes);
                if (nbytes > 0)
                    System.out.println(new String(bytes));
                file.skip(-2);
                nbytes = file.read(bytes);
                if (nbytes > 0)
                    System.out.println(new String(bytes));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static void somethingDontKnowLol() {
        String lol = "00000000000000000000000000000000000000000000000000000000000000";
        System.out.println(Objects.requireNonNull(Objects.requireNonNull(objectToBytes(new Conectar()))).length);
        createThread();
        System.out.println("started thread");
        try {
            Thread.sleep(3000);

            System.out.println("finishing program...");
            Thread.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static public void createThread() {
        new thread();
    }


    //testar o password hashing
    static void testhash() {
        String hash = null, password = "lol";
        try {
            hash = Password.getSaltedHash(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(password + ":" + hash);
        try {
            assert hash != null;
            System.out.println("Match:" + Password.check(password, hash));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //brincar com a database
    static void sql() {
        Connection conn = null;//connecao à base de dados
        Statement stmt = null;//mesagem a enviar
        ResultSet rs = null;//resultado

        int id = -1;
        try {
            Class.forName(JDBC_DRIVER);

//STEP 2: Open a connection
            System.out.println("Connecting to database...");

            //conn = DriverManager.getConnection(DB_URL, USER, PASS);

//STEP 3: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM utilizadores;");
            int i = 0;
            while (rs.next())
                System.out.println("utilizador " + (++i) + ":" + rs.getString("username") + ", hash da password:" + rs.getString("hash"));
            if (i == 0)
                System.out.println("nao existem users");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    //testar multiplas thread num so objecto -> nao resulta
    static void test() {
        //        LinkedList<IpServidor> ll = new LinkedList<>();
//        Respostas.RUdpClientServerPreConnection resposta = new Respostas.RUdpClientServerPreConnection(6969, ll);
//        System.out.println("size resposta + linkedlist empty:" + Objects.requireNonNull(Utils.objectToBytes(resposta)).length);
//        ll.add(new IpServidor("255.255.255.255", 6969));
//        System.out.println("Size ipservidor:"+ Objects.requireNonNull(Utils.objectToBytes(ll.get(0))).length);
//        System.out.println("size resposta + linkedlist com 1 elemento:"+ Objects.requireNonNull(Utils.objectToBytes(resposta)).length);
//        while(Objects.requireNonNull(Utils.objectToBytes(resposta)).length < 5000){
//            ll.add(new IpServidor("255.255.255.255", 6969));
//        }
//        System.out.println("Maximo de ips => " + (ll.size()-1));
        var lol = new thread();
        System.out.println("teste 1");
        lol.start();
        System.out.println("teste 2");
        lol.start();
        System.out.println("teste 3");
        lol.start();
        System.out.println("teste 4");
        lol.start();
        System.out.println("teste 5");
        lol.start();
    }


    static class thread extends Thread {
        public static int conas = 1;

        public thread() {
            setDaemon(true);
            start();
        }

        @Override
        public void run() {
            while (true) {
                System.out.println("ping...");
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
