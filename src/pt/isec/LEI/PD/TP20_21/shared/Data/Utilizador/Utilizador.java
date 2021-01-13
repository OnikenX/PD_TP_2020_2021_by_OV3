package pt.isec.LEI.PD.TP20_21.shared.Data.Utilizador;

import pt.isec.LEI.PD.TP20_21.shared.Data.DataBase;

public class Utilizador  implements DataBase {
    private final int id;
    private final String username;
    private final String nome;



    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getNome() {
        return nome;
    }

    public Utilizador(int id, String username, String nome) {
        this.id = id;
        this.username = username;
        this.nome = nome;
    }
}
