package org.example.scene;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.image.ImageView;

public class Background {

    public ImageView getBackground(){
        ImageView backgroundView = new ImageView(FXGL.getAssetLoader().loadImage("lugar.png"));
        backgroundView.setFitWidth(FXGL.getAppWidth());
        System.out.println(FXGL.getAppWidth());
        backgroundView.setFitHeight(FXGL.getAppHeight());
        return  backgroundView;
    }
}
