package com.superglue.toweroffense.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import com.superglue.toweroffense.player.PlayerConstants;
import com.superglue.toweroffense.player.PlayerController;
import com.superglue.toweroffense.util.Constants;

public class ShootState extends BaseState {
    private static ShootState instance;
    public static ShootState getInstance() {
        if(instance == null) instance = new ShootState();
        return instance;
    }

    private Vector2 crosshairVelCache;
    private boolean hasShot;

    @Override
    public void enter(PlayerController playerController){
        super.enter(playerController);

        player.setState(PlayerState.SHOOTING);

        player.setVX(0);
        player.setVY(0);

        hasShot = false;

        if(crosshairVelCache == null){
            crosshairVelCache = new Vector2();
        }
    }

    @Override
    public void update(PlayerController playerController){
        super.update(playerController);

//        crosshairVelCache.x = inputController.getMovementDir().x * PlayerConstants.DEFAULT_CROSSHAIR_SPEED;
//        crosshairVelCache.y = inputController.getMovementDir().y * PlayerConstants.DEFAULT_CROSSHAIR_SPEED;
//        playerCrosshair.setPosition(
//                playerCrosshair.getX() + crosshairVelCache.x,
//                playerCrosshair.getY() + crosshairVelCache.y
//        );

        float offset_x = (Gdx.input.getX() - PlayerConstants.CROSSHAIR_OFFSET_X);
        float offset_y = (Gdx.input.getY() - PlayerConstants.CROSSHAIR_OFFSET_Y);
        playerCrosshair.setPosition(playerController.getCamera().position.x + offset_x, playerController.getCamera().position.y - offset_y);

        if(inputController.didShoot() || hasShot){
            stateMachine.changeState(MobileState.getInstance());
        }

        else if(inputController.leftClick()){
            if (player.getAmmo() > 0)
            {
                player.setJustShot(true);
                hasShot = true;

                player.addAmmo(-1);
            }
        }
    }

    @Override
    public void exit(PlayerController playerController) {
        super.exit(playerController);

        playerCrosshair.setVX(0);
        playerCrosshair.setVY(0);

        playerCrosshair.setPosition(PlayerConstants.DEFAULT_CROSSHAIR_POS.x, PlayerConstants.DEFAULT_CROSSHAIR_POS.y);

        player.setJustShot(false);
        hasShot = false;

        player.toggleAttackType();
    }
}
