package com.superglue.toweroffense.projectiles;

import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import java.util.HashSet;

import com.superglue.toweroffense.util.Updatable;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.*;


/** This class provides a pool of pre-allocated projectiles. */
public class ProjectilePool extends Pool<Projectile> {
    /** Time in seconds since last frame */
    private float dt;

    /** ArrayList of all projectiles that exist, active and inactive */
    private HashSet<Projectile> allProjectiles;
    public Array<Projectile> deadProjectiles;

    private World world;

    /** Creates a pre-allocated pool of projectiles with the given capacity */
    public ProjectilePool(int cap, World w) {
        super(100, cap);

        allProjectiles = new HashSet<Projectile>();
        deadProjectiles = new Array<Projectile>();

//        for (int i = 0; i < cap; i++) {
//            Projectile p = newObject();
//            free(p);
//            allProjectiles.add(p);
//        }

        world = w;
    }

    /** Return the time passed since the last frame in seconds */
    public float getDt() { return dt; }

    /** Update all of the projectiles in this pool */
//    public void update(float dt) {
//        for (Projectile p : allProjectiles) {
//            if (!p.isAlive()) {
//                deadProjectiles.add(p);
//            }
//        }
//    }

    public Projectile newObject() {
        Projectile p = new Projectile();
        allProjectiles.add(p);
        return p;
    }

    public Array<Projectile> getDeadProjectiles(){
        return deadProjectiles;
    }
    public void clearDeadProjectiles(){
        deadProjectiles.clear();
    }

    /** Allocates a new projectile with the given attributes */
    public Projectile allocate(float x, float y, float width, float height, Vector2 velocity, float angle, float life) {
        Projectile p = obtain();
        p.set(x, y, width, height, velocity, angle, life, this);
        if(p.hasBody()) {
            p.getBody().setActive(true);
        }
        if(!allProjectiles.contains(p))
            allProjectiles.add(p);
        return p;
    }
}
