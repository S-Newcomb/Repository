package com.superglue.toweroffense.level.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.math.Vector2;

public class LevelNodeConnection implements Connection<LevelNode> {
    private LevelNode fromNode;
    private LevelNode toNode;
    private float cost;

    public LevelNodeConnection(LevelNode fromNode, LevelNode toNode){
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.cost = Vector2.dst(fromNode.x, fromNode.y, toNode.x, toNode.y);
    }

    @Override
    public float getCost() {
        return cost;
    }

    @Override
    public LevelNode getFromNode() {
        return fromNode;
    }

    @Override
    public LevelNode getToNode() {
        return toNode;
    }
}
