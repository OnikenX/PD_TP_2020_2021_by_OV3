package pt.isec.lei.pd.tp20_21.Server.Model.Connectivity.RMI;

import pt.isec.lei.pd.tp20_21.Client.RMI.ClientRMIInterface;
import pt.isec.lei.pd.tp20_21.RMIRegistoObserver.RMIObserverInterface;
import pt.isec.lei.pd.tp20_21.Server.Model.Data.ServerDB;
import pt.isec.lei.pd.tp20_21.shared.Data.Utilizador.UtilizadorServer;
import pt.isec.lei.pd.tp20_21.shared.Utils;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServerRMI extends UnicastRemoteObject implements ServerRMIInterface {
    String serviceName;
    int serverNumber;
    ServerDB sb;
    List<RMIObserverInterface> observers;
    List<ClientRMIInterface> clientesLogados;

    public ServerRMI(int serverNumber, String serviceName) throws RemoteException, ClassNotFoundException, SQLException {
        observers = new ArrayList<>();
        clientesLogados = new ArrayList<>();
        sb = new ServerDB(serverNumber);
        this.serviceName = serviceName;
        this.serverNumber = serverNumber;
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

    public void registoCliente(UtilizadorServer user) throws java.rmi.RemoteException, SQLException {
        sb.addUser(user.getUsername(), user.getNome(), user.getHash());
        notifyObservers("Registo concluido");
    }

    public void loginCliente(ClientRMIInterface ci) throws java.rmi.RemoteException {
        clientesLogados.add(ci);
        notifyObservers("Utilizador entrou no server");
    }

    public void enviaMensagem(String conteudo) throws java.rmi.RemoteException {
        for (var it : clientesLogados)
            it.receberMensagem(conteudo);
        notifyObservers("mensagem: " + conteudo);
    }

    public void enviaMensagem(String conteudo, String destinatario) throws java.rmi.RemoteException {
        for (var it : clientesLogados) {
            if (it.getNome().equals(destinatario))
                it.receberMensagem(conteudo);
        }
    }

    public static void main(String[] args) {

        Utils.argumentos = new Utils.Argumentos(args);

        try {
            Registry r;
            try {
                if (InetAddress.getByName(Utils.argumentos.getRmiip()).getHostAddress().equals(InetAddress.getByName("localhost").getHostAddress())) {
                    System.out.println("Tentativa de lancamento do registry no porto " +
                            Registry.REGISTRY_PORT + "...");
                    r = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
                } else
                    r = LocateRegistry.getRegistry();
                System.out.println("Registry lancado!");
            } catch (RemoteException e) {
                System.out.println("Registry provavelmente ja' em execucao!");
                r = LocateRegistry.getRegistry();
            }

            /*
             * Cria o servico
             */
            int serverNumber = Utils.argumentos.getNserver();

            /*
             * Regista o servico no rmiregistry local para que os clientes possam localiza'-lo, ou seja,
             * obter a sua referencia remota (endereco IP, porto de escuta, etc.).
             */
            ServerRMI serverRegisto;
            String serviceName = Utils.argumentos.getServiceName();


            serverRegisto = new ServerRMI(serverNumber, serviceName);
            //String meh = "rmi://" + Utils.argumentos.getRmiip() + "/" + Utils.argumentos.getServiceName();
            //r.rebind(meh, serverRegisto);
            r.rebind(Utils.argumentos.getServiceName(), serverRegisto);

            System.out.println("Servico " + Utils.argumentos.getServiceName() + " registado no registry...");

            /*
             * Para terminar um servico RMI do tipo UnicastRemoteObject:
             *
             *  UnicastRemoteObject.unexportObject(fileService, true);
             */

        } catch (RemoteException e) {
            System.out.println("Erro remoto - " + e);
            System.exit(1);
        } catch (Exception e) {
            System.out.println("Erro - " + e);
            System.exit(1);
        }
    }

}
