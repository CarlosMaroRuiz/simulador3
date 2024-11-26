package org.example.threads;

import org.example.models.Places;

public class Recepcionista extends Thread {

    private final Places places;
    private boolean running = true;

    public Recepcionista(Places places) {
        this.places = places;
    }

    @Override
    public void run() {
        while (running) {
            synchronized (this) {
                if (places.verifyPlace()) {
                    places.placeClient();
                } else {
                    System.out.println("Todas las mesas están ocupadas.");
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Recepcionista interrumpido.");
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public synchronized void stopRecepcionista() {
        running = false;
        System.out.println("Recepcionista detenido.");
    }
}
