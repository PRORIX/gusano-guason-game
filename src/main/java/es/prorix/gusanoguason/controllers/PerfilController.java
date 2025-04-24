package es.prorix.gusanoguason.controllers;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import es.prorix.gusanoguason.database.ConexionBD;
import es.prorix.gusanoguason.models.Usuario;
import es.prorix.gusanoguason.util.UsuarioService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Button;



 

public class PerfilController {

    @FXML private Label labelNombre;
    @FXML private Label labelCorreo;
    @FXML private Label labelRecord;
    @FXML private Button jugarButton;
    @FXML private Button cerrarSesionButton;

    @FXML
    public void jugarButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/jugar.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void cerrarSesionButtonClick(ActionEvent event) {
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

    @FXML
    public void initialize() {
        Usuario usuario = UsuarioService.getUsuarioActual();
        if (usuario != null) {
            labelNombre.setText("👤 Nombre: " + usuario.getNombre());
            labelCorreo.setText("📧 Correo: " + usuario.getEmail());
            labelRecord.setText("🏆 Récord: " + getRecordBBDD());
        }



    }

    public int getRecordBBDD(){
        Connection conn = ConexionBD.getConexion();
        try {
            Usuario usuarioActual = UsuarioService.getUsuarioActual();
            String querry = "SELECT record FROM usuarios WHERE email=?";
            PreparedStatement pS = conn.prepareStatement(querry);
            pS.setString(1, usuarioActual.getEmail());
            ResultSet rs= pS.executeQuery();
            if (rs.next()) {
                return rs.getInt("record");
            }else{
            return 0;
        }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }finally{
            ConexionBD.cerrarConexion();
        }

    }

}
