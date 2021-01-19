package pt.isec.lei.pd.tp20_21.Client;
import pt.isec.lei.pd.tp20_21.Client.cli.TextUserInterface;
import pt.isec.lei.pd.tp20_21.shared.Utils;

public class Client_Main {
    public static void main(String[] args) {
        if (Utils.Consts.DEBUG)
            System.out.println("A comecar o cliente....");
        //var pedido = new Pedido.Conectar("lol","lol" , "password", false);
        //ClientServerConnection connection = new ClientServerConnection(pedido);
        ClientObservavel clientObservavel = new ClientObservavel();
        TextUserInterface cli = new TextUserInterface(clientObservavel);
        cli.UI();
    }


}
