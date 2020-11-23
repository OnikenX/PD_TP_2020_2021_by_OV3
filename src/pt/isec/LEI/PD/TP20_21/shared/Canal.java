package pt.isec.LEI.PD.TP20_21.shared;

import pt.isec.LEI.PD.TP20_21.shared.Data.Utilizador.Utilizador;

import java.util.ArrayList;

public class Canal {
    private String nome;
    private String descricao;
    private String password;
    private Utilizador moderator;
    private ArrayList<Ficheiro> ficheiros;
    private ArrayList<Utilizador> utilizadores;
    private ArrayList<Mensagem> mensagens;

}
