package org.example.connect;

import org.example.factories.GeneratorClient;
import org.example.models.Places;
import org.example.models.QueueClients;
import org.example.threads.Recepcionista;
import org.example.observers.NotifyGenerator;


public class LogicClients {
    private final QueueClients queueClients;
    private final GeneratorClient generatorClient;
    private final NotifyGenerator notifyGenerator;
    private final Recepcionista recepcionista;
    private final Places places;
    public LogicClients(QueueClients queueClients) {
        this.queueClients = queueClients;

        this.places = new Places(queueClients);
        this.notifyGenerator = new NotifyGenerator();
        this.recepcionista = new Recepcionista(this.places);
        // Inicializar los objetos necesarios
        this.generatorClient = new GeneratorClient(2, queueClients,places);
    }

    public void makeConect() {

        this.notifyGenerator.addGenerator(this.generatorClient);

        this.queueClients.addNotify(this.notifyGenerator);
        this.places.addNotify(this.notifyGenerator);
        generatorClient.start();
        recepcionista.start();
    }
}
