package com.superglue.toweroffense.player;

import com.superglue.toweroffense.InputController;
import com.superglue.toweroffense.ResourceManager;
import com.superglue.toweroffense.particles.ShroomCloudPool;
import com.superglue.toweroffense.player.states.PlayerState;
import com.superglue.toweroffense.util.Direction;
import com.superglue.toweroffense.util.FilmStrip;
import com.superglue.toweroffense.util.Updatable;

public class PlayerAnimationController implements Updatable {
    private Player player;
    private InputController inputController;

    public ShroomCloudPool shroomCloudPool;

    private FilmStrip idleSprite;
    private FilmStrip idleUpSprite;
    private FilmStrip idleDownSprite;
    private FilmStrip fallingSprite1;
    private FilmStrip fallingSprite2;
    private FilmStrip fallingSpriteUp;
    private FilmStrip fallingSpriteDown;
    private FilmStrip walkingSprite;
    private FilmStrip walkingUpSprite;
    private FilmStrip walkingDownSprite;

    boolean isRight;
    float animationTimer;

    public PlayerAnimationController(Player player){
        this.player = player;

        idleSprite = ResourceManager.towerIdleSprite;
        idleUpSprite = ResourceManager.towerIdleUpSprite;
        idleDownSprite = ResourceManager.towerIdleDownSprite;
        fallingSprite1 = ResourceManager.towerFallingAnimation1;
        fallingSprite2 = ResourceManager.towerFallingAnimation2;
        fallingSpriteUp = ResourceManager.towerFallingUpAnimation;
        fallingSpriteDown = ResourceManager.towerFallingDownAnimation;
        walkingSprite = ResourceManager.towerWalkingAnimation;
        walkingUpSprite = ResourceManager.towerWalkingUpAnimation;
        walkingDownSprite = ResourceManager.towerWalkingDownAnimation;

        inputController = InputController.getInstance();

        animationTimer = 0;
        isRight = true;
    }

