package pt.isec.LEI.PD.TP20_21.Server.Connectivity;

import pt.isec.LEI.PD.TP20_21.Server.Model.Server;
import pt.isec.LEI.PD.TP20_21.shared.Mensagens;
import pt.isec.LEI.PD.TP20_21.shared.Password;
import pt.isec.LEI.PD.TP20_21.shared.Respostas;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Objects;

/**
 * Uma thread que fica constantemente a espera de connections de clientes.
 */
public class UdpServerClientPreConnection extends Thread {
    int port;
    Server server;

    public UdpServerClientPreConnection(Server server, int port) {
        super();
        this.port = port;
        this.server = server;
    }

    public void run() {
        int listeningPort;
        DatagramSocket socket = null;
        DatagramPacket packet; //para receber os pedidos e enviar as respostas
        String receivedMsg;
        try {
            listeningPort = 6969;//Integer.parseInt(args[0]);
            socket = new DatagramSocket(listeningPort);
            Mensagens.PedidoDeLigar pedidoDeLigar;
            Respostas.RUdpClientServerPreConnection resposta = new Respostas.RUdpClientServerPreConnection();
            if (Utils.Consts.DEBUG)
                System.out.println("UdpServerClientPreConnection iniciado...");

            while (true) {
                packet = new DatagramPacket(new byte[Mensagens.PedidoDeLigar.SIZE], 0, Mensagens.PedidoDeLigar.SIZE);
                socket.receive(packet);
                if (Utils.Consts.DEBUG)
                    System.out.println("Foi recebido pedido do cliente {" + packet.getAddress() + "," + packet.getPort() + "} para ligação tcp");
                pedidoDeLigar = (Mensagens.PedidoDeLigar) Utils.bytesToObject(packet.getData());
                if (pedidoDeLigar == null) {
                    if (Utils.Consts.DEBUG)
                        System.out.println("O pedido de ligar");
                    continue;
                }
                if (Utils.Consts.DEBUG)
                    System.out.println("Recebido \"" + pedidoDeLigar + "\" de " +
                            packet.getAddress().getHostAddress() + ":" + packet.getPort());

                //registar utilizador
                if (!pedidoDeLigar.isRegistado()) {

                    //verificar se existe o username
                    if (server.getServerData().userExist(pedidoDeLigar.getUsername())) {
                        //addicionar user
                        server.getServerData().addUser(pedidoDeLigar.getUsername(), Password.getSaltedHash(pedidoDeLigar.getPassword()));
                    }
                }


                if (!server.getServerData().userExist(Objects.requireNonNull(pedidoDeLigar).getUsername())) {
                } else if (server.verifyServerAvailability()) {
                    if (Utils.Consts.DEBUG)
                        System.out.println("Foi aceito o cliente {" + packet.getAddress() + "," + packet.getPort() + "} para se ligar ao tcp");
                    resposta = new Respostas.RUdpClientServerPreConnection(server.getTcpPort(), server.getServersForClient());
                } else {
                    if (Utils.Consts.DEBUG)
                        System.out.println("Foi aceito o cliente {" + packet.getAddress() + "," + packet.getPort() + "} para se ligar ao tcp");
                    resposta = new Respostas.RUdpClientServerPreConnection(-1, server.getServersForClient());
                }

                byte[] respostabytes = Utils.objectToBytes(resposta);
                assert respostabytes != null;
                packet.setData(respostabytes, 0, respostabytes.length);
                //O ip e porto de destino ja' se encontram definidos em packet
                socket.send(packet);
            }

        } catch (NumberFormatException e) {
            System.out.println("O porto de escuta deve ser um inteiro positivo.");
        } catch (SocketException e) {
            System.out.println("Ocorreu um erro ao nivel do socket UDP:\n\t" + e);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t" + e);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }


}
