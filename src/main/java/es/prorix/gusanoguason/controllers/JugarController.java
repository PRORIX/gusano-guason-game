package es.prorix.gusanoguason.controllers;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import es.prorix.gusanoguason.database.ConexionBD;
import es.prorix.gusanoguason.models.Usuario;
import es.prorix.gusanoguason.util.UsuarioService;

/**
 * Clase controladora del funcionamiento del juego
 * 
 * @author prorix
 * @version 1.3.5
 */
public class JugarController {

    @FXML
    private Canvas lienzo;

    @FXML
    private VBox cajaGameOver;

    @FXML
    private Label etiquetaGameOver;

    @FXML
    private Button botonVolverJugar;

    @FXML
    private Button botonSalir;

    private final int tamanoCelda = 25;
    private final int ancho = 20;
    private final int alto = 20;

    private enum Direccion {
        ARRIBA, ABAJO, IZQUIERDA, DERECHA
    }

    private Direccion direccion = Direccion.DERECHA;
    private Direccion siguienteDireccion = Direccion.DERECHA;

    private List<int[]> gusano;
    private int[] manzana;
    private boolean jugando = false;
    private boolean revivido = false;
    private int puntuacion = 0;
    private int mejorPuntuacion = 0;
    private final Random aleatorio = new Random();

    private AnimationTimer temporizador;
    private long velocidad = 200_000_000;

    /**
     * Metodo inicializar
     */
    @FXML
    public void initialize() {
        lienzo.setFocusTraversable(true);
        lienzo.addEventHandler(KeyEvent.KEY_PRESSED, this::manejarTecla);
        iniciarJuego();
    }

    /**
     * Metodo que da comienzo al juego
     */
    private void iniciarJuego() {
        cajaGameOver.setVisible(false);
        gusano = new LinkedList<>();
        gusano.add(new int[] { 10, 10 });
        direccion = Direccion.DERECHA;
        siguienteDireccion = Direccion.DERECHA;
        puntuacion = 0;
        velocidad = 200_000_000;
        revivido = false;
        generarManzana();
        jugando = true;

        temporizador = new AnimationTimer() {
            long ultimoTick = 0;

            @Override
            public void handle(long ahora) {
                if (ultimoTick == 0) {
                    ultimoTick = ahora;
                    actualizar();
                    return;
                }
                if (ahora - ultimoTick > velocidad) {
                    ultimoTick = ahora;
                    actualizar();
                }
            }
        };
        temporizador.start();
    }

    /**
     * Metodo que actualiza la pantalla del juego constantemente
     */
    private void actualizar() {
        if (!jugando)
            return;

        direccion = siguienteDireccion;
        int[] cabeza = gusano.get(0);
        int nuevoX = cabeza[0];
        int nuevoY = cabeza[1];

        switch (direccion) {
            case ARRIBA -> nuevoY--;
            case ABAJO -> nuevoY++;
            case IZQUIERDA -> nuevoX--;
            case DERECHA -> nuevoX++;
        }

        int[] nuevaCabeza = new int[] { nuevoX, nuevoY };

        boolean colision = nuevoX < 0 || nuevoY < 0 || nuevoX >= ancho || nuevoY >= alto
                || gusano.stream().anyMatch(p -> Arrays.equals(p, nuevaCabeza));

        if (colision) {
            if (!revivido && aleatorio.nextDouble() < 0.3) {
                revivido = true;
                return;
            } else {
                jugando = false;
                temporizador.stop();
                if (puntuacion > mejorPuntuacion)
                    mejorPuntuacion = puntuacion;
                cajaGameOver.setVisible(true);
                etiquetaGameOver.setText("¡Has perdido!\nPuntuación: " + puntuacion);
                revisarRecord();
                return;
            }
        }

        gusano.add(0, nuevaCabeza);

        if (Arrays.equals(nuevaCabeza, manzana)) {
            puntuacion++;
            generarManzana();
            ajustarVelocidad();
        } else {
            gusano.remove(gusano.size() - 1);
        }

        dibujar();
    }

