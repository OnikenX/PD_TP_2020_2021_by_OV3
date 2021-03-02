package pt.isec.LEI.PD.TP20_21.Server.Model.Connectivity.RMI;

import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos.Conectar;
import pt.isec.LEI.PD.TP20_21.Client.RMI.ClientRMIInterface;
import pt.isec.LEI.PD.TP20_21.RMIRegistoObserver.RMIObserverInterface;

import java.sql.SQLException;

public interface ServerRMIInterface extends java.rmi.Remote {

    public void addObserver(RMIObserverInterface observer) throws java.rmi.RemoteException;
    public void removeObserver(RMIObserverInterface observer) throws java.rmi.RemoteException;
    public void registoCliente(Conectar user) throws Exception;
    public void listaClientes(ClientRMIInterface user) throws java.rmi.RemoteException;
    public boolean loginCliente(ClientRMIInterface ci, String id, String pass) throws java.rmi.RemoteException;
    public void enviaMensagem(String conteudo, String nome) throws java.rmi.RemoteException;
    public void enviaMensagem(String conteudo, String destinatario, String nome) throws java.rmi.RemoteException;
}
