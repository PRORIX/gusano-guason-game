package es.prorix.gusanoguason.controllers;

import es.prorix.gusanoguason.database.ConexionBD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

/**
 * Clase controladora de la pantalla de registro de usuarios
 * @author prorix
 * @version 1.0.6
 */
public class RegistroController {

    @FXML private TextField nombreField;
    @FXML private TextField emailField;
    @FXML private PasswordField contrasenaField;
    @FXML private PasswordField repetirContrasenaField;
    @FXML private Label mensajeLabel;
    @FXML private Button regresarButton;

    /**
     * Metodo del boton para regresar
     * @param event evento
     */
    @FXML
    public void regresarButtonClick(ActionEvent event){
                try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo del boton de confirmar el registro de usuario
     * @param event event
     */
    @FXML
    private void registrarUsuario(ActionEvent event) {
        String nombre = nombreField.getText().trim();
        String email = emailField.getText().trim();
        String contrasena = contrasenaField.getText();
        String repetirContrasena = repetirContrasenaField.getText();

        if (nombre.isEmpty() || email.isEmpty() || contrasena.isEmpty() || repetirContrasena.isEmpty()) {
            mensajeLabel.setText("⚠️ Por favor, completa todos los campos.");
            return;
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            mensajeLabel.setText("📧 El correo electrónico no tiene un formato válido.");
            return;
        }

        if (!contrasena.equals(repetirContrasena)) {
            mensajeLabel.setText("🔐 Las contraseñas no coinciden.");
            return;
        }

        Connection conn = ConexionBD.getConexion();

        try  {
            String consulta = "SELECT * FROM usuarios WHERE email = ?";
            PreparedStatement checkStmt = conn.prepareStatement(consulta);
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                mensajeLabel.setText("❌ Ya existe un usuario con ese correo.");
            } else {
                String insertar = "INSERT INTO usuarios(nombre, email, contrasena, record) VALUES (?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertar);
                insertStmt.setString(1, nombre);
                insertStmt.setString(2, email);
                insertStmt.setString(3, contrasena);
                insertStmt.setInt(4, 0);

                int filas = insertStmt.executeUpdate();
                if (filas > 0) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
                            Parent root = loader.load();
                            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            stage.setScene(new Scene(root));
                            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
                } else {
                    mensajeLabel.setText("❌ Error al registrar el usuario.");
                }
            }
        } catch (SQLException e) {
            mensajeLabel.setText("❌ Error en la base de datos: " + e.getMessage());
        }finally{
            ConexionBD.cerrarConexion();
        }
    }


}
