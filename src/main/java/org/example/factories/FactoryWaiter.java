package org.example.factories;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import org.example.components.WaiterComponent;
import org.example.threads.Waiter;
import org.example.observers.NotifyWaiter;

public class FactoryWaiter {

    public void createWaiters(){
        int actualX = 500;
        NotifyWaiter notifyWaiter = NotifyWaiter.getInstance();
        for(byte i = 0; i < 2;i++){
            WaiterComponent waiterComponent = new WaiterComponent();

            Entity waiterBuild = FXGL.entityBuilder()
                    .at(actualX, 280)
                    .with(waiterComponent)
                    .buildAndAttach();
            Waiter waiter = new Waiter(waiterComponent);
            notifyWaiter.suscribeWaiter(waiter);
            waiter.start();
            actualX = actualX + 20;
        }

    }
}
