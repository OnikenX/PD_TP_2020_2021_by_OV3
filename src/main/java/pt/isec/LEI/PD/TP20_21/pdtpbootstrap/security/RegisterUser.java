package pt.isec.LEI.PD.TP20_21.pdtpbootstrap.security;

public class RegisterUser {
    private String username;
    private String nome;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RegisterUser(String username, String nome, String pass) {
        this.username = username;
        this.nome = nome;
        this.password = pass;
    }
}