package org.example.models;

import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.dsl.FXGL;
import org.example.components.ChefComponent;
import org.example.storages.QueueOrders;
import org.example.storages.QueueReadyOrders;

public class Chef extends Thread {
    private final ChefComponent chefComponent;
    private final QueueOrders queueOrders;
    private final QueueReadyOrders queueReadyOrders;
    private final Music cookingSound;
    private Pending pendingCook;

    private static final double KITCHEN_X = 660;
    private static final double KITCHEN_Y = 385;

    public Chef(ChefComponent chefComponent) {
        this.chefComponent = chefComponent;
        this.queueOrders = QueueOrders.getInstance();
        this.queueReadyOrders = QueueReadyOrders.getInstance();
        this.cookingSound = FXGL.getAssetLoader().loadMusic("cooking.wav");
        this.pendingCook = null; // Inicialmente no tiene un pedido asignado
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                revisarPedido(); // Verificar si hay pedidos pendientes
                if (pendingCook != null) {
                    moveToKitchen(); // Mover a la cocina
                    cook(); // Cocinar el pedido actual
                    regresarMostrador(); // Entregar el pedido
                } else {
                    System.out.println("No hay pedidos asignados para cocinar.");
                }
            } catch (InterruptedException e) {
                System.out.println("Chef interrumpido.");
                Thread.currentThread().interrupt();
            }
        }
    }

    private void revisarPedido() throws InterruptedException {
        System.out.println("Chef revisando pedidos...");

        // Extraer pedido bloqueante si la cola está vacía
        pendingCook = queueOrders.desencolar();

        if (pendingCook != null) {
            System.out.println("Pedido recibido: Cliente ID=" + pendingCook.getClient().getId());
        }
    }

    private void moveToKitchen() throws InterruptedException {
        System.out.println("Chef moviéndose a la cocina...");
        chefComponent.setMovimiento(KITCHEN_X, KITCHEN_Y);
        chefComponent.setMovent(true);

        // Simular tiempo de movimiento
        Thread.sleep(2000); // Ajusta según la distancia/velocidad

        chefComponent.setMovent(false);
        System.out.println("Chef llegó a la cocina.");
    }

    private void cook() throws InterruptedException {
        if (pendingCook == null) {
            System.out.println("No hay pedido para cocinar. Saliendo del método.");
            return;
        }

        System.out.println("Chef cocinando para Cliente ID=" + pendingCook.getClient().getId());

        // Reproducir sonido de cocina
        FXGL.getAudioPlayer().playMusic(cookingSound);

        // Simular el tiempo de cocina
        Thread.sleep(5000); // 5 segundos

        // Detener el sonido de cocina
        FXGL.getAudioPlayer().stopMusic(cookingSound);

        System.out.println("Orden lista para Cliente ID=" + pendingCook.getClient().getId());
    }

    private void regresarMostrador() throws InterruptedException {
        System.out.println("Chef regresando al mostrador...");

        moveToMostrador();

        playBellSound();

        // Encolar el pedido listo
        synchronized (queueReadyOrders) {
            if (pendingCook != null) {
                queueReadyOrders.encolar(pendingCook);
                System.out.println("Orden lista encolada para Cliente ID=" + pendingCook.getClient().getId());
            } else {
                System.out.println("Error: No hay pedido listo para encolar.");
            }
        }

        // Limpiar el pedido actual
        pendingCook.getClient().attend();
        pendingCook = null;
    }

    private void moveToMostrador() throws InterruptedException {
        // Simular movimiento del chef al mostrador
        chefComponent.resetPosition();
        chefComponent.setMovent(true);

        // Simular tiempo de movimiento

        chefComponent.setMovent(false);
        System.out.println("Chef llegó al mostrador.");
    }

    private void playBellSound() {
        Music bellSound = FXGL.getAssetLoader().loadMusic("bell.wav");
        FXGL.getAudioPlayer().playMusic(bellSound);
        System.out.println("Timbre tocado para notificar pedido listo.");
    }
}
