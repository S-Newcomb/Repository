package com.superglue.toweroffense.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.Color;
import com.superglue.toweroffense.GameCanvas;
import com.superglue.toweroffense.collision.CollisionConstants;
import com.superglue.toweroffense.gameobjects.BoxGameObject;
import com.badlogic.gdx.utils.*;
import com.superglue.toweroffense.util.Updatable;

/** This class defines what a projectile is. */
public class Projectile extends BoxGameObject implements Pool.Poolable, Updatable {
    /** Number of seconds left to live (-1 for dead) */
    private float life;
    /** True if this projectile has a body currently assigned to it, false otherwise */
    private boolean hasBeenAddedToWorld;

    private float angle;
    private ProjectilePool pool;

    /** Creates a projectile */
    public Projectile() {
        super(-100, -100, 1, 1);

        life = 5.0f;

        // Make this projectile a bullet for more accurate collisions
        bodyinfo.bullet = true;
    }

    @Override
    public boolean activatePhysics(World world){
        boolean returnVal = super.activatePhysics(world);

        // Collision filtering
        Filter filter = new Filter();
        filter.categoryBits = CollisionConstants.LAYER_PROJECTILE;
        filter.maskBits = CollisionConstants.MASK_PROJECTILE;

        return returnVal;
    }


    /** "Allocates" a new projectile with the given attributes */
    public void set(float x, float y, float width, float height, Vector2 velocity, float angle, float life, ProjectilePool pool) {
        setPosition(x, y);
        setWidth(width);
        setHeight(height);

        this.life = life;
        this.angle = angle;
        this.pool = pool;
        setLinearVelocity(velocity);
    }

    /** Reset this photon so it can be allocated again */
    public void reset() {
        setPosition(-100, -100);
        setLinearVelocity(Vector2.Zero);

        life = 1.0f;
        this.setActive(true);
        hasBeenAddedToWorld = false;
    }


    public void setHasBeenAddedToWorld(){
        hasBeenAddedToWorld = true;
    }
    public boolean hasBeenAddedToWorld(){
        return hasBeenAddedToWorld;
    }

    public boolean hasBody(){
        return body != null;
    }

    /** Return how much time a projectile has left before it is deleted */
    public float getLife() { return life; }

    /** Decrement life of this projectile by dt */
    public void decLife(float dt) { life -= dt; }

    public void killProjectile(){
        life = 0;
    }

    /** Return true if this projectile is alive, false otherwise */
    public boolean isAlive() { return life > 0; }

    /** Update all of the projectiles in this pool */
    public void update(float dt) {
        decLife(dt);
        if (life <= 0){
            pool.deadProjectiles.add(this);
        }
    }
    public void draw(GameCanvas canvas) {
        canvas.draw(texture, Color.WHITE, 0, 0, getX(), getY(), angle, life, life);
    }
}
