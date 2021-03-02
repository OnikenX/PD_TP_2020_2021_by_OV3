package pt.isec.LEI.PD.TP20_21.pdtpbootstrap.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorController {
    public ErrorController(){}

    @GetMapping ("/error")
    public String error(){return "Error default";}

}
