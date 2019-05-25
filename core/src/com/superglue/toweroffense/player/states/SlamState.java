package com.superglue.toweroffense.player.states;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.superglue.toweroffense.player.PlayerConstants;
import com.superglue.toweroffense.util.Direction;
import com.superglue.toweroffense.player.PlayerController;

public class SlamState extends BaseState {
    private static SlamState instance;

    public static SlamState getInstance(){
        if(instance == null) instance = new SlamState();
        return instance;
    }

    private enum AttackState {
        DOWN, STAY, UP
    }

    private Direction attackDir;
    private AttackState attackState;

    private float timer;
    private float targetAngle;
    private float angle;
    private boolean hasSlammed;

    public float slerpAngle(float a, float b, float t){
        if(t < 0) t = 0;
        else if(t > 1) t = 1;

        return a + t * t * t * t * b;
    }

    @Override
    public void enter(PlayerController playerController){
        super.enter(playerController);

        player.setState(PlayerState.FALLING);

        getPlayerControllerValues(playerController);
        this.attackDir = inputController.getLastDirectionMoved();
        attackState = AttackState.DOWN;

        hasSlammed = false;

        timer = 0;

        player.setLinearVelocity(Vector2.Zero);

        targetAngle = 0;
        if(this.attackDir == Direction.LEFT){
            targetAngle = (float)(Math.PI) / 2;
        } else if(this.attackDir == Direction.RIGHT){
            targetAngle = -(float)(Math.PI) / 2;
            player.togglePivotPoint();
        } else if(this.attackDir == Direction.UP || this.attackDir == Direction.DOWN){
            targetAngle = 0;
        }
    }

    @Override
    public void update(PlayerController playerController) {
        super.update(playerController);

        switch(attackState) {
            case DOWN:
                updateDown();
                break;
            case STAY:
                updateStay();
                break;
            case UP:
                updateUp();
                break;
        }

        player.getBody().setTransform(player.getPosition(), angle);
        player.setAngle(angle);
    }

    @Override
    public void exit(PlayerController playerController) {
        if(attackDir == Direction.RIGHT){
            player.togglePivotPoint();
        }

        this.angle = 0;
        this.attackDir = Direction.NONE;

        player.setJustLanded(false);
        player.getBody().setTransform(player.getPosition(), angle);
        player.setAngle(0);
    }

    // what should happen every frame while the player is falling down
    private void updateDown() {
        angle = slerpAngle(
                0,
                targetAngle,
                1 - (PlayerConstants.SLAM_DOWN_TIME - timer) / PlayerConstants.SLAM_DOWN_TIME
        );

        if(timer < PlayerConstants.SLAM_DOWN_TIME){
            timer += dt;
        } else {
            timer = 0;
            attackState = AttackState.STAY;
        }
    }

    // what should happen every frame while the player is on the ground
    private void updateStay(){
        if(!hasSlammed) {
            hasSlammed = true;
            player.setJustLanded(true);
            player.createAttackHitBoxes(attackDir);
        } else {
            player.setJustLanded(false);
        }

        player.setVX(0);
        player.setVY(0);

        if(timer < PlayerConstants.SLAM_STAY_TIME){
            timer += dt;
        } else {
            timer = 0;
            attackState = AttackState.UP;
        }
    }

    // what should happen every frame while the player is getting up
    private void updateUp(){
        player.destroyAttackHitBox();

        angle = slerpAngle(
                0,
                targetAngle,
                (PlayerConstants.SLAM_UP_TIME - timer) / PlayerConstants.SLAM_UP_TIME);

        if(timer < PlayerConstants.SLAM_UP_TIME){
            timer += dt;
        } else {
            timer = 0;
            stateMachine.changeState(MobileState.getInstance());
        }
    }
}
