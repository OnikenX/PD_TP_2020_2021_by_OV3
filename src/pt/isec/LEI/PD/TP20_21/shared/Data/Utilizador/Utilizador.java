package pt.isec.LEI.PD.TP20_21.shared.Data.Utilizador;
public class Utilizador {
    protected String nome;
    protected String foto;
    public Utilizador(String nome, String foto){
        this.nome = nome;
        this.foto = foto;
    }

    public String getNome() {
        return nome;
    }

    public String getFoto() {
        return foto;
    }
}
