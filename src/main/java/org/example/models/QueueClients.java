package org.example.models;

import org.example.observers.NotifyGenerator;
import org.example.threads.Client;

import java.util.LinkedList;
import java.util.Queue;

public class QueueClients {
    private final Queue<Client> queueClient = new LinkedList<>();
    private NotifyGenerator notifyGenerator;

    public void addNotify(NotifyGenerator notifyGenerator) {
        this.notifyGenerator = notifyGenerator;
    }

    public synchronized void agregarCliente(Client client) {
        queueClient.add(client);
        System.out.println("Cliente añadido a la cola. Total en cola: " + queueClient.size());

        if (queueClient.size() >= 5) {
            notifyGenerator.stopGenerate();
        }
    }

    public synchronized Client obtenerCliente() {
        if (!queueClient.isEmpty()) {
            Client client = queueClient.poll();

            return client;
        }
        System.out.println("Cola vacía.");
        return null;
    }

    public boolean estaVacia() {
        return queueClient.isEmpty();
    }
}
