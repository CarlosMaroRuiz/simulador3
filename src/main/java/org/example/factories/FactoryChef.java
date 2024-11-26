package org.example.factories;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import org.example.components.ChefComponent;
import org.example.threads.Chef;

public class FactoryChef {

    public void createChefs() {
        double startX = 520; // Posición inicial en X
        double spacing = 80; // Espaciado entre los chefs en X

        for (byte i = 0; i < 2; i++) {
            ChefComponent chefComponent = new ChefComponent();


            double chefX = startX + (i * spacing);
            double chefY = 380;

            Entity chefBuild = FXGL.entityBuilder()
                    .at(chefX, chefY)
                    .with(chefComponent)
                    .buildAndAttach();

            System.out.println("Chef creado en posición: X=" + chefX + ", Y=" + chefY);

            Chef chef = new Chef(chefComponent);
            chef.start();
        }
    }
}
