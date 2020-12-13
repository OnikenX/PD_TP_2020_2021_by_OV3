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
    //Connection sqlConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "P4ssword@");
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
        conn = DriverManager.getConnection(DB_URL + server_number, "server_" + server_number, "W-pass-123");
        conn.setAutoCommit(true);
        stmt = conn.createStatement();
//STEP 3: Execute a query

//                rs = stmt.executeQuery("SELECT * FROM utilizadores where id = 0;");
//                rs.next();
//                System.out.println("utilizador 0:"+rs.getString("username")+", hash da password:"+rs.getString("hash"));
    }

    public boolean verifyUser(String username, String password) {
        String hash;
        boolean return_value;
        ResultSet rs = null;
        try {
            rs = executeQuery("SELECT * FROM utilizadores where username =" + username + ";");
            if ((return_value = rs.next())) {
                hash = rs.getString("hash");
            } else {
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

    public boolean userExist(String username) throws SQLException {
        var rs = stmt.executeQuery("SELECT * FROM utilizadores where username =\"" + username + "\";");
        return rs.next();
    }

    public synchronized ResultSet executeQuery(String sqlcommand) throws SQLException {
        var result = stmt.executeQuery(sqlcommand);
        return result;
    }
    public synchronized int executeUpdate(String sqlcommand) throws SQLException {
        return stmt.executeUpdate(sqlcommand);
    }

    public boolean verifyExistenceOf(String table, String condition) throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + table + " where " + condition + ";");
        var exists = rs.next();
        rs.close();
        return exists;
    }

    public int addUser(String username, String name, String hash) throws SQLException {
            return stmt.executeUpdate("INSERT INTO utilizadores (username,nome, hash)\n" +
                    "VALUES ('" + username + "', '"+name+"', '" + hash + "');");
    }

    /**
     * Verfica se o canal dm com a pessoa1 e 2 existem
     */
    public int verifyDMExists(String pessoa1, String pessoa2) throws SQLException {
        var rs =stmt.executeQuery("select * from canaisDM where (pessoaDest = '"+pessoa1+"' and pessoaDest = '"+pessoa2+"') or  (pessoaDest = pessoa2  and pessoaDest = lol)");
        if(rs.next())
            return rs.getInt("canal_id");
        return -1;
    }



    public synchronized ResultSet addDmUser(String username) throws SQLException {
        ResultSet rs = executeQuery("SELECT * FROM " + "utilizadores WHERE nome=" + username);
        return rs;
    }
}


