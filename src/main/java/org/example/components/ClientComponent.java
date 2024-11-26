package org.example.components;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;

public class ClientComponent extends FatherComponent {
    private  double initX;
    private  double initY;
    private double toX;
    private double toY;
    @Override
    public void onAdded() {
        Texture entityTexture = FXGL.getAssetLoader().loadTexture("client.png");
        entityTexture.setFitWidth(50);
        entityTexture.setFitHeight(100);
        entity.getViewComponent().addChild(entityTexture);
        //los caso iniciales son para recepcion
        this.initX = entity.getX();
        this.initY = entity.getY();
    }

    @Override
    public void onUpdate(double tpf) {
        getMovent();
    }

    @Override
    public void setMovimiento(double x, double y) {
        this.toX = x;
        this.toY = y;
    }

    @Override
    public void getMovent() {
        entity.setPosition(toX,toY);
    }

    public double getInitY() {
        return initY;
    }
}
