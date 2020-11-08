package pt.isec.LEI.PD.TP20_21.Client.init;

import java.io.IOException;
import java.net.*;
import java.util.StringTokenizer;

import static pt.isec.LEI.PD.TP20_21.shared.Consts.*;

public class UdpClient {

    public static final int MAX_SIZE = 256;
    public static final String TIME_REQUEST = "TIME";
    public static final int TIMEOUT = 10; //segundos

    public static void main(String[] args) {
        InetAddress serverAddr;
        int serverPort = -1;
        DatagramSocket socket = null;
        DatagramPacket packet;
        String response;

        if (args.length != 2) {
            System.out.println("Sintaxe: java UdpTimeClient serverAddress serverUdpPort");
            return;
        }

        try {
            serverAddr = InetAddress.getByName(args[0]);
            serverPort = Integer.parseInt(args[1]);

            socket = new DatagramSocket();
            socket.setSoTimeout(TIMEOUT * 1000);
            packet = new DatagramPacket(PEDIR_CONECCAO.getBytes(), PEDIR_CONECCAO.length(), serverAddr, serverPort);

            socket.send(packet);

            packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
            socket.receive(packet);

            response = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Hora indicada pelo servidor: " + response);

            //******************************************************************
            //Exemplo de como retirar os valores da mensagem de texto
            try {
                StringTokenizer tokens = new StringTokenizer(response, " :");

                int hour = Integer.parseInt(tokens.nextToken().trim());
                int minute = Integer.parseInt(tokens.nextToken().trim());
                int second = Integer.parseInt(tokens.nextToken().trim());

                System.out.println("Horas: " + hour + " ; Minutos: " + minute + " ; Segundos: " + second);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            //******************************************************************

        } catch (UnknownHostException e) {
            System.out.println("Destino desconhecido:\n\t" + e);
        } catch (NumberFormatException e) {
            System.out.println("O porto do servidor deve ser um inteiro positivo.");
        } catch (SocketTimeoutException e) {
            System.out.println("Nao foi recebida qualquer resposta:\n\t" + e);
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
}
