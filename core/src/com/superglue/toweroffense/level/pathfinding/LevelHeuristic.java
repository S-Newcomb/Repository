package com.superglue.toweroffense.level.pathfinding;

import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.math.Vector2;

public class LevelHeuristic implements Heuristic<LevelNode> {
    @Override
    public float estimate(LevelNode currentNode, LevelNode goalNode) {
        return Vector2.dst(currentNode.x, currentNode.y, goalNode.x, goalNode.y);
    }
}
