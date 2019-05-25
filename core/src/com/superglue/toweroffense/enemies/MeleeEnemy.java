package com.superglue.toweroffense.enemies;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.superglue.toweroffense.ResourceManager;
import com.superglue.toweroffense.ai.SteeringAgent;
import com.superglue.toweroffense.ai.SteeringStates;
import com.superglue.toweroffense.enemies.enemystates.EnemyState;
import com.superglue.toweroffense.enemies.enemystates.MeleeEnemyState;
import com.superglue.toweroffense.player.Player;
import com.superglue.toweroffense.player.PlayerConstants;
import com.superglue.toweroffense.util.FilmStrip;
import com.superglue.toweroffense.util.Updatable;

public class MeleeEnemy extends Enemy implements Updatable {
    public StateMachine<MeleeEnemy, MeleeEnemyState> stateMachine;
    float animationTimer;

    public MeleeEnemy(float x, float y, float width, float height) {
        super(x, y, width, height);
        stateMachine = new DefaultStateMachine<>(this, MeleeEnemyState.SPAWNING);

        setTexture(ResourceManager.swordWalkingAnimation);
        animationTimer = 0;
    }

    public void update(float dt){
        if(target == null) return;

        stateMachine.update();

        float xDif = drawScale.x*(target.getPosition().x - getPosition().x);
        float yDif = drawScale.y*(target.getPosition().y - getPosition().y);
        float distance = (float)Math.sqrt((xDif*xDif)+(yDif*yDif));

        if(distance < 40) {
            Player player = (Player)target.getBody().getUserData();

            if(player.isDead()) return;

            player.addHealth(-0.05f*player.getDefenseBuffScale());
            if(player.getHealth() <= 1)
                player.died();
        }

        animationTimer += dt;
        if(animationTimer >= 1/5f) {
            ((FilmStrip)getTexture()).setFrame((((FilmStrip)getTexture()).getFrame()+1)%((FilmStrip)getTexture()).getSize());
            animationTimer = 0;
        }
    }

//    public void update(Vector2 playerPos) {

//        if (steering == null){
//            steering = new SteeringAgent(this.body, false);
//            Arrive<Vector2> arriveSB = SteeringStates.getArrive(steering, player.steering,1f, 2f, 10);
//            steering.setSteeringBehavior(arriveSB);
//        }
//        float xDif = drawScale.x*(playerPos.x - getPosition().x);
//        float yDif = drawScale.y*(playerPos.y - getPosition().y);
//
//        revFactor = xDif;
//
//        float distance = (float)Math.sqrt((xDif*xDif)+(yDif*yDif));
//
//        if(distance > 1) {
//            movementVector.set(xDif, yDif);
//        }
//
//        else movementVector.set(0, 0);
//
//        movementVector.nor();
//        movementVector.set(movementVector.x * SPEED_CONSTANT * drawScale.x, movementVector.y * SPEED_CONSTANT * drawScale.y);
//        setLinearVelocity(movementVector);
//    }

}
