package pt.isec.LEI.PD.TP20_21.Server.Connectivity;

import pt.isec.LEI.PD.TP20_21.shared.Respostas;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.io.*;
import java.net.*;

public class UdpMultiCast extends Thread {

    public static int MAX_SIZE = 1000;
    protected String username;
    protected MulticastSocket s;
    protected boolean running;

    public void terminate() {
        running = false;
    }

    @Override
    public void run() {

        int listeningPort;
        DatagramPacket packet; //para receber os pedidos e enviar as respostas
        String receivedMsg;

        if (s == null || !running)
            return;

        try {
            packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);

            if (Utils.Consts.DEBUG)
                System.out.println("UdpServerClientPreConnection iniciado...");

            while (running) {
                packet = new DatagramPacket(Utils.Consts.PEDIR_CONECCAO.getBytes(), 0, Utils.Consts.PEDIR_CONECCAO.getBytes().length);
                s.receive(packet);
                if(Utils.Consts.DEBUG)
                    System.out.println("Foi recebido pedido do cliente {"+packet.getAddress()+","+ packet.getPort()+"} para ligação tcp");

                receivedMsg = new String(packet.getData(), 0, packet.getLength());
                if (Utils.Consts.DEBUG)
                    System.out.println("Recebido \"" + receivedMsg + "\" de " +
                            packet.getAddress().getHostAddress() + ":" + packet.getPort());

                if (!receivedMsg.equalsIgnoreCase(Utils.Consts.PEDIR_CONECCAO)) {
                    continue;
                }

                byte[] respostabytes = Utils.objectToBytes(resposta);
                assert respostabytes != null;
                packet.setData(respostabytes, 0, respostabytes.length);
                //O ip e porto de destino ja' se encontram definidos em packet
                s.send(packet);
            }

        } catch (NumberFormatException e) {
            System.out.println("O porto de escuta deve ser um inteiro positivo.");
        } catch (SocketException e) {
            System.out.println("Ocorreu um erro ao nivel do socket UDP:\n\t" + e);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t" + e);
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }



    public static void comunicaMultiCast() {
        InetAddress group;
        int port;
        MulticastSocket socket = null;
        DatagramPacket dgram;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String msg;
        ByteArrayOutputStream buff;
        ObjectOutputStream out;

        UdpMultiCast t = null;

        try {
            group = InetAddress.getByName(args[1]);
            port = Integer.parseInt(args[2]);

            socket = new MulticastSocket(port);

            try {
                socket.setNetworkInterface(NetworkInterface.getByInetAddress(InetAddress.getByName(args[2])));
            } catch (SocketException | NullPointerException | UnknownHostException | SecurityException e) {
                socket.setNetworkInterface(NetworkInterface.getByName(args[3]));
            }

            socket.joinGroup(group);

            t = new MulticastChat_v2(args[0], socket);
            t.start();

            System.out.println("> ");
            while (true) {
                msg = in.readLine();
                if (msg.equalsIgnoreCase(EXIT)) {
                    break;
                }
                buff = new ByteArrayOutputStream();
                out = new ObjectOutputStream(buff);
                out.writeObject(new Msg(args[0], msg));
                out.flush();
                out.close();

                dgram = new DatagramPacket(buff.toByteArray(), buff.size(), group, port);
                socket.send(dgram);
            }
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        } finally {
            if (t != null) {
                t.terminate();
            }
            if (socket != null) {
                socket.close();
            }
        }
    }
}
