package pt.isec.LEI.PD.TP20_21.pdtpbootstrap.security;

public class LoginUser {
    private String username;
    private String password;

    @Override
    public String toString() {
        return "{" +
                "'username':'" + username + '\'' +
                ", 'password':'" + password + '\'' +
                '}';
    }

    public LoginUser() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginUser(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
