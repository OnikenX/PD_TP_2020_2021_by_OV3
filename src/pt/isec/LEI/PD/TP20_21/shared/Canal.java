package pt.isec.LEI.PD.TP20_21.shared;

import pt.isec.LEI.PD.TP20_21.shared.Cliente.Cliente;

import java.util.ArrayList;

public class Canal {
    private String nome;
    private String descricao;
    private String password;
    private Cliente moderator;
    private ArrayList<Ficheiro> ficheiros;
    private ArrayList<Cliente> utilizadores;
    private ArrayList<Mensagem> mensagens;

}
