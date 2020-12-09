package pt.isec.LEI.PD.TP20_21.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static pt.isec.LEI.PD.TP20_21.shared.Utils.Consts.*;

public class CriaBaseDeDados {
    Connection conn = null;//connecao à base de dados
    Statement stmt = null;//mesagem a enviar
    boolean rs;//resultado

    public static void main(String[] args) throws SQLException {
        new CriaBaseDeDados(args);
    }

    CriaBaseDeDados(String[] args) throws SQLException {
        if (args.length < 1) {
            System.out.println("precisa de correr o prgrama da seguinte forma:");
            System.out.println("java CriarBaseDeDados <root_user> <root_password> <numero de base de dados e users a criar> <resetar ou nao os dbs (true/false)>");
        }
        boolean resetar = false;
        int numeros;
        try {
            numeros = Integer.parseInt(args[2]);
            if (args.length >= 4)
                resetar = Boolean.parseBoolean(args[3]);
        } catch (Exception e) {
            System.out.println("É preciso de um numero no ultimo argumento.");
            return;
        }

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection("jdbc:mysql://" + DB_ADDRESS + ":" + DB_PORT, args[0], args[1]);
            stmt = conn.createStatement();

        } catch (Exception e) {
            System.out.println("Problemas lol");
            e.printStackTrace();
            return;
        }
        rs = stmt.execute("SET GLOBAL time_zone = '+0:00';");


        for (int i = 0; i < numeros; ++i)
            sql(i, resetar);

    }


    void sql(int i, boolean resetar) {

        try {
            stmt.execute("CREATE DATABASE IF NOT EXISTS messager_db_"+i+";");
            stmt.execute("USE messager_db_"+i+";");
            if (false) {
                stmt.execute(
                        "DROP TABLE IF EXISTS mensagens;\n" +
                                "DROP TABLE IF EXISTS canais;\n" +
                                "DROP TABLE IF EXISTS utilizadores;\n" +
                                "drop table if exists ficheiros;\n");
            }
            stmt.execute("create table if not exists ficheiros" +
                    "(" +
                    "    id   int  not null auto_increment unique," +
                    "    nome text not null," +
                    "    PRIMARY KEY (id)" +
                    ");");
            stmt.execute("create table if not exists utilizadores\n" +
                    "(\n" +
                    "    id           int         not null auto_increment unique,\n" +
                    "    username     varchar(50) not null unique,\n" +
                    "    hash         text        not null,\n" +
                    "    profilepicId int         not null default 1,\n" +
                    "    PRIMARY KEY (id),\n" +
                    "    FOREIGN KEY (profilepicId) REFERENCES ficheiros (id)\n" +
                    ");");
                    stmt.execute( "create table if not exists canais\n" +
                    "(\n" +
                    "    id          int  not null auto_increment unique,\n" +
                    "    nome        text not null,\n" +
                    "    descricao   text,\n" +
                    "    password    text not null,\n" +
                    "    moderadorId int  not null,\n" +
                    "    PRIMARY KEY (id),\n" +
                    "    foreign key (moderadorId) references utilizadores (id)\n" +
                    ");\n" );
                    stmt.execute("create table if not exists mensagens(\n"+
                    "    id            int                                 not null auto_increment unique,\n" +
                    "    dataHoraEnvio timestamp DEFAULT CURRENT_TIMESTAMP not null,\n" +
                    "    authorId      int                                 not null,\n" +
                    "    canalId       int                                 not null,\n" +
                    "    isAFile       bool                                not null,\n" +
                    "    mensagem      text,\n" +
                    "    fileId        int,\n" +
                    "    PRIMARY KEY (id),\n" +
                    "    FOREIGN KEY (authorId) REFERENCES utilizadores (id),\n" +
                    "    foreign key (canalId) references canais (id),\n" +
                    "    foreign key (fileId) references ficheiros(id)\n" +
                    ");");
                    stmt.execute("CREATE USER IF NOT EXISTS `server_"+i+"`@`localhost` IDENTIFIED BY 'W-pass-123';\n" );
                    stmt.execute("GRANT SELECT, INSERT, UPDATE, DELETE ON * TO `server_"+i+"`@`localhost`;\n" );

                    stmt.execute("FLUSH PRIVILEGES;" );

                   stmt.executeQuery( "COMMIT;");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
