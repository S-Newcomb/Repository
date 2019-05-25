package com.superglue.toweroffense.ai;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.superglue.toweroffense.ai.Position;
import com.superglue.toweroffense.util.AIUtils;
import com.superglue.toweroffense.util.Updatable;

public class SteeringAgent implements Steerable<Vector2>, Updatable {

    private static final SteeringAcceleration<Vector2> steeringOutput =
            new SteeringAcceleration<Vector2>(new Vector2());

    protected SteeringBehavior<Vector2> steeringBehavior;

    Body body;
    boolean isTagged;
    boolean isIndependentFacing;    // defines whether entity can move in a direction that it is not facing

    float boundingRadius;           // the radius of the smallest circle required to cover the entire object
    float maxLinearSpeed;           // max speed the entity can go in
    float maxLinearAccel;
    float maxAngularSpeed;
    float maxAngularAccel;
    float zeroLinSpeedThresh = 0.001f; // 0.01f means entity must be within 0.01f of target location - overshooting and undershooting behaviors
    public Object oldUserData;

    public SteeringAgent(Body body, boolean isIndependentFacing) {
        this.body                   = body;
        this.isIndependentFacing    = isIndependentFacing;
        this.isTagged               = false;
        //assert (body != null);
        //body.setUserData(this);
    }

    public void setOldUserData(Object oldUserData) {
        this.oldUserData = oldUserData;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body){
        this.body = body;
    }

    public void setIndependentFacing(boolean independentFacing) {
        this.isIndependentFacing = independentFacing;
    }

    public boolean isIndependentFacing() {
        return isIndependentFacing;
    }

    @Override
    public Vector2 getLinearVelocity() {
        return body.getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        return body.getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isTagged() {
        return isTagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.isTagged = tagged;
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return zeroLinSpeedThresh;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {
        zeroLinSpeedThresh = value;
    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAccel;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAccel = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return this.maxAngularAccel;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAccel = maxAngularAcceleration;
    }

    @Override
    public Vector2 getPosition() {
        return body.getPosition();
    }

    @Override
    public float getOrientation() {
        return body.getAngle();
    }

    @Override
    public void setOrientation(float orientation) {
        body.setTransform(getPosition(), orientation);
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return AIUtils.vectorToAngle(vector);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return AIUtils.angleToVector(outVector,angle);
    }

    @Override
    public Location<Vector2> newLocation() {
        return new Position();
    }

    public SteeringBehavior<Vector2> getSteeringBehavior(){
        return steeringBehavior;
    }

    public void setSteeringBehavior(SteeringBehavior<Vector2> steeringBehavior){
        this.steeringBehavior = steeringBehavior;
    }

    public void update(float dt) {
        if (steeringBehavior != null) {
            // Calculate steering acceleration\
            steeringBehavior.calculateSteering(steeringOutput);
            applySteering(steeringOutput, dt);
        }
    }

    private void applySteering(SteeringAcceleration<Vector2> steering, float dt){
        boolean anyAccelerations = false;

        // Update position and linear velocity.
        if (!steeringOutput.linear.isZero()) {
            // dt is already scaled
            body.applyForceToCenter(steeringOutput.linear, true);
            anyAccelerations = true;
        }

        // Update orientation and angular velocity
        if (isIndependentFacing()) {
            if (steeringOutput.angular != 0) {
                body.applyTorque(steeringOutput.angular, true);
                anyAccelerations = true;
            }
        } else {
            // Don't do anything if no linear velocity
            Vector2 linVel = getLinearVelocity();
            if (!linVel.isZero(getZeroLinearSpeedThreshold())) {
                float newOrientation = vectorToAngle(linVel);
                body.setAngularVelocity((newOrientation - getAngularVelocity()) * dt);
                body.setTransform(body.getPosition(), newOrientation);
            }
        }

        if (anyAccelerations) {
            // Cap the linear speed
            Vector2 velocity = body.getLinearVelocity();
            float currentSpeedSquare = velocity.len2();
            float maxLinearSpeed = getMaxLinearSpeed();
            if (currentSpeedSquare > (maxLinearSpeed * maxLinearSpeed)) {
                body.setLinearVelocity(velocity.scl(maxLinearSpeed / (float)Math.sqrt(currentSpeedSquare)));
            }
            // Cap the angular speed
            float maxAngVelocity = getMaxAngularSpeed();
            if (body.getAngularVelocity() > maxAngVelocity) {
                body.setAngularVelocity(maxAngVelocity);
            }
        }
    }

}
