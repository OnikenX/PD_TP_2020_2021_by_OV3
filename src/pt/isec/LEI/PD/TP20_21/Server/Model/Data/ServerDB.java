package pt.isec.LEI.PD.TP20_21.Server.Model.Data;


import pt.isec.LEI.PD.TP20_21.Server.Model.Server;
import pt.isec.LEI.PD.TP20_21.shared.Password;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.sql.*;

import static pt.isec.LEI.PD.TP20_21.shared.Utils.Consts.*;

/**
 * Faz manegamento dos objectos na memoria, toma conta dos dados e mexe na database
 */
public class ServerDB {
    private final Statement statement;
    private final Server server;
    private boolean updated;

    public ServerDB(Server server, int server_number) throws SQLException, ClassNotFoundException {
        //sql configs
//STEP 2: Register JDBC driver
        Class.forName(JDBC_DRIVER);
        this.server = server;
//STEP 2: Open a connection
        if (DEBUG)
            System.out.println("Connecting to database...");
        //sql vars
        //Connection sqlConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "P4ssword@");
        Connection conn = DriverManager.getConnection(DB_URL + server_number, "server_" + server_number, "W-pass-123");
        conn.setAutoCommit(true);
        statement = conn.createStatement();
//STEP 3: Execute a query

//                rs = stmt.executeQuery("SELECT * FROM utilizadores where id = 0;");
//                rs.next();
//                System.out.println("utilizador 0:"+rs.getString("username")+", hash da password:"+rs.getString("hash"));
    }

    public long getChecksum(String tabela) throws SQLException {
        long returned=-1;
        var rs = statement.executeQuery("checksum table " + Utils.Consts.DB_TABLE + server.server_number + "."+tabela+";");
        if(rs.next())
            returned = rs.getLong(2);
        rs.close();
        return returned;
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
        var rs = statement.executeQuery("SELECT * FROM utilizadores where username =\"" + username + "\";");
        return rs.next();
    }

    public synchronized ResultSet executeQuery(String sqlcommand) throws SQLException {
        var result = statement.executeQuery(sqlcommand);
        return result;
    }
    public synchronized int executeUpdate(String sqlcommand) throws SQLException {
        return statement.executeUpdate(sqlcommand);
    }

    public boolean verifyExistenceOf(String table, String condition) throws SQLException {
        ResultSet rs = executeQuery("SELECT * FROM " + table + " where " + condition + ";");
        var exists = rs.next();
        rs.close();
        return exists;
    }

    public int addUser(String username, String name, String hash) throws SQLException {
            return statement.executeUpdate("INSERT INTO utilizadores (username,nome, hash)\n" +
                    "VALUES ('" + username + "', '"+name+"', '" + hash + "');");
    }

    public boolean isUpdated() {
        return updated;
    }
}


