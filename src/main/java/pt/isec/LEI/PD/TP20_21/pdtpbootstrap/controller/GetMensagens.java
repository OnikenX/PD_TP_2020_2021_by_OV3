package pt.isec.LEI.PD.TP20_21.pdtpbootstrap.controller;

import jdk.jshell.execution.Util;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.isec.LEI.PD.TP20_21.Server.Model.Data.ServerDB;
import pt.isec.LEI.PD.TP20_21.shared.Data.Mensagem;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RestController
public class GetMensagens {
    @GetMapping("/get-mensagens")
    public List<String> getMensagens(@RequestParam(value = "n", required = false, defaultValue = "5") String numero) throws SQLException {
        int intnum = Integer.parseInt(numero);
        var db = Utils.getServerDBSingleton();
        return db.getLastMensagemDMs(intnum);
    }



}
