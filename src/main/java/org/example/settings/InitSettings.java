package org.example.settings;

import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.dsl.FXGL;
import org.example.connect.LogicClients;
import org.example.factories.FactoryChef;
import org.example.factories.FactoryWaiter;
import org.example.models.QueueClients;
import org.example.scene.Background;

public class InitSettings {
     public InitSettings(){
         this.createSettings();
         this.objetsSimulation();
     }

     private void createSettings(){
         FXGL.getGameScene().getContentRoot().getChildren().add(0, new Background().getBackground());
         Music backgroundMusic = FXGL.getAssetLoader().loadMusic("restaurant.wav");
         FXGL.getAudioPlayer().loopMusic(backgroundMusic);
     }

     private void objetsSimulation(){
         QueueClients queueClients;
         queueClients = new QueueClients();
         LogicClients bridgeClients =  new LogicClients(queueClients);
         bridgeClients.makeConect();
         new FactoryWaiter().createWaiters();
         new FactoryChef().createChefs();
     }
}
