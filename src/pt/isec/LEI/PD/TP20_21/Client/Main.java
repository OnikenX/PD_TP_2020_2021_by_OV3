package pt.isec.LEI.PD.TP20_21.Client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pt.isec.LEI.PD.TP20_21.Client.Connectivity.ClientServerConnection;
import pt.isec.LEI.PD.TP20_21.Client.gui.Gui;
import pt.isec.LEI.PD.TP20_21.shared.Utils;

public class Main extends Application {
    public static void main(String[] args) {
        if (Utils.Consts.DEBUG)
            System.out.println("A comecar o cliente....");
        ClientServerConnection connection = new ClientServerConnection();
        connection.start();
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        MaqEstados maqEstados = new MaqEstados();
        MaqEstadosObservavel maqEstadosObservavel = new MaqEstadosObservavel(maqEstados);

        Scene scene = new Scene(new Gui(maqEstadosObservavel), 1200, 720);
        stage.setTitle("Cliente");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
