package pt.isec.LEI.PD.TP20_21.pdtpbootstrap.security;

public class RMIConnection {
    private String iprmi;
    private String nome;

    public RMIConnection() {
    }

    public String getIprmi() {
        return iprmi;
    }

    public void setIprmi(String iprmi) {
        this.iprmi = iprmi;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public RMIConnection(String iprmi, String nome) {
        this.iprmi = iprmi;
        this.nome = nome;
    }
}
