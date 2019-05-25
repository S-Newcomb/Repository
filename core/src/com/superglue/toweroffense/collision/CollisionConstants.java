package com.superglue.toweroffense.collision;

public class CollisionConstants {
    public static final short LAYER_PLAYER = 0x0002; // we don't start at 0x0001 because libgdx defaults to it. 0b10
    public static final short LAYER_ENEMY = 0x0004; // 0b100
    public static final short LAYER_VILLAGE = 0x0008; // 0b1000 ... and so on
    public static final short LAYER_BOULDER = 0x0010;
    public static final short LAYER_OBSTACLE = 0x0020;
    public static final short LAYER_ATTACK = 0x0040; // for player attack hitboxes
    public static final short LAYER_PROJECTILE = 0x0080;
    public static final short LAYER_WATER = 0x0100;


    // player/objects can collide with everything.
    public static final short MASK_DEFAULT = ~0;
    // enemies should collide with everything but the player, villages, and enemies
    public static final short MASK_ENEMY = ~0 & ~LAYER_PLAYER & ~LAYER_VILLAGE; // & ~LAYER_ENEMY;
    // projectiles should not collide with enemies, themselves, or water;
    public static final short MASK_PROJECTILE = ~0 & ~LAYER_ENEMY & ~LAYER_PROJECTILE & ~LAYER_WATER;
    // water should collide with everything except projectiles;
    public static final short MASK_WATER = ~0 & ~LAYER_PROJECTILE;

}
