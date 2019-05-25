package com.superglue.toweroffense.player;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.superglue.toweroffense.particles.ShroomCloudPool;
import com.superglue.toweroffense.util.Updatable;

import java.util.Random;


public class CameraController implements Updatable  {
    private static float MAX_SHAKE_ANGLE = 1f;
    private static float MAX_SHAKE_OFFSET_X = 0.1f;
    private static float MAX_SHAKE_OFFSET_Y = 0.1f;

    // how much to subtract trauma by every frame.
    private static float TRAUMA_DECREASE_RATE = 0.1f;

    private OrthographicCamera camera;

    private Player player;
    private float leftEdge;
    private float rightEdge;
    private float bottomEdge;
    private float topEdge;

    // this describes the level of shakiness we should be experiencing at any given time
    // is constantly decreasing. Add to this when we want to shake more
    // should always be between 0 and 1
    private float trauma;
    // has the camera been rocked by some amount? used to reset position for next shake
    private boolean shook;
    private float oldAngle;
    private float oldOffsetX;
    private float oldOffsetY;


    public CameraController(Player player){
        this.player = player;

        shook = false;
        oldAngle = 0;
        oldOffsetX = 0;
        oldOffsetY = 0;
    }

    public OrthographicCamera getCamera() { return camera; }
    public void setCamera(final OrthographicCamera camera, float le, float re, float be, float te) {
        this.camera = camera;
        this.camera.position.x = player.getX();
        this.camera.position.y = player.getY() + player.getTexture().getRegionHeight()/6;
        leftEdge = le;
        rightEdge = re;
        bottomEdge = be;
        topEdge = te;
    }

    /**
     * Shakes the camera a random amount
     */
    private void shake(){
        Random rand = new Random();
        // multiply by trauma^2 so that the camera shaking feels more natural
        float angle = MAX_SHAKE_ANGLE * trauma * trauma * rand.nextFloat() * 2 - 1;
        float offsetX = MAX_SHAKE_OFFSET_X * trauma * trauma * rand.nextFloat() * 2 - 1;
        float offsetY = MAX_SHAKE_OFFSET_Y * trauma * trauma * rand.nextFloat() * 2 - 1;

        if(!shook){
            camera.rotate(angle);
            camera.translate(offsetX, offsetY);

            oldAngle = angle;
            oldOffsetX = offsetX;
            oldOffsetY = offsetY;

        } else {
            undoShake();
        }

        shook = !shook;
    }

    // Undoes resets camera to its one true position
    private void undoShake(){
        camera.rotate(-oldAngle);
        camera.translate(-oldOffsetX, -oldOffsetY);

        oldAngle = 0;
        oldOffsetX = 0;
        oldOffsetY = 0;

    }

    private void followPlayer() {
        // move camera with player
        if(player.getX() > leftEdge && player.getX() < rightEdge) {
            camera.position.x = player.getX();
        }
        if(player.getY() > bottomEdge && player.getY() < topEdge) {
            camera.position.y = player.getY() + player.getTexture().getRegionHeight()/6f;
        }
    }

    private void handleShaking(){
        // subtract from trauma
        if(trauma - TRAUMA_DECREASE_RATE >= 0) trauma -= TRAUMA_DECREASE_RATE;
        else trauma = 0;

        // set shaking to happen if you just landed
        if(player.hasJustLanded()){
            trauma += 1;

        } if(player.hasJustShot()){
            trauma += 1;

           //if(shroomCloudPool != null) shroomCloudPool.makeParticle(player.getShotLocation().x*50 + 100, player.getShotLocation().y*50 + 100);
        }


        if(trauma > 0) shake();
        else undoShake();
    }

    @Override
    public void update(float dt) {
        followPlayer();

        handleShaking();
    }
}