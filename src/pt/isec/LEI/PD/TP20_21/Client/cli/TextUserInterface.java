package pt.isec.LEI.PD.TP20_21.Client.cli;

import pt.isec.LEI.PD.TP20_21.Client.ClientObservavel;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos.Conectar;
import pt.isec.LEI.PD.TP20_21.shared.Data.Mensagem;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

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
    private void menu_ler_mensagens()
    {
        int op;
        int carrega=999999;
        listar_users();
        listar_canais();
        System.out.println("1- Ler mensagens de canal.");
        System.out.println("2- Ler mensagens de utilizador.");
        do {
            System.out.print("Escolha uma opcao: ");
            op = s.nextInt();
        } while(op < 1 || op > 2);
        System.out.println("Nome do cliente alvo ou canal.");
        String nome= s.nextLine();
        //verifica se existe tal canal ou utilizador e mostra as mensagens por orderm cronológica
        var listUser = clientObservavel.getClientModel().listUsers;
        var listGroups = clientObservavel.getClientModel().listCanaisGrupos;
        var mensagens = clientObservavel.getClientModel().listMensagens;
        System.out.println("Quantas mensagens deve carregar ?");
        while ((carrega>mensagens.size()&&carrega<0))
        {
            carrega = s.nextInt();
        }

        if(op==1)
        {
            for(int i=0;i<listGroups.size();i++)
            {
                if (nome==listGroups.get(i).getNome())
                {
                    for(int j=0;j<carrega;j++)
                    {
                        if(mensagens.get(j).getId()==listGroups.get(i).getId())
                        {
                            System.out.println(listGroups.get(i).getNome()+" "+mensagens.get(j).getMensagem());
                        }
                    }
                }
            }
        }
        if(op==2)
        {
            for(int i=0;i<listUser.size();i++)
            {
                if (nome==listUser.get(i).getNome())
                {
                    for(int j=0;j<carrega;j++)
                    {
                        if(mensagens.get(j).getId()==listUser.get(i).getId())
                        {
                            System.out.println(listUser.get(i).getNome()+" "+mensagens.get(j).getMensagem());
                        }
                    }
                }
            }
        }

    }

    private void menu_editar_canais()
    {
        int op;
        String c;
        listar_canais();
        System.out.println("1- Criar canal");
        System.out.println("2- Apagar canal");
        do {
            System.out.print("Escolha uma opcao: ");
            op = s.nextInt();
        } while(op < 1 || op > 3);
       switch (op)
       {
           case 1:
               System.out.println("Insira o nome do canal a criar.");
               String nome= s.nextLine();
               System.out.println("Insira a senha do canal.");
               String password= s.nextLine();
               System.out.println("Insira a descrição do canal.");
               String desc= s.nextLine();
               break;
           case 4:
               System.out.println("Insira o nome do canal a apagar.");
               nome= s.nextLine();
               //procura canal, se nao encontrar diz
               do {
                   System.out.print("Tem a certeza ? Y/N ");
                   c= s.nextLine();
               } while((c.substring(0,1)!= "Y") ||(c.substring(0,1)!= "N"));
               if((c.substring(0,1)!= "Y"))
               {
                   //apaga canal
               }
               break;

       }

    }
    private int menuLogado() {//chama isto depois do login
        int op;
        System.out.println("\n\n1- Listar Canais");
        System.out.println("2- Listar Utilizadores");
        System.out.println("3- Mandar mensagem privada");
        System.out.println("4- Listar n ultimas mensagens");
        System.out.println("5- Editar canais");
        System.out.println("6- Enviar ficheiros");
        System.out.println("7- Listar ficheiros recebidos");
        System.out.println("8- Apresentar estatisticas de um canal");
        System.out.println("9- Sair");
        do {
            System.out.print("Escolha uma opcao: ");
            op = s.nextInt();
        } while(op < 1 || op > 9);
        return op;

    }


    public void UI() {
        do {
            int i = menuPrincipal();
            switch (i) {
                case 1:
                    Conectar log = login();
                    clientObservavel.getClientModel().registarOuLogin(log);
                    do {
                        i = menuLogado();
                        switch (i) {
                            case 1:
                                listar_canais();
                                break;
                            case 2:
                                listar_users();
                                break;
                            case 3:
                                String remetente = getInput("Utilizador a quem pretende enviar");
                                String mensagem = getInput("Mensagem a enviar");
                                int idr = clientObservavel.getClientModel().getUserIdByName(remetente);
                                int idMeu = clientObservavel.getClientModel().getMyId();
                                clientObservavel.getClientModel().mandaMensPessoal(idr, idMeu, mensagem, false);
                                break;
                            case 4:
                                menu_ler_mensagens();
                                break;
                            case 5:
                                menu_editar_canais();
                                break;
                            case 6:
                                String remetenteFich = getInput("Utilizador a quem pretende enviar");
                                String mensagemFich = getInput("Nome do ficheiro a enviar");
                                int idrFich = clientObservavel.getClientModel().getUserIdByName(remetenteFich);
                                int idMeuFich = clientObservavel.getClientModel().getUserIdByName(clientObservavel.getClientModel().pedido.getNome());
                                clientObservavel.getClientModel().mandaMensPessoal(idrFich, idMeuFich, mensagemFich, true);
                                break;
                            case 7:
                                listar_ficheiros();
                                break;
                            case 8:
                                break;
                        }
                    } while(i != 9);
                    break;
                case 2:
                    Conectar regis = registar();
                    clientObservavel.getClientModel().registarOuLogin(regis);
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

//    /**
//     * Menu depois de login
//     *
//     * @return true for logout, false for exist
//     */
//    private void listarConversasComPessoas() {
//        var listMens = clientObservavel.getClientModel().listMensagens;
//        var listUser = clientObservavel.getClientModel().listUsers;
//        var listDN = clientObservavel.getClientModel().listCanaisDM;
//        List<CanalDM> listMyDn= new LinkedList<>();
//        var myId = clientObservavel.getClientModel().getMyId();
//
//        int input;
//        System.out.println("Lista de dms:");
//        for(var ld : listDN){
//            if(ld.getPessoaDest() == myId || ld.getPessoaCria() == myId)
//                System.out.println(ld.getId() +" - Para "+
//                    ((Utilizador)clientObservavel.getClientModel().getObjectById(ld.getId(), listUser)).getNome());
//        }
//        boolean sair = false;
//        while(!sair) {
//            System.out.println("Digite o numero do canal para abrir a conversa: ");
//            while (!s.hasNextInt()) ;
//            s.nextInt();
//            for(var i : clientObservavel.getClientModel().listCanaisDM){
//                if(i.getId() == input){
//                    sair = true;
//                    break;
//                }
//            }
//        }
//        for(var i : listMens){
//            if(i.getCanalId())
//        }
//
//
//    }
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
        String input;
        System.out.println(info + ": ");
        do {
            input = s.nextLine();
        }while(input.isEmpty());
        return input;
    }


}
