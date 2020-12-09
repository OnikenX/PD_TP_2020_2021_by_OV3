package pt.isec.LEI.PD.TP20_21.Server.Model.Data;


import pt.isec.LEI.PD.TP20_21.Server.Model.Server;
import pt.isec.LEI.PD.TP20_21.shared.Password;

import java.sql.*;

import static pt.isec.LEI.PD.TP20_21.shared.Utils.Consts.*;

/**
 * Faz manegamento dos objectos na memoria, toma conta dos dados e mexe na database
 */
public class ServerDB {
    //sql vars
    Connection sqlConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "P4ssword@");
    Connection conn;
    Statement stmt;
    Server server;

    public ServerDB(Server server, int server_number) throws SQLException, ClassNotFoundException {
        //sql configs
//STEP 2: Register JDBC driver
        Class.forName(JDBC_DRIVER);
        this.server = server;
//STEP 2: Open a connection
        if (DEBUG)
            System.out.println("Connecting to database...");
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        conn.setAutoCommit(true);
         stmt = conn.createStatement();
//STEP 3: Execute a query

//                rs = stmt.executeQuery("SELECT * FROM utilizadores where id = 0;");
//                rs.next();
//                System.out.println("utilizador 0:"+rs.getString("username")+", hash da password:"+rs.getString("hash"));
    }

    public boolean verifyUser(String username,String password) {
        String hash;
        boolean return_value;
        ResultSet rs = null;
        try {
            rs = executeQuery("SELECT * FROM utilizadores where username =" + username+";");
            if((return_value = rs.next())){
                hash = rs.getString("hash");
            }else {
                return false;
            }
            Password.check(password, hash);
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new Error("Problemas com o sql, sem capacidade de verificar dados.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error("Problemas no verificador de passwords.");
        }
        return return_value;
    }
    
    public boolean userExist(String username) {
        String hash;
        boolean return_value;
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery("SELECT * FROM utilizadores where username ="+username+";");
        return_value = rs.next();
        rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new Error("Error em execução do sql.");
        }
        return return_value;
    }

    public synchronized ResultSet executeQuery(String sqlcommand) throws SQLException {
        var result = stmt.executeQuery(sqlcommand);
        conn.commit();
        return result;
    }

    public  boolean verifyExistenceOf(String table, String condition) throws SQLException {
        ResultSet rs = executeQuery("SELECT * FROM "+table+" where "+condition+";");
        var exists = rs.next();
        rs.close();
        return exists;
    }

    public void addUser(String username, String hash){
        ResultSet rs;
        try {
            rs = stmt.executeQuery("INSERT INTO utilizadores (username, hash)\n" +
                    "VALUES ('"+username+"', '"+hash+"');");
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new Error("Error em execução do sql.");
        }

    }
}


