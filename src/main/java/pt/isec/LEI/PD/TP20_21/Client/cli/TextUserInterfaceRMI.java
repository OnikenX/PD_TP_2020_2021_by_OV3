package pt.isec.LEI.PD.TP20_21.Client.cli;

import pt.isec.LEI.PD.TP20_21.Client.RMI.ClientRMIInterface;
import pt.isec.LEI.PD.TP20_21.Server.Model.Connectivity.RMI.ServerRMIInterface;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos.Conectar;
import pt.isec.LEI.PD.TP20_21.shared.Password;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.util.Scanner;

public class TextUserInterfaceRMI {

    private Scanner s;
    private boolean exit;


    public TextUserInterfaceRMI() {
        s = new Scanner(System.in);
        exit = false;
    }

    //TODO: mudar o que recebe para Conectar
    private Conectar registar() {
        var nome = getInput("Nome: ");
        var username = getInput("Username: ");
        var pass = getInput("Pass: ");
        return new Conectar(username, nome, pass, false);
    }

    private Conectar loginUI() {
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
        } while (op < 1 || op > 3);
        return op;
    }

    private int menuLogado() {//chama isto depois do login
        int op;
        System.out.println("1- Listar Utilizadores");
        System.out.println("2- Mandar mensagem privada");
        System.out.println("3- Mandar mensagem para todos os utilizadores de um canal");
        System.out.println("4- Sair");
        do {
            System.out.print("Escolha uma opcao: ");
            op = s.nextInt();
        } while (op < 1 || op > 4);
        return op;

    }

    public void UI() {
        do {
            int i = menuPrincipal();
            switch (i) {
                case 1:
                    Conectar log = loginUI();
                    //para fazer login precisas do username e da pass
                    //os ids sao coisas de bases de dados, os ids só são usados quando em logicas de managemento das dbs ou para enviar a outro servidor para guardar na sua db
                    try {
                        if (!server.loginCliente(client, log.getUsername(), log.getPassword())) {
                            System.out.println("Dados mal!!!");
                            break;
                        }
                    } catch (RemoteException e) {
                        System.out.println("Erroooo");
                        if (Utils.Consts.DEBUG)
                            e.printStackTrace();
                    } catch (Exception e) {
                        if (Utils.Consts.DEBUG)
                            e.printStackTrace();
                    }
                    do {
                        i = menuLogado();
                        switch (i) {
                            case 1:
                                try {
                                    server.listaClientes(client);
                                } catch (RemoteException e) {
                                    System.out.println("Erroooo");
                                    if (Utils.Consts.DEBUG)
                                        e.printStackTrace();
                                }
                                break;
                            case 2:
                                String remetente = getInput("Utilizador a quem pretende enviar");
                                String mensagem = getInput("Mensagem a enviar");
                                try {
                                    server.enviaMensagem(mensagem, remetente, client.getNome());
                                } catch (RemoteException e) {
                                    System.out.println("Erro---");
                                    if (Utils.Consts.DEBUG)
                                        e.printStackTrace();
                                }
                                break;
                            case 4:
                                mensagem = getInput("Mensagem a enviar");
                                try {
                                    server.enviaMensagem(mensagem, client.getNome());
                                } catch (RemoteException e) {
                                    System.out.println("Erro---");
                                    if (Utils.Consts.DEBUG)
                                        e.printStackTrace();
                                }
                                break;
                        }
                    } while (i != 5);
                    break;
                case 2:
                    Conectar regis = registar();
                    try {
                        server.registoCliente(regis);
                    } catch (RemoteException e) {
                        System.out.println("erro remoto");
                        if (Utils.Consts.DEBUG)
                            e.printStackTrace();
                    } catch (SQLException throwables) {
                        System.out.println("Erro sql");
                        if (Utils.Consts.DEBUG)
                            throwables.printStackTrace();
                    } catch (Exception e) {
                        if (Utils.Consts.DEBUG)
                            e.printStackTrace();
                    }
                    break;
            }
        } while (!exit);
    }

    private String getInput(String info) {
        String input;
        System.out.println(info + ": ");
        do {
            input = s.nextLine();
        } while (input.isEmpty());
        return input;
    }

}


