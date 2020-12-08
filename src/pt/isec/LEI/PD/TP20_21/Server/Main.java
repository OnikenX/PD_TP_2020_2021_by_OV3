package pt.isec.LEI.PD.TP20_21.Server;


import pt.isec.LEI.PD.TP20_21.Server.Model.Server;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.io.IOException;
import java.sql.SQLException;

// Classe de iniciação do Servidor
public class Main {
    public static void main(String[] args){
        int server_number = 1;
        if (args.length >= 1)
            try{server_number = Integer.parseInt(args[0]);}catch(Exception ignore){}
        if(Utils.Consts.DEBUG)
            System.out.println("A iniciar o servidor...");
        try {
            var s = new Server(server_number);
        } catch (SQLException | IOException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("Acabado sem grandes problemas....");
    }
}
