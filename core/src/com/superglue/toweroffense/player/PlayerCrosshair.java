package com.superglue.toweroffense.player;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import com.superglue.toweroffense.GameCanvas;
import com.superglue.toweroffense.ResourceManager;
import com.superglue.toweroffense.gameobjects.BoxGameObject;
import com.superglue.toweroffense.gameobjects.SimpleGameObject;
import com.superglue.toweroffense.util.Constants;

public class PlayerCrosshair extends BoxGameObject {

    public PlayerCrosshair(float x, float y, float width, float height){
        super(x, y, width, height);
    }

    @Override
    public boolean activatePhysics(World world){
        boolean return_val = super.activatePhysics(world);
        body.destroyFixture(body.getFixtureList().get(0));
        return return_val;
    }

    public void draw(GameCanvas canvas){
        canvas.draw(
                ResourceManager.boulderTexture,
                Color.BLACK,
                0, 0,
                getX() - 13 * Constants.RES_SCALE_X, getY(),
                ResourceManager.boulderTexture.getRegionWidth(),
                ResourceManager.boulderTexture.getRegionHeight()
        );
    }
}
