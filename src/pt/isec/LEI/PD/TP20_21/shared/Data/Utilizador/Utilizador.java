package pt.isec.LEI.PD.TP20_21.shared.Data.Utilizador;
public class Utilizador {
    protected String username;
    protected String nome;
    public Utilizador(String username, String nome){
        this.username = username;
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public String getUsername() {
        return username;
    }
}
