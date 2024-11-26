package org.example.storages;

import org.example.models.Pending;

import java.util.LinkedList;
import java.util.Queue;

public class QueueReadyOrders {
    private final Queue<Pending> readyOrders; // Cola de órdenes listas
    private static QueueReadyOrders instance; // Instancia única (singleton)

    private QueueReadyOrders() {
        this.readyOrders = new LinkedList<>();
    }

    // Obtener la instancia única (singleton)
    public static synchronized QueueReadyOrders getInstance() {
        if (instance == null) {
            instance = new QueueReadyOrders();
        }
        return instance;
    }

    // Agregar una orden lista a la cola
    public synchronized void encolar(Pending order) {
        if (order == null) {
            System.out.println("Error: No se puede encolar una orden nula.");
            return;
        }
        readyOrders.add(order);
        System.out.println("Orden lista añadida a la cola. Total en cola: " + readyOrders.size());
        notifyAll(); // Notificar a los hilos en espera
    }

    // Retirar una orden lista de la cola
    public synchronized Pending desencolar() {
        while (readyOrders.isEmpty()) {
            try {
                System.out.println("No hay órdenes listas. Esperando...");
                wait(); // Esperar hasta que haya órdenes en la cola
            } catch (InterruptedException e) {
                System.out.println("Interrupción durante la espera de órdenes listas.");
                Thread.currentThread().interrupt(); // Reestablecer estado de interrupción
                return null; // Retornar nulo si el hilo es interrumpido
            }
        }
        Pending order = readyOrders.poll();
        System.out.println("Orden lista obtenida de la cola. Restantes: " + readyOrders.size());
        return order;
    }

    // Verificar si hay órdenes en la cola
    public synchronized boolean hasElements() {
        return !readyOrders.isEmpty();
    }
}
