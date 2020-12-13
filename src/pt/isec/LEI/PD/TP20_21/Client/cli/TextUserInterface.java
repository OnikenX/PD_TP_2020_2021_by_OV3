package pt.isec.LEI.PD.TP20_21.Client.cli;

import pt.isec.LEI.PD.TP20_21.Client.ClientObservavel;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos.Conectar;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class TextUserInterface {

    private Scanner s;
    private boolean exit;
    private Conectar pedido;

    public TextUserInterface() {
        s = new Scanner(System.in);
        exit = false;
        UI();
    }

    public void registar() {

    }


    int menuPrincipal() {
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


    private void UI() {
        do {
            int i = menuPrincipal();
            switch (i) {
                case 1:
                    break;
                case 2:
                    break;
            }
        } while(!exit);
    }




    private void UIh() {
        int value;
        boolean isRegistado;
        boolean exit = false;
        String username, pass, name;
        do {
            System.out.println("1- Registo");
            System.out.println("2- Login");
            while (!s.hasNextInt())
                s.next();
            value = s.nextInt();
            if (value > 2 || value < 1) {
                System.out.println("valor nao aceitavel");
                continue;
            }
            isRegistado = value != 1;
            break;
        } while (true);
        int connectionreturn;
        do {

            username = getInput("Username");
            name = getInput("Name");
            pass = getInput("Pass");
            username = getInput("Username");
            pedido = new Conectar(username, name, pass, isRegistado);
            connectionreturn = clientObservavel.connectToServer(pedido);
            switch (connectionreturn) {
                case Utils.Consts.ERROR_SERVER_FULL:
                    System.out.println("Servidor Cheio???");
                    break;
                case Utils.Consts.ERROR_USER_ALREADY_EXISTS:
                    System.out.println("Um utilizador com o mesmo nome ja existe, por favor insira outro.");
                    break;
                case Utils.Consts.ERROR_USER_INFO_NOT_MATCH:
                    System.out.println("Fizeste login com credenciais erradas, por favor insere outra coisa.");
                    break;
                case 0:
                    System.out.println("Servidores nao existentes, podes tentar outra vez ou dar granda yeet!");
                default:
                    System.out.println("connectado com o servidor [" + connectionreturn + "]");
            }
            clientObservavel.setPedido(pedido);
            mainMenu();
        } while (exit);
    }

    /**
     * Menu depois de login
     *
     * @return true for logout, false for exist
     */
    private void mainMenu() {
        LinkedList<String> ops = new LinkedList<>();
        ops.add("Adicinar pessoa");
        ops.add("Listar conversas com pessoas");

//        ops.add("Entrar em sala");
//        ops.add("Criar sala");
//        ops.add("Apagar sala");
        //os 3 anteriores podem ser inseridos num gerir canais
        ops.add("Gerir canais");
        ops.add("Apresentar dados estatisticos de canal");//este podia ser uma opcao quando se esta numa conversa ou num canal
        ops.add("Listar canais entrados");

        ops.add("Listar utilizadores no sistema");
        ops.add("Listar mensagens");//isto provavelmente que e para mostrar quando se abre um canal
        ops.add("sair");

        int resultado = menu(ops);
        switch (resultado){
            case 0:
                adicinarPessoa();
                break;
            case 1:
//                listarConversasComPessoas();
                break;
            case 2:
//                gerirCanais();
                break;
            case 3:
//                apresentarDadosEstatisticosDeCanais();
                break;
            case 4:
//                listarCanaisEntrados();
                break;
            case 5:
//                listarUtilizadoresNoSistema();
                break;
            case 6:
//                listarMensagens();
                break;
            default:
                return;

        }
    }

    private void adicinarPessoa() {
        System.out.println("Digite o username da pessoa: ");
        String nomePessoa = getInput("username da pessoa");

    }

    /**
     * Cria um menu com uma lista
     *
     * @return returna a resposta
     */
    private int menu(List<String> options) {
        int resposta;
        do {
            for (int i = 0; i < options.size(); ++i) {
                System.out.println((i + 1) + " - " + options.get(i));
            }
            System.out.print("> ");
            while (!s.hasNextInt())
                s.next();
            resposta = s.nextInt();
            if (resposta < 1 || resposta > options.size()) {
                System.out.println("Input fora de range, por favor digite o numero do input que quer meter.");
            } else return resposta-1;
        } while (true);

    }

    private String getInput(String info) {
        System.out.println(info + ": ");
        return s.nextLine();
    }


    private void UIRegisto() {
        System.out.println("Id: ");
        String user = s.nextLine();
        System.out.println("Pass: ");
        String pass = s.nextLine();
    }

}
