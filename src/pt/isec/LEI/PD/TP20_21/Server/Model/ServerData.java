package pt.isec.LEI.PD.TP20_21.Server.Model;


import java.sql.*;

import static pt.isec.LEI.PD.TP20_21.shared.Utils.Consts.*;

/**
 * Faz manegamento dos objectos na memoria, toma conta dos dados e mexe na database
 */
public class ServerData {
    private Servidores servers;

    //sql vars
    Connection sqlConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "P4ssword@");
    Connection conn;
    Statement stmt;
    public ServerData() throws SQLException, ClassNotFoundException {
        //sql configs
//STEP 2: Register JDBC driver
        Class.forName(JDBC_DRIVER);
//STEP 2: Open a connection
        if (DEBUG)
            System.out.println("Connecting to database...");
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
         stmt = conn.createStatement();
//STEP 3: Execute a query

//                rs = stmt.executeQuery("SELECT * FROM utilizadores where id = 0;");
//                rs.next();
//                System.out.println("utilizador 0:"+rs.getString("username")+", hash da password:"+rs.getString("hash"));

    }

    public boolean userExist(String username) throws SQLException {
        String hash;
        ResultSet rs = executeQuery("SELECT * FROM utilizadores where username =" + username+";");
        if(rs.next()){
            rs.getString("hash");
        }

    }
    public synchronized ResultSet executeQuery(String sqlcommand) throws SQLException {
        return stmt.executeQuery(sqlcommand);
    }

    public  boolean verifyExistenceOf(String table, String condition) throws SQLException {
        ResultSet rs = executeQuery("SELECT * FROM "+table+" where "+condition+";");
        var exists = rs.next();
        rs.close();
        return exists;
    }

    public synchronized Servidores getServers() {
        return servers;
    }

    public void addUser(String username, String hash) {
        executeQuery("");
    }
}


