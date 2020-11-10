package pt.isec.LEI.PD.TP20_21.shared.Cliente;

import pt.isec.LEI.PD.TP20_21.shared.Cliente.Cliente;

class ClienteServer extends Cliente {
    protected String username;
    private String hash;
    public ClienteServer(String nome, String foto, String username, String password){
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
