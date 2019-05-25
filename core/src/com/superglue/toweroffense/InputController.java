/*
 * InputController.java
 *
 * This class buffers in input from the devices and converts it into its
 * semantic meaning. If your game had an option that allows the player to
 * remap the control keys, you would store this information in this class.
 * That way, the main GameEngine does not have to keep track of the current
 * key mapping.
 *
 * Author: Walker M. White
 * Based on original PhysicsDemo Lab by Don Holden, 2007
 * LibGDX version, 2/6/2015
 */
package com.superglue.toweroffense;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;

import com.superglue.toweroffense.util.*;

/**
 * Class for reading player input. 
 *
 * This supports both a keyboard and X-Box controller. In previous solutions, we only 
 * detected the X-Box controller on start-up.  This class allows us to hot-swap in
 * a controller via the new XBox360Controller class.
 */
public class InputController {
    /** The singleton instance of the input controller */
    private static InputController theController = null;

    /**
     * Return the singleton instance of the input controller
     *
     * @return the singleton instance of the input controller
     */
    public static InputController getInstance() {
        if (theController == null) {
            theController = new InputController();
        }
        return theController;
    }

    // Fields to manage button
    private static final int LEFT_KEY = Input.Keys.A;
    private static final int RIGHT_KEY = Input.Keys.D;
    private static final int UP_KEY = Input.Keys.W;
    private static final int DOWN_KEY = Input.Keys.S;

    private static final int PAUSE_KEY = Input.Keys.P;

    private static final int ATTACK_KEY = Input.Keys.SPACE;

    private static final int SHOOT_KEY = Input.Keys.F;
    private static final int DASH_KEY = Input.Keys.SHIFT_LEFT;

    private Vector2 movementCache;
    private Vector2 mousePosCache;
    private Vector2 lastDirectionCache;

    private Direction attackDir;
    private Direction lastDirectionMoved = Direction.RIGHT;

    private boolean resetPressed;

    /** Whether the exit button was pressed. */
    private boolean exitPressed;
    private boolean exitPrevious;
    /** Whether the button to advance worlds was pressed */
    private boolean nextPressed;
    private boolean nextPrevious;

    /**Level Editor Keys*/
    private boolean enterPressed;

    /**Mouse Stuff*/
    private boolean leftClicked;
    private boolean rightClicked;

    public InputController() {
        movementCache = new Vector2(0, 0);
        mousePosCache = new Vector2(0, 0);
        lastDirectionCache = new Vector2(1, 0);

        exitPressed = false;
        exitPrevious = false;
    }

    public boolean didPause(){
        return Gdx.input.isKeyJustPressed(PAUSE_KEY);
    }

    public Vector2 getMovementDir(){ return movementCache; }
    public boolean didMove() { return movementCache.x != 0 || movementCache.y != 0; }

    public Direction getAttackDir(){ return attackDir; }
    public boolean didAttack() { return attackDir != Direction.NONE; }
    public boolean isHoldingAttack() { return Gdx.input.isKeyPressed(ATTACK_KEY); }
    public boolean rightClick (){ return rightClicked; }
    public boolean leftClick() { return leftClicked; }
    public Vector2 getMousePos () {return mousePosCache; }

    public boolean didShoot() { return Gdx.input.isKeyJustPressed(SHOOT_KEY); }
    public boolean didDash() { return Gdx.input.isKeyJustPressed(DASH_KEY); }

    /**
     * Returns true if the exit button was pressed.
     *
     * @return true if the exit button was pressed.
     */
    public boolean didExit() {
        return exitPressed && !exitPrevious;
    }

    /** Returns true if the player wants to go to the next level */
    public boolean didAdvance() {
        return nextPressed && !nextPrevious;
    }

    public boolean didPrompt() {enterPressed = !enterPressed; return !enterPressed; }

    public boolean didReset() { return resetPressed; }

    /**
     * Reads the input for the player and converts the result into game logic.
     */
    public void readInput() {
        exitPrevious = exitPressed;
        nextPrevious = nextPressed;

        readKeyboard();
        readMouse();
    }

    private void readKeyboard(){
        readMovement();
        attackDir = readAttack();

        nextPressed = Gdx.input.isKeyPressed(Input.Keys.EQUALS);
        enterPressed = Gdx.input.isKeyPressed(Input.Keys.ENTER);
//        exitPressed = Gdx.input.isKeyPressed(Input.Keys.ESCAPE);
        resetPressed = Gdx.input.isKeyPressed(Input.Keys.R);
        leftClicked =(Gdx.input.isButtonPressed(Input.Buttons.LEFT));
        rightClicked = (Gdx.input.isButtonPressed(Input.Buttons.RIGHT));
    }

    private void readMouse(){
        mousePosCache.set(Gdx.input.getX(), Gdx.input.getY());
    }

    private void readMovement() {
        float horizontalAxis = 0;
        float verticalAxis = 0;

        if(Gdx.input.isKeyPressed(LEFT_KEY)){
            horizontalAxis -= 1;
            lastDirectionMoved = Direction.LEFT;
            lastDirectionCache.set(-1, 0);
        }
        if(Gdx.input.isKeyPressed(RIGHT_KEY)){
            horizontalAxis += 1;
            lastDirectionMoved = Direction.RIGHT;
            lastDirectionCache.set(1, 0);
        }
        if(Gdx.input.isKeyPressed(UP_KEY)){
            verticalAxis += 1;
            lastDirectionMoved = Direction.UP;
            lastDirectionCache.set(0, 1);
        }
        if(Gdx.input.isKeyPressed(DOWN_KEY)){
            verticalAxis -= 1;
            lastDirectionMoved = Direction.DOWN;
            lastDirectionCache.set(0, -1);
        }

        movementCache.x = horizontalAxis;
        movementCache.y = verticalAxis;
        movementCache.nor();
    }

    public Direction getLastDirectionMoved(){
        return lastDirectionMoved;
    }
    public Vector2 getLastDirectionVec() { return lastDirectionCache; }

    private Direction readAttack() {
        if(!Gdx.input.isKeyPressed(ATTACK_KEY)) return Direction.NONE;

        if(Gdx.input.isKeyPressed(LEFT_KEY)){ return Direction.LEFT; }
        if(Gdx.input.isKeyPressed(RIGHT_KEY)){ return Direction.RIGHT; }
        if(Gdx.input.isKeyPressed(UP_KEY)){ return Direction.UP; }
        if(Gdx.input.isKeyPressed(DOWN_KEY)){ return Direction.DOWN; }

        return Direction.NONE;
    }
}