package com.cilsa.ui;

import javafx.control.StatusBar;
import javafx.control.StatusBarPane;

/**
 * Barra de estado personalizada para la aplicación.
 * Proporciona un método conveniente para actualizar el mensaje de información.
 */
public class CustomStatusBar extends StatusBar {

    private final StringProperty infoMessage = new SimpleStringProperty("Listo");

    public CustomStatusBar() {
        setStatusVisibility(true);
        getPanes().add(createInfoPane());
    }

    private StatusBarPane createInfoPane() {
        StatusBarPane pane = new StatusBarPane();
        pane.textProperty().bind(infoMessage);
        return pane;
    }

    public void setInfo(String mensaje) {
        infoMessage.setValue(mensaje);
    }

    public String getInfo() {
        return infoMessage.getValue();
    }
}