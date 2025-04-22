package es.prorix.gusanoguason.controllers;

import es.prorix.gusanoguason.database.ConexionBD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;

public class RegistroController {

    @FXML private TextField nombreField;
    @FXML private TextField emailField;
    @FXML private PasswordField contrasenaField;
    @FXML private PasswordField repetirContrasenaField;
    @FXML private Label mensajeLabel;

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

        // Validación de formato de email
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            mensajeLabel.setText("📧 El correo electrónico no tiene un formato válido.");
            return;
        }

        // Comprobación de contraseñas iguales
        if (!contrasena.equals(repetirContrasena)) {
            mensajeLabel.setText("🔐 Las contraseñas no coinciden.");
            return;
        }

        Connection conn = ConexionBD.getConexion();

        try  {
            // Verificar si ya existe el usuario
            String consulta = "SELECT * FROM usuarios WHERE email = ?";
            PreparedStatement checkStmt = conn.prepareStatement(consulta);
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                mensajeLabel.setText("❌ Ya existe un usuario con ese correo.");
            } else {
                // Insertar nuevo usuario
                String insertar = "INSERT INTO usuarios(nombre, email, contrasena, record) VALUES (?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertar);
                insertStmt.setString(1, nombre);
                insertStmt.setString(2, email);
                insertStmt.setString(3, contrasena);
                insertStmt.setInt(4, 0);

                int filas = insertStmt.executeUpdate();
                if (filas > 0) {
                    mensajeLabel.setText("✅ Usuario registrado correctamente.");
                    limpiarCampos();
                } else {
                    mensajeLabel.setText("❌ Error al registrar el usuario.");
                }
            }
        } catch (SQLException e) {
            mensajeLabel.setText("❌ Error en la base de datos: " + e.getMessage());
        }
    }

    private void limpiarCampos() {
        nombreField.clear();
        emailField.clear();
        contrasenaField.clear();
        repetirContrasenaField.clear();
    }
}
