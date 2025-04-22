package es.prorix.gusanoguason.controllers;

import es.prorix.gusanoguason.database.ConexionBD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField contrasenaField;
    @FXML private Label mensajeLabel;

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
                mensajeLabel.setText("✅ Inicio de sesión correcto.");
                // Aquí más adelante puedes cargar el menú principal o el perfil
            } else {
                mensajeLabel.setText("❌ Usuario o contraseña incorrectos.");
            }
        } catch (SQLException e) {
            mensajeLabel.setText("❌ Error al conectar: " + e.getMessage());
        }
    }
}
