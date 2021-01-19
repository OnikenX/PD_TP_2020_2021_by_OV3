package pt.isec.lei.pd.tp20_21.Client.RMI;

public interface ClientRMIInterface extends java.rmi.Remote {

    public void receberMensagem(String conteudo) throws java.rmi.RemoteException;
    public String getNome() throws java.rmi.RemoteException;
}
