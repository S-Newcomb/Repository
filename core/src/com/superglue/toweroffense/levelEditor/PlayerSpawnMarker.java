package com.superglue.toweroffense.levelEditor;

import com.superglue.toweroffense.obstacles.Obstacle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class PlayerSpawnMarker extends Obstacle {
    public PlayerSpawnMarker(float x, float y, float width, float height){
        super(x, y, width, height);
    }
}

