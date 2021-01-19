package pt.isec.lei.pd.tp20_21.Server;

import pt.isec.lei.pd.tp20_21.Server.Model.Data.ServerDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
            Class.forName(ServerDB.JDBC_DRIVER);
            conn = DriverManager.getConnection("jdbc:mysql://" + ServerDB.DB_ADDRESS + ":" + ServerDB.DB_PORT + "?useTimezone=true&serverTimezone=UTC", args[0], args[1]);
            stmt = conn.createStatement();

        } catch (Exception e) {
            System.out.println("Problemas lol");
            e.printStackTrace();
            return;
        }
        rs = stmt.execute("SET GLOBAL time_zone = '+0:00';");


        for (int i = 0; i < numeros; ++i)
            sql(i+1, resetar);

    }

    void sql(int i, boolean resetar) {
        try {
            if (resetar)
                stmt.execute("DROP schema if exists messager_db_" + i + ";");
            stmt.execute("CREATE DATABASE IF NOT EXISTS messager_db_" + i + ";");
            stmt.execute("USE messager_db_" + i + ";");
            stmt.execute("create table if not exists utilizadores\n" +
                    "(\n" +
                    "    id       int         not null auto_increment unique,\n" +
                    "    username varchar(64) not null unique,\n" +
                    "    nome     text        not null,\n" +
                    "    hash     text        not null,\n" +
                    "    PRIMARY KEY (id)\n" +
                    ");\n" +
                    "\n");
            stmt.execute("create table if not exists canais\n" +
                    "(\n" +
                    "    id int not NULL unique auto_increment,\n" +
                    "    pessoaCria   int  not null,\n" +
                    "    primary key (id),\n" +
                    "    foreign key (pessoaCria) references utilizadores (id) ON DELETE CASCADE\n" +
                    ");\n" +
                    "\n" +
                    "\n");
            stmt.execute("create table if not exists canaisGrupo\n" +
                    "(\n" +
                    "    id    int not null,\n" +
                    "    nome        text not null unique,\n" +
                    "    descricao   text,\n" +
                    "    password    text not null,\n" +
                    "    foreign key (id) references canais (id) ON DELETE CASCADE\n" +
                    ");");

            //criar canais, para saber de que para quem e a mensagem
            stmt.execute("create table if not exists canaisDM\n" +
                    "(\n" +
                    "    id    int not null,\n" +
                    "    pessoaDest   int  not null,\n" +
                    "    foreign key (id) references canais (id) ON DELETE CASCADE,\n" +
                    "    foreign key (pessoaDest) references utilizadores (id) ON DELETE CASCADE\n" +
                    ");");
            stmt.execute("create table if not exists mensagens\n" +
                    "(\n" +
                    "    id            int                                 not null auto_increment unique,\n" +
                    "    dataHoraEnvio timestamp DEFAULT CURRENT_TIMESTAMP not null,\n" +
                    "    authorId      int                                 not null,\n" +
                    "    canalId       int                                 not null,\n" +
                    "    isAFile       bool                                not null,\n" +
                    "    mensagem      varchar(900),\n" +
                    "    PRIMARY KEY (id),\n" +
                    "    FOREIGN KEY (authorId) REFERENCES utilizadores (id) ON DELETE CASCADE,\n" +
                    "    foreign key (canalId) references canais (id) ON DELETE CASCADE\n" +
                    ");");
            stmt.execute("CREATE USER IF NOT EXISTS `server_" + i + "`@`localhost` IDENTIFIED BY 'W-pass-123';\n");
            stmt.execute("GRANT SELECT, INSERT, UPDATE, DELETE ON * TO `server_" + i + "`@`localhost`;\n");

            stmt.execute("FLUSH PRIVILEGES;");

            stmt.executeQuery("COMMIT;");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
