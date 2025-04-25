package es.prorix.gusanoguason.controllers;

import es.prorix.gusanoguason.database.ConexionBD;
import es.prorix.gusanoguason.main.MainApp;
import es.prorix.gusanoguason.models.Usuario;
import es.prorix.gusanoguason.util.UsuarioService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase controladora de la pantalla de login
 * 
 * @author prorix
 * @version 1.1.3
 */
public class LoginController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField contrasenaField;
    @FXML
    private Label mensajeLabel;
    @FXML
    private Hyperlink registrarLink;
    @FXML
    private Button olvidarPass;
    @FXML
    private Button salirButton;

    /**
     * Metodo del boton de salir del juego
     */
    @FXML
    public void salirButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/views/cerrarJuego.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Estadísticas del Jugador");
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo del boton para acceder a la pantalla de olvidar la contrasenia
     * 
     * @param event evento
     */
    @FXML
    private void olvidarPassClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/recuperarContrasenia.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo del boton para acceder a la pantalla de registro
     * 
     * @param event evento
     */
    @FXML
    private void registrarLinkClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/registro.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo del boton de iniciar sesion
     * 
     * @param event evento
     */
    @FXML
    private void iniciarSesion(ActionEvent event) {
        String email = emailField.getText().trim();
        String contrasena = contrasenaField.getText();

        if (email.isEmpty() || contrasena.isEmpty()) {
            mensajeLabel.setText("⚠️ Introduce el correo y la contraseña.");
            return;
        }

        Connection conn = ConexionBD.getConexion();

        try {
            String consulta = "SELECT * FROM usuarios WHERE email = ? AND contrasena = ?";
            PreparedStatement stmt = conn.prepareStatement(consulta);
            stmt.setString(1, email);
            stmt.setString(2, contrasena);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombre");
                int record = rs.getInt("record");

                Usuario usuario = new Usuario(nombre, email, "", record); // contrasena vacía
                UsuarioService.setUsuarioActual(usuario);

                // Cambiar de pantalla al perfil
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/perfil.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                mensajeLabel.setText("❌ Usuario o contraseña incorrectos.");
            }
        } catch (SQLException e) {
            mensajeLabel.setText("❌ Error al conectar: " + e.getMessage());
        } catch (Exception e) {
            mensajeLabel.setText("❌ Error al cargar perfil: " + e.getMessage());
        } finally {
            ConexionBD.cerrarConexion();
        }
    }
}
