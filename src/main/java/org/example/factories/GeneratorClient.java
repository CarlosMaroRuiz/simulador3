package org.example.factories;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.application.Platform;
import org.example.components.ClientComponent;
import org.example.threads.Client;
import org.example.models.Places;
import org.example.models.QueueClients;


import java.util.Random;

public class GeneratorClient extends Thread {
    private final Random random = new Random();
    private final double meanArrivalRate;
    private final QueueClients queueClients;
    private volatile boolean isGenerating = true;
    private final Places places;
    public GeneratorClient(double meanArrivalRate, QueueClients queueClients,Places places) {
        this.meanArrivalRate = meanArrivalRate;
        this.queueClients = queueClients;
        this.places = places;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (this) {
                    while (!isGenerating) {
                        wait();
                    }
                }

                long waitTime = (long) (-Math.log(1.0 - random.nextDouble()) / meanArrivalRate * 1000);
                Thread.sleep(waitTime);


                Platform.runLater(this::createClients);
            }
        } catch (InterruptedException e) {
            System.out.println("Generación de clientes interrumpida.");
            Thread.currentThread().interrupt();
        }
    }

    private void createClients() {
        int numberOfClients = random.nextInt(2) + 1;

        for (int i = 0; i < numberOfClients; i++) {
            ClientComponent clientComponent = new ClientComponent();

            double randomY = 90 + random.nextDouble() * (150 - 90);


            Entity entyClient = FXGL.entityBuilder()
                    .at(710, randomY)
                    .with(clientComponent)
                    .buildAndAttach();

            Client client = new Client(clientComponent,places);
            queueClients.agregarCliente(client);
            client.start();

            System.out.println("Nuevo cliente generado en posición: x=710, y=" + randomY);
        }
    }

    public synchronized void stopGeneration() {
        isGenerating = false;
        System.out.println("Generación de clientes detenida.");
    }

    public synchronized void resumeGeneration() {
        isGenerating = true;
        notify();
        System.out.println("Generación de clientes reanudada.");
    }
}
