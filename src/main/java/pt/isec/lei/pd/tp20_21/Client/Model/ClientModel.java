package pt.isec.lei.pd.tp20_21.Client.Model;

import pt.isec.lei.pd.tp20_21.Client.Model.Connectivity.ClientServerConnection;
import pt.isec.lei.pd.tp20_21.Server.Model.Data.ServerDB;
import pt.isec.lei.pd.tp20_21.shared.Comunicacoes.ListasParaOClient;
import pt.isec.lei.pd.tp20_21.shared.Comunicacoes.Pedidos.Conectar;
import pt.isec.lei.pd.tp20_21.shared.Comunicacoes.Pedidos.MensagemDM;
import pt.isec.lei.pd.tp20_21.shared.Comunicacoes.Pedidos.MensagemGrupo;
import pt.isec.lei.pd.tp20_21.shared.Comunicacoes.Respostas.Respostas;
import pt.isec.lei.pd.tp20_21.shared.Data.Canais.CanalDM;
import pt.isec.lei.pd.tp20_21.shared.Data.Canais.CanalGrupo;
import pt.isec.lei.pd.tp20_21.shared.Data.DataBase;
import pt.isec.lei.pd.tp20_21.shared.Data.Mensagem;
import pt.isec.lei.pd.tp20_21.shared.Data.Utilizador.Utilizador;
import pt.isec.lei.pd.tp20_21.shared.Utils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientModel {
    public  List<Utilizador> listUsers = null;
    public List<CanalGrupo> listCanaisGrupos = null;
    public List<CanalDM> listCanaisDM = null;
    public List<Mensagem> listMensagens =null;
    public Integer myID = null;
    public Conectar pedido = null;
    public ClientServerConnection csc = null;

    public void processListaParaOClient(ListasParaOClient lista){
        var tabela = lista.getTabela();
        for(var i : lista.getList()){
            switch(tabela){
                case ServerDB.table_canaisDM:
                    listCanaisDM.add((CanalDM)i);
                    break;
                case ServerDB.table_canaisGrupo:
                    listCanaisGrupos.add((CanalGrupo)i);
                    break;
                case ServerDB.table_utilizadores:
                    listUsers.add((Utilizador)i);
                    break;
                case ServerDB.table_mensagens:
                    listMensagens.add((Mensagem)i);
                    break;
                default:
                    throw new Error("Esse nao existe.");
            }
        }
    }

    public Conectar getPedido() {
        return pedido;
    }

    public void setPedido(Conectar pedido) {
        this.pedido = pedido;
    }

    public ClientModel(ClientServerConnection csc) {

        this.csc = csc;
        this.listUsers = new CopyOnWriteArrayList<>();
        this.listCanaisDM = new CopyOnWriteArrayList<>();
        this.listCanaisGrupos = new CopyOnWriteArrayList<>();
        this.listMensagens = new CopyOnWriteArrayList<>();

    }

    public int getUserIdByName(String nome){
        for (var i : listUsers)
            if(i.getNome().equals(nome))
                return i.getId();
        return -1;
    }

    /**
     *
     */
    public boolean mandaMensPessoal(int usernameRemetente, int userQueManda, String conteudo, boolean isAFile) {
        try {
            csc.getOtputStreamTCP().write(Utils.objectToBytes(new MensagemDM(userQueManda, usernameRemetente, conteudo, isAFile)));
//            byte[] input = csc.getInputPipe().readAllBytes();
//            Object obj = Utils.bytesToObject(input);
//            if (obj instanceof Respostas) {
//                //duvida de classe de resposta
//            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }



    public boolean mandaMensCanais( int userQueEnvia, int grupo, String conteudo, boolean isAFile) {
        try {
            csc.getOtputStreamTCP().write(Utils.objectToBytes(new MensagemGrupo(userQueEnvia, grupo, conteudo, isAFile)));
            byte[] input = csc.getInputPipe().readAllBytes();
            Object obj = Utils.bytesToObject(input);
            if (obj instanceof Respostas) {
                //duvida de classe de resposta
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    public int getMyId(){
        if(myID == null)
        {
            for (var i : listUsers)
                if (i.getUsername().equals(pedido.getUsername()))
                    myID = i.getId();
        }
        return myID;
    }

    public int registarOuLogin(Conectar con) {
        return csc.connectToServer(con);
    }


    public <T extends DataBase> Object getObjectById(int id, List<T> list){
        for(var l : list ){
            if (l.getId() == id)
                return l;
        }
        return null;
    }

//    /**
//     * Canais mais utilizadores
//     * @param user
//     * @return
//     */
//    public List<Canal> listadeTudo(String user) {
//        try {
//
//
//            csc.getOtputStreamTCP().write(Utils.objectToBytes(new ListaCanais(user)));
//            byte[] input = csc.getInputPipe().readAllBytes();
//            Object obj = Utils.bytesToObject(input);
//            if (obj instanceof Respostas) {
//                //sera que posso receber lista ou tem de ser 1 a 1?
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//        return
//    }

//    public List<Canal> listaUsers(String user) {
//        try {
//            csc.getOtputStreamTCP().write(Utils.objectToBytes(new ListaCanais(user)));
//            byte[] input = csc.getInputPipe().readAllBytes();
//            Object obj = Utils.bytesToObject(input);
//            if (obj instanceof Respostas) {
//                //sera que posso receber lista ou tem de ser 1 a 1?
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//        return
//    }

//    public List<Double> listaCanais(String canal) {
//        try {
//            csc.getOtputStreamTCP().write(Utils.objectToBytes(new Estatisticas(canal)));
//            byte[] input = csc.getInputPipe().readAllBytes();
//            Object obj = Utils.bytesToObject(input);
//            if (obj instanceof Respostas) {
//                //sera que posso receber lista ou tem de ser 1 a 1?
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    public List<Utilizador> getListUsers() {
//        return listUsers;
//    }

//    public List<Canal> getListCanais() {
//        return listCanais;
//    }

//    public List<Mensagem> getListMensagens() {
//        return listMensagens;
//    }
}
