package org.example.models;

import com.almasb.fxgl.entity.Entity;
import javafx.application.Platform;
import org.example.components.ClientComponent;

public class Client extends Thread {
    private final ClientComponent clientComponent;
    private volatile boolean isWaitingForTable;
    private Cordenads cordenads;
    private int idMesa;
    private final Places places;

    public Client(ClientComponent clientComponent, Places places) {
        this.clientComponent = clientComponent;
        this.isWaitingForTable = true;
        this.places = places;
    }

    @Override
    public void run() {
        try {
            entrar();

            synchronized (this) {
                while (isWaitingForTable) {
                    wait(); // Espera a que una mesa esté disponible
                }
                moveToCoordinate(this.cordenads.getX(), this.cordenads.getY());
                notifyMesero();
                removeFromScene();
                places.liberarMesa(idMesa);
            }

        } catch (InterruptedException e) {
            System.out.println("Cliente interrumpido durante la espera.");
            Thread.currentThread().interrupt();
        }
    }

    public synchronized void assignTable(double x, double y, int idMesa) {
        this.cordenads = new Cordenads(x, y);
        this.idMesa = idMesa;
        isWaitingForTable = false;
        notify(); // Notificar al cliente que tiene una mesa asignada
        System.out.println("Cliente asignado a una mesa en posición: X=" + x + ", Y=" + y);
    }

    private void entrar() throws InterruptedException {
        System.out.println("Cliente entrando al restaurante...");
        moveToCoordinate(600, clientComponent.getInitY());
    }

    private void moveToCoordinate(double targetX, double targetY) throws InterruptedException {
        double currentX = clientComponent.getEntity().getX();
        double currentY = clientComponent.getEntity().getY();
        double speed = 100;

        while (Math.abs(currentX - targetX) > 1 || Math.abs(currentY - targetY) > 1) {
            double deltaX = targetX - currentX;
            double deltaY = targetY - currentY;

            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            double moveX = (deltaX / distance) * speed * 0.016; // Movimiento por frame
            double moveY = (deltaY / distance) * speed * 0.016;

            currentX += moveX;
            currentY += moveY;
            clientComponent.setMovimiento(currentX, currentY);

            Thread.sleep(16);
        }

        clientComponent.setMovimiento(targetX, targetY);
        System.out.println("Cliente llegó a la posición: X=" + targetX + ", Y=" + targetY);
    }

    private void removeFromScene() {
        Platform.runLater(() -> {
            Entity entity = clientComponent.getEntity();
            if (entity != null) {
                entity.removeFromWorld();
                System.out.println("Cliente eliminado del escenario.");
            }
        });
    }

    private void notifyMesero() {
        System.out.println("Cliente llamando al mesero.");
    }
}
