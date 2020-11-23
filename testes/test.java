import pt.isec.LEI.PD.TP20_21.shared.IpServidor;
import pt.isec.LEI.PD.TP20_21.shared.Respostas;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

import java.util.LinkedList;
import java.util.Objects;

public class test {
    public static void main(String[] args) {
        LinkedList<IpServidor> ll = new LinkedList<>();
        Respostas.RUdpClientServerPreConnection resposta = new Respostas.RUdpClientServerPreConnection(6969, ll);
        System.out.println("size resposta + linkedlist empty:" + Objects.requireNonNull(Utils.objectToBytes(resposta)).length);
        ll.add(new IpServidor("255.255.255.255", 6969));
        System.out.println("Size ipservidor:"+ Objects.requireNonNull(Utils.objectToBytes(ll.get(0))).length);
        System.out.println("size resposta + linkedlist com 1 elemento:"+ Objects.requireNonNull(Utils.objectToBytes(resposta)).length);
        while(Objects.requireNonNull(Utils.objectToBytes(resposta)).length < 5000){
            ll.add(new IpServidor("255.255.255.255", 6969));
        }
        System.out.println("Maximo de ips => " + (ll.size()-1));
    }
}
