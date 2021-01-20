package pt.isec.LEI.PD.TP20_21.Client.RMI;

public interface ClientRMIInterface extends java.rmi.Remote {

    public void receberMensagem(String conteudo, String usuario) throws java.rmi.RemoteException;
    public String getNome() throws java.rmi.RemoteException;
    public void setNome(String nome) throws java.rmi.RemoteException;
}
