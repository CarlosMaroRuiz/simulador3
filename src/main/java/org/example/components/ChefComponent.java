package org.example.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.Texture;
import org.example.interfaces.Movent;

public class ChefComponent extends Component implements Movent {
    private double initX;
    private double initY;
    private double toX;
    private double toY;
    private boolean isMovent;
    private double speed = 100; // Velocidad en píxeles por segundo

    @Override
    public void onAdded() {
        Texture entityTexture = FXGL.getAssetLoader().loadTexture("chef.png");
        entityTexture.setFitWidth(50);
        entityTexture.setFitHeight(100);
        entity.getViewComponent().addChild(entityTexture);

        // Inicializar las coordenadas iniciales
        this.initX = entity.getX();
        this.initY = entity.getY();
        this.isMovent = false;
    }

    @Override
    public void onUpdate(double tpf) {
        if (isMovent) {
            updateMovent(tpf);
        }
    }

    public void setMovent(boolean movent) {
        this.isMovent = movent;
    }

    @Override
    public void setMovimiento(double x, double y) {
        this.toX = x;
        this.toY = y;
        this.isMovent = true; // Activar el movimiento
    }

    @Override
    public void getMovent() {
        // No se requiere implementación directa; `onUpdate` lo maneja.
    }

    private void updateMovent(double tpf) {
        double currentX = entity.getX();
        double currentY = entity.getY();

        // Calcular la dirección hacia el objetivo
        double deltaX = toX - currentX;
        double deltaY = toY - currentY;

        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        // Si está cerca del objetivo, detener el movimiento
        if (distance < 1) {
            entity.setPosition(toX, toY);
            isMovent = false; // Detener el movimiento
            return;
        }

        // Normalizar la dirección y calcular el desplazamiento
        double moveX = (deltaX / distance) * speed * tpf;
        double moveY = (deltaY / distance) * speed * tpf;

        // Actualizar la posición del chef
        entity.translateX(moveX);
        entity.translateY(moveY);
    }

    public void resetPosition() {
        entity.setPosition(initX, initY);
        isMovent = false;
    }
}
