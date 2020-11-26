package pt.isec.LEI.PD.TP20_21.Server.Model;


import static pt.isec.LEI.PD.TP20_21.shared.Utils.Consts.VERIFICA_SERVERS;

public class VerificaServers extends Thread {
    Servidores ips;
    public VerificaServers(Servidores ips) {
        this.ips = ips;
    }

    @Override
    public void run() {
        while (true) {
            ips.removeTimedOut();
            try {
                sleep(VERIFICA_SERVERS * 1000);
            } catch (InterruptedException ignored) {
            }

        }
    }
}
