package pt.isec.LEI.PD.TP20_21.RMIRegistoObserver;

import pt.isec.LEI.PD.TP20_21.Server.Model.Connectivity.RMI.ServerRMIInterface;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIObserver extends UnicastRemoteObject implements RMIObserverInterface {

    public RMIObserver() throws RemoteException {}

    @Override
    public void notifyNewOperationConcluded(String description) throws RemoteException {
        System.out.println(description);
    }

    public static void main(String[] args) {
        try{

            //Cria e lanca o servico
            RMIObserver observer = new RMIObserver();
            System.out.println("Servico GetRemoteFileObserver criado e em execucao...");

            //Localiza o servico remoto nomeado "GetRemoteFile"
            String objectUrl = "rmi://127.0.0.1/ServerRMI"; //rmiregistry on localhost

            if(args.length > 0)
                objectUrl = "rmi://"+args[0]+"/ServerRMI";

            ServerRMIInterface getRemoteFileService = (ServerRMIInterface) Naming.lookup(objectUrl);

            //adiciona observador no servico remoto
            getRemoteFileService.addObserver(observer);

            System.out.println("<Enter> para terminar...");
            System.out.println();
            System.in.read();

            getRemoteFileService.removeObserver(observer);
            UnicastRemoteObject.unexportObject(observer, true);

        }catch(RemoteException e){
            System.out.println("Erro remoto - " + e);
            System.exit(1);
        }catch(IOException | NotBoundException e){
            System.out.println("Erro - " + e);
            System.exit(1);
        }
    }
}
