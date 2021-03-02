package pt.isec.LEI.PD.TP20_21.pdtpbootstrap.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    public HelloWorldController(){}

    @GetMapping("/hello-world")
    public String helloWorld(@RequestParam(value="name", required=false /*, defaultValue="World"*/) String name)
    {
        return "Hello " + (name == null ? "World" : name) + "!";
    }
    
    @GetMapping("/ola-mundo")
    public String olaMundo(@RequestParam(value="name", required=false /*, defaultValue="Mundo"*/) String name)
    {
        return "Ola " + (name == null ? "Mundo" : name) + "!";
    }
    
}
