package pt.isec.LEI.PD.TP20_21.Server;

import java.sql.*;

import static pt.isec.LEI.PD.TP20_21.shared.Utils.Consts.*;
import static pt.isec.LEI.PD.TP20_21.shared.Utils.Consts.PASS;

public class CriaBaseDeDados {
    public static void main(String[] args) {
        if(args.length<1){
            System.out.println("precisa de correr o prgrama da seguinte forma:");
            System.out.println("java CriarBaseDeDados <root_user> <root_password> <numero de base de dados e users a criar>");
        }
        try{Integer.parseInt(args[2]);}catch(Exception e){
            System.out.println("É preciso de um numero no ultimo argumento.");
            return;
        }
    }

    static void sql(String user, String password, int numero) {
        Connection conn = null;//connecao à base de dados
        Statement stmt = null;//mesagem a enviar
        ResultSet rs = null; //resultado
        int id = -1;
        try {
            Class.forName(JDBC_DRIVER);

            System.out.println("Connecting to database...");

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM utilizadores;");
            int i = 0;
            while (rs.next())
                System.out.println("utilizador "+ (++i) +":" + rs.getString("username") + ", hash da password:" + rs.getString("hash"));
            if (i == 0)
                System.out.println("nao existem users");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
