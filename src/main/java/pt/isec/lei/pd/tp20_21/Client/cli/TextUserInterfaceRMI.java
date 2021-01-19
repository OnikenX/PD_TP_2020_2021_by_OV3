package pt.isec.lei.pd.tp20_21.Client.cli;

import pt.isec.lei.pd.tp20_21.Client.ClientObservavel;
import pt.isec.lei.pd.tp20_21.shared.Comunicacoes.Pedidos.Conectar;

import java.util.Scanner;

public class TextUserInterfaceRMI {

    private Scanner s;
    private boolean exit;


    public TextUserInterfaceRMI() {
        s = new Scanner(System.in);
        exit = false;
    }

    private Conectar registar() {
        var nome = getInput("Nome: ");
        var username = getInput("Username: ");
        var pass = getInput("Pass: ");
        return new Conectar(username, nome, pass, false);
    }

    private Conectar login() {
        var username = getInput("Username: ");
        var pass = getInput("Pass: ");
        return new Conectar(username, pass);
    }

    private int menuPrincipal() {//menu de login
        int op;
        System.out.println("\n\n1 - Login");
        System.out.println("2 - Registo");
        System.out.println("3 - Sair");
        do {
            System.out.print("Escolha uma opcao: ");
            op = s.nextInt();
        } while(op < 1 || op > 3);
        return op;
    }
}
