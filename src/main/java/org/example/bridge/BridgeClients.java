package org.example.bridge;

import org.example.factories.GeneratorClient;
import org.example.models.Places;
import org.example.models.QueueClients;
import org.example.models.Recepcionista;
import org.example.observers.NotifyGenerator;

// Puente entre la lógica de generación y la gestión de clientes
public class BridgeClients {
    private final QueueClients queueClients;
    private final GeneratorClient generatorClient;
    private final NotifyGenerator notifyGenerator;
    private final Recepcionista recepcionista;
    private final Places places;
    public BridgeClients(QueueClients queueClients) {
        this.queueClients = queueClients;

        this.places = new Places(queueClients);
        this.notifyGenerator = new NotifyGenerator();
        this.recepcionista = new Recepcionista(this.places);
        // Inicializar los objetos necesarios
        this.generatorClient = new GeneratorClient(2, queueClients,places);
    }

    public void makeBridge() {

        this.notifyGenerator.addGenerator(this.generatorClient);

        this.queueClients.addNotify(this.notifyGenerator);
        this.places.addNotify(this.notifyGenerator);
        generatorClient.start();
        recepcionista.start();
    }
}
