import pt.isec.LEI.PD.TP20_21.shared.Password;

import java.sql.*;

import static pt.isec.LEI.PD.TP20_21.shared.Utils.Consts.*;

public class test {
    public static void main(String[] args) {
        sql();

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

        int id = 0;
        try {
            Class.forName(JDBC_DRIVER);

//STEP 3: Open a connection
            System.out.println("Connecting to database...");

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

//STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM utilizadores where id = 1;");
            rs.next();
            System.out.println("utilizador 1:"+rs.getString("username")+", hash da password:"+rs.getString("hash"));
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

        @Override
        public void run() {
            super.run();
            int threadnumber = conas++;
            System.out.println("testing thread " + threadnumber + "...");
            try {
                sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("testing thread " + threadnumber + "...");
        }
    }

}
