package pt.isec.LEI.PD.TP20_21.pdtpbootstrap.controller;


import org.springframework.web.bind.annotation.*;
import pt.isec.LEI.PD.TP20_21.Server.Model.Data.ServerDB;
import pt.isec.LEI.PD.TP20_21.pdtpbootstrap.security.LoginUser;
import pt.isec.LEI.PD.TP20_21.pdtpbootstrap.security.RegisterUser;
import pt.isec.LEI.PD.TP20_21.pdtpbootstrap.security.Token;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.sql.SQLException;

@RestController
//@RequestMapping("user")
public class UserController {
    public UserController() {
    }

    ServerDB db = Utils.getServerDBSingleton();


    /**
     * @param loginUser
     * @return Token de login
     */
    @PostMapping("user/login")
    public String login(@RequestBody LoginUser loginUser) {
        if (db.verifyUser(loginUser.getUsername(), loginUser.getPassword())) {
            try {
                return Token.getNewToken(loginUser.getUsername());
            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
        } else
            return "Erro: user nao existe.";
    }

    //TODO
    @PostMapping("user/register")
    public String register(@RequestBody RegisterUser registerUser) {
        try {
            db.addNewUser(registerUser.getUsername(), registerUser.getNome(), registerUser.getPassword());
            return "Logado";
        } catch (SQLException throwables) {
            if (Utils.Consts.DEBUG)
                throwables.printStackTrace();
            try {
                if (db.userExist(registerUser.getUsername()))
                    return "User exist";
            } catch (SQLException e) {
                if (Utils.Consts.DEBUG)
                    e.printStackTrace();
                return e.getMessage();
            }
        } catch (Exception e) {
            if (Utils.Consts.DEBUG)
                e.printStackTrace();
            return e.getMessage();
        }
        return null;
    }
}

