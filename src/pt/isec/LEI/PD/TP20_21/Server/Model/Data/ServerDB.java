package pt.isec.LEI.PD.TP20_21.Server.Model.Data;


import pt.isec.LEI.PD.TP20_21.Server.Model.Server;
import pt.isec.LEI.PD.TP20_21.shared.Data.Canais.CanalDM;
import pt.isec.LEI.PD.TP20_21.shared.Data.Canais.CanalGrupo;
import pt.isec.LEI.PD.TP20_21.shared.Data.Mensagem;
import pt.isec.LEI.PD.TP20_21.shared.Data.Utilizador.UtilizadorServer;
import pt.isec.LEI.PD.TP20_21.shared.Password;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import static pt.isec.LEI.PD.TP20_21.shared.Utils.Consts.DEBUG;

/**
 * Faz manegamento dos objectos na memoria, toma conta dos dados e mexe na database
 */
public class ServerDB {
    //DATABASE STUFF
    private final Statement statement;
    private final Server server;
    private final Connection conn;
    //table names
    public final static String table_canais = "canais";
    public final static String table_utilizadores = "utilizadores";
    public final static String table_canaisDM = "canaisDM";
    public final static String table_canaisGrupo = "canaisGrupo";
    public final static String table_mensagens = "mensagens";

    //connections
    public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final int DB_PORT = 3306;
    public static final String DB_ADDRESS = "localhost";
    public static final String DB_TABLE = "messager_db_";
    public static final String DB_URL = "jdbc:mysql://" + DB_ADDRESS + ":" + DB_PORT + "/" + DB_TABLE;

    public int canal_id_max;
    public int mensagem_id_max;
    public int utilizador_id_max;


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
        conn = DriverManager.getConnection(DB_URL + server_number, "server_" + server_number, "W-pass-123");
        conn.setAutoCommit(true);

