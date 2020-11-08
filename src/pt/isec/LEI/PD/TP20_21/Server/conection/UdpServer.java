package pt.isec.LEI.PD.TP20_21.Server.conection;

import pt.isec.LEI.PD.TP20_21.shared.IpServidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Calendar;

import static pt.isec.LEI.PD.TP20_21.shared.Consts.*;


public class UdpServer {
    public static final int MAX_SIZE = 256;
    public static final String TIME_REQUEST = "TIME";

    public static void listener(String[] args) {

        int listeningPort;
        DatagramSocket socket = null;
        DatagramPacket packet; //para receber os pedidos e enviar as respostas
        String receivedMsg, timeMsg;
        Calendar calendar;

        if (args.length != 1) {
            System.out.println("Sintaxe: java UdpTimeServer_v2 listeningPort");
            return;
        }

        try {

            listeningPort = Integer.parseInt(args[0]);
            socket = new DatagramSocket(listeningPort);

            if (DEBUG)
                System.out.println("UDP receive conections iniciado...");

            while (true) {

                packet = new DatagramPacket(PEDIR_CONECCAO.getBytes(), PEDIR_CONECCAO.length(), MAX_SIZE);
                socket.receive(packet);

                receivedMsg = new String(packet.getData(), 0, packet.getLength());

                if (DEBUG)
                    System.out.println("Recebido \"" + receivedMsg + "\" de " +
                        packet.getAddress().getHostAddress() + ":" + packet.getPort());

                if (!receivedMsg.equalsIgnoreCase(TIME_REQUEST)) {
                    continue;
                }

//                calendar = GregorianCalendar.getInstance();
//                timeMsg = calendar.get(GregorianCalendar.HOUR_OF_DAY) + ":" +
//                        calendar.get(GregorianCalendar.MINUTE) + ":" + calendar.get(GregorianCalendar.SECOND);
//
//                packet.setData(timeMsg.getBytes());
//                packet.setLength(timeMsg.length());
                IpServidor resposta;
                if(verificarServidor()){
    resposta =   new IpServidores();
                    packet.setData(ACEITAR_CONECCAO.getBytes());
                    packet.setLength(ACEITAR_CONECCAO.length());
                }else{
                    var aEnviar = .getBytes();

                    packet.setData();
                    packet.setLength(REJEITAR_CONECCAO.length());

                }
                //O ip e porto de destino ja' se encontram definidos em packet
                socket.send(packet);

            }

        } catch (NumberFormatException e) {
            System.out.println("O porto de escuta deve ser um inteiro positivo.");
        } catch (SocketException e) {
            System.out.println("Ocorreu um erro ao nivel do socket UDP:\n\t" + e);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t" + e);
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }




    private static boolean verificarServidor() {
        return true;
    }
}
