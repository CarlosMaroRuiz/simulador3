package org.example.factories;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import org.example.components.WaiterComponent;
import org.example.models.Waiter;

public class FactoryWaiter {

    public void createWaiters(){
        int actualX = 500;

        for(byte i = 0; i < 2;i++){
            WaiterComponent waiterComponent = new WaiterComponent();

            Entity waiterBuild = FXGL.entityBuilder()
                    .at(actualX, 280)
                    .with(waiterComponent)
                    .buildAndAttach();
            Waiter waiter = new Waiter(waiterComponent);
            waiter.start();
            actualX = actualX + 20;
        }

    }
}
