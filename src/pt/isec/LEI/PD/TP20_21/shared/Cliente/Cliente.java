package pt.isec.LEI.PD.TP20_21.shared.Cliente;
public class Cliente {
    protected String nome;
    protected String foto;
    public Cliente(String nome, String foto){
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
