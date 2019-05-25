package com.superglue.toweroffense.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.superglue.toweroffense.GameCanvas;
import com.superglue.toweroffense.ResourceManager;
import com.superglue.toweroffense.collision.CollisionConstants;
import com.superglue.toweroffense.player.Player;
import com.superglue.toweroffense.util.FilmStrip;
import com.superglue.toweroffense.util.Updatable;

public class SpearEnemy extends Enemy implements Updatable {
    private static final float SPEED_CONSTANT = 0.15f;
    Vector2 movementVector;
    float revFactor;

    float animationTimer;

    public SpearEnemy(float x, float y, float width, float height){
        super(x, y, width, height);
        movementVector = new Vector2();

        setTexture(ResourceManager.spearWalkingAnimation);
        animationTimer = 0;
    }

    public void update(float dt) {
        if(target == null) return;

        Vector2 playerPos = target.getPosition();
        float xDif = drawScale.x * (playerPos.x - getPosition().x);
        float yDif = drawScale.y * (playerPos.y - getPosition().y);

        revFactor = xDif;

        float distance = (float) Math.sqrt((xDif * xDif) + (yDif * yDif));
        float angle = (float) Math.atan2(xDif, yDif);

        float xComponent = (float) Math.cos(angle);
        float yComponent = (float) Math.sin(angle);

        if(distance > 300) {
            movementVector.set(xDif, yDif);
        }
        if (distance < 40){
            movementVector.set(xComponent - xDif, yComponent - yDif);

            Player player = (Player)target.getBody().getUserData();

            if(player.isDead()) return;


            player.addHealth(-0.15f*player.getDefenseBuffScale());
            if(player.getHealth() <= 1)
                player.died();
        }
//        else {
//            //code for circling the player
//            if (randomFactor > .5) movementVector.set(xComponent, -yComponent);
//            else movementVector.set(-xComponent, yComponent);
//            //code for stopping instead
//            //movementVector.set(0,0);
//       }
        movementVector.nor();
        movementVector.set(movementVector.x * SPEED_CONSTANT * drawScale.x, movementVector.y * SPEED_CONSTANT * drawScale.y);
        setLinearVelocity(movementVector);

        animationTimer += dt;
        if(animationTimer >= 1/5f) {
            ((FilmStrip)getTexture()).setFrame((((FilmStrip)getTexture()).getFrame()+1)%((FilmStrip)getTexture()).getSize());
            animationTimer = 0;
        }
    }

//    @Override
//    public boolean activatePhysics(World world){
//        boolean returnVal = super.activatePhysics(world);
//
//        Filter filter = new Filter();
//        filter.categoryBits = CollisionConstants.LAYER_ENEMY;
//        filter.maskBits = CollisionConstants.MASK_ENEMY & ~CollisionConstants.LAYER_ENEMY;
//        body.getFixtureList().get(0).setFilterData(filter);
//
//        return returnVal;
//    }

    public void draw(GameCanvas canvas) {
        if (isDead)
            return;
        float revScale = revFactor < 0 ? -1 : 1;
        canvas.draw(texture, Color.WHITE, texture.getRegionWidth()/2, 0, getX(), getY(), 0, revScale, 1);
    }

}
