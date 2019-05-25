package com.superglue.toweroffense.gameobjects;

import com.superglue.toweroffense.level.Background;
import com.superglue.toweroffense.level.CheatBackground;
import com.superglue.toweroffense.obstacles.Water;
import com.superglue.toweroffense.player.PlayerCrosshair;

import java.util.Comparator;

public class GameObjectComparator implements Comparator<GameObject> {
    private static GameObjectComparator instance;
    public static GameObjectComparator getInstance(){
        if(instance == null) instance = new GameObjectComparator();
        return instance;
    }

    @Override
    public int compare(GameObject o1, GameObject o2) {
        // background should always be behind everything else
        if(o1 instanceof Background || o1 instanceof CheatBackground) {
            return -1;
        }
        if(o2 instanceof Background || o2 instanceof CheatBackground){
            return 1;
        }
        if(o1 instanceof Water){
            return -1;
        }
        if(o2 instanceof Water){
            return 1;
        }

        // crosshair should always be in front of everything else.
        if(o1 instanceof PlayerCrosshair){
            return 1;
        }
        if(o2 instanceof PlayerCrosshair){
            return -1;
        }

        return -Float.compare(o1.getY(), o2.getY());
    }
}
