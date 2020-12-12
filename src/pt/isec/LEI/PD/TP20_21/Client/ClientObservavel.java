package pt.isec.LEI.PD.TP20_21.Client;

import pt.isec.LEI.PD.TP20_21.Client.Model.ClientModel;
import pt.isec.LEI.PD.TP20_21.Client.Model.Connectivity.ClientServerConnection;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedido;
import pt.isec.LEI.PD.TP20_21.shared.Data.Canais.Canal;
import pt.isec.LEI.PD.TP20_21.shared.Data.Mensagem;
import pt.isec.LEI.PD.TP20_21.shared.Data.Utilizador.Utilizador;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class ClientObservavel {

    private PropertyChangeSupport propertyChangeSupport;
    private ClientModel clientModel;
    private ClientServerConnection clientServerConnection;

    public ClientObservavel(ClientModel clientModel) {
        this.clientModel = clientModel;
        propertyChangeSupport = new PropertyChangeSupport(this.clientModel);
        clientServerConnection = new ClientServerConnection();
    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }


    /// DELEGATED CLIENT MODEL


    public Pedido.Conectar getPedido() {
        return clientModel.getPedido();
    }

    public void setPedido(Pedido.Conectar pedido) {
        clientModel.setPedido(pedido);
    }

    public boolean adicionaPessoa(String username) {
        return clientModel.adicionaPessoa(username);
    }

    public List<Utilizador> getListUsers() {
        return clientModel.getListUsers();
    }

    public List<Canal> getListCanais() {
        return clientModel.getListCanais();
    }

    public List<Mensagem> getListMensagens() {
        return clientModel.getListMensagens();
    }

    /// DELEGATED CLIENTSERVERCONNECTION

    public int connectToServer(Pedido.Conectar pedido) {
        return clientServerConnection.connectToServer(pedido);
    }

    public void updateServer(Object enviar) {
        clientServerConnection.updateServer(enviar);
    }

    //public InteracaoEsperada getInteracaoEsperada() {return MaqEstados.getInteracaoEsperada();}
}
