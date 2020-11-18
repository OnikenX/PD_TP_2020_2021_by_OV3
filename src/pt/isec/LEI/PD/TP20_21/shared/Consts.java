package pt.isec.LEI.PD.TP20_21.shared;

import java.util.ArrayList;

public class Consts {
    public static boolean DEBUG = true;
    //codigos de mensagem
    public static final String PEDIR_CONECCAO = "1";
    public static final String ACEITAR_CONECCAO = "2";
    public static final String REJEITAR_CONECCAO = "-1";

    //o tamanho que os codigos tem
    public static final byte SIZE_CODE = 1;

    //Porta a ser partilhada a todos os server para fazer multicast
    public static final int UDP_MulticastServerPort = 69690;

    //Porta de default para o client se ligar ao servidor
    public static int UDPClientRequestPort = 69691;

    //maximo de users existente num servidor
    public static final int MAX_LOTACAO = 5;



}

