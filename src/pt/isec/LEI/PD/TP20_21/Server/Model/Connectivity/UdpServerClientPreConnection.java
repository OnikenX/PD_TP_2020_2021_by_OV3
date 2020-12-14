package pt.isec.LEI.PD.TP20_21.Server.Model.Connectivity;

import pt.isec.LEI.PD.TP20_21.Server.Model.Server;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos.Conectar;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Respostas.PedidoDeLigar;
import pt.isec.LEI.PD.TP20_21.shared.Password;
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
        DatagramPacket packet = null; //para receber os pedidos e enviar as respostas
        String receivedMsg;
        try {
            try {
                socket = new DatagramSocket(Utils.Consts.UDP_CLIENT_REQUEST_PORT);
            } catch (Exception e) {
                System.out.println("Erro a ligar, a port " + Utils.Consts.UDP_CLIENT_REQUEST_PORT + "a tentar conectar outra porta do sistema aleatoria.");
                socket = new DatagramSocket();
            }
            Conectar conectar = null;
            byte[] conectarBytes =new byte[Utils.Consts.MAX_SIZE_PER_PACKET];
            PedidoDeLigar resposta = null;
            boolean registar = false;
            if (Utils.Consts.DEBUG)
                System.out.println("UdpServerClientPreConnection iniciado...");
            while (true) {
                //rececao de dados
                try{
                    packet = new DatagramPacket(conectarBytes, conectarBytes.length);
                    resposta = null;
                    registar = false;
                    //recebe o pedido de login/registo
                    socket.receive(packet);
                    if (Utils.Consts.DEBUG)
                        System.out.println("Foi recebido pedido do cliente {" + packet.getAddress() + "," + packet.getPort() + "} para ligação tcp");
                    conectar = (Conectar) Utils.bytesToObject(packet.getData());
                    //verifica se foi bem convertido para objecto
                    if (conectar == null) {
                        if (Utils.Consts.DEBUG)
                            System.out.println("Pedido de ligar mal recebido, a esperar por outro...");
                        continue;
                    }
                    if (Utils.Consts.DEBUG)
                        System.out.println("Recebido \"" + conectar + "\" de " +
                                packet.getAddress().getHostAddress() + ":" + packet.getPort());
                }catch(Exception e){
                    new Exception("Erro na rececao de dados.").printStackTrace();
                    e.printStackTrace();
                    continue;
                }

                //
                if(Utils.Consts.DEBUG)
                    System.out.println("verificação do pedido");
                try {
                    if (!conectar.isRegistado()) {//
                         if(Utils.Consts.DEBUG)
                        System.out.println("registar utilizador");
                        if(Utils.Consts.DEBUG)
                            System.out.println("verificar se existe o username");
                        if (server.getServerDB().userExist(conectar.getUsername())) {
                            //resposta = new Comunicacoes.PedidoDeLigar(Utils.Consts.ERROR_USER_ALREADY_EXISTS, server.udpMultiCastManager.getServidoresForClient());
                            if (Utils.Consts.DEBUG)
                                System.out.println("[registo]O utilizador ja existe...");
                        } else //addicionar user
                        {
                            if(Utils.Consts.DEBUG)
                                System.out.println("adicionar user");
                            registar = true;
                        }
                    } else//verifica login
                        if (!server.getServerDB().verifyUser(conectar.getUsername(), conectar.getPassword())) {
                            if(Utils.Consts.DEBUG)
                                System.out.println("a verificar login");
                            resposta = new PedidoDeLigar(Utils.Consts.ERROR_USER_INFO_NOT_MATCH, server.udpMultiCastManager.getServidoresForClient());
                            if(Utils.Consts.DEBUG)
                                System.out.println("verificado, resposta :"+ resposta.toString());
                        }
                } catch (Exception e) {
                    new Exception("Erros na verificaçao").printStackTrace();
                    e.printStackTrace();
                    continue;
                }

                //verificação da lotação
                try {
                        if(Utils.Consts.DEBUG)
                            System.out.println("verificação da lotação");
                    //caso nao tenha hado erros na verificacao do login/registo
                    if (resposta == null)
                        if (server.udpMultiCastManager.verifyServerAvailability(server.getTcpConnections_size() / 2)) {
                            if (Utils.Consts.DEBUG)
                                System.out.println("Foi aceito o cliente {" + packet.getAddress() + "," + packet.getPort() + "} para se ligar ao tcp");
                            if (registar) {
                                try {
                                    server.getServerDB().addUser(conectar.getUsername(), conectar.getUsername(), Password.getSaltedHash(conectar.getPassword()));
                                    if (Utils.Consts.DEBUG)
                                        System.out.println("[registo] um novo user foi adicionado");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            resposta = new PedidoDeLigar(server.getTcpPort(), server.udpMultiCastManager.getServidoresForClient());
                        } else {
                            if (Utils.Consts.DEBUG)
                                System.out.println("Foi aceito o cliente {" + packet.getAddress() + "," + packet.getPort() + "} para se ligar ao tcp");
                            resposta = new PedidoDeLigar(Utils.Consts.ERROR_SERVER_FULL, server.udpMultiCastManager.getServidoresForClient());
                        }
                } catch (Exception e) {
                    new Exception("Erro na verificação da lotação").printStackTrace();
                    e.printStackTrace();
                    continue;
                }

                //enviar a resposta
                byte[] respostabytes = objectToBytes(resposta);
                if (respostabytes == null)
                    throw new Error("respostas estao null");
                packet.setData(respostabytes, 0, respostabytes.length);
                //O ip e porto de destino ja' se encontram definidos em packet
                socket.send(packet);
                if(Utils.Consts.DEBUG)
                    System.out.println("mensagem enviada");
            }


        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

}
