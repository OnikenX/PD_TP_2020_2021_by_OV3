package pt.isec.LEI.PD.TP20_21.Server.Model;


import pt.isec.LEI.PD.TP20_21.Server.Model.Servidores;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Faz manegamento dos objectos na memoria, toma conta dos dados e mexe na database
 */
 public class ServerData {
    private Servidores servers;
    public ServerData() throws SQLException {
        this.servers = new LinkedList<>();
        Connection sqlConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root","P4ssword@");

    }


    public synchronized Servidores getServers() {
        return servers;
    }
}


