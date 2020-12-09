package pt.isec.LEI.PD.TP20_21.Client;

import pt.isec.LEI.PD.TP20_21.Client.gui.InteracaoEsperada;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class MaqEstadosObservavel {

    private PropertyChangeSupport propertyChangeSupport;
    private MaqEstados MaqEstados;

    public MaqEstadosObservavel(MaqEstados jogoMaqEstados) {
        this.MaqEstados = jogoMaqEstados;
        propertyChangeSupport = new PropertyChangeSupport(jogoMaqEstados);
    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
    //public InteracaoEsperada getInteracaoEsperada() {return MaqEstados.getInteracaoEsperada();}
}
