package org.example.models;

import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.dsl.FXGL;
import org.example.components.WaiterComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Waiter extends Thread {
    private final List<Cordenads> coordinates = new ArrayList<>();
    private final WaiterComponent waiterComponent;
    private final Random random = new Random(); // Para seleccionar lugares aleatorios
    private final Music orderMusic; // Cargar el audio una sola vez

    public Waiter(WaiterComponent waiterComponent) {
        this.waiterComponent = waiterComponent;

        // Coordenadas para las mesas
        coordinates.add(new Cordenads(194.34784556330453, 160.34784556330453));
        coordinates.add(new Cordenads(294.34784556330453, 285));
        coordinates.add(new Cordenads(204.34784556330453, 289));

        // Cargar el audio una vez al crear el objeto
        orderMusic = FXGL.getAssetLoader().loadMusic("order.wav");
    }

    @Override
    public void run() {
        while (true) {
            waiterComponent.setStates(1); // Cambiar a estado WALKING
            try {
                int randomIndex = random.nextInt(coordinates.size());
                goToTable(randomIndex); // Ir a un lugar aleatorio
                returnToStart();       // Regresar a la posición inicial
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void goToTable(int numTable) throws InterruptedException {
        Cordenads selectedCord = coordinates.get(numTable);
        System.out.println("Yendo a la mesa en: X=" + selectedCord.getX() + ", Y=" + selectedCord.getY());

        // Mover al camarero a la coordenada seleccionada
        moveToCoordinate(selectedCord.getX(), selectedCord.getY());

        // Reproducir audio al llegar a la mesa
        FXGL.getAudioPlayer().playMusic(orderMusic);

        // Simular una pausa en la mesa
        Thread.sleep(2000);

        // Detener la música al terminar la interacción
        FXGL.getAudioPlayer().stopMusic(orderMusic);
    }

    public void returnToStart() throws InterruptedException {
        System.out.println("Regresando a la posición inicial...");
        moveToCoordinate(waiterComponent.getInitX(), waiterComponent.getInitY());

        // Detenerse un momento al regresar
        Thread.sleep(2000);

        // Cambiar al estado IDLE
        waiterComponent.setStates(0);
    }

    private void moveToCoordinate(double targetX, double targetY) throws InterruptedException {
        double currentX = waiterComponent.getEntity().getX();
        double currentY = waiterComponent.getEntity().getY();
        double speed = 100; // Velocidad en píxeles por segundo

        while (Math.abs(currentX - targetX) > 1 || Math.abs(currentY - targetY) > 1) {
            double deltaX = targetX - currentX;
            double deltaY = targetY - currentY;

            // Calcular dirección normalizada
            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            double moveX = (deltaX / distance) * speed * 0.016; // 16 ms por frame (aproximadamente 60 FPS)
            double moveY = (deltaY / distance) * speed * 0.016;

            // Actualizar posición actual
            currentX += moveX;
            currentY += moveY;

            // Aplicar posición al componente
            waiterComponent.setMovimiento(currentX, currentY);

            // Pausar para simular el tiempo entre frames
            Thread.sleep(16);
        }

        // Ajustar a la posición final exacta
        waiterComponent.setMovimiento(targetX, targetY);
    }
}
