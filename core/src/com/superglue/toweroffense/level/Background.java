package com.superglue.toweroffense.level;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.superglue.toweroffense.GameCanvas;
import com.badlogic.gdx.graphics.Color;
import com.superglue.toweroffense.gameobjects.SimpleGameObject;


public class Background extends SimpleGameObject {
    private float widthFactor, heightFactor;
    private TextureRegion texture1, texture2, texture3;

    public Background(float w, float h){
        setPosition(0, 0);
        widthFactor = w;
        heightFactor = h;
    }

    public boolean activatePhysics(World world){ return super.activatePhysics(world); }

    protected void createFixtures() {};
    protected void releaseFixtures() {};

    public void addTextures(TextureRegion tex1, TextureRegion tex2, TextureRegion tex3){
        texture1 = tex1;
        texture2 = tex2;
        texture3 = tex3;
    }

    public float getWidthFactor(){
        return widthFactor;
    }

    public float getHeightFactor() {
        return heightFactor;
    }

    public void setWidthFactor(float x){
        widthFactor = x;
    }

    public void setHeightFactor(float x){
        heightFactor = x;
    }


    public void draw(GameCanvas canvas) {
        for(int i = 0; i < widthFactor; i++)
            for(int j = 0; j < heightFactor; j++)
                canvas.draw(texture, Color.WHITE, i*texture.getRegionWidth(), j*texture.getRegionHeight(), texture.getRegionWidth(), texture.getRegionHeight());
    }

    public void drawDebug(GameCanvas canvas){ }
}
