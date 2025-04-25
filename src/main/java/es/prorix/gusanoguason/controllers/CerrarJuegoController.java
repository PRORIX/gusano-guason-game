package main.java.es.prorix.gusanoguason.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;

/**
 * Metodo controlador de la pantalla de confirmacion de cerrar el juego
 * 
 * @author prorix
 * @version 1.0.1
 */
public class CerrarJuegoController {
    @FXML
    private Button cerrarButton;

    @FXML
    private Button cancelarButton;

    /**
     * Metodo del boton de confirmacion de cerrar el juego
     */
    @FXML
    public void cerrarButtonClick() {
        Platform.exit();
    }

    /**
     * Metodo del boton de cancelar
     */
    @FXML
    public void cancelarButtonClick() {
        Stage stage = (Stage) cancelarButton.getScene().getWindow();
        stage.close();
    }
}
