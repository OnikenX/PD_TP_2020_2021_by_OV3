package pt.isec.LEI.PD.TP20_21.shared.Data.Utilizador;

class UtilizadorServer extends Utilizador {
    protected String username;
    private String hash;
    private int profilepicId;//guarda a imagem do utilizador, default é 0, o id de uma mensagem sem nada
    public UtilizadorServer(String nome, String foto, String username, String password){
        super(nome, foto);
        this.username = username;
        createHash(password);
    }

    public boolean verifyUser(String username, String password){
        if (!this.username.equals(username))
            return false;
        return verifyPassword(password);
    }

    private void createHash(String password){
        this.hash = password;
    }
    private boolean verifyPassword(String password){
        return password.equals(this.hash);
    }
}
