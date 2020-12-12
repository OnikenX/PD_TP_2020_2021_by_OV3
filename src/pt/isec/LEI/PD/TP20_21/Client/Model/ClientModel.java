package pt.isec.LEI.PD.TP20_21.Client.Model;

import pt.isec.LEI.PD.TP20_21.Client.Model.Connectivity.ClientServerConnection;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos.Conectar;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos.MensagemDM;
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
    public boolean adicionaPessoa(String username) {
        for (var user : listUsers)
            if (user.getUsername().equals(username)) {
                try {
                    csc.getOtputStreamTCP().write(Utils.objectToBytes(new MensagemDM(pedido.getUsername(), user.getUsername(), pedido.getNome() + " adicionou " + user.getNome())));
                    byte [] input = csc.getInputPipe().readAllBytes();
                    Object obj = Utils.bytesToObject(input);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        return true;
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
