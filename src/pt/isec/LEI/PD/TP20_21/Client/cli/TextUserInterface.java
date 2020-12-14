package pt.isec.LEI.PD.TP20_21.Client.cli;

import pt.isec.LEI.PD.TP20_21.Client.ClientObservavel;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos.Conectar;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos.MensagemDM;
import pt.isec.LEI.PD.TP20_21.shared.Data.Mensagem;
import pt.isec.LEI.PD.TP20_21.shared.Data.Utilizador.Utilizador;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class TextUserInterface {

    private Scanner s;
    private boolean exit;
    private Conectar pedido;
    private final ClientObservavel clientObservavel;

    public TextUserInterface(ClientObservavel clientObservavel) {
        s = new Scanner(System.in);
        exit = false;
        this.clientObservavel = clientObservavel;
    }


    public Conectar registar() {
        System.out.print("Nome próprio: ");
        var nome = s.nextLine();
        var username = getInput("Username: ");
        var pass = getInput("Pass: ");
        return new Conectar(nome, username, pass, false);
    }

    public Conectar login() {
        var username = getInput("Username: ");
        var pass = getInput("Pass: ");
        return new Conectar(nome, username, pass, true);
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


    public void UI() {
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

        } while (exit);
    }

    /**
     * Menu depois de login
     *
     * @return true for logout, false for exist
     */
    private void listarConversasComPessoas() {
        var listMens = clientObservavel.getClientModel().listMensagens;
        var listUser = clientObservavel.getClientModel().listUsers;
        var listDN = clientObservavel.getClientModel().listCanaisDM;
        List<CanalDM> listMyDn= new LinkedList<>();
        var myId = clientObservavel.getClientModel().getMyId();

        int input;
        System.out.println("Lista de dms:");
        for(var ld : listDN){
            if(ld.getPessoaDest() == myId || ld.getPessoaCria() == myId)
                System.out.println(ld.getId() +" - Para "+
                    ((Utilizador)clientObservavel.getClientModel().getObjectById(ld.getId(), listUser)).getNome());
        }
        boolean sair = false;
        while(!sair) {
            System.out.println("Digite o numero do canal para abrir a conversa: ");
            while (!s.hasNextInt()) ;
            s.nextInt();
            for(var i : clientObservavel.getClientModel().listCanaisDM){
                if(i.getId() == input){
                    sair = true;
                    break;
                }
            }
        }
        for(var i : listMens){
            if(i.getCanalId())
        }


    }
    private void listar_canais()
    {
        var listGroups = clientObservavel.getClientModel().listCanaisGrupos;
        System.out.println("Canais: ");
        for(int i=0;i<listGroups.size();i++)
        {
            System.out.println(listGroups.get(i).getNome()+" Descrição: "+listGroups.get(i).getDescricao()+" Criador: "+listGroups.get(i).getPessoaCria());
        }
    }
    private void listar_users()
    {
        var listUser = clientObservavel.getClientModel().listUsers;
        System.out.println("Canais: ");
        for(int i=0;i<listUser.size();i++)
        {
            System.out.println(listUser.get(i).getNome());
        }
    }

    private void listar_ficheiros()
    {
        List<Mensagem> listMensagens = clientObservavel.getClientModel().listMensagens;
        System.out.println("Canais: ");
        for(int i = 0; i < listMensagens.size(); i++) {
            if(listMensagens.get(i).isAFile())
                System.out.println(listMensagens.get(i).getMensagem());
        }
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


}
