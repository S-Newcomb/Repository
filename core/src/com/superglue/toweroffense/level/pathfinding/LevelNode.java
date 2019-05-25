package com.superglue.toweroffense.level.pathfinding;

public class LevelNode {
    public int x;
    public int y;

    public int index;

    public boolean isOccupied;

    public LevelNode(int x, int y){
        this.x = x;
        this.y = y;
    }
}
