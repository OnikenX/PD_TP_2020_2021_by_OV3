package pt.isec.LEI.PD.TP20_21.Server.Model.Connectivity.RMI;

import pt.isec.LEI.PD.TP20_21.Client.RMI.ClientRMIInterface;
import pt.isec.LEI.PD.TP20_21.RMIRegistoObserver.RMIObserverInterface;
import pt.isec.LEI.PD.TP20_21.shared.Data.Utilizador.UtilizadorServer;

import java.sql.SQLException;

public interface ServerRMIInterface extends java.rmi.Remote {

    public void addObserver(RMIObserverInterface observer) throws java.rmi.RemoteException;
    public void removeObserver(RMIObserverInterface observer) throws java.rmi.RemoteException;
    public void registoCliente(UtilizadorServer user) throws java.rmi.RemoteException, SQLException;
    public void loginCliente(ClientRMIInterface ci) throws java.rmi.RemoteException;
    public void enviaMensagem(String conteudo) throws java.rmi.RemoteException;
}
