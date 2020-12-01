package pt.isec.LEI.PD.TP20_21.Server;


import pt.isec.LEI.PD.TP20_21.Server.Model.Server;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.sql.SQLException;

// Classe de iniciação do Servidor
public class Main {
    public static void main(String[] args){
        if(Utils.Consts.DEBUG)
            System.out.println("A iniciar o servidor...");
        try {
            var s = new Server();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("Acabado sem grandes problemas....");
    }
}
