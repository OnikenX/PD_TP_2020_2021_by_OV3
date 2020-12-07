package pt.isec.LEI.PD.TP20_21.Client;

import java.beans.PropertyChangeSupport;

public class MaqEstadosObservavel {

    private PropertyChangeSupport propertyChangeSupport;
    private MaqEstados jogoMaqEstados;

    public MaqEstadosObservavel(MaqEstados jogoMaqEstados) {
        this.jogoMaqEstados = jogoMaqEstados;
        propertyChangeSupport = new PropertyChangeSupport(jogoMaqEstados);
    }
}
