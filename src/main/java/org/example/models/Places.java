package org.example.models;

import org.example.observers.NotifyGenerator;
import org.example.threads.Client;

import java.util.Arrays;

public class Places {
    private final Client[] mesasDisponibles = new Client[5];
    private final QueueClients queueClients;
    private final Cordenads[] ubiTable; // Declaración del arreglo de ubicaciones
    private NotifyGenerator notifyGenerator;

    public Places(QueueClients queueClients) {
        this.queueClients = queueClients;
        Arrays.fill(mesasDisponibles, null);

        // Inicialización del arreglo de ubicaciones de mesas
        ubiTable = new Cordenads[] {
                new Cordenads(152, 100),
                new Cordenads(152, 240),
                new Cordenads(320, 240),
                new Cordenads(152, 345),
                new Cordenads(320, 345)
        };
    }

    public synchronized void placeClient() {
        if (!queueClients.estaVacia()) {
            Client client = queueClients.obtenerCliente();
            int mesaDisponible = findAvailableTable();

            if (mesaDisponible != -1) {
                mesasDisponibles[mesaDisponible] = client;
                Cordenads ubicacionMesa = ubiTable[mesaDisponible]; // Obtener las coordenadas de la mesa
                client.assignTable(ubicacionMesa.getX(), ubicacionMesa.getY(), mesaDisponible);
                System.out.println("Cliente asignado a la mesa " + (mesaDisponible + 1) +
                        " en posición X=" + ubicacionMesa.getX() + ", Y=" + ubicacionMesa.getY());
            }
        } else {
            System.out.println("No hay clientes en la cola.");
            if (notifyGenerator != null) {
                notifyGenerator.continueGenerated(); // Reanudar generación si no hay clientes
            }
        }
    }

    public synchronized void liberarMesa(int numeroMesa) {
        if (numeroMesa >= 0 && numeroMesa < mesasDisponibles.length) {
            if (mesasDisponibles[numeroMesa] != null) {
                System.out.println("Liberando mesa " + (numeroMesa + 1) +
                        " de Cliente ID: " + mesasDisponibles[numeroMesa].getId());
                mesasDisponibles[numeroMesa] = null;
                notifyAll(); // Notificar a los clientes en espera
            } else {
                System.out.println("La mesa " + (numeroMesa + 1) + " ya está disponible.");
            }
        } else {
            System.out.println("Número de mesa inválido.");
        }
    }

    public synchronized boolean verifyPlace() {
        for (Client client : mesasDisponibles) {
            if (client == null) {
                return true;
            }
        }
        return false;
    }

    private synchronized int findAvailableTable() {
        for (int i = 0; i < mesasDisponibles.length; i++) {
            if (mesasDisponibles[i] == null) {
                return i;
            }
        }
        return -1;
    }

    public void addNotify(NotifyGenerator notifyGenerator) {
        this.notifyGenerator = notifyGenerator;
    }
}
