package com.superglue.toweroffense.ai;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.steer.behaviors.*;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.math.Vector2;
import com.superglue.toweroffense.ai.SteeringAgent;
import com.superglue.toweroffense.level.pathfinding.LevelNode;

public class SteeringStates {
    public static Arrive<Vector2>getArrive(SteeringAgent runner, SteeringAgent target,
                                           float timeToTarget, float arrivalTolerance, float decelRadius){
        Arrive<Vector2> arrive = new Arrive<Vector2>(runner, target)
                .setTimeToTarget(timeToTarget)
                .setArrivalTolerance(arrivalTolerance)
                .setDecelerationRadius(decelRadius);
        return arrive;
    }

    public static Seek<Vector2> getSeek(SteeringAgent seeker, SteeringAgent target){
        Seek<Vector2> seek = new Seek<Vector2>(seeker,target);
        return seek;
    }

    public static Flee<Vector2> getFlee(SteeringAgent runner, SteeringAgent fleeingFrom){
        Flee<Vector2> seek = new Flee<Vector2>(runner,fleeingFrom);
        return seek;
    }

    public static Pursue<Vector2> getPursue(SteeringAgent pursuer, SteeringAgent target, float maxPredTime){
        Pursue<Vector2> pursue = new Pursue<>(pursuer, target, maxPredTime);
        return pursue;
    }

    public static FollowPath<Vector2, LinePath.LinePathParam> getFollowPath(SteeringAgent pursuer, LinePath<Vector2> path){
        final float PATH_OFFSET = 30;
        FollowPath<Vector2, LinePath.LinePathParam> followPath = new FollowPath<>(pursuer, path, PATH_OFFSET);
        return followPath;
    }
}
