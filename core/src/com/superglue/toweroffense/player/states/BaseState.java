package com.superglue.toweroffense.player.states;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.superglue.toweroffense.InputController;
import com.superglue.toweroffense.player.Player;
import com.superglue.toweroffense.player.PlayerAttackType;
import com.superglue.toweroffense.player.PlayerController;
import com.superglue.toweroffense.player.PlayerCrosshair;

public abstract class BaseState implements State<PlayerController> {
    // useful information for any state to have
    protected Player player;
    protected PlayerCrosshair playerCrosshair;
    protected PlayerAttackType attackType;
    protected PlayerStateMachine stateMachine;
    protected InputController inputController;
    protected float dt;

    @Override
    public void update(PlayerController playerController) {
        // update info
        getPlayerControllerValues(playerController);
    }

    /**
     * Gets useful data from playerController
     */
    protected void getPlayerControllerValues(PlayerController playerController){
        player = playerController.getPlayer();
        playerCrosshair = playerController.getPlayerCrosshair();
        attackType = player.getAttackType();
        stateMachine = playerController.getStateMachine();
        inputController = InputController.getInstance();
        dt = playerController.getDt();
    }

    // Unimplemented functions
    @Override
    public void enter(PlayerController playerController) {
        getPlayerControllerValues(playerController);
    }
    @Override
    public void exit(PlayerController playerController) {
        getPlayerControllerValues(playerController);
    }
    @Override
    public boolean onMessage(PlayerController playerController, Telegram telegram) { return false; }
}
