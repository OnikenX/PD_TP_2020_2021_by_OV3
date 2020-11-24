package pt.isec.LEI.PD.TP20_21.Client;

import javafx.application.*;
import javafx.stage.Stage;
import pt.isec.LEI.PD.TP20_21.Client.Connectivity.UDPClientServerConnect;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

public class Main {
    public static void main(String[] args) {
        if (Utils.Consts.DEBUG)
            System.out.println("A comecar o cliente....");
        UDPClientServerConnect connection = new UDPClientServerConnect();
        connection.start();
    }

}
