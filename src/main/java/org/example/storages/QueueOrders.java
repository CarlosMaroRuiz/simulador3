package org.example.storages;

import org.example.models.Pending;

import java.util.LinkedList;
import java.util.Queue;

public class QueueOrders {
    private final Queue<Pending> ordersPending;
    private static QueueOrders instance;

    private QueueOrders() {
        this.ordersPending = new LinkedList<>();
    }

    public static synchronized QueueOrders getInstance() {
        if (instance == null) {
            instance = new QueueOrders();
        }
        return instance;
    }

    public synchronized void encolar(Pending pending) {
        if (pending != null) {
            ordersPending.add(pending);
            System.out.println("Pedido encolado: " + pending);
        } else {
            System.err.println("Error: Pedido nulo no puede ser encolado.");
        }
    }

    public synchronized Pending desencolar() {
        if (!ordersPending.isEmpty()) {
            Pending pending = ordersPending.poll();
            System.out.println("Pedido desencolado: " + pending);
            return pending;
        } else {
            System.out.println("No hay pedidos en cola.");
            return null;
        }
    }

    public synchronized boolean hasElements() {
        return !ordersPending.isEmpty();
    }
}
