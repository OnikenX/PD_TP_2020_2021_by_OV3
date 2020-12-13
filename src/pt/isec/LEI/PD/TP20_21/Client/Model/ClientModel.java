package pt.isec.LEI.PD.TP20_21.Client.Model;

import pt.isec.LEI.PD.TP20_21.Client.Model.Connectivity.ClientServerConnection;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos.*;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Respostas.Respostas;
import pt.isec.LEI.PD.TP20_21.shared.Data.Canais.Canal;
import pt.isec.LEI.PD.TP20_21.shared.Data.Mensagem;
import pt.isec.LEI.PD.TP20_21.shared.Data.Utilizador.Utilizador;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ClientModel {
    private List<Utilizador> listUsers = Collections.synchronizedList(new LinkedList<>());
    private List<Canal> listCanais = Collections.synchronizedList(new LinkedList<>());
    private List<Mensagem> listMensagens = Collections.synchronizedList(new LinkedList<>());
    private Conectar pedido;
    private ClientServerConnection csc;

    public Conectar getPedido() {
        return pedido;
    }

    public void setPedido(Conectar pedido) {
        this.pedido = pedido;
    }

    public ClientModel(ClientServerConnection csc) {
        this.csc = csc;
    }

    /**
     *
     */
    public boolean mandaMensPessoal(String usernameRemetente, String userQueManda, String conteudo) {
        try {
            csc.getOtputStreamTCP().write(Utils.objectToBytes(new MensagemDM(userQueManda, usernameRemetente, conteudo)));
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

    public boolean mandaMensCanais(String grupoNome, String conteudo, String userQueEnvia) {
        try {
            csc.getOtputStreamTCP().write(Utils.objectToBytes(new MensagemGrupo(userQueEnvia, grupoNome, conteudo)));
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

    public List<Canal> listaCanais(String user) {
        try {
            csc.getOtputStreamTCP().write(Utils.objectToBytes(new ListaCanais(user)));
            byte[] input = csc.getInputPipe().readAllBytes();
            Object obj = Utils.bytesToObject(input);
            if (obj instanceof Respostas) {
                //sera que posso receber lista ou tem de ser 1 a 1?
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Double> listaCanais(String canal) {
        try {
            csc.getOtputStreamTCP().write(Utils.objectToBytes(new Estatisticas(canal)));
            byte[] input = csc.getInputPipe().readAllBytes();
            Object obj = Utils.bytesToObject(input);
            if (obj instanceof Respostas) {
                //sera que posso receber lista ou tem de ser 1 a 1?
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Utilizador> getListUsers() {
        return listUsers;
    }

    public List<Canal> getListCanais() {
        return listCanais;
    }

    public List<Mensagem> getListMensagens() {
        return listMensagens;
    }
}
