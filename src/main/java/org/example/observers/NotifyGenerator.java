package org.example.observers;

import org.example.factories.GeneratorClient;


public class NotifyGenerator {
    private GeneratorClient generatorClient;

    public void addGenerator(GeneratorClient generatorClient) {
        this.generatorClient = generatorClient;
    }

    public void stopGenerate() {
        generatorClient.stopGeneration();
    }

    public void continueGenerated() {
       generatorClient.resumeGeneration();
    }
}