    private void updateAnimation(float dt) {
        animationTimer += dt;
        if(animationTimer >= 1 / PlayerConstants.ANIMATION_FRAME_RATE) {
            ((FilmStrip)player.getTexture()).setFrame((((FilmStrip)player.getTexture()).getFrame()+1)%((FilmStrip)player.getTexture()).getSize());
            animationTimer = 0;
        }

        if(player.getTexture().equals(walkingSprite) &&
                inputController.getMovementDir().x == 0 &&
                inputController.getMovementDir().y == 0){
            player.setTexture(idleSprite);
        }

        if((player.getTexture().equals(walkingUpSprite) &&
                inputController.getMovementDir().x == 0 &&
                inputController.getMovementDir().y == 0)){
            player.setTexture(idleUpSprite);
        }

        if((player.getTexture().equals(walkingDownSprite) &&
                inputController.getMovementDir().x == 0 &&
                inputController.getMovementDir().y == 0)){
            player.setTexture(idleDownSprite);
        }

        if(player.getState() != PlayerState.SHOOTING && (player.getTexture().equals(idleSprite) || player.getTexture().equals(idleUpSprite) || player.getTexture().equals(idleDownSprite))){
            if(inputController.getMovementDir().x != 0){
                player.setTexture(walkingSprite);
            }
            else if(inputController.getMovementDir().y > 0){
                player.setTexture(walkingUpSprite);
            }
            else if(inputController.getMovementDir().y < 0){
                player.setTexture(walkingDownSprite);
            }
        }

        if(player.getState() == PlayerState.WALKING && player.getTexture().equals(walkingSprite)){
            if(inputController.getMovementDir().y > 0 && inputController.getMovementDir().x == 0)
                player.setTexture(walkingUpSprite);
            else if(inputController.getMovementDir().y < 0 && inputController.getMovementDir().x == 0)
                player.setTexture(walkingDownSprite);
        }

        if(player.getState() == PlayerState.WALKING && player.getTexture().equals(walkingUpSprite)){
            if(inputController.getMovementDir().x != 0)
                player.setTexture(walkingSprite);
            else if (inputController.getMovementDir().y < 0)
                player.setTexture(walkingDownSprite);
        }

        if(player.getState() == PlayerState.WALKING && player.getTexture().equals(walkingDownSprite)){
            if(inputController.getMovementDir().x != 0)
                player.setTexture(walkingSprite);
            else if (inputController.getMovementDir().y > 0)
                player.setTexture(walkingUpSprite);
        }

        if(player.getTexture().equals(fallingSprite1) && ((FilmStrip)player.getTexture()).getFrame() == ((FilmStrip)player.getTexture()).getSize() - 1) {
            player.setTexture(fallingSprite2);
            ((FilmStrip)player.getTexture()).setFrame(0);

        }

        if(player.getTexture().equals(fallingSprite2) && ((FilmStrip)player.getTexture()).getFrame() == ((FilmStrip)player.getTexture()).getSize() - 1){
            player.setTexture(idleSprite);
            setAnimationFrameRate(15);
            player.setFallAnimationOffset_X(0);
        }

        if(player.getTexture().equals(fallingSpriteUp) && ((FilmStrip)player.getTexture()).getFrame() == ((FilmStrip)player.getTexture()).getSize() - 1){
            player.setTexture(idleUpSprite);
            setAnimationFrameRate(15);
            player.setFallAnimationOffset_Y(0);

        }

        if(player.getTexture().equals(fallingSpriteDown) && ((FilmStrip)player.getTexture()).getFrame() == ((FilmStrip)player.getTexture()).getSize() - 1){
            player.setTexture(idleDownSprite);
            setAnimationFrameRate(15);
            player.setFallAnimationOffset_Y(0);

        }

        if(player.getAttackType() != PlayerAttackType.SHOOT && (inputController.getMovementDir().x > 0 || inputController.getAttackDir() == Direction.RIGHT)
                && !isRight && (player.getTexture().equals(walkingSprite) || player.getTexture().equals(idleSprite))){
            isRight = true;
            player.flipX();
        }

        if(player.getAttackType() != PlayerAttackType.SHOOT && (inputController.getMovementDir().x < 0 || inputController.getAttackDir() == Direction.LEFT)
                && isRight  && (player.getTexture().equals(walkingSprite) || player.getTexture().equals(idleSprite))){
            isRight = false;
            player.flipX();
        }

        if(!player.getTexture().equals(fallingSprite1) &&
                !player.getTexture().equals(fallingSprite2) &&
                !player.getTexture().equals(fallingSpriteUp) &&
                !player.getTexture().equals(fallingSpriteDown) &&
                inputController.isHoldingAttack() &&
                (inputController.getLastDirectionMoved() == Direction.LEFT || inputController.getLastDirectionMoved() == Direction.RIGHT)){
            player.setTexture(fallingSprite1);
            ((FilmStrip)player.getTexture()).setFrame(0);
            setAnimationFrameRate(40);
            player.setFallAnimationOffset_X(PlayerConstants.FALL_ANIMATION_CONSTANT_X);
            makeDust();
        }

        if(!player.getTexture().equals(fallingSprite1) &&
                !player.getTexture().equals(fallingSprite2) &&
                !player.getTexture().equals(fallingSpriteUp) &&
                !player.getTexture().equals(fallingSpriteDown) &&
                inputController.isHoldingAttack() &&
                (inputController.getLastDirectionMoved() == Direction.UP)){

            player.setTexture(fallingSpriteUp);
            ((FilmStrip)player.getTexture()).setFrame(0);
            setAnimationFrameRate(40);
            player.setFallAnimationOffset_Y(PlayerConstants.FALL_ANIMATION_CONSTANT_Y);
            makeDust();
        }

        if(!player.getTexture().equals(fallingSprite1) &&
                !player.getTexture().equals(fallingSprite2) &&
                !player.getTexture().equals(fallingSpriteUp) &&
                !player.getTexture().equals(fallingSpriteDown) &&
                inputController.isHoldingAttack() &&
                (inputController.getLastDirectionMoved() == Direction.DOWN)){
            player.setTexture(fallingSpriteDown);
            ((FilmStrip)player.getTexture()).setFrame(0);
            setAnimationFrameRate(40);
            player.setFallAnimationOffset_Y(PlayerConstants.FALL_ANIMATION_CONSTANT_Y);
            makeDust();
        }
    }

    private void setAnimationFrameRate(int x){
        PlayerConstants.ANIMATION_FRAME_RATE = x;
    }

    @Override
    public void update(float dt) {
        updateAnimation(dt);
    }

    private void makeDust(){
        if (shroomCloudPool != null && !player.hasJustLanded())
        {
            Direction dir = InputController.getInstance().getLastDirectionMoved();
            int offx = 0; int offy = 0;
            if (dir == Direction.LEFT) {
                offx -= 120;
            }else if (dir == Direction.RIGHT)
            {
                offx += 120;
            }else if (dir == Direction.UP) {
                offy += 100;
            }else{
                offy -= 50;
            }
            shroomCloudPool.makeParticle(player.getX() + offx, player.getY() + offy);
        }
    }
}
