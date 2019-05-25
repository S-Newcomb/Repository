package com.superglue.toweroffense.ai;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.superglue.toweroffense.util.AIUtils;

public class Position implements Location<Vector2> {

    Vector2 position;
    float orientation;

    public Position() {
        this.position = new Vector2();
        this.orientation = 0;
    }

    public Position(Vector2 position) {
        this.position = position;
        this.orientation = 0;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public float getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(float orientation) {
        this.orientation = orientation;
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return AIUtils.vectorToAngle(vector);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return AIUtils.angleToVector(outVector, angle);
    }

    @Override
    public Location<Vector2> newLocation() {
        return new Position();
    }
}
