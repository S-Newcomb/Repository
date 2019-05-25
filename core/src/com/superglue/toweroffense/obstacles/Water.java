package com.superglue.toweroffense.obstacles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.superglue.toweroffense.GameCanvas;
import com.superglue.toweroffense.collision.CollisionConstants;
import com.superglue.toweroffense.gameobjects.BoxGameObject;
import com.superglue.toweroffense.util.Constants;

public class Water extends BoxGameObject {
    public Water(float x, float y, float width, float height){
        super(x, y, width, height);
    }

    public boolean activatePhysics(World world){
        bodyinfo.type = BodyDef.BodyType.StaticBody;

        boolean returnVal = super.activatePhysics(world);

        Filter filter = new Filter();
        filter.categoryBits = CollisionConstants.LAYER_WATER;
        filter.maskBits = CollisionConstants.MASK_WATER;

        return returnVal;
    }

    public void draw(GameCanvas canvas){
        canvas.draw(
                texture,
                Color.WHITE,
                texture.getRegionWidth()/2, 0,
                getX(), getY(),
                0,
                drawScale.x/(50f * Constants.RES_SCALE_X), drawScale.y/(50f * Constants.RES_SCALE_Y)
        );
    }
}
