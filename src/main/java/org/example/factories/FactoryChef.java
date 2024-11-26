package org.example.factories;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import org.example.components.ChefComponent;
import org.example.models.Chef;

public class FactoryChef {

    public void createChefs() {
        double startX = 520; // Posición inicial en X
        double spacing = 80; // Espaciado entre los chefs en X

        for (byte i = 0; i < 2; i++) {
            ChefComponent chefComponent = new ChefComponent();

            // Crear y posicionar el chef
            double chefX = startX + (i * spacing); // Calcular la posición en X
            double chefY = 380; // Posición fija en Y

            Entity chefBuild = FXGL.entityBuilder()
                    .at(chefX, chefY)
                    .with(chefComponent)
                    .buildAndAttach();

            // Log para depuración
            System.out.println("Chef creado en posición: X=" + chefX + ", Y=" + chefY);

            Chef chef = new Chef(chefComponent);
            chef.start();
        }
    }
}
