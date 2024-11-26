package org.example.threads;

import com.almasb.fxgl.entity.Entity;
import javafx.application.Platform;
import org.example.components.ClientComponent;
import org.example.models.Cordenads;
import org.example.models.Pending;
import org.example.models.Places;
import org.example.observers.NotifyWaiter;
import static org.example.utils.MoveCordinated.moveToCoordinate;

public class Client extends Thread {
    private final ClientComponent clientComponent;
    private volatile boolean isWaitingForTable;
    private Cordenads cordenads;
    private int idMesa; // ID de la mesa asignada
    private final Places places; // Referencia al gestor de mesas
    private volatile boolean isAttending; // Indica si el cliente está siendo atendido

    public Client(ClientComponent clientComponent, Places places) {
        this.clientComponent = clientComponent;
        this.isWaitingForTable = true;
        this.places = places;
        this.isAttending = false;
    }

    @Override
    public void run() {
        try {
            entrar();
            synchronized (this) {
                while (isWaitingForTable) {
                    System.out.println("Cliente esperando una mesa...");
                    wait();
                }
            }

            moveToCoordinate(this.cordenads.getX(), this.cordenads.getY(),this.clientComponent);
            notifyMesero(); // Notifica al mesero que necesita atención


            synchronized (this) {
                while (!isAttending) {
                    System.out.println("Cliente esperando ser atendido...");
                    wait();
                }
            }

            removeFromScene();
            places.liberarMesa(idMesa);

        } catch (InterruptedException e) {
            System.out.println("Cliente interrumpido durante la espera.");
            Thread.currentThread().interrupt();
        }
    }

    public synchronized void assignTable(double x, double y, int idMesa) {
        this.cordenads = new Cordenads(x, y);
        this.idMesa = idMesa;
        isWaitingForTable = false;
        notify();
        System.out.println("Cliente asignado a una mesa en posición: X=" + x + ", Y=" + y);
    }

    private void entrar() throws InterruptedException {
        System.out.println("Cliente entrando al restaurante...");
        moveToCoordinate(600, clientComponent.getInitY(),this.clientComponent);
    }


    private void removeFromScene() {
        Platform.runLater(() -> {
            Entity entity = clientComponent.getEntity();
            if (entity != null) {
                entity.removeFromWorld();
                System.out.println("Cliente eliminado del escenario.");
            }
        });
    }

    private void notifyMesero() {
        if (cordenads == null) {
            System.err.println("Error: Las coordenadas de la mesa son nulas.");
            return;
        }
        NotifyWaiter notifyWaiter = NotifyWaiter.getInstance();
        System.out.println("Cliente notificando al mesero...");
        notifyWaiter.notifyWaiter(new Pending(this, cordenads));
    }

    public synchronized void attend() {
        this.isAttending = true;
        System.out.println("Cliente siendo atendido...");
        notify(); // Notifica al cliente que ha sido atendido
    }
}
