package pt.isec.LEI.PD.TP20_21.Server.Model.Data;


import pt.isec.LEI.PD.TP20_21.Server.Model.Server;
import pt.isec.LEI.PD.TP20_21.shared.Data.Canais.CanalDM;
import pt.isec.LEI.PD.TP20_21.shared.Data.Canais.CanalGrupo;
import pt.isec.LEI.PD.TP20_21.shared.Data.DataBase;
import pt.isec.LEI.PD.TP20_21.shared.Data.Mensagem;
import pt.isec.LEI.PD.TP20_21.shared.Data.Utilizador.UtilizadorServer;
import pt.isec.LEI.PD.TP20_21.shared.Password;

import java.sql.*;
import java.util.ArrayList;
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

    synchronized private Connection getConn() {
        return conn;
    }

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

        if (canal_id == -1){
            getStatement().executeUpdate(
                    "INSERT INTO " + table_canais + " (pessoaCria) VALUES (" + pessoaCria + ");"
            );
        canal_id = getTableLastMax(table_canais);
    }else {
            //TODO: verificar se a tabela existe
            getStatement().executeUpdate(
                    "INSERT INTO " + table_canais + " (id, pessoaCria) VALUES (" + canal_id + ", " + pessoaCria + ");"
            );
        }
        getStatement().executeUpdate(
                "INSERT INTO " + table_canaisDM + " (id, pessoaDest) VALUES (" + canal_id + ", " + pessoaDest + ");"
        );
        return canal_id;
    }

    //canalgroup
    synchronized public int addCanalGroup(int pessoaCria, String nome, String descricao, String password) throws SQLException {
        return addCanalGroup(-1,  pessoaCria, nome, descricao, password);
    }


    synchronized public int addCanalGroup(int canal_id, int pessoaCria, String nome, String descricao, String password) throws SQLException {

        var st = conn.createStatement();
        if (canal_id == -1){
            st.executeUpdate(
                    "INSERT INTO " + table_canais + " (pessoaCria) VALUES (" + pessoaCria + ");"
            );
        var rs = st.executeQuery("select MAX(id) from " + table_canais + ";");
        if (rs.next()) {
            canal_id = rs.getInt(0);
        } else {
            return -1;
        }
    }else{
            //TODO: verificar se a tabela existe
            getStatement().executeUpdate(
                    "INSERT INTO " + table_canais + " (id, pessoaCria) VALUES (" + canal_id + ", " + pessoaCria + ");"
            );

        }

        getStatement().executeUpdate(
                "INSERT INTO " + table_canaisDM + " (id, nome, descricao, password) VALUES (" + canal_id + ", "+ "nome +"+","  +"descri" +
                         "password +");

        return canal_id;
    }


    public int canalDmExists(int pessoaEnvia, int pessoaRecebe)
            throws SQLException {
          var rs = getConn().createStatement().executeQuery("SELECT canais.id, pessoaCria, pessoaDest " +
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
        return mensagemGrupo(-1,timestamp, pessoaEnvia,  canal_id, isAFile , conteudo);
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
            getStatement().executeUpdate(
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


    public List<Object> getListaTabela(String tabela) throws Exception {
        var statment = getConn().createStatement();
        var statmenttemp = getConn().createStatement();
        var rs = statment.executeQuery("select * from " + tabela + ";");

        List<Object> ll = new LinkedList<>();
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

    public  void verificaMudancas(ArrayList<Object> lista, String tabela) throws Exception {
        var statment = getConn().createStatement();
        var statmenttemp = getConn().createStatement();
        var rs = statment.executeQuery("select * from " + tabela + " order by id;");
        int id;
        CanalDM canalDM;
        CanalGrupo canalGrupo;
        Mensagem mensagem;
        ResultSet rstemp = null;
        int lowest = 0, i = 0 ;
        boolean found = false;
        while (rs.next()) {
            switch (tabela) {
                case table_canaisDM:
                    id = rs.getInt(1);
                    rstemp = getStatement().executeQuery("select * from " + table_canais + ";");
                    rstemp.next();
                    //id, rs.getInt(2), rstemp.getInt(2);
                    found = false;
                    for( i = lowest;id <= ((CanalDM)lista.get(i)).getId(); ++i){
                        if(((CanalDM)lista.get(i)).getId() == id){
                            found = true;
                            break;
                        }
                    }
                    canalDM = (CanalDM) lista.get(i);
                    if(found) {
                        if (!((rs.getInt(2) == canalDM.getPessoaCria()) && (rstemp.getInt(2) == canalDM.getPessoaDest()))){
                            getStatement().executeUpdate("UPDATE canais SET pessoaCria = " + canalDM.getPessoaCria() + " WHERE id=" + id + ";");
                            getStatement().executeUpdate("UPDATE canaisDM SET pessoaDest = " + canalDM.getPessoaDest() + " WHERE id=" + id + ";");
                        }
                    }else{
                        addCanalDM(id, canalDM.getPessoaCria(), canalDM.getPessoaDest());
                    }
                    lowest = id;
                    break;
                case table_canaisGrupo:
                    id = rs.getInt(1);
                    rstemp = getStatement().executeQuery("select * from " + table_canais + ";");
                    rstemp.next();

//                    new CanalGrupo(id, rs.getInt(2), rstemp.getString(2), rstemp.getString(3), rstemp.getString(4)));

                    found = false;
                    for( i = lowest;id <= ((CanalGrupo)lista.get(i)).getId(); ++i  ){
                        if(((CanalGrupo)lista.get(i)).getId() == id){
                            found = true;
                            break;
                        }
                    }
                    canalGrupo = (CanalGrupo)lista.get(i);
                    if(found) {
                        if (!((rs.getInt(2) ==  canalGrupo.getPessoaCria()) &&
                                rstemp.getString(2).equals(canalGrupo.getNome()) &&
                                rstemp.getString(3).equals(canalGrupo.getDescricao()) &&
                                rstemp.getString(4).equals(canalGrupo.getPassword()))){
                            getStatement().executeUpdate("UPDATE canais SET pessoaCria = " + canalGrupo.getPessoaCria() + " WHERE id=" + id + ";");
                            getStatement().executeUpdate("UPDATE canaisDM SET " +

                                    ", nome = " + canalGrupo.getNome() +

                                    ", descricao = "+canalGrupo.getDescricao()+

                                    ", password = " + canalGrupo.getPassword()+

                                    "  WHERE id=" + id + ";");
                        }
                    }else{
                        addCanalGroup(id, canalGrupo.getPessoaCria(), canalGrupo.getNome(), canalGrupo.getDescricao(), canalGrupo.getPassword());
                    }
                    lowest = id;
                    break;
                case table_mensagens:
                    id = rs.getInt(1);
                    found = false;
                    for( i = lowest;id <= ((Mensagem)lista.get(i)).getId(); ++i  ){
                        if(((CanalGrupo)lista.get(i)).getId() == id){
                            found = true;
                            break;
                        }
                    }
                    mensagem = (Mensagem) lista.get(i);
                    if(found) {
                        if (!(id == mensagem.getId() &&
                                rs.getTimestamp(2).equals(mensagem.getDataHoraEnvio()) &&
                                rs.getInt(3) == mensagem.getAuthorId() &&
                                rs.getInt(4) == mensagem.getCanalId() &&
                                rs.getBoolean(5) == mensagem.isAFile() &&
                                rs.getString(6).equals(mensagem.getMensagem()))){

                            getStatement().executeUpdate("UPDATE mensagens SET " +
                                    ", dataHoraEnvio = " + mensagem.getDataHoraEnvio() +
                                    ", authorId = "+ mensagem.getAuthorId() +
                                    ", canalId = " + mensagem.getCanalId() +
                                    ", isAFile = " +
                                    " WHERE id=" + id + ";");
                        }
                    }else{
                        addCanalGroup(id, canalGrupo.getPessoaCria(), canalGrupo.getNome(), canalGrupo.getDescricao(), canalGrupo.getPassword());
                    }
                    lowest = id;
                    break;

                    ll.add(new Mensagem(rs.getInt(1), rs.getTimestamp(2), rs.getInt(3), rs.getInt(4), rs.getBoolean(5), rs.getString(6)));
                    break;
                case table_utilizadores:
                    //ll.add(new UtilizadorServer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
                    break;
                default:

                    throw new Exception("Tabela nao existe.");
            }
        }
        if(rstemp != null)
            rstemp.close();
        rs.close();
        statement.close();

    }





}


