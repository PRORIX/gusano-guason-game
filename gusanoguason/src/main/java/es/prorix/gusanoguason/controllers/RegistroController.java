package es.prorix.gusanoguason.controllers;

import es.prorix.gusanoguason.database.ConexionBD;
import es.prorix.gusanoguason.models.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;

public class RegistroController {

    @FXML private TextField nombreField;
    @FXML private TextField emailField;
    @FXML private PasswordField contrasenaField;
    @FXML private Label mensajeLabel;

    @FXML
    private void registrarUsuario(ActionEvent event) {
        String nombre = nombreField.getText().trim();
        String email = emailField.getText().trim();
        String contrasena = contrasenaField.getText();

        if (nombre.isEmpty() || email.isEmpty() || contrasena.isEmpty()) {
            mensajeLabel.setText("⚠️ Por favor, completa todos los campos.");
            return;
        }

        try (Connection conn = ConexionBD.getConexion()) {
            // Verificamos si el usuario ya existe
            String consulta = "SELECT * FROM usuarios WHERE email = ?";
            PreparedStatement checkStmt = conn.prepareStatement(consulta);
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                mensajeLabel.setText("❌ Ya existe un usuario con ese correo.");
            } else {
                // Insertamos el nuevo usuario
                String insertar = "INSERT INTO usuarios(nombre, email, contrasena, record) VALUES (?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertar);
                insertStmt.setString(1, nombre);
                insertStmt.setString(2, email);
                insertStmt.setString(3, contrasena); // podrías aplicar hash más adelante
                insertStmt.setInt(4, 0); // récord inicial

                int filas = insertStmt.executeUpdate();
                if (filas > 0) {
                    mensajeLabel.setText("✅ Usuario registrado correctamente.");
                    nombreField.clear();
                    emailField.clear();
                    contrasenaField.clear();
                } else {
                    mensajeLabel.setText("❌ Error al registrar el usuario.");
                }
            }
        } catch (SQLException e) {
            mensajeLabel.setText("❌ Error en la base de datos: " + e.getMessage());
        }
    }
}
