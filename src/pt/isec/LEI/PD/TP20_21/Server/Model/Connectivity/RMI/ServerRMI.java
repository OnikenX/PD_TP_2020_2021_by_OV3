package pt.isec.LEI.PD.TP20_21.Server.Model.Connectivity.RMI;

import pt.isec.LEI.PD.TP20_21.RMIRegistoObserver.RMIObserverInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ServerRMI extends UnicastRemoteObject implements ServerRMIInterface {
    public static final String SERVICE_NAME = "RegistoUtilizadores";
    public static final int MAX_CHUNK_SIZE = 10000;
    List<RMIObserverInterface> observers;

    public ServerRMI() throws RemoteException {
        observers = new ArrayList<>();
    }

    public synchronized void addObserver(RMIObserverInterface observer) throws java.rmi.RemoteException {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("+ um observador.");
        }

    }

    public synchronized void removeObserver(RMIObserverInterface observer) throws java.rmi.RemoteException {
        if (observers.remove(observer))
            System.out.println("- um observador.");
    }

    public synchronized void notifyObservers(String msg) {
        int i;

        for (i = 0; i < observers.size(); i++) {
            try {
                observers.get(i).notifyNewOperationConcluded(msg);
            } catch (RemoteException e) {
                observers.remove(i--);
                System.out.println("- um observador (observador inacessivel).");
            }
        }
    }

    public static void main(String[] args) {

        try{

            Registry r;

            try{

                System.out.println("Tentativa de lancamento do registry no porto " +
                        Registry.REGISTRY_PORT + "...");

                r = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);

                System.out.println("Registry lancado!");

            }catch(RemoteException e){
                System.out.println("Registry provavelmente ja' em execucao!");
                r = LocateRegistry.getRegistry();
            }

            /*
             * Cria o servico
             */
            ServerRMI serverRegisto = new ServerRMI();

            System.out.println("Servico GetRemoteFile criado e em execucao ("+serverRegisto.getRef().remoteToString()+"...");

            /*
             * Regista o servico no rmiregistry local para que os clientes possam localiza'-lo, ou seja,
             * obter a sua referencia remota (endereco IP, porto de escuta, etc.).
             */

            r.bind(SERVICE_NAME, serverRegisto);

            System.out.println("Servico " + SERVICE_NAME + " registado no registry...");

            /*
             * Para terminar um servico RMI do tipo UnicastRemoteObject:
             *
             *  UnicastRemoteObject.unexportObject(fileService, true);
             */

        }catch(RemoteException e){
            System.out.println("Erro remoto - " + e);
            System.exit(1);
        }catch(Exception e){
            System.out.println("Erro - " + e);
            System.exit(1);
        }
    }

}
