package org.example.utils;

import org.example.components.FatherComponent;
import org.example.interfaces.Movent;

public class MoveCordinated {
    public static void moveToCoordinate(double targetX, double targetY, FatherComponent childComponent) throws InterruptedException {
        double currentX = childComponent.getEntity().getX();
        double currentY = childComponent.getEntity().getY();
        double speed = 100;
        
        //move the entity component
        while (Math.abs(currentX - targetX) > 1 || Math.abs(currentY - targetY) > 1) {
            double deltaX = targetX - currentX;
            double deltaY = targetY - currentY;

            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            double moveX = (deltaX / distance) * speed * 0.016; // 16 ms por frame (aproximadamente 60 FPS)
            double moveY = (deltaY / distance) * speed * 0.016;

            currentX += moveX;
            currentY += moveY;

            childComponent.setMovimiento(currentX, currentY);

            Thread.sleep(16);
        }
    }
}
