package pt.isec.LEI.PD.TP20_21.Server.Connectivity;

import pt.isec.LEI.PD.TP20_21.Server.Model.Server;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.io.*;
import java.net.*;


public class UdpMultiCast extends Thread {

    //    protected String username;
    protected MulticastSocket multicastSocket;
    protected boolean running;
    protected Server server;

    public UdpMultiCast(Server server) throws IOException {
        this.multicastSocket = new MulticastSocket(Utils.Consts.UDP_MULTICAST_PORT);
        multicastSocket.joinGroup(InetAddress.getByName(Utils.Consts.UDP_MULTICAST_GROUP));

        this.server = server;
        this.running = false;
        setDaemon(true);

        start();
    }

    public synchronized void terminate() {
        running = false;
    }

    //recebe uma mensagem multicast
    @Override
    public void run() {

        DatagramPacket packet; //para receber os pedidos e enviar as respostas
        Object receivedObject;
        if (multicastSocket == null || !running)
            return;
        try {
            packet = new DatagramPacket(new byte[Utils.Consts.MAX_SIZE_PER_PACKET], Utils.Consts.MAX_SIZE_PER_PACKET);
            if (Utils.Consts.DEBUG)
                System.out.println("UdpMultiCast activado iniciado...");
            while (running) {
                multicastSocket.receive(packet);
                if (Utils.Consts.DEBUG)
                    System.out.println("Foi recebido pedido do cliente {" + packet.getAddress() + "," + packet.getPort() + "} para ligação UdpMulticast");

                receivedObject = Utils.bytesToObject(packet.getData());
                if (receivedObject == null) {
                    if (Utils.Consts.DEBUG)
                        System.out.println("Received object is null, skipping...");
                    continue;
                }
                if (Utils.Consts.DEBUG)
                    System.out.println("Recebido \"" + receivedObject + "\" de " +
                            packet.getAddress().getHostAddress() + ":" + packet.getPort() + " [" + packet.getLength() + " bytes]");

                if (!receivedMsg.equalsIgnoreCase(Utils.Consts.PEDIR_CONECCAO)) {
                    continue;
                }

                byte[] respostabytes = Utils.objectToBytes(resposta);
                assert respostabytes != null;
                packet.setData(respostabytes, 0, respostabytes.length);
                //O ip e porto de destino ja' se encontram definidos em packet
                multicastSocket.send(packet);
            }
        } catch (NumberFormatException e) {
            System.out.println("O porto de escuta deve ser um inteiro positivo.");
        } catch (SocketException e) {
            System.out.println("Ocorreu um erro ao nivel do socket UDP:\n\t" + e);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t" + e);
        } finally {
            if (multicastSocket != null) {
                multicastSocket.close();
            }
        }
    }


    //enviar mensagem

    /**
     * Envia uma mensagem por multicast
     *
     * @param mensagem       a enviar
     * @param recebeResposta se deve esperar por uma resposta ou não, returna null se nao receber uma.
     */
    synchronized public Object enviaMulticast(Object mensagem, boolean recebeResposta) {

    }
}
