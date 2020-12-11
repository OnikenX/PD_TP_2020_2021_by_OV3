package pt.isec.LEI.PD.TP20_21.Server.Model.Connectivity;

import pt.isec.LEI.PD.TP20_21.Server.Model.Server;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Respostas;
import pt.isec.LEI.PD.TP20_21.shared.Password;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedido;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import static pt.isec.LEI.PD.TP20_21.shared.Utils.objectToBytes;

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
        this.setDaemon(true);
    }

    public void run() {
        int listeningPort;
        DatagramSocket socket = null;
        DatagramPacket packet; //para receber os pedidos e enviar as respostas
        String receivedMsg;
        try {
            try {
                socket = new DatagramSocket(Utils.Consts.UDP_CLIENT_REQUEST_PORT);
            } catch (Exception e) {
                System.out.println("Erro a ligar, a port " + Utils.Consts.UDP_CLIENT_REQUEST_PORT + "a tentar conectar outra porta do sistema aleatoria.");
                socket = new DatagramSocket();
            }
            Pedido.Conectar conectar;
            byte[] conectarBytes = objectToBytes(new Pedido.Conectar());

            Respostas.PedidoDeLigar resposta;
            boolean registar;
            if (Utils.Consts.DEBUG)
                System.out.println("UdpServerClientPreConnection iniciado...");
            while (true) {
                //rececao de dados
                {
                    packet = new DatagramPacket(conectarBytes, conectarBytes.length);
                    resposta = null;
                    registar = false;
                    //recebe o pedido de login/registo
                    socket.receive(packet);
                    resposta = null;
                    if (Utils.Consts.DEBUG)
                        System.out.println("Foi recebido pedido do cliente {" + packet.getAddress() + "," + packet.getPort() + "} para ligação tcp");
                    conectar = (Pedido.Conectar) Utils.bytesToObject(packet.getData());
                    //verifica se foi bem convertido para objecto
                    if (conectar == null) {
                        if (Utils.Consts.DEBUG)
                            System.out.println("Pedido de ligar mal recebido, a esperar por outro...");
                        continue;
                    }
                    if (Utils.Consts.DEBUG)
                        System.out.println("Recebido \"" + conectar + "\" de " +
                                packet.getAddress().getHostAddress() + ":" + packet.getPort());
                }

                //verificação do pedido
                {
                    if (!conectar.isRegistado()) {//registar utilizador
                        //verificar se existe o username
                        if (server.getServerData().userExist(conectar.getUsername())) {
                            //resposta = new Comunicacoes.PedidoDeLigar(Utils.Consts.ERROR_USER_ALREADY_EXISTS, server.udpMultiCastManager.getServidoresForClient());
                            if (Utils.Consts.DEBUG)
                                System.out.println("[registo]O utilizador ja existe...");
                        } else //addicionar user
                        {
                            registar = true;
                        }
                    } else//verifica login
                        if (!server.getServerData().verifyUser(conectar.getUsername(), conectar.getPassword())) {
                            resposta = new Respostas.PedidoDeLigar(Utils.Consts.ERROR_USER_INFO_NOT_MATCH, server.udpMultiCastManager.getServidoresForClient());
                        }
                }

                //verificação da lotação
                {

                    //caso nao tenha hado erros na verificacao do login/registo
                    if (resposta == null)
                        if (server.udpMultiCastManager.verifyServerAvailability(server.getTcpConnections_size() / 2)) {
                            if (Utils.Consts.DEBUG)
                                System.out.println("Foi aceito o cliente {" + packet.getAddress() + "," + packet.getPort() + "} para se ligar ao tcp");
                            if (registar) {
                                if (Utils.Consts.DEBUG)
                                    System.out.println("[registo] um novo user foi adicionado");
                                try {

                                    server.getServerData().addUser(conectar.getUsername(), Password.getSaltedHash(conectar.getPassword()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            resposta = new Respostas.PedidoDeLigar(server.getTcpPort(), server.udpMultiCastManager.getServidoresForClient());
                        } else {
                            if (Utils.Consts.DEBUG)
                                System.out.println("Foi aceito o cliente {" + packet.getAddress() + "," + packet.getPort() + "} para se ligar ao tcp");
                            resposta = new Respostas.PedidoDeLigar(Utils.Consts.ERROR_SERVER_FULL, server.udpMultiCastManager.getServidoresForClient());
                        }
                }

                //enviar a resposta
                byte[] respostabytes = objectToBytes(resposta);
                if (respostabytes == null)
                    throw new Error("respostas estao null");
                packet.setData(respostabytes, 0, respostabytes.length);
                //O ip e porto de destino ja' se encontram definidos em packet
                socket.send(packet);
            }

        } catch (
                NumberFormatException e) {
            System.out.println("O porto de escuta deve ser um inteiro positivo.");
        } catch (
                SocketException e) {
            System.out.println("Ocorreu um erro ao nivel do socket UDP:\n\t" + e);
        } catch (
                IOException e) {
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t" + e);
        } catch (
                Exception throwables) {
            throwables.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

}
