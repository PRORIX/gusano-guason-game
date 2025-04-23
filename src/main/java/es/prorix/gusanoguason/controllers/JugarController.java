package es.prorix.gusanoguason.controllers;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.util.*;

public class JugarController {

    @FXML
    private Canvas canvas;

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
    private int bestScore = 0; // Puedes luego guardar esto en la base de datos del perfil

    private final Random random = new Random();

    @FXML
    public void initialize() {
        canvas.setFocusTraversable(true);
        canvas.addEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPress);
        startGame();
    }

    private void startGame() {
        snake = new LinkedList<>();
        snake.add(new int[]{10, 10});
        direction = Direction.RIGHT;
        nextDirection = Direction.RIGHT;
        score = 0;
        spawnApple();
        running = true;

        AnimationTimer timer = new AnimationTimer() {
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

        // Check collision with wall or itself
        if (newX < 0 || newY < 0 || newX >= width || newY >= height || snake.stream().anyMatch(p -> Arrays.equals(p, newHead))) {
            if (!revived && random.nextDouble() < 0.3) {
                revived = true;
                // simplemente ignora el tick donde muere y continúa en el siguiente
                return;
            } else {
                running = false;
                if (score > bestScore) bestScore = score;
                // Aquí podrías mostrar un menú para volver a jugar o volver al perfil
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

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.web("#1e1e1e"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw apple
        gc.setFill(Color.RED);
        gc.fillOval(apple[0] * tileSize, apple[1] * tileSize, tileSize, tileSize);

        // Draw snake
        for (int i = 0; i < snake.size(); i++) {
            int[] p = snake.get(i);
            gc.setFill(i == 0 ? Color.LIME : Color.GREEN);
            gc.fillRect(p[0] * tileSize, p[1] * tileSize, tileSize, tileSize);
        }

        // Draw score
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
}