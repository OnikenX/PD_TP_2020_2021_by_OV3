package pt.isec.LEI.PD.TP20_21.Client.cli;

import java.util.Scanner;

public class TextUserInterface {

    private Scanner s;
    private boolean exit;

    public TextUserInterface() {
        s = new Scanner(System.in);
        exit = false;
    }

    public void run() {
        while(!exit) {

        }
    }


    private void UI() {
        int value;
        System.out.println("1- Registo");
        System.out.println("2- Login");
        while(!s.hasNextInt())
            s.next();
        value = s.nextInt();
        if(value == 1)
            UILogin();
        else {
            UIRegisto();
        }
    }

    private void UILogin() {
        System.out.println("Id: ");
        String user = s.nextLine();
        System.out.println("Pass: ");
        String pass = s.nextLine();
    }

    private void UIRegisto() {
        System.out.println("Id: ");
        String user = s.nextLine();
        System.out.println("Pass: ");
        String pass = s.nextLine();
    }

}
