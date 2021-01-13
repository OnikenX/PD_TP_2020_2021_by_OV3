package pt.isec.LEI.PD.TP20_21.Client.RMI;

public interface ClientRMIInterface extends java.rmi.Remote {

    public void receberMensagem(String conteudo) throws java.rmi.RemoteException;
}
