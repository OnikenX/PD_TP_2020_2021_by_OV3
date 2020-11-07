package tp_2021_pd_joaoValente_joaoGoncalves_VascoOliveira.Server.conection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class UdpServer {
    public static final int MAX_SIZE = 256;
    public static final String TIME_REQUEST = "TIME";

    public static void main(String[] args) {

        int listeningPort;
        DatagramSocket socket = null;
        DatagramPacket packet; //para receber os pedidos e enviar as respostas
        String receivedMsg, timeMsg;
        Calendar calendar;

        if(args.length != 1){
            System.out.println("Sintaxe: java UdpTimeServer_v2 listeningPort");
            return;
        }

        try{

            listeningPort = Integer.parseInt(args[0]);
            socket = new DatagramSocket(listeningPort);

            System.out.println("UDP Time Server iniciado...");

            while(true){

                packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
                socket.receive(packet);

                receivedMsg = new String(packet.getData(), 0, packet.getLength());

                System.out.println("Recebido \"" + receivedMsg + "\" de " +
                        packet.getAddress().getHostAddress() + ":" + packet.getPort());

                if(!receivedMsg.equalsIgnoreCase(TIME_REQUEST)){
                    continue;
                }

                calendar = GregorianCalendar.getInstance();
                timeMsg = calendar.get(GregorianCalendar.HOUR_OF_DAY)+":"+
                        calendar.get(GregorianCalendar.MINUTE)+":"+calendar.get(GregorianCalendar.SECOND);

                packet.setData(timeMsg.getBytes());
                packet.setLength(timeMsg.length());

                //O ip e porto de destino ja' se encontram definidos em packet
                socket.send(packet);

            }

        }catch(NumberFormatException e){
            System.out.println("O porto de escuta deve ser um inteiro positivo.");
        }catch(SocketException e){
            System.out.println("Ocorreu um erro ao nivel do socket UDP:\n\t"+e);
        }catch(IOException e){
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t"+e);
        }finally{
            if(socket != null){
                socket.close();
            }
        }
    }
}
