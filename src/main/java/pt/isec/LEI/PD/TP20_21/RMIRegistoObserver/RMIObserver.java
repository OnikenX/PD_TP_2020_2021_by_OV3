package pt.isec.LEI.PD.TP20_21.RMIRegistoObserver;

import pt.isec.LEI.PD.TP20_21.Server.Model.Connectivity.RMI.ServerRMIInterface;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

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

            String objectUrl;
            if(args.length < 1) {
                System.out.println("Faltam argumentos -> [1] IP registry");
            }
            String [] serviceList = Naming.list(args[0]);
            List<String> serviceName = new ArrayList<>();

            for(int i = 0; i < serviceList.length; i++) {
                StringTokenizer tokens = new StringTokenizer(serviceList[i], "/: ");
                tokens.nextToken(); //OMITE O VALOR DO PORTO
                serviceName.add(tokens.nextToken());
                //System.out.println(serviceName);
            }
            System.out.println("Selecione uma opcao:");
            for (int i = 0; i < serviceName.size(); i++) {
                System.out.println(i-1 + "- " + serviceName.get(i));
            }
            Scanner sc = new Scanner(System.in);
            int indice = 0;
            do {
                indice = sc.nextInt();
            } while(indice < 1 || indice > serviceName.size());
            objectUrl = "rmi://"+args[0]+ "/" + serviceName.get(indice - 1);

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
