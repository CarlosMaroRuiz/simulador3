package org.example.storages;
import org.example.models.Pending;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueOrders {
    
    private final BlockingQueue<Pending> orders;
    
    private static QueueOrders instance;

    private QueueOrders() {
        
        this.orders = new LinkedBlockingQueue<>(); // Manejo automático de concurrencia

    }

    public static QueueOrders getInstance() {
        if (instance == null) {
            synchronized (QueueOrders.class) {
                if (instance == null) {
                    
                    instance = new QueueOrders();
                    
                }
            }
        }
        return instance;
    }

    public void encolar(Pending order) {
        try {
            orders.put(order); // Bloquea si la cola está llena
            System.out.println("Pedido encolado: " + order);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Interrumpido al encolar pedido.");
        }
    }

    public Pending desencolar() {
        try {
            return orders.take(); // Bloquea si la cola está vacía
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Interrumpido al desencolar pedido.");
            return null;
        }
    }

    public boolean hasElements() {
        return !orders.isEmpty();
    }
}
