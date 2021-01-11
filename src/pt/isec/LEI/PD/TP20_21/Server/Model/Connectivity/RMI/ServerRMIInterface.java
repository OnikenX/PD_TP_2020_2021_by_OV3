package pt.isec.LEI.PD.TP20_21.Server.Model.Connectivity.RMI;

import pt.isec.LEI.PD.TP20_21.RMIRegistoObserver.RMIObserverInterface;

public interface ServerRMIInterface extends java.rmi.Remote {

    public void addObserver(RMIObserverInterface observer) throws java.rmi.RemoteException;
    public void removeObserver(RMIObserverInterface observer) throws java.rmi.RemoteException;
}
