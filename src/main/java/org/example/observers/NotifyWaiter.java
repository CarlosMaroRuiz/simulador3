package org.example.observers;

import org.example.models.Pending;
import org.example.threads.Waiter;
import org.example.storages.QueuePending;

import java.util.ArrayList;

public class NotifyWaiter {
    private static NotifyWaiter instance; // Instancia única de la clase
    private final ArrayList<Waiter> waiters = new ArrayList<>();
    private QueuePending queuePendingAttend = QueuePending.getInstance();
    private NotifyWaiter() {}


    public static synchronized NotifyWaiter getInstance() {
        if (instance == null) {
            instance = new NotifyWaiter();
        }
        return instance;
    }

    // Suscribir un mesero
    public synchronized void suscribeWaiter(Waiter waiter) {
        this.waiters.add(waiter);
    }

    // Notificar a un mesero disponible
    public synchronized void notifyWaiter(Pending pending) {
        // Buscar un mesero disponible
        for (Waiter waiter : waiters) {
            if (!waiter.isAttending()) {
                waiter.callForAttend(pending); // Llamar al mesero para atender
                return;
            }
        }

        queuePendingAttend.encolar(pending);

    }


}
