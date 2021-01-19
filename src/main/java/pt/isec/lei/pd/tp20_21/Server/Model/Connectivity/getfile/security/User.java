package pt.isec.lei.pd.tp20_21.Server.Model.Connectivity.getfile.security;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User
{
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password; 
    private String token;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
