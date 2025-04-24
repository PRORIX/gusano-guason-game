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
import java.util.*;

import es.prorix.gusanoguason.database.ConexionBD;
import es.prorix.gusanoguason.models.Usuario;
import es.prorix.gusanoguason.util.UsuarioService;


public class JugarController {

    @FXML
    private Canvas canvas;

    @FXML
    private VBox gameOverBox;

    @FXML
    private Label gameOverLabel;

    @FXML
    private Button volverJugar;

    @FXML
    private Button salir;

    private final int tileSize = 25;
    private final int width = 20;
    private final int height = 20;

    private enum Direction {UP, DOWN, LEFT, RIGHT}

    private Direction direction = Direction.RIGHT;
    private Direction nextDirection = Direction.RIGHT;

    private List<int[]> snake;
    private int[] apple;
    private boolean running = false;
    private boolean revived = false;
    private int score = 0;
    private int bestScore = 0; // Enlaza con base de datos si quieres
    private final Random random = new Random();

    private AnimationTimer timer;

    @FXML
    public void initialize() {
        canvas.setFocusTraversable(true);
        canvas.addEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPress);
        startGame();
    }

    private void startGame() {
        gameOverBox.setVisible(false);
        snake = new LinkedList<>();
        snake.add(new int[]{10, 10});
        direction = Direction.RIGHT;
        nextDirection = Direction.RIGHT;
        score = 0;
        revived = false;
        spawnApple();
        running = true;

        timer = new AnimationTimer() {
            long lastTick = 0;

            @Override
            public void handle(long now) {
                if (lastTick == 0) {
                    lastTick = now;
                    tick();
                    return;
                }
                if (now - lastTick > 200_000_000) {
                    lastTick = now;
                    tick();
                }
            }
        };
        timer.start();
    }

    private void tick() {
        if (!running) return;

        direction = nextDirection;
        int[] head = snake.get(0);
        int newX = head[0];
        int newY = head[1];

        switch (direction) {
            case UP -> newY--;
            case DOWN -> newY++;
            case LEFT -> newX--;
            case RIGHT -> newX++;
        }

        int[] newHead = new int[]{newX, newY};

        // Colisión
        boolean collision = newX < 0 || newY < 0 || newX >= width || newY >= height
                || snake.stream().anyMatch(p -> Arrays.equals(p, newHead));

        if (collision) {
            if (!revived && random.nextDouble() < 0.3) {
                revived = true;
                return; // continúa como si nada hubiera pasado
            } else {
                running = false;
                timer.stop();
                if (score > bestScore) bestScore = score;
                gameOverBox.setVisible(true);
                gameOverLabel.setText("¡Has perdido!\nPuntuación: " + score);
                revisarRecord();
                return;
            }
        }



        snake.add(0, newHead);

        if (Arrays.equals(newHead, apple)) {
            score++;
            spawnApple();
        } else {
            snake.remove(snake.size() - 1);
        }

        draw();
    }

    public boolean revisarRecord(){
        Usuario usuarioActual = UsuarioService.getUsuarioActual();
        String emailActual = usuarioActual.getEmail();
        try {
            Connection conn2 = ConexionBD.getConexion();
            usuarioActual.setRecord(score);
            String queryS = "SELECT record FROM usuarios WHERE email=?";
            PreparedStatement pStatement = conn2.prepareStatement(queryS);
            record.setInt(1, emailActual);
            return record.executeUpdate()>0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally{
            ConexionBD.cerrarConexion();
        }

        if (score > usuarioActual.getRecord()) {
            try {
                Connection conn = ConexionBD.getConexion();
                usuarioActual.setRecord(score);
                String updateRecord = "UPDATE usuarios SET record=? WHERE email=?";
                PreparedStatement record = conn.prepareStatement(updateRecord);
                record.setInt(1, score);
                record.setString(2, usuarioActual.getEmail());
                return record.executeUpdate()>0;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }finally{
                ConexionBD.cerrarConexion();
            }

        }
        return false;
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.web("#1e1e1e"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Dibujar manzana
        gc.setFill(Color.RED);
        gc.fillOval(apple[0] * tileSize, apple[1] * tileSize, tileSize, tileSize);

        // Dibujar gusano
        for (int i = 0; i < snake.size(); i++) {
            int[] p = snake.get(i);
            gc.setFill(i == 0 ? Color.LIME : Color.GREEN);
            gc.fillRect(p[0] * tileSize, p[1] * tileSize, tileSize, tileSize);
        }

        // Puntos
        gc.setFill(Color.WHITE);
        gc.fillText("Puntos: " + score, 10, 20);
    }

    private void spawnApple() {
        do {
            apple = new int[]{random.nextInt(width), random.nextInt(height)};
        } while (snake.stream().anyMatch(p -> Arrays.equals(p, apple)));
    }

    private void handleKeyPress(KeyEvent e) {
        KeyCode code = e.getCode();
        switch (code) {
            case UP -> {
                if (direction != Direction.DOWN) nextDirection = Direction.UP;
            }
            case DOWN -> {
                if (direction != Direction.UP) nextDirection = Direction.DOWN;
            }
            case LEFT -> {
                if (direction != Direction.RIGHT) nextDirection = Direction.LEFT;
            }
            case RIGHT -> {
                if (direction != Direction.LEFT) nextDirection = Direction.RIGHT;
            }
        }
    }

    @FXML
    public void onVolverAJugar() {
        startGame();
    }

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
