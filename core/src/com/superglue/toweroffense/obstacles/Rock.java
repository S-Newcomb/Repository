package com.superglue.toweroffense.obstacles;

import com.badlogic.gdx.graphics.Color;
import com.superglue.toweroffense.GameCanvas;
import com.superglue.toweroffense.util.Constants;

public class Rock extends Obstacle {
    private static final float DEFAULT_HEALTH = 5;
    private float health;

    private Color tint;
    private Color deadColor;

    public Rock(float x, float y, float width, float height) {
        super(x, y, width, height);

        health = DEFAULT_HEALTH;
        tint = Color.WHITE.cpy();
        deadColor = Color.RED.cpy();
    }

    public void addHealth(float amount) {
        health += amount;

        tint.lerp(deadColor, 1 - health / DEFAULT_HEALTH);
    }

    public boolean isDead() { return health <= 0; }

    @Override
    public void draw(GameCanvas canvas) {
        canvas.draw(
            texture,
            tint,
            texture.getRegionWidth()/2, 0,
            getX(), getY(), 0,
            drawScale.x/(50f * Constants.RES_SCALE_X),
            drawScale.y/(50f * Constants.RES_SCALE_Y)
        );
    }
}
