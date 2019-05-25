package com.superglue.toweroffense.collision;

import com.badlogic.gdx.physics.box2d.*;
import com.superglue.toweroffense.enemies.*;
import com.superglue.toweroffense.gameobjects.GameObject;
import com.superglue.toweroffense.obstacles.Obstacle;
import com.superglue.toweroffense.obstacles.Rock;
import com.superglue.toweroffense.particles.ShroomCloudPool;
import com.superglue.toweroffense.player.Player;
import com.superglue.toweroffense.projectiles.Projectile;
import com.superglue.toweroffense.util.SoundController;

import java.util.HashSet;

public class CollisionManager implements ContactListener {

    public ShroomCloudPool scp;
    private static CollisionManager instance;
    public static CollisionManager getInstance() {
        if(instance == null) instance = new CollisionManager();
        return instance;
    }

    private HashSet<GameObject> objectsToDestroy;

    private CollisionManager() {
        objectsToDestroy = new HashSet<>();
    }

    public HashSet<GameObject> getObjectsToDestroy() { return objectsToDestroy; }
    public void clearObjectsToDestroy() { objectsToDestroy.clear(); }

    private void playerEnemyCollisions(Player player, Enemy enemy) {
        if (player.hasJustLanded() && !objectsToDestroy.contains(enemy)) {
            objectsToDestroy.add(enemy);
            //player.addPopulation(1);
            if (enemy instanceof SpearEnemy){
                scp.makeSpearsExplode(enemy.getX(), enemy.getY());
            }else if (enemy instanceof MeleeEnemy)
            {
                scp.makeSwordsExplode(enemy.getX(), enemy.getY());
            }else if (enemy instanceof DefenderEnemy)
            {
                scp.makeArcherExplode(enemy.getX(), enemy.getY());
            }
            player.addEnemiesKilled(1);
           // SoundController.getInstance().scream();
        }
    }

    private void playerRockCollisions (Player player, Rock rock){
        if (!player.hasJustLanded() || rock == null) return;
//        if (player.canCrushRock()) {
//            objectsToDestroy.add(rock);
//            player.ammoCounter += 1;
//        }
        rock.addHealth(-player.getCrushPower());
        if(rock.isDead()) {
            objectsToDestroy.add(rock);
            scp.makeFire(rock.getX(), rock.getY());
        }
    }

    private void playerVillageCollisions(Player player, Village village){
        if (!player.hasJustLanded() || village == null) return;
        int health = village.getHealth();
        if (health > 1) {
            village.setHealth(health-1);
            player.addPopulation(10);
        }
        else {
            scp.makeOrangeExplosion(village.getX(), village.getY());
            objectsToDestroy.add(village);
            player.addPopulation(30);
        }
    }

    private void playerProjectileCollisions(Player player){
        player.addHealth(-0.75f*player.getDefenseBuffScale());
        if(player.getHealth() <= 1) {
            player.died();
            objectsToDestroy.add(player);
        }
    }

    // CONTACT LISTENER FUNCTIONS
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Object objectA = fixtureA.getBody().getUserData();
        Object objectB = fixtureB.getBody().getUserData();

        // player enemy
        if(objectA instanceof Player && objectB instanceof Enemy) {
            playerEnemyCollisions((Player)objectA, (Enemy)objectB);
        } else if(objectA instanceof Enemy && objectB instanceof Player) {
            playerEnemyCollisions((Player)objectB, (Enemy)objectA);
        }

        // player rock
        if(objectA instanceof Player && objectB instanceof Rock) {
          playerRockCollisions((Player)objectA, (Rock)objectB);
        } else if(objectA instanceof Rock && objectB instanceof Player) {
            playerRockCollisions((Player)objectB, (Rock)objectA);
        }

        // projectile obstacle
        if(objectA instanceof Projectile && (objectB instanceof Obstacle || objectB instanceof Player)) {
            ((Projectile)objectA).killProjectile();
            if(objectB instanceof Player){
                playerProjectileCollisions((Player)objectB);
            }
        } else if((objectA instanceof Obstacle || objectA instanceof Player) && objectB instanceof Projectile){
            ((Projectile)objectB).killProjectile();
            if(objectA instanceof Player) {
                playerProjectileCollisions((Player)objectA);
            }
        }

        // player village
        if(objectA instanceof Player && objectB instanceof Village){
            playerVillageCollisions((Player)objectA, (Village)objectB);
        }
        else if(objectA instanceof Village && objectB instanceof Player){
            playerVillageCollisions((Player)objectB, (Village)objectA);
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}
