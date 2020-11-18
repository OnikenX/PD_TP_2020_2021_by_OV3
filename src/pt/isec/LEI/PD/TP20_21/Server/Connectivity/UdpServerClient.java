package pt.isec.LEI.PD.TP20_21.Server.Connectivity;

import pt.isec.LEI.PD.TP20_21.Server.Model.Server;
import pt.isec.LEI.PD.TP20_21.shared.IpServidores;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import static pt.isec.LEI.PD.TP20_21.shared.Consts.*;


public class UdpServerClient extends Thread {
    int port;
    Server server;

    public UdpServerClient(Server server, int port){
        super();
        this.port = port;
        this.server = server;
    }

    public void run() {

        int listeningPort;
        DatagramSocket socket = null;
        DatagramPacket packet; //para receber os pedidos e enviar as respostas

//        if (args.length != 1) {
//            System.out.println("Sintaxe: java UdpTimeServer_v2 listeningPort");
//            return;
//        }

        try {

            listeningPort = 6969;//Integer.parseInt(args[0]);
            socket = new DatagramSocket(listeningPort);

            if (DEBUG)
                System.out.println("UDP receive conections iniciado...");

            while (true) {

                packet = new DatagramPacket(PEDIR_CONECCAO.getBytes(), PEDIR_CONECCAO.length(), PEDIR_CONECCAO.length());
                socket.receive(packet);

                receivedMsg =
                if (DEBUG)
                    System.out.println("Recebido \"" + receivedMsg + "\" de " +
                            packet.getAddress().getHostAddress() + ":" + packet.getPort());

                if (!receivedMsg.equalsIgnoreCase(PEDIR_CONECCAO)) {
                    continue;
                }

//                calendar = GregorianCalendar.getInstance();
//                timeMsg = calendar.get(GregorianCalendar.HOUR_OF_DAY) + ":" +
//                        calendar.get(GregorianCalendar.MINUTE) + ":" + calendar.get(GregorianCalendar.SECOND);
//
//                packet.setData(timeMsg.getBytes());
//                packet.setLength(timeMsg.length());


                IpServidores resposta;
                if (Server.verificarServidor()) {

                    //crea uma
                    int TCPport = server.createTCPClientConnection();
                    resposta = new IpServidores(true, null, TCPport);
                } else {
                    resposta = new IpServidores(false, server.getServersInfo(), -1);
                }


                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream out = null;
                try {
                    out = new ObjectOutputStream(bos);
                    out.writeObject((Object) resposta);
                    out.flush();
                    byte[] respostaBytes = bos.toByteArray();
                    packet.setData(respostaBytes);
                    packet.setLength(respostaBytes.length);
                } finally {
                    try {
                        bos.close();
                    } catch (IOException ex) {
                        // ignore close exception
                    }
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


}
