package com.superglue.toweroffense.player.states;

import com.badlogic.gdx.ai.fsm.StackStateMachine;
import com.superglue.toweroffense.player.PlayerController;

public class PlayerStateMachine extends StackStateMachine<PlayerController, BaseState> {
    public PlayerStateMachine(PlayerController playerController) {
        super(playerController, MobileState.getInstance());
    }
}
