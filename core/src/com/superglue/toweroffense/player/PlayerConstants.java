package com.superglue.toweroffense.player;

import com.badlogic.gdx.math.Vector2;
import com.superglue.toweroffense.util.Constants;

public class PlayerConstants {
    public static final int DEFAULT_MAX_HEALTH = 100;
    public static final int DEFAULT_MOVEMENT_SPEED = 10;

    public static final int DEFAULT_CRUSH_POWER = 1;

    public static final int DEFAULT_PEOPLE_PER_CLICK = 10;

    public static final float DEFAULT_CROSSHAIR_SPEED = 20f;
    public static Vector2 DEFAULT_CROSSHAIR_POS = new Vector2(-1000, -1000);

    public static final float PLAYER_WIDTH = 1f;
    public static final float PLAYER_HEIGHT = 0.2f;

    // how long does it take for a dash attack to complete
    public static final float DEFAULT_DASH_TIME = .1f;
    public static final float DEFAULT_MAX_DASH_SPEED = DEFAULT_MOVEMENT_SPEED * 10f;

    // offset for player's non-attack hitbox, the regular hitbox idk what to call it.
    public static Vector2 REGULAR_HITBOX_OFFSET = new Vector2(0, 0);

    public static final float FALL_ANIMATION_CONSTANT_X = -100 * Constants.RES_SCALE_X;
    public static final float FALL_ANIMATION_CONSTANT_Y = 100 * Constants.RES_SCALE_Y;

    public static final float HORIZONTAL_ATTACK_HITBOX_WIDTH = 1f;
    public static final float HORIZONTAL_ATTACK_HITBOX_HEIGHT = 3.4f;

    public static Vector2 ATTACK_HITBOX_OFFSET_RIGHT = new Vector2(
            -HORIZONTAL_ATTACK_HITBOX_WIDTH, HORIZONTAL_ATTACK_HITBOX_HEIGHT
    );
    public static Vector2 ATTACK_HITBOX_OFFSET_LEFT = new Vector2(
            HORIZONTAL_ATTACK_HITBOX_WIDTH, HORIZONTAL_ATTACK_HITBOX_HEIGHT
    );
    // additional portion of hitbox that is always going to be there
    public static final float FEET_ATTACK_HITBOX_RADIUS = 1f;

    public static final float BOULDER_OFFSET_X = 1.6f * Constants.RES_SCALE_X;
    public static final float BOULDER_OFFSET_Y = 0f * Constants.RES_SCALE_Y;

    public static final float CROSSHAIR_OFFSET_X = 875 * Constants.RES_SCALE_X;
    public static final float CROSSHAIR_OFFSET_Y = 365 * Constants.RES_SCALE_Y;

    // Time it takes for the player to slam onto the ground in seconds
    public final static float SLAM_DOWN_TIME = 0.5f;
    // Time the player stays on the ground
    public final static float SLAM_STAY_TIME = SLAM_DOWN_TIME / 4f;
    // Time it takes for the player to get back up;
    public final static float SLAM_UP_TIME = 0.4f;

    public static float ANIMATION_FRAME_RATE = 15;

    public static final float DEFEAT_LABEL_OFFSET_X = -200 * Constants.RES_SCALE_X;
    public static final float VICTORY_LABEL_OFFSET_X = -200 * Constants.RES_SCALE_Y;
}
