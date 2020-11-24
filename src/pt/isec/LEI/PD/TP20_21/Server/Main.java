package pt.isec.LEI.PD.TP20_21.Server;


import pt.isec.LEI.PD.TP20_21.Server.Model.Server;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

// Classe de iniciação do Servidor
public class Main {
    public static void main(String[] args){
        if(Utils.Consts.DEBUG)
            System.out.println("A iniciar o servidor...");
        var s = new Server();
        System.out.println("Acabado sem grandes problemas....");
    }
}
