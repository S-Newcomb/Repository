package com.superglue.toweroffense.player.states;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.superglue.toweroffense.InputController;
import com.superglue.toweroffense.player.PlayerConstants;
import com.superglue.toweroffense.player.PlayerController;
import com.superglue.toweroffense.util.Direction;

public class DashState extends BaseState {
    private static DashState instance;
    public static DashState getInstace(){
        if(instance == null) instance = new DashState();
        return instance;
    }

    private float dashLerp(float t){
        // this should never go past DEFAULT_DASH_TIME
        t = Math.min(PlayerConstants.DEFAULT_DASH_TIME, t);

        // for the dash lerp I want to start really fast, and get exponentially slower
        Interpolation circleIn = Interpolation.fade;
        // to invert on the x-axis, I take 1 - progress ratio, instead of just using the progress ratio.
        float progress = 1 - (t / PlayerConstants.DEFAULT_DASH_TIME);
//        float alpha = circleIn.apply(progress);
        return circleIn.apply(progress);
    }

    private float elapsed;

    private Vector2 dashVelCache;

    @Override
    public void enter(PlayerController playerController){
        super.enter(playerController);

        player.setState(PlayerState.DASHING);

        player.setVX(0);
        player.setVY(0);

        elapsed = 0;

        if(dashVelCache == null) dashVelCache = new Vector2(0, 0);
        else dashVelCache.set(0, 0);
    }

    @Override
    public void update(PlayerController playerController){
        super.update(playerController);

        elapsed += dt;

        float speed = PlayerConstants.DEFAULT_MAX_DASH_SPEED * dashLerp(elapsed);

        Direction dir = InputController.getInstance().getLastDirectionMoved();
        if(dir == Direction.LEFT) dashVelCache.set(-1, 0);
        else if(dir == Direction.RIGHT) dashVelCache.set(1, 0);
        else if(dir == Direction.UP) dashVelCache.set(0, 1);
        else if(dir == Direction.DOWN) dashVelCache.set(0, -1);
        else dashVelCache.set(0, 0);

        player.setLinearVelocity(dashVelCache.scl(speed));

        if(elapsed > PlayerConstants.DEFAULT_DASH_TIME) {
            stateMachine.changeState(MobileState.getInstance());
        }
    }

    @Override
    public void exit(PlayerController playerController){
        super.exit(playerController);
    }
}
