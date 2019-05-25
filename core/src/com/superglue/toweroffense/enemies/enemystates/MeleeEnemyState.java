package com.superglue.toweroffense.enemies.enemystates;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.math.Vector2;
import com.superglue.toweroffense.ai.SteeringAgent;
import com.superglue.toweroffense.ai.SteeringStates;
import com.superglue.toweroffense.enemies.MeleeEnemy;

public enum MeleeEnemyState implements State<MeleeEnemy> {
    SPAWNING(){
        public void update(MeleeEnemy entity){
                entity.steering = new SteeringAgent(entity.getBody(), false);
                entity.steering.setMaxLinearSpeed(10);
                entity.steering.setMaxLinearAcceleration(100);
                entity.stateMachine.changeState(MeleeEnemyState.PURSUING);
        }
    },
    ARRIVING(){
        public void update(MeleeEnemy entity){
            Arrive<Vector2> arriveSB = SteeringStates.getArrive(entity.steering, entity.getTarget(),.3f, 0.001f, 3f);
            entity.steering.setSteeringBehavior(arriveSB);
            entity.steering.update(GdxAI.getTimepiece().getDeltaTime());
            if (entity.getPosition().dst2(entity.target.getPosition()) >= 40f){
                entity.stateMachine.changeState(MeleeEnemyState.PURSUING);
            }
        }
    },
    PURSUING(){
        public void update(MeleeEnemy entity) {
            Pursue<Vector2> pursueSB = SteeringStates.getPursue(entity.steering, entity.getTarget(), 3f);
            entity.steering.setSteeringBehavior(pursueSB);
            entity.steering.update(GdxAI.getTimepiece().getDeltaTime());
            if (entity.getPosition().dst2(entity.target.getPosition()) <= 40f){
                entity.stateMachine.changeState(MeleeEnemyState.ARRIVING);
            }
        }
    },
    FLEEING(){
        @Override
        public void update(MeleeEnemy entity){
            entity.steering.setSteeringBehavior(SteeringStates.getFlee(entity.steering, entity.getTarget()));
//            entity.steering.setSteeringBehavior(SteeringStates.getFollowPath(entity.steering, ));
            entity.steering.update(GdxAI.getTimepiece().getDeltaTime());
        }

    };
    @Override
    public void enter(MeleeEnemy entity) {
        if (entity == null)
            return;
    }


    @Override
    public void exit(MeleeEnemy entity) {
    }

    @Override
    public boolean onMessage(MeleeEnemy entity, Telegram telegram) {
        return false;
    }
}

