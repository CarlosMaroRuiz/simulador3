package org.example.storages;

import org.example.models.Pending;

import java.util.LinkedList;
import java.util.Queue;

public class QueuePending {
    private final Queue<Pending> cordenadsAttend;
    private static QueuePending instance;


    private QueuePending() {
        this.cordenadsAttend = new LinkedList<>();
    }

    public static synchronized QueuePending getInstance() {
        if (instance == null) {
            instance = new QueuePending();
        }
        return instance;
    }

    public synchronized boolean hasElements() {
        return !cordenadsAttend.isEmpty();
    }

    public synchronized void encolar(Pending pending) {
        cordenadsAttend.add(pending);
        notifyAll();
    }


    public synchronized Pending desencolar() {
        while (cordenadsAttend.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Hilo interrumpido mientras esperaba en la cola.");
                Thread.currentThread().interrupt();
                return null;
            }
        }
        Pending pending = cordenadsAttend.poll();
        return pending;
    }
}
