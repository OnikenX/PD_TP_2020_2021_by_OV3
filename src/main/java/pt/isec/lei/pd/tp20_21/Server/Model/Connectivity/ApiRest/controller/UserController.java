package pt.isec.lei.pd.tp20_21.Server.Model.Connectivity.ApiRest.controller;

import org.springframework.web.bind.annotation.*;
import pt.isec.lei.pd.tp20_21.Server.Model.Connectivity.ApiRest.security.Token;
import pt.isec.lei.pd.tp20_21.Server.Model.Connectivity.ApiRest.security.User;

@RestController
//@RequestMapping("user")
public class UserController
{
    @PostMapping("user/login")
    public User login(@RequestBody User user)
    {
        // TODO: Login with database (check username and password)?

        String token = Token.getNewToken(user.getUsername());
        user.setToken(token);
        return user;
    }
}