    /**
     * Metodo para aumentar la velocidad segun el jugador gana puntos
     */
    private void ajustarVelocidad() {
        if (puntuacion % 10 == 0 && puntuacion <= 50) {
            velocidad = Math.max(velocidad - 20_000_000, 100_000_000);
        }
    }

    /**
     * Metodo que revisa si la partida jugada es un nuevo record
     */
    public boolean revisarRecord() {
        Usuario usuarioActual = UsuarioService.getUsuarioActual();
        String emailActual = usuarioActual.getEmail();
        try {
            Connection conn = ConexionBD.getConexion();
            Connection conn2 = ConexionBD.getConexion();
            usuarioActual.setRecord(puntuacion);
            String queryS = "SELECT record FROM usuarios WHERE email=?";
            PreparedStatement pStatement = conn2.prepareStatement(queryS);
            pStatement.setString(1, emailActual);
            ResultSet resultRecord = pStatement.executeQuery();
            int recordActual = resultRecord.getInt("record");

            if (puntuacion > recordActual) {
                usuarioActual.setRecord(puntuacion);
                String updateRecord = "UPDATE usuarios SET record=? WHERE email=?";
                PreparedStatement record = conn.prepareStatement(updateRecord);
                record.setInt(1, puntuacion);
                record.setString(2, usuarioActual.getEmail());
                return record.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            ConexionBD.cerrarConexion();
        }
        return false;
    }

    /**
     * Metodo que dibuja los elementos de la pantalla
     */
    private void dibujar() {
        GraphicsContext gc = lienzo.getGraphicsContext2D();

        gc.setFill(Color.web("#1e1e1e"));
        gc.fillRect(0, 0, lienzo.getWidth(), lienzo.getHeight());

        gc.setFill(Color.RED);
        gc.fillOval(manzana[0] * tamanoCelda, manzana[1] * tamanoCelda, tamanoCelda, tamanoCelda);

        for (int i = 0; i < gusano.size(); i++) {
            int[] p = gusano.get(i);
            gc.setFill(i == 0 ? Color.LIMEGREEN : Color.DARKGREEN);
            gc.fillRoundRect(p[0] * tamanoCelda, p[1] * tamanoCelda, tamanoCelda, tamanoCelda, 5, 5);
        }

        gc.setFill(Color.web("#ffffff"));
        gc.fillText("Puntos: " + puntuacion, 10, 20);
    }

    /**
     * Metodo que genera las manzanas en posiciones aleatorias en el mapa
     */
    private void generarManzana() {
        do {
            manzana = new int[] { aleatorio.nextInt(ancho), aleatorio.nextInt(alto) };
        } while (gusano.stream().anyMatch(p -> Arrays.equals(p, manzana)));
    }

    private void manejarTecla(KeyEvent e) {
        KeyCode codigo = e.getCode();
        switch (codigo) {
            case UP -> {
                if (direccion != Direccion.ABAJO)
                    siguienteDireccion = Direccion.ARRIBA;
            }
            case DOWN -> {
                if (direccion != Direccion.ARRIBA)
                    siguienteDireccion = Direccion.ABAJO;
            }
            case LEFT -> {
                if (direccion != Direccion.DERECHA)
                    siguienteDireccion = Direccion.IZQUIERDA;
            }
            case RIGHT -> {
                if (direccion != Direccion.IZQUIERDA)
                    siguienteDireccion = Direccion.DERECHA;
            }
        }
    }

    /**
     * Metodo del boton de volver a jugar cuando se pierde
     */
    @FXML
    public void onVolverAJugar() {
        iniciarJuego();
    }

    /**
     * Metodo del boton para volver al perfil cuando se pierde
     */
    @FXML
    public void onIrAlPerfil(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/perfil.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
