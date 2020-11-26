package pt.isec.LEI.PD.TP20_21.Client;

import pt.isec.LEI.PD.TP20_21.Client.Connectivity.ClientServerConnection;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

public class Main {
    public static void main(String[] args) {
        if (Utils.Consts.DEBUG)
            System.out.println("A comecar o cliente....");
        ClientServerConnection connection = new ClientServerConnection();
        connection.start();
    }

}
