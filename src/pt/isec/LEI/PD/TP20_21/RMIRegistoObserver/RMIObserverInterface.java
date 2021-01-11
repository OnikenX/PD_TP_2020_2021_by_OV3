package pt.isec.LEI.PD.TP20_21.RMIRegistoObserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIObserverInterface extends Remote {

    public void notifyNewOperationConcluded(String description) throws RemoteException;
}