        statement = conn.createStatement();
//STEP 3: Execute a query

//                rs = stmt.executeQuery("SELECT * FROM utilizadores where id = 0;");
//                rs.next();
//                System.out.println("utilizador 0:"+rs.getString("username")+", hash da password:"+rs.getString("hash"));
    }

    private synchronized Statement getStatement() {
        return statement;
    }

    public long getChecksum(String tabela) throws SQLException {
        long returned = -1;
        var rs = getStatement().executeQuery("checksum table " + DB_TABLE + server.server_number + "." + tabela + ";");
        if (rs.next())
            returned = rs.getLong(2);
        rs.close();
        return returned;
    }

    public boolean verifyUser(String username, String password) {
        String hash;
        boolean return_value;
        ResultSet rs = null;
        try {
            rs = getStatement().executeQuery("SELECT * FROM utilizadores where username =" + username + ";");
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
        return getStatement().executeQuery("SELECT * FROM utilizadores where username =\"" + username + "\";").next();
    }


    private boolean verifyExistenceOf(String table, String condition) throws SQLException {
        ResultSet rs = getStatement().executeQuery(
                "SELECT * FROM " + table + " where " + condition + ";"
        );
        var exists = rs.next();
        rs.close();
        return exists;
    }

    private boolean verifyItemInTable(int id, String table) throws SQLException {
        return verifyExistenceOf(table, "id = " + id);
    }

    public int deleteCanal(int id) throws SQLException {
        if (!verifyItemInTable(id, table_canais))
            return 0;
        return getStatement().executeUpdate("delete FROM canais where id = " + id + ";");
    }




    public int getTableLastMax(String tablename) throws SQLException {
        var rs = getStatement().executeQuery("SELECT MAX(id) FROM " + tablename);
        int idmax;
        rs.next();
        return rs.getInt(0);
    }

    //users
    public int addUser(String username, String name, String hash) throws SQLException {
        return addUser(-1, username, name, hash);
    }

    synchronized public int addUser(int id, String username, String name, String hash) throws SQLException {
        if (id == -1) {
            getStatement().executeUpdate("INSERT INTO utilizadores (username,nome, hash)\n" +
                    "VALUES ('" + username + "', '" + name + "', '" + hash + "');");
        } else {
            //TODO: verificar se a tabela existe
            getStatement().executeUpdate("INSERT INTO utilizadores (id, username,nome, hash)\n" +
                    "VALUES ( " + id + " , '" + username + "', '" + name + "', '" + hash + "');");
        }
        return getTableLastMax(table_utilizadores);
    }

    //canaldm
    public int addCanalDM(int pessoaCria, int pessoaDest) throws SQLException {
        return addCanalDM(-1, pessoaCria, pessoaDest);
    }

    synchronized public int addCanalDM(int canal_id, int pessoaCria, int pessoaDest) throws SQLException {
        if (canal_id == -1)
            getStatement().executeUpdate(
                    "INSERT INTO " + table_canais + " (pessoaCria) VALUES (" + pessoaCria + ");"
            );
        else {
            //TODO: verificar se a tabela existe
            getStatement().executeUpdate(
                    "INSERT INTO " + table_canais + " (id, pessoaCria) VALUES (" + canal_id + ", " + pessoaCria + ");"
            );
        }
        getStatement().executeUpdate(
                "INSERT INTO " + table_canaisDM + " (canal_id) VALUES (" + canal_id + ", " + pessoaDest + ");"
        );
        canal_id = getTableLastMax(table_canais);
        return canal_id;
    }

    //canalgroup
    synchronized public int addCanalGroup(int pessoaCria, int pessoaDest) throws SQLException {
        return addCanalGroup(-1, pessoaCria, pessoaDest);
    }


    synchronized public int addCanalGroup(int canal_id, int pessoaCria, int pessoaDest) throws SQLException {
        if (canal_id == -1)
            getStatement().executeUpdate(
                    "INSERT INTO " + table_canais + " (pessoaCria) VALUES (" + pessoaCria + ");"
            );
        else {
            //TODO: verificar se a tabela existe
            getStatement().executeUpdate(
                    "INSERT INTO " + table_canais + " (id, pessoaCria) VALUES (" + canal_id + ", " + pessoaCria + ");"
            );
        }

        canal_id = getTableLastMax(table_canais);
        getStatement().executeUpdate(
                "INSERT INTO " + table_canaisDM + " (canal_id) VALUES (" + canal_id + ", " + pessoaDest + ");"
        );
        return canal_id;
    }

    public List<Object> getListaTabela(String tabela) throws Exception {
        var statment = conn.createStatement();
        var statmenttemp = conn.createStatement();
        var rs = statment.executeQuery("select * from " + tabela + ";");

        List<Object> ll = new LinkedList<Object>();
        int id;
        ResultSet rstemp;
        while (rs.next()) {
            switch (tabela) {
                case table_canaisDM:
                    id = rs.getInt(1);
                    rstemp = getStatement().executeQuery("select * from " + table_canais + ";");
                    rstemp.next();
                    ll.add(new CanalDM(id, rs.getInt(2), rstemp.getInt(2)));
                    break;
                case table_canaisGrupo:
                    id = rs.getInt(1);
                    rstemp = getStatement().executeQuery("select * from " + table_canais + ";");
                    rstemp.next();
                    ll.add(new CanalGrupo(id, rs.getInt(2), rstemp.getString(2), rstemp.getString(3), rstemp.getString(4)));
                    break;
                case table_mensagens:
                    ll.add(new Mensagem(rs.getInt(1), rs.getTimestamp(2), rs.getInt(3), rs.getInt(4), rs.getBoolean(5), rs.getString(6)));
                    break;
                case table_utilizadores:
                    ll.add(new UtilizadorServer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
                    break;
                default:
                    throw new Exception("Tabela nao existe.");
            }
        }
        return ll;
    }


    public int canalDmExists(int pessoaEnvia, int pessoaRecebe)
            throws SQLException {
          var rs = conn.createStatement().executeQuery("SELECT canais.id, pessoaCria, pessoaDest " +
                "FROM canais INNER JOIN canaisDM cD on canais.id = cD.id " +
                "where " +
                "(pessoaDest = "+pessoaEnvia+" and pessoaCria = "+pessoaRecebe+") " +
                "or " +
                "(pessoaDest = "+pessoaRecebe+" and pessoaCria = "+pessoaEnvia+") ;\n");
                if(rs.next())
                    return rs.getInt("id");
                else return -1;

    }

    public int mensagemGrupo(Timestamp timestamp, int pessoaEnvia, int canal_id,boolean isAFile , String conteudo) throws SQLException {
        return mensagemDM(-1,timestamp, pessoaEnvia,  canal_id, isAFile , conteudo);
    }

    synchronized public int mensagemGrupo(int id, Timestamp timestamp, int pessoaEnvia, int canal_id,boolean isAFile , String conteudo) throws SQLException {
        if(id == -1){
            getStatement().executeUpdate("" +
                    "insert into " +
                    "mensagens(dataHoraEnvio, authorId, canalId, isAFile, mensagem) " +
                    "values " +
                    "("+timestamp+", "+pessoaEnvia+", "+canal_id+", "+
                    (isAFile? 1:0)+", "+conteudo+")");
        }else{
            getStatement().executeUpdate("" +
                    "insert into " +
                    "mensagens(id, dataHoraEnvio, authorId, canalId, isAFile, mensagem) " +
                    "values " +
                    "("+id+", "+timestamp+", "+pessoaEnvia+", "+canal_id+", "+
                    (isAFile? 1:0)+", "+conteudo+")");
        }
        return getTableLastMax(table_mensagens);
    }

    public int mensagemDM(Timestamp timestamp, int pessoaEnvia, int pessoaRecebe,boolean isAFile , String conteudo) throws SQLException {
        return mensagemDM(-1,timestamp, pessoaEnvia,  pessoaRecebe, isAFile , conteudo);
    }

    synchronized public int mensagemDM(int id, Timestamp timestamp, int pessoaEnvia, int pessoaRecebe,boolean isAFile , String conteudo) throws SQLException {
        int canal_id;
        if(-1==(canal_id = canalDmExists(pessoaEnvia, pessoaRecebe))) {
            canal_id  = addCanalDM(pessoaEnvia, pessoaRecebe);
        }

        if(id == -1){
            getStatement().executeUpdate("" +
                    "insert into " +
                    "mensagens(dataHoraEnvio, authorId, canalId, isAFile, mensagem) " +
                    "values " +
                    "("+timestamp+", "+pessoaEnvia+", "+canal_id+", "+
                    (isAFile? 1:0)+", "+conteudo+")");
        }else{

            getStatement().executeUpdate("" +
                    "insert into " +
                    "mensagens(id, dataHoraEnvio, authorId, canalId, isAFile, mensagem) " +
                    "values " +
                    "("+id+", "+timestamp+", "+pessoaEnvia+", "+canal_id+", "+
                    (isAFile? 1:0)+", "+conteudo+")");
        }
        return getTableLastMax(table_mensagens);
    }

}


