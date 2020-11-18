package pt.isec.LEI.PD.TP20_21.Server.Connectivity;

import java.io.*;
import java.net.*;
class Msg implements Serializable {
    public static final long serialVersionUID = 10L;

    protected String nickname;
    protected String msg;

    public Msg(String nickname, String msg) {
        this.nickname = nickname;
        this.msg = msg;

    }

    public String getNickname() {
        return nickname;
    }

    public String getMsg() {
        return msg;
    }

}

public class MulticastChat_v2 extends Thread {
    public static final String LIST = "LIST";
    public static String EXIT = "EXIT";
    public static int MAX_SIZE = 1000;

    protected String username;
    protected MulticastSocket s;
    protected boolean running;

    public MulticastChat_v2(String username, MulticastSocket s) {
        this.username = username;
        this.s = s;
        running = true;
    }

    public void terminate() {
        running = false;
    }

    @Override
    public void run() {
        ObjectInputStream in;
        Object obj;
        DatagramPacket pkt;
        Msg msg;
        ByteArrayOutputStream buff;
        ObjectOutputStream out;

        if (s == null || !running)
            return;

        try {
            while (running) {
                pkt = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
                s.receive(pkt);

                try {
                    in = new ObjectInputStream(new ByteArrayInputStream(pkt.getData(), 0, pkt.getLength()));
                    obj = in.readObject();
                    in.close();

                    if (obj instanceof Msg) {
                        msg = (Msg) obj;
                        if (msg.getMsg().toUpperCase().contains(LIST)) {
                            buff = new ByteArrayOutputStream();
                            out = new ObjectOutputStream(buff);
                            out.writeObject(username);
                            out.flush();
                            out.close();
                            pkt.setData(buff.toByteArray());
                            pkt.setLength(buff.size());
                            s.send(pkt);

                            continue;
                        }
                        System.out.println();
                        System.out.println("(" + pkt.getAddress().getHostAddress() + ":" + pkt.getLength());
                        System.out.println(msg.getNickname() + ":" + msg.getMsg() + " (" + msg.getClass());
                    } else if (obj instanceof String) {
                        System.out.println((String) obj + " (" + obj.getClass() + ") ");
                    }

                    System.out.println();
                    System.out.println("> ");

                } catch (ClassNotFoundException e) {
                    System.out.println();
                    System.out.println("Mensagem recebida de tipo inesperado!" + e);

                } catch (IOException e) {
                    System.out.println();
                    System.out.println("Impossiblidade de aceder ao conteudo da mensagem recebida! " + e);
                } catch (Exception e) {
                    System.out.println();
                    System.out.println("Excepcao: " + e);
                }
            }
        } catch (IOException e) {
            if (running) {
                System.out.println(e);
            }
            if (!s.isClosed()) {
                s.close();
            }
        }
    }


    public static void main(String[] args) throws UnknownHostException, IOException {
        InetAddress group;
        int port;
        MulticastSocket socket = null;
        DatagramPacket dgram;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String msg;
        ByteArrayOutputStream buff;
        ObjectOutputStream out;

        MulticastChat_v2 t = null;

        if (args.length != 4) {
            System.out.println("Sintaxe: java MulticastChat <nickname> <group multicast> <porto> <interface>");
            return;
        }

        try {
            group = InetAddress.getByName(args[1]);     //group multicast
            port = Integer.parseInt(args[2]);    //porto

            socket = new MulticastSocket(port);

            try {
                socket.setNetworkInterface(NetworkInterface.getByInetAddress(InetAddress.getByName(args[2]))); //porto
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

