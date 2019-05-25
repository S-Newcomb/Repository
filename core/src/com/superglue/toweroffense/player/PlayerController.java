package com.superglue.toweroffense.player;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.superglue.toweroffense.InputController;
import com.superglue.toweroffense.particles.ShroomCloudPool;
import com.superglue.toweroffense.player.states.MobileState;
import com.superglue.toweroffense.player.states.PlayerState;
import com.superglue.toweroffense.player.states.PlayerStateMachine;
import com.superglue.toweroffense.player.states.SlamState;
import com.superglue.toweroffense.util.Direction;
import com.superglue.toweroffense.util.FilmStrip;
import com.superglue.toweroffense.util.Updatable;

public class PlayerController implements Updatable {
    private Player player;
    private PlayerCrosshair playerCrosshair;
    private PlayerStateMachine stateMachine;
    // delta time, the time between frames
    private float dt;

    private CameraController cameraController;
    private PlayerAnimationController animationController;

    public PlayerController(Player player, PlayerCrosshair playerCrosshair){
        this.player = player;
        this.playerCrosshair = playerCrosshair;
        this.player.playerCrosshair = playerCrosshair;
        this.stateMachine = new PlayerStateMachine(this);
        this.dt = 0;

        this.cameraController = new CameraController(player);
        this.animationController = new PlayerAnimationController(player);
    }

    public Player getPlayer(){ return player; }
    public PlayerCrosshair getPlayerCrosshair() { return playerCrosshair; }
    public PlayerStateMachine getStateMachine() { return stateMachine; }
    public float getDt() { return dt; }

    public OrthographicCamera getCamera(){
        return cameraController.getCamera();
    }

    public void setCamera(final ShroomCloudPool scp, final OrthographicCamera camera, float le, float re, float be, float te) {

        cameraController.setCamera(camera, le, re, be, te);
        animationController.shroomCloudPool = scp;
    }

    public void update(float dt) {
        cameraController.update(dt);
        animationController.update(dt);

        this.dt = dt;
        stateMachine.getCurrentState().update(this);
    }
}
