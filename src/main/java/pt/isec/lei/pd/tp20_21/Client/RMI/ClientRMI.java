package pt.isec.lei.pd.tp20_21.Client.RMI;

import pt.isec.lei.pd.tp20_21.Server.Model.Connectivity.RMI.ServerRMIInterface;
import pt.isec.lei.pd.tp20_21.shared.Data.Utilizador.UtilizadorServer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ClientRMI extends UnicastRemoteObject implements ClientRMIInterface {

    static private String nome;

    public ClientRMI() throws RemoteException {

    }

    public void receberMensagem(String conteudo) throws java.rmi.RemoteException {
        System.out.println(conteudo);
    }

    public String getNome() throws java.rmi.RemoteException {
        return nome;
    }


    public static void main(String[] args) {
        // Fazer aqui tipo menu


        String objectUrl;

        FileOutputStream localFileOutputStream = null;

        ClientRMI myRemoteService = null;
        ServerRMIInterface remoteFileService;

        /*
         * Trata os argumentos da linha de comando
         */

        if(args.length != 1){
            System.out.print("Deve passar na linha de comando: (1) a localizacao do servico ");
            return;
        }



        try{
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
            remoteFileService = (ServerRMIInterface) Naming.lookup(objectUrl);

            /*
             * Lanca o servico local para acesso remoto por parte do servidor.
             */
            myRemoteService = new ClientRMI();

            //menu chamado para aqui:
            UtilizadorServer us = new UtilizadorServer(27, "ola12345", "joao caralhao", "oi123");
            remoteFileService.registoCliente(us);
            remoteFileService.loginCliente(myRemoteService);
            nome = "ola123";
            remoteFileService.enviaMensagem("caralhooooooo");

        }catch(RemoteException e){
            System.out.println("Erro remoto - " + e);
        }catch(NotBoundException e){
            System.out.println("Servico remoto desconhecido - " + e);
        }catch(IOException e){
            System.out.println("Erro E/S - " + e);
        }catch(Exception e){
            System.out.println("Erro - " + e);
        }finally{
            if(myRemoteService != null){
                /*
                 * Termina o serviço local
                 */
                try{
                    UnicastRemoteObject.unexportObject(myRemoteService, true);
                }catch(NoSuchObjectException ignored){}
            }
        }
    }
}
