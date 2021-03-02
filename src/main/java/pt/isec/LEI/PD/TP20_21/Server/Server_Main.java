package pt.isec.LEI.PD.TP20_21.Server;


import pt.isec.LEI.PD.TP20_21.Server.Model.Connectivity.RMI.ServerRMI;
import pt.isec.LEI.PD.TP20_21.Server.Model.Server;
import pt.isec.LEI.PD.TP20_21.pdtpbootstrap.PdtpbootstrapApplication;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.io.IOException;
import java.sql.SQLException;

// Classe de iniciação do Servidor
public class Server_Main {
    public static void main(String[] args) {

        System.out.println("A iniciar o servidor...");
        Utils.argumentos = new Utils.Argumentos(args);
        Utils.getServerDBSingleton();
        ServerRMI.main(args);

        PdtpbootstrapApplication.main(args);

//          Server por sockets
//            var s = new Server(server_number);


        System.out.println("Acabado sem grandes problemas....");
    }
}
