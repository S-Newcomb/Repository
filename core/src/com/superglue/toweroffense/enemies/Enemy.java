package com.superglue.toweroffense.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import com.superglue.toweroffense.GameCanvas;
import com.superglue.toweroffense.ai.SteeringAgent;
import com.superglue.toweroffense.gameobjects.BoxGameObject;
import com.superglue.toweroffense.player.Player;
import com.superglue.toweroffense.collision.CollisionConstants;

public class Enemy extends BoxGameObject implements Pool.Poolable {

    public boolean isDead = false;
    public boolean isDying = false;
    public SteeringAgent target;
    public SteeringAgent steering;

    public Enemy(float x, float y, float width, float height){
        super(x, y, width, height);
    }


    public boolean activatePhysics(World world){
        boolean returnVal = super.activatePhysics(world);

        // collision filtering
        Filter filter = new Filter();
        filter.categoryBits = CollisionConstants.LAYER_ENEMY;
        filter.maskBits = CollisionConstants.MASK_ENEMY;
        body.getFixtureList().get(0).setFilterData(filter);

        return returnVal;
    }

    public void setTarget(Player p){
        this.target = p.steering;
    }

    public SteeringAgent getTarget(){
        return target;
    }

    public void draw(GameCanvas canvas) {
        if (isDead == true)
            return;

        float revScale = 1;
        if (target != null){ revScale = (target.getPosition().x - getPosition().x) < 0 ? -1 : 1;}
        canvas.draw(texture, Color.WHITE, texture.getRegionWidth()/2, 0, getX(), getY(), 0, revScale, 1);
    }

    @Override
    public void reset() {
        target = null;
        steering = null;
        isDead = false;
        isDying = false;
    }
}

