package pt.isec.LEI.PD.TP20_21.Client.Model;

import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedido;
import pt.isec.LEI.PD.TP20_21.shared.Data.Canais.Canal;
import pt.isec.LEI.PD.TP20_21.shared.Data.Mensagem;
import pt.isec.LEI.PD.TP20_21.shared.Data.Utilizador.Utilizador;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ClientModel {
    private List<Utilizador> listUsers = Collections.synchronizedList(new LinkedList<>());
    private List<Canal> listCanais = Collections.synchronizedList(new LinkedList<>());
    private List<Mensagem> listMensagens = Collections.synchronizedList(new LinkedList<>());
    private Pedido.Conectar pedido;


    public Pedido.Conectar getPedido() {
        return pedido;
    }

    public void setPedido(Pedido.Conectar pedido) {
        this.pedido = pedido;
    }

    public ClientModel() {
    }

    /**
     *
     */
    public boolean adicionaPessoa(String username) {
        for (var user : listUsers)
            if (user.getUsername().equals(username)) {
                new Pedido.MensagemDM(pedido.getUsername(), user.getUsername(), pedido.getNome() + " adicionou " + user.getNome());
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
