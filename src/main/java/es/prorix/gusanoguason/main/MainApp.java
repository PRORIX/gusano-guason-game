package es.prorix.gusanoguason.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase de la ejecucion inicial de la aplicacion
 * @author prorix
 * @version 1.0.0
 */
public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/views/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Registro - Gusanoguasón");
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Metodo main
     * Ejecuta la aplicacion
     */
    public static void main(String[] args) {
        launch();
    }
}
