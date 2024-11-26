package org.example.models;

import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.dsl.FXGL;
import org.example.components.WaiterComponent;
import org.example.storages.QueueOrders;
import org.example.storages.QueuePending;

public class Waiter extends Thread {
    private final WaiterComponent waiterComponent;
    private final Music orderMusic;
    private double targetX;
    private double targetY;
    private volatile boolean isAttending;
    private final QueuePending queueAttendClients;
    private final QueueOrders queueOrder;
    private Pending sendPending; // Para enviar pedido a la cocina

    public Waiter(WaiterComponent waiterComponent) {
        this.waiterComponent = waiterComponent;
        this.isAttending = false;
        this.queueAttendClients = QueuePending.getInstance();
        this.queueOrder = QueueOrders.getInstance();
        orderMusic = FXGL.getAssetLoader().loadMusic("order.wav");
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                // Verificar si hay solicitudes en la cola para atender
                if (!isAttending && queueAttendClients.hasElements()) {
                    attendPending(); // Atender una solicitud pendiente
                }

                if (isAttending) {
                    goToTable(); // Ir a la mesa
                    returnToStart(); // Regresar a la posición inicial
                    sendPendingToKitchen(); // Enviar pendiente a la cocina
                }

                Thread.sleep(100); // Reducir uso de CPU en caso de inactividad
            } catch (InterruptedException e) {
                System.out.println("Mesero interrumpido.");
                Thread.currentThread().interrupt();
            }
        }
    }

    public synchronized void callForAttend(Pending pending) {
        if (!isAttending) {
            this.targetX = pending.getTablePoint().getX();
            this.targetY = pending.getTablePoint().getY();
            this.isAttending = true;
            sendPending = pending;
            waiterComponent.setStates(1);
            notify();
        }
    }

    public void goToTable() throws InterruptedException {
        moveToCoordinate(targetX, targetY);

        // Reproducir audio al llegar a la mesa
        FXGL.getAudioPlayer().playMusic(orderMusic);

        // Simular una pausa en la mesa
        Thread.sleep(2000);

        // Detener la música al terminar la interacción
        FXGL.getAudioPlayer().stopMusic(orderMusic);

        System.out.println("Mesero atendió la mesa en posición: X=" + targetX + ", Y=" + targetY);
    }

    public void returnToStart() throws InterruptedException {
        System.out.println("Regresando a la posición inicial...");
        moveToCoordinate(waiterComponent.getInitX(), waiterComponent.getInitY());

        // Simular pausa al regresar
        Thread.sleep(2000);

        synchronized (this) {
            isAttending = false;
            waiterComponent.setStates(0);
        }

        System.out.println("Mesero regresó a su posición inicial.");
    }

    private void moveToCoordinate(double targetX, double targetY) throws InterruptedException {
        double currentX = waiterComponent.getEntity().getX();
        double currentY = waiterComponent.getEntity().getY();
        double speed = 100;

        while (Math.abs(currentX - targetX) > 1 || Math.abs(currentY - targetY) > 1) {
            double deltaX = targetX - currentX;
            double deltaY = targetY - currentY;

            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            double moveX = (deltaX / distance) * speed * 0.016; // 16 ms por frame (aproximadamente 60 FPS)
            double moveY = (deltaY / distance) * speed * 0.016;

            currentX += moveX;
            currentY += moveY;

            waiterComponent.setMovimiento(currentX, currentY);

            Thread.sleep(16);
        }

        waiterComponent.setMovimiento(targetX, targetY);
    }

    public synchronized boolean isAttending() {
        return this.isAttending;
    }

    private void attendPending() {
        Pending pending = queueAttendClients.desencolar();
        if (pending != null) {
            callForAttend(pending);
            System.out.println("Mesero atendiendo solicitud de la cola: X=" + pending.getTablePoint().getX() + ", Y=" + pending.getTablePoint().getY());
        }
    }

    private void sendPendingToKitchen() {
        if (sendPending != null) {
            queueOrder.encolar(sendPending);
            System.out.println("Pedido enviado a la cocina: " + sendPending);
            sendPending = null;
        }
    }
}
