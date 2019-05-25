package com.superglue.toweroffense.enemies;

import com.badlogic.gdx.math.Vector2;
import com.superglue.toweroffense.ResourceManager;
import com.superglue.toweroffense.player.Player;
import com.superglue.toweroffense.projectiles.Projectile;
import com.superglue.toweroffense.projectiles.ProjectilePool;
import com.superglue.toweroffense.util.Constants;
import com.superglue.toweroffense.util.Updatable;

import java.util.ArrayList;

public class EnemyController implements Updatable {
    private Player player;
    private ProjectilePool ppool;
    private ArrayList<Enemy> enemyList;
    private ArrayList<Projectile> lastProjectilesSpawned;

    private Vector2 diffVecCache;

    private boolean shoot = false;
    private float timer = 0;

    private float fireRate = 1.75f;
    private float projectileLife = 3f;

    public EnemyController(Player p, ProjectilePool pool){
        player  = p;
        enemyList = new ArrayList<Enemy>();
        lastProjectilesSpawned = new ArrayList<Projectile>();
        ppool = pool;

        diffVecCache = new Vector2();
    }

    public void setProjectileLife(float f){
        projectileLife = f;
    }

    public void update(float dt){
        lastProjectilesSpawned.clear();

        shoot = false;
        timer += dt;
        if(timer >= fireRate){
            shoot = true;
            timer = 0;
        }

        for(Enemy e: enemyList) {
            if (e.getClass().equals(DefenderEnemy.class)) {
                float xDist = (player.getPosition().x - e.getPosition().x);
                float yDist = (player.getPosition().y - e.getPosition().y);
                float dist = (float) Math.sqrt((xDist * xDist) + (yDist * yDist));
                if (dist < 15) {
                    if(shoot) {
                        e.setTexture(ResourceManager.archerShootingAnimation);
                        float angle = 270 - (float) Math.atan2(xDist, yDist);
                        diffVecCache.x = xDist;
                        diffVecCache.y = yDist + 0.5f;
                        Projectile p = ppool.allocate(
                                e.getPosition().x + (float) (Math.random() * 4 - 2), e.getPosition().y + 0.5f + (float) (Math.random() * 4 - 2),
                                1, 1,
                                diffVecCache.nor().scl(18),
                                angle,
                                projectileLife
                        );

                        if (!p.hasBeenAddedToWorld()) {
                            lastProjectilesSpawned.add(p);
                            p.setHasBeenAddedToWorld();
                        }
                    }
                }
                else{
                    e.setTexture(ResourceManager.defenderTexture);
                }
            }
        }
    }

    public ArrayList<Projectile> getLastProjectilesSpawned(){
        return lastProjectilesSpawned;
    }

    public boolean getShoot(){
        return shoot;
    }

    public void addEnemy(Enemy e) {
        enemyList.add(e);
        e.setTarget(player);
    }
    public void removeEnemy(Enemy e) {
        enemyList.remove(e);
    }

}
