package es.prorix.gusanoguason.controllers;

import javafx.scene.paint.Color;

import java.util.*;

public class JugarController {

    @FXML
    private Canvas canvas;

    private final int TAM_CASILLA = 20;
    private final int FILAS = 25;
    private final int COLUMNAS = 25;

    private List<int[]> gusano = new ArrayList<>();
    private String direccion = "RIGHT";
    private boolean enJuego = true;
    private int[] manzana;

    private long ultimaActualizacion = 0;
    private final long INTERVALO = 200_000_000;

    public void initialize() {
        // Inicializa gusano
        gusano.clear();
        gusano.add(new int[]{5, 5});

        generarManzana();

        // Captura teclas
        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            switch (code) {
                case UP: if (!direccion.equals("DOWN")) direccion = "UP"; break;
                case DOWN: if (!direccion.equals("UP")) direccion = "DOWN"; break;
                case LEFT: if (!direccion.equals("RIGHT")) direccion = "LEFT"; break;
                case RIGHT: if (!direccion.equals("LEFT")) direccion = "RIGHT"; break;
            }
        });

        // Bucle de juego
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - ultimaActualizacion >= INTERVALO) {
                    if (enJuego) {
                        actualizar();
                        dibujar();
                    }
                    ultimaActualizacion = now;
                }
            }
        };
        timer.start();
    }

    private void actualizar() {
        int[] cabeza = gusano.get(0);
        int nuevaX = cabeza[0];
        int nuevaY = cabeza[1];

        switch (direccion) {
            case "UP": nuevaY--; break;
            case "DOWN": nuevaY++; break;
            case "LEFT": nuevaX--; break;
            case "RIGHT": nuevaX++; break;
        }

        // Comprobación de límites o autocolisión
        if (nuevaX < 0 || nuevaY < 0 || nuevaX >= COLUMNAS || nuevaY >= FILAS || colision(nuevaX, nuevaY)) {
            enJuego = false;
            return;
        }

        gusano.add(0, new int[]{nuevaX, nuevaY});

        if (nuevaX == manzana[0] && nuevaY == manzana[1]) {
            generarManzana();
        } else {
            gusano.remove(gusano.size() - 1);
        }
    }

    private void dibujar() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Dibujar manzana
        gc.setFill(Color.RED);
        gc.fillOval(manzana[0] * TAM_CASILLA, manzana[1] * TAM_CASILLA, TAM_CASILLA, TAM_CASILLA);

        // Dibujar gusano
        gc.setFill(Color.LIMEGREEN);
        for (int[] segmento : gusano) {
            gc.fillRect(segmento[0] * TAM_CASILLA, segmento[1] * TAM_CASILLA, TAM_CASILLA, TAM_CASILLA);
        }

        if (!enJuego) {
            gc.setFill(Color.WHITE);
            gc.fillText("¡Has perdido!", canvas.getWidth() / 2 - 30, canvas.getHeight() / 2);
        }
    }

    private boolean colision(int x, int y) {
        for (int[] segmento : gusano) {
            if (segmento[0] == x && segmento[1] == y) {
                return true;
            }
        }
        return false;
    }

    private void generarManzana() {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(COLUMNAS);
            y = rand.nextInt(FILAS);
        } while (colision(x, y));
        manzana = new int[]{x, y};
    }
}
