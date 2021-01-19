package pt.isec.lei.pd.tp20_21.Client.gui;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import pt.isec.lei.pd.tp20_21.Client.ClientObservavel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Gui extends BorderPane {
    private ClientObservavel modeloObs;

    public Gui(ClientObservavel modelo) {
        this.modeloObs = modelo;
        this.modeloObs.addPropertyChangeListener(
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        atualizaVista();
                    }
                }
        );
        organizaComponentes();
    }

    private void organizaComponentes() {


        //Cria componentes
        StackPane areaUtil = new StackPane();
        areaUtil.setPadding(new Insets(10));
        //Criação das varias classes que dao origem às interfaces de utilizador de cada estado

    }
    private void atualizaVista(){
        //InteracaoEsperada interacaoEsperada = modeloObs.getInteracaoEsperada();
    }
}
