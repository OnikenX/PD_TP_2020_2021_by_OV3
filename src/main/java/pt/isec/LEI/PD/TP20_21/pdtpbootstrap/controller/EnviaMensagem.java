package pt.isec.LEI.PD.TP20_21.pdtpbootstrap.controller;

import org.springframework.web.bind.annotation.*;
import pt.isec.LEI.PD.TP20_21.pdtpbootstrap.security.Token;
@RestController
public class EnviaMensagem {
    @PostMapping("/send-mensagem")
    public String sendMensagem(@RequestHeader(value = "authentication", required = true)String token, @RequestBody String mensagem){
        if(Token.validateToken(token))
            return "token invalido";
        String username = Token.getUsernameByToken(token);

        return "";
    }
}
