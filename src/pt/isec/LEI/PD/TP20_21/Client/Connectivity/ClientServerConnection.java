package pt.isec.LEI.PD.TP20_21.Client.Connectivity;

import pt.isec.LEI.PD.TP20_21.shared.IpPort;
import pt.isec.LEI.PD.TP20_21.shared.Mensagens;
import pt.isec.LEI.PD.TP20_21.shared.Respostas;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.io.IOException;
import java.net.*;
import java.util.LinkedList;

import static pt.isec.LEI.PD.TP20_21.shared.Utils.objectToBytes;

public class ClientServerConnection extends Thread {
    private final LinkedList<pt.isec.LEI.PD.TP20_21.shared.IpPort> servers = new LinkedList<>();
    private int tries;
    private int retries;
    private Respostas.RUdpClientServerPreConnection resposta = null;
    private Mensagens.PedidoDeLigar pedido;
    public ClientServerConnection(Mensagens.PedidoDeLigar pedido){
        this.pedido = pedido;

    }

    /**
     * timeout para conectar com o server(em segundos)
     */
    private final int TIMEOUT = 1;

    @Override
    public void run() {
        super.run();
        InetAddress server = null;
        while (true) {//condição para que o servidor esteja sempre a conectar
            try {
                while ((server = connectUdp(pedido)) == null) ;
            } catch (Error e) {
                //Caso ocorra erro a ligar a um server ele cancela a coneçao.
                break;
            }
            if (Utils.Consts.DEBUG)
                System.out.println("Recebido resposta de : " + server + "; port:" + resposta.TcpPort + "; servers: " + resposta.servers);
            if (resposta.TcpPort != -1) {
                try {
                    connectTcp(server);
                } catch (Exception e) {
                    break;
                }
            }
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
                if (Utils.Consts.DEBUG)
                    System.err.println("Não existem servidores online contactaveis.");
                throw new Error("Não existem servidores online contactaveis.");
            }
        } else {
            if (retries < 2) {
                tries++;
                retries = 0;
            }
            if (tries >= servers.size())
                throw new Error("Não existem servidores online contactaveis.");
            return new IpPort(servers.get(tries).ip, servers.get(tries).port);
        }
    }

    /**
     * tenta fazer a coneção udp com um server
     *
     * @return o ip do servidor em caso de se fazer uma resposta com sucesso, caso contrario returna null
     */
    private InetAddress connectUdp(Mensagens.PedidoDeLigar pedido) {
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
            resposta = (Respostas.RUdpClientServerPreConnection) Utils.bytesToObject(packet.getData());
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

    private void connectTcp(InetAddress ip) throws Exception {
        //tenta connectar
        throw new Exception();
    }

    public void resumeThis() {
        notify();
    }

    private static LinkedList<pt.isec.LEI.PD.TP20_21.shared.IpPort> connectToServer(String ip, int port, Mensagens.PedidoDeLigar pedido) {
        var thread = new ClientServerConnection(pedido);
        thread.start();
        onSpinWait();
        return thread.servers;
    }
    
}
