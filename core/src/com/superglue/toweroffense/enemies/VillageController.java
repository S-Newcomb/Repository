package com.superglue.toweroffense.enemies;

import com.badlogic.gdx.ai.fma.patterns.DefensiveCircleFormationPattern;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.superglue.toweroffense.player.Player;
import com.superglue.toweroffense.util.Updatable;

import java.util.ArrayList;

public class VillageController implements Updatable {
    private EnemyController enemyController;
    private ArrayList<Village> villageList;
    private Player player;

    public VillageController(EnemyController ec, Player p){
        enemyController = ec;
        villageList = new ArrayList<Village>();
        player = p;
    }

    public Village createVillage(float x, float y, float width, float height, Village.EnemyType eType, float ew, float eh, Vector2 es, TextureRegion etr){
        assert(x != 0  && y != 0);
        Village v = new Village(x, y, width, height, eType, ew, eh, es, etr);
        villageList.add(v);
        return v;
    }

    public ArrayList<Village> getVillageList(){
        return villageList;
    }

    public void removeVillage(Village v){
        v.villageSound.stop();
        v.villageSound.dispose();
        villageList.remove(v);

    }

    @Override
    public void update(float dt) {
        for(Village v: villageList){
            if(v.getSoundStarted()){
                v.updateVillageSound(player);
            }
            else {
                v.playVillageSound(player);
            }
            if (v.canSpawn(dt) && enemyController != null){
                enemyController.addEnemy(v.addEnemy());
            }
        }
    }

}
