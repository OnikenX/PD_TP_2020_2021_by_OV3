package pt.isec.LEI.PD.TP20_21.shared;

import pt.isec.LEI.PD.TP20_21.shared.Data.Utilizador.Utilizador;

import java.util.ArrayList;

public class Canal {
    private String nome;
    private String descricao;
    private Utilizador moderator;
    private ArrayList<Ficheiro> ficheiros;
    private ArrayList<Utilizador> utilizadores;
    private ArrayList<Mensagem> mensagens;

    public Canal(String nome, String descricao, Utilizador moderator, ArrayList<Ficheiro> ficheiros, ArrayList<Utilizador> utilizadores, ArrayList<Mensagem> mensagens) {
        this.nome = nome;
        this.descricao = descricao;
        this.moderator = moderator;
        this.ficheiros = ficheiros;
        this.utilizadores = utilizadores;
        this.mensagens = mensagens;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Utilizador getModerator() {
        return moderator;
    }

    public void setModerator(Utilizador moderator) {
        this.moderator = moderator;
    }

    public ArrayList<Ficheiro> getFicheiros() {
        return ficheiros;
    }

    public void setFicheiros(ArrayList<Ficheiro> ficheiros) {
        this.ficheiros = ficheiros;
    }

    public ArrayList<Utilizador> getUtilizadores() {
        return utilizadores;
    }

    public void setUtilizadores(ArrayList<Utilizador> utilizadores) {
        this.utilizadores = utilizadores;
    }

    public ArrayList<Mensagem> getMensagens() {
        return mensagens;
    }

    public void setMensagens(ArrayList<Mensagem> mensagens) {
        this.mensagens = mensagens;
    }
}
