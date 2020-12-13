package pt.isec.LEI.PD.TP20_21.Client.Model.Connectivity;

import pt.isec.LEI.PD.TP20_21.Client.ClientObservavel;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.ListasParaOClient;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Pedidos.Conectar;
import pt.isec.LEI.PD.TP20_21.shared.Comunicacoes.Respostas.PedidoDeLigar;
import pt.isec.LEI.PD.TP20_21.shared.Data.Mensagem;
import pt.isec.LEI.PD.TP20_21.shared.IpPort;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.io.*;
import java.net.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static pt.isec.LEI.PD.TP20_21.shared.Utils.bytesToObject;
import static pt.isec.LEI.PD.TP20_21.shared.Utils.objectToBytes;

public class ClientServerConnection extends Thread {
    private final List<IpPort> servers = Collections.synchronizedList(new LinkedList<>());
    private int tries = 0;
    private int retries = 0;
    private PedidoDeLigar resposta = null;
    private Conectar pedido = null;
    private InetAddress serverAdd = null;
    private Socket socket = null;
    private InputStream isTCP = null;
    private OutputStream osTCP = null;
    private PipedOutputStream outputPipe;
    private PipedInputStream inputPipe;
    private ClientObservavel clientObservavel;


    public ClientServerConnection(ClientObservavel clientObservavel) {
        this.clientObservavel = clientObservavel;
    }

    public PipedInputStream getInputPipe() {
        return inputPipe;
    }

    public synchronized OutputStream getOtputStreamTCP() {
        return osTCP;
    }

    /**
     * timeout para conectar com o server(em segundos)
     */
    private final int TIMEOUT = 1;

    /**
     * @return code for the tcpConnection
     * if it returns 0 means there is a problem connnecting to tcp
     */
    public int connectToServer(Conectar pedido) {
        this.pedido = pedido;
        while (true) {//condição para que o servidor esteja sempre a conectar
            try {
                while ((serverAdd = connectUdp(pedido)) == null) ;
            } catch (Error e) {
                //Caso ocorra erro a ligar a um server ele cancela a coneçao.
                interrupt();
                e.printStackTrace();
                return resposta.TcpPort;
            }
            if (Utils.Consts.DEBUG)
                System.out.println("Recebido resposta de : " + serverAdd + "; port:" + resposta.TcpPort + "; servers: " + resposta.servers);

            if (0 < resposta.TcpPort) {
                try {
                    connectTcp(serverAdd);
                    start();
                    return resposta.TcpPort;
                } catch (Exception e) {
                    break;
                }
            }
        }
        return 0;
    }

    public void updateServer(Object enviar) {

        try {
            osTCP.write(Objects.requireNonNull(objectToBytes(enviar)));
        } catch (IOException e) {
            if (Utils.Consts.DEBUG) {
                System.out.println("Disconectado do server, a tentar reconnectar a um servidor...");
                this.connectToServer(pedido);
                e.printStackTrace();
            }

        }
    }

    /**
     * Thread que recebe as mensagens do server
     */
    @Override
    public void run() {
        super.run();
        byte[] receivedBytes;
        Object receivedObject;
        try {
            while (true) {
                receivedBytes = isTCP.readAllBytes();
                receivedObject = bytesToObject(receivedBytes);
                if (receivedObject == null) {
                    if (Utils.Consts.DEBUG)
                        System.out.println("Problemas com o objecto recebido.");
                    continue;
                }
                if (Utils.Consts.DEBUG)
                    System.out.println("Recebido um objeto");

                if (receivedObject == Mensagem.class){
                    outputPipe.write(receivedBytes);
                    outputPipe.flush();
                }else if(receivedObject.getClass() == ListasParaOClient.class) {
                    clientObservavel.getClientModel().processListaParaOClient((ListasParaOClient) receivedObject);

                }
                else{
                    System.err.println("Nao sei o que fazer com isto.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Seleciona o proximo ip/port para o servidor a ligar
     */
    private IpPort getIpPort() {
        if (servers.size() == 0) {
            if (retries <= 3) {
                return new IpPort(Utils.Consts.SERVER_ADDRESS, Utils.Consts.UDP_CLIENT_REQUEST_PORT);
            } else {
                throw new Error("Não existem servidores online contactaveis.");
            }
        } else {
            if (retries < 2) {
                tries++;
                retries = 0;
            }
            if (tries >= servers.size())
                throw new Error("Não existem servidores online contactaveis.  if (tries >= servers.size())");
            return new IpPort(servers.get(tries).ip, servers.get(tries).port);
        }
    }

    /**
     * tenta fazer a coneção udp com um server
     *
     * @return o ip do servidor em caso de se fazer uma resposta com sucesso, caso contrario returna null
     */
    private InetAddress connectUdp(Conectar pedido) {
        try {
            IpPort ipPort = getIpPort();
            var pedidoBytes = objectToBytes(pedido);
            DatagramSocket socket = new DatagramSocket();
            assert pedidoBytes != null;
            DatagramPacket packet = new DatagramPacket(
                    pedidoBytes, pedidoBytes.length,
                    InetAddress.getByName(Utils.Consts.SERVER_ADDRESS),
                    Utils.Consts.UDP_CLIENT_REQUEST_PORT
            );
            socket.setSoTimeout(TIMEOUT * 1000);
            //send packet
            socket.send(packet);

            //receive packet
            byte[] buffer = new byte[Utils.Consts.MAX_SIZE_PER_PACKET];
            packet.setData(buffer, 0, buffer.length);
            socket.receive(packet);
            resposta = (PedidoDeLigar) Utils.bytesToObject(packet.getData());
            retries = 0;
            tries = 0;
            return packet.getAddress();
        } catch (SocketTimeoutException | SocketException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        ++retries;
        return null;
    }

    /**
     * Connecta o client ao tcp do servidor
     *
     * @param ip do servidor a se ligar
     * @throws Exception caso problemas aconteçam
     */
    private void connectTcp(InetAddress ip) throws Exception {
        socket = new Socket(ip, resposta.TcpPort);
        isTCP = socket.getInputStream();
        osTCP = socket.getOutputStream();
    }
}
