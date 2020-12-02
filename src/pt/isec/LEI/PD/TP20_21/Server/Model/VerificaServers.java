package pt.isec.LEI.PD.TP20_21.Server.Model;


import static pt.isec.LEI.PD.TP20_21.shared.Utils.Consts.SERVER_VERIFY_SERVERS_TIMER;

/**
 * Verificação se os servidores estão activos.
 *
 * Esta é uma thread auto enicializada e do tipo daemon.
 */
public class VerificaServers extends Thread {
    private Server server;
    public VerificaServers(Server server) {
        this.server = server;
        this.setDaemon(true);
        this.start();
    }

    @Override
    public void run() {
        while (true) {
            server.getIps().removeTimedOut();
            try {
                sleep(SERVER_VERIFY_SERVERS_TIMER * 1000);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
