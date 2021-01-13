package pt.isec.LEI.PD.TP20_21.Client.RMI;

import pt.isec.LEI.PD.TP20_21.Server.Model.Connectivity.RMI.ServerRMIInterface;
import pt.isec.LEI.PD.TP20_21.shared.Data.Utilizador.UtilizadorServer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientRMI extends UnicastRemoteObject implements ClientRMIInterface {

    public ClientRMI() throws RemoteException {

    }

    public void receberMensagem(String conteudo) throws java.rmi.RemoteException {
        System.out.println(conteudo);
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

        objectUrl = "rmi://"+args[0]+"/ServerRMI";

        try{
            /*
             * Obtem a referencia remota para o servico com nome "GetRemoteFile"
             */
            remoteFileService = (ServerRMIInterface) Naming.lookup(objectUrl);

            /*
             * Lanca o servico local para acesso remoto por parte do servidor.
             */
            myRemoteService = new ClientRMI();

            //menu chamado para aqui:
            UtilizadorServer us = new UtilizadorServer(25, "ola123", "joao caralhao", "oi123");
            remoteFileService.registoCliente(us);
            remoteFileService.loginCliente(myRemoteService);
            remoteFileService.enviaMensagem("caralhoooooo");

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
                }catch(NoSuchObjectException e){}
            }
        }
    }
}