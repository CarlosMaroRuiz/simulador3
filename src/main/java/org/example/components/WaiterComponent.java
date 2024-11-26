package org.example.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.Texture;
import org.example.enums.WaiterMovent;
import org.example.interfaces.Movent;

public class WaiterComponent extends Component implements Movent {


    private WaiterMovent states = WaiterMovent.IDLE;
    private  double initX;
    private  double initY;
    //atributos a los puntos a llegar
    private double toX;
    private double toY;

    @Override
    public void onAdded() {
        Texture entityTexture = FXGL.getAssetLoader().loadTexture("mesero_frontal.png");
        entityTexture.setFitWidth(50);
        entityTexture.setFitHeight(100);
        entity.getViewComponent().addChild(entityTexture);
        this.initX = entity.getX();
        this.initY = entity.getY();
    }

    @Override
    public void onUpdate(double tpf) {

        switch (states){
            case IDLE:
                anyMovent();
            break;
            case WALKING:
                getMovent();
              break;

        }
    }

    public void anyMovent(){
         entity.setPosition(initX,initY);
    }
    public void setStates(int id_state){
            switch (id_state){
                case 0:this.states = WaiterMovent.IDLE;break;
                case 1:this.states = WaiterMovent.WALKING;break;
            }
    }
    public WaiterMovent getStates(){
        return states;
    }
    public void setMovimiento(double x,double y){
          this.toX = x;
          this.toY = y;
    }
    public  void getMovent(){
        entity.setPosition(toX,toY);
    }

    public double getInitX() {
        return initX;
    }

    public double getInitY() {
        return initY;
    }
}
