package org.example.threads;

import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.dsl.FXGL;
import org.example.components.WaiterComponent;
import org.example.models.Pending;
import org.example.storages.QueueOrders;
import org.example.storages.QueuePending;

import static org.example.utils.MoveCordinated.moveToCoordinate;

public class Waiter extends Thread {
    private final WaiterComponent waiterComponent;
    private final Music orderMusic;
    private double targetX;
    private double targetY;
    private volatile boolean isAttending;
    private final QueuePending queueAttendClients;
    private final QueueOrders queueOrder;
    private Pending sendPending;

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
                if (!isAttending && queueAttendClients.hasElements()) {
                    attendPending(); // Atender una solicitud pendiente
                }

                if (isAttending) {
                    goToTable();
                    returnToStart();
                    sendPendingToKitchen();
                }

                Thread.sleep(100);
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
        moveToCoordinate(targetX,targetY,this.waiterComponent);

        FXGL.getAudioPlayer().playMusic(orderMusic);

        Thread.sleep(2000);


        FXGL.getAudioPlayer().stopMusic(orderMusic);

        System.out.println("Mesero atendió la mesa en posición: X=" + targetX + ", Y=" + targetY);
    }

    public void returnToStart() throws InterruptedException {
        System.out.println("Regresando a la posición inicial...");
        moveToCoordinate(waiterComponent.getInitX(), waiterComponent.getInitY(),waiterComponent);

        // Simular pausa al regresar
        Thread.sleep(2000);

        synchronized (this) {
            isAttending = false;
            waiterComponent.setStates(0);
        }

        System.out.println("Mesero regresó a su posición inicial.");
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
