package org.example;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import org.example.settings.InitSettings;
import org.example.settings.SettingsGame;

/*
   Cosas a recordar
    FXGL.entityBuilder() -->metodo que nos ayuda con la construcion de entidades

------------ Metodos que dispone entityBuilder--------------------------------------------

     .at(x,y)-->En que lugar del scenario queremos que respamee
     .with() --> componente que queremos que este asociado
     .buildAtach -> lanzamiento del componente a la vista
----------------------------------------------------------------------------------------
 */

/*
Colisiones de rango en lo borde
x= 80 -- 700
y= 60 -- 490
 */
public class Main extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        new SettingsGame(settings);
    }
    @Override
    protected void initGame() {
            new InitSettings();
    }
    public static void main(String[] args) {
        launch(args);
    }
}