package org.example.settings;

import com.almasb.fxgl.app.GameSettings;

public class SettingsGame{
    private GameSettings settings;
    public SettingsGame(GameSettings settings){
        this.settings = new  GameSettings();
    }
    public GameSettings getSetting(){
        this.settings.setWidth(600);
        this.settings.setHeight(600);
        this.settings.setTitle("FXGL Background Example");
        this.settings.setVersion("1.0");
        return this.settings;
    };
}
