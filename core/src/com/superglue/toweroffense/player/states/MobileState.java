package com.superglue.toweroffense.player.states;

import com.badlogic.gdx.math.Vector2;
import com.superglue.toweroffense.player.PlayerAttackType;
import com.superglue.toweroffense.player.PlayerController;

public class MobileState extends BaseState {
    private static MobileState instance;
    private boolean isRight = true;

    public static MobileState getInstance(){
        if(instance == null) instance = new MobileState();
        return instance;
    }

    @Override
    public void enter(PlayerController playerController){
        player.setState(PlayerState.WALKING);
    }

    @Override
    public void update(PlayerController playerController){
        super.update(playerController);

        Vector2 velocity = inputController.getMovementDir().scl(player.getMovementSpeed());
        player.setLinearVelocity(velocity);

        if(inputController.isHoldingAttack()){
           if(player.getAttackType() == PlayerAttackType.SLAM){
                stateMachine.changeState(SlamState.getInstance());
            }
        }

        if(inputController.didShoot()){
            player.toggleAttackType();
            stateMachine.changeState(ShootState.getInstance());
        }

//        if(inputController.didDash()){
//            stateMachine.changeState(DashState.getInstace());
//        }
    }

}
