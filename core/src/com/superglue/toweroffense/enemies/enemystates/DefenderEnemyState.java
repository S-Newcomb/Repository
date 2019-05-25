package com.superglue.toweroffense.enemies.enemystates;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.*;
import com.badlogic.gdx.ai.steer.limiters.AngularLimiter;
import com.badlogic.gdx.ai.steer.limiters.LinearLimiter;
import com.badlogic.gdx.ai.steer.limiters.NullLimiter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.superglue.toweroffense.ai.SteeringAgent;
import com.superglue.toweroffense.ai.SteeringStates;
import com.superglue.toweroffense.enemies.DefenderEnemy;

public enum DefenderEnemyState implements State<DefenderEnemy> {

    SPAWNING(){
    },

    FLEEING(){
    },

    DEFENDING(){

    },

    ATTACKING(){

    };

    protected DefenderEnemy enemy;

    @Override
    public void enter(DefenderEnemy enemy) {

    }

    @Override
    public void exit(DefenderEnemy enemy) {

    }


    @Override
    public void update(DefenderEnemy entity){
        if (entity.steering == null){
            entity.steering = new SteeringAgent(entity.getBody(), false);
            entity.steering.setMaxLinearSpeed(3500);
            entity.steering.setMaxLinearAcceleration(1000);
        }

        if (entity.reachOrientationSB == null) {

            Arrive<Vector2> arriveSB = new Arrive<Vector2>(entity.steering, entity.getTargetLocation()) //
                    .setTimeToTarget(0.1f) //
                    .setArrivalTolerance(0.001f) //
                    .setDecelerationRadius(40);
            entity.steering.setSteeringBehavior(arriveSB);

            entity.reachOrientationSB = new ReachOrientation<Vector2>(entity.steering, entity.getTargetLocation()) //
                    .setLimiter(new AngularLimiter(100, 20)) //
                    .setTimeToTarget(0.1f) //
                    .setAlignTolerance(0.001f) //
                    .setDecelerationRadius(MathUtils.PI);

            entity.lookWhereYouAreGoingSB = new LookWhereYouAreGoing<Vector2>(entity.steering) //
                    .setLimiter(new AngularLimiter(100, 20)) //
                    .setTimeToTarget(0.1f) //
                    .setAlignTolerance(0.001f) //
                    .setDecelerationRadius(MathUtils.PI);

            BlendedSteering<Vector2> reachPositionAndOrientationSB = new BlendedSteering<Vector2>(entity.steering)
                    .setLimiter(NullLimiter.NEUTRAL_LIMITER) //
                    .add(arriveSB, 1f) //
                    .add(entity.reachOrientationSB, 1f) //
                    .add(entity.lookWhereYouAreGoingSB, 1f);

            entity.steering.setSteeringBehavior(reachPositionAndOrientationSB);
        }
        boolean lwyag = entity.getPosition().dst2(entity.getTargetLocation().getPosition()) > 1000;
        entity.lookWhereYouAreGoingSB.setEnabled(lwyag);
        entity.reachOrientationSB.setEnabled(!lwyag);


        if (entity.isDying){
            entity.stateMachine.changeState(DefenderEnemyState.FLEEING);
        }
        entity.steering.update(GdxAI.getTimepiece().getDeltaTime());
    }

    @Override
    public boolean onMessage(DefenderEnemy enemy, Telegram telegram) {
        return false;
    }
}
