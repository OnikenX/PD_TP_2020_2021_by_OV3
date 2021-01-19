package pt.isec.lei.pd.tp20_21.Client;

import pt.isec.lei.pd.tp20_21.Client.Model.ClientModel;
import pt.isec.lei.pd.tp20_21.Client.Model.Connectivity.ClientServerConnection;
import pt.isec.lei.pd.tp20_21.shared.Comunicacoes.Pedidos.Conectar;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ClientObservavel {

    private PropertyChangeSupport propertyChangeSupport;
    private ClientModel clientModel;
    private ClientServerConnection clientServerConnection;

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    public ClientModel getClientModel() {
        return clientModel;
    }



    public ClientServerConnection getClientServerConnection() {
        return clientServerConnection;
    }

    public ClientObservavel() {
        clientServerConnection = new ClientServerConnection(this);
        clientModel = new ClientModel(clientServerConnection);
        propertyChangeSupport = new PropertyChangeSupport(this.clientModel);
    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }


    /// DELEGATED CLIENT MODEL


    public Conectar getPedido() {
        return clientModel.getPedido();
    }

    public void setPedido(Conectar pedido) {
        clientModel.setPedido(pedido);
    }
/*
    public boolean adicionaPessoa(String username) {
        return clientModel.mandaMensPessoal(username);
    }*/

    /// DELEGATED CLIENTSERVERCONNECTION

    public int connectToServer(Conectar pedido) {
        return clientServerConnection.connectToServer(pedido);
    }

    public void updateServer(Object enviar) {
        clientServerConnection.updateServer(enviar);
    }

    //public InteracaoEsperada getInteracaoEsperada() {return MaqEstados.getInteracaoEsperada();}
}
