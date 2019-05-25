package com.superglue.toweroffense.factories;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.superglue.toweroffense.ResourceManager;
import com.superglue.toweroffense.WorldController;
import com.superglue.toweroffense.enemies.*;
import com.superglue.toweroffense.level.Background;
import com.superglue.toweroffense.levelEditor.ObjectType;
import com.superglue.toweroffense.levelEditor.PlayerSpawnMarker;
import com.superglue.toweroffense.obstacles.Rock;
import com.superglue.toweroffense.obstacles.Tree;
import com.superglue.toweroffense.obstacles.Water;
import com.superglue.toweroffense.obstacles.Water2;
import com.superglue.toweroffense.player.*;

import java.util.ArrayList;

/**
 * Functions in this class deal with creating level-specific objects.
 * A generic Enemy is not created in this class.
 *
 * Requires that you run init. Please run init. Does not error check.
 * Dangerous class...
 */
public final class LevelFactory {
    private static Vector2 scale;
    private static ResourceManager resourceManager;
    private static WorldController worldController;

    private static ArrayList<Object> walls;

    public static void init(WorldController worldController_, Vector2 scale_, ResourceManager resourceManager_){
        worldController = worldController_;
        if (scale_ != null)
            scale = scale_;
        resourceManager = resourceManager_;

        walls = new ArrayList<Object>();
    }

    private static float pixelsToMeters(float pixels) {
        return pixels / scale.x;
    }

    public static PlayerController addPlayer(float x, float y){
        float playerWidth = PlayerConstants.HORIZONTAL_ATTACK_HITBOX_WIDTH;
        float playerHeight = PlayerConstants.HORIZONTAL_ATTACK_HITBOX_HEIGHT;
        Player player =  new Player(x, y, playerWidth, playerHeight);
        player.setDrawScale(scale);
        player.setTexture(resourceManager.towerIdleSprite);
        worldController.addObject(player);

        // starts off negative so that it is placed off-screen
        Vector2 CROSSHAIR_START_POS = PlayerConstants.DEFAULT_CROSSHAIR_POS;

        final float CROSSHAIR_WIDTH = pixelsToMeters(resourceManager.crosshairTexture.getRegionWidth());
        final float CROSSHAIR_HEIGHT = pixelsToMeters(resourceManager.crosshairTexture.getRegionHeight());
        PlayerCrosshair playerCrosshair = new PlayerCrosshair(
                CROSSHAIR_START_POS.x, CROSSHAIR_START_POS.y,
                CROSSHAIR_WIDTH, CROSSHAIR_HEIGHT
        );
        playerCrosshair.setTexture(resourceManager.crosshairTexture);
        worldController.addObject(playerCrosshair);
        PlayerController playerController = new PlayerController(player, playerCrosshair);
        return playerController;
    }

    public static Enemy addEnemy(float x, float y, TextureRegion t, Village.EnemyType type){
        Enemy enemy = null;
        switch(type) {
            case MELEE:
                enemy = new MeleeEnemy(pixelsToMeters(x), pixelsToMeters(y), 1, 1);
                break;
            case SPEAR:
                enemy = new SpearEnemy(pixelsToMeters(x), pixelsToMeters(y), 1, 1);
                break;
            case DEFENDER:
                enemy = new DefenderEnemy(pixelsToMeters(x), pixelsToMeters(y), 1, 1, new Vector2(x, y));
                break;
        }
        enemy.setDrawScale(scale);
        enemy.setTexture(t);
        worldController.addObject(enemy);
        return enemy;
    }

    public static Rock addRock(float x, float y, TextureRegion t){
        Rock rock = new Rock(pixelsToMeters(x), pixelsToMeters(y),
                pixelsToMeters(t.getRegionWidth()),
                pixelsToMeters(t.getRegionHeight()));
        rock.setDrawScale(scale);
        rock.setTexture(t);
        worldController.addObject(rock);
        return rock;
    }

    public static PlayerSpawnMarker addPlayerSpawn(float x, float y, TextureRegion t){
        PlayerSpawnMarker marker = new PlayerSpawnMarker(pixelsToMeters(x), pixelsToMeters(y),
                pixelsToMeters(t.getRegionWidth()),
                pixelsToMeters(t.getRegionHeight()));
        marker.setDrawScale(scale);
        marker.setTexture(t);
        worldController.addObject(marker);
        return marker;
    }

    public static Tree addTree(float x, float y, TextureRegion t){
        Tree tree = new Tree(pixelsToMeters(x), pixelsToMeters(y), pixelsToMeters(t.getRegionWidth()),
                pixelsToMeters(t.getRegionHeight()));
        tree.setDrawScale(scale);
        tree.setTexture(t);
        worldController.addObject(tree);
        return tree;
    }

    public static Water addWater(float x, float y, TextureRegion t){
        Water water = new Water(pixelsToMeters(x), pixelsToMeters(y),
                pixelsToMeters(t.getRegionWidth()),
                pixelsToMeters(t.getRegionHeight()));
        water.setDrawScale(scale);
        water.setTexture(t);
        worldController.addObject(water);
        return water;
    }

    public static Water2 addWater2(float x, float y, TextureRegion t){
        Water2 water = new Water2(pixelsToMeters(x), pixelsToMeters(y),
                pixelsToMeters(t.getRegionWidth()),
                pixelsToMeters(t.getRegionHeight()));
        water.setDrawScale(scale);
        water.setTexture(t);
        worldController.addObject(water);
        return water;
    }

    public static Village addVillage(VillageController vc, ObjectType objectType, float x, float y, TextureRegion t){
        Village.EnemyType enemyType;
        TextureRegion tex;
        switch(objectType){
            case DEFENDER_VILLAGE:
                enemyType = Village.EnemyType.DEFENDER;
                tex = resourceManager.archerShootingAnimation;
                break;
            case SPEAR_VILLAGE:
                enemyType = Village.EnemyType.SPEAR;
                tex = resourceManager.spearWalkingAnimation;
                break;
            case MELEE_VILLAGE:
                enemyType = Village.EnemyType.MELEE;
                tex = resourceManager.swordWalkingAnimation;
                break;
            default:
                enemyType = Village.EnemyType.MELEE;
                tex = resourceManager.meleeTexture;
                break;
        }

        float x_ = pixelsToMeters(x);
        float y_ = pixelsToMeters(y);
        float ew = pixelsToMeters(tex.getRegionWidth());
        float eh = pixelsToMeters(tex.getRegionHeight());
        Village village = vc.createVillage(x_, y_, pixelsToMeters(t.getRegionWidth()),
                pixelsToMeters(t.getRegionHeight()), enemyType, ew, eh, scale, tex);
        village.setDrawScale(scale);
        village.setTexture(t);
        village.setHPBarTextures(resourceManager.HPBarTexture1, resourceManager.HPBarTexture2, resourceManager.HPBarTexture3);
        worldController.addObject(village);

        // draw the enemy in front of the village so that we remember what kind of village this is
//        Enemy enemy = null;
//        switch(enemyType){
//            case MELEE:
//                enemy = new MeleeEnemy(x_, y_,
//                        ew/scale.x, eh/scale.y);
//                enemy.setTexture(resourceManager.meleeTexture);
//                break;
//            case SPEAR:
//                enemy = new SpearEnemy(x_, y_,
//                        ew/scale.x, eh/scale.y);
//                enemy.setTexture(resourceManager.spearTexture);
//                break;
//            case DEFENDER:
//                enemy = new DefenderEnemy(x_, y_,
//                        ew/scale.x, eh/scale.y, new Vector2(x, y));
//                enemy.setTexture(resourceManager.defenderTexture);
//                break;
//            default:
//                enemy = new MeleeEnemy(x_, y_,
//                        ew/scale.x, eh/scale.y);
//                enemy.setTexture(resourceManager.meleeTexture);
//                break;
//        }

//        worldController.addObject(village);
//        //worldController.addObject(enemy);
//
//        // setup health bars
//        TextureRegion fullHealth = resourceManager.HPBarTexture1;
//        TextureRegion oneThirdHealth = resourceManager.HPBarTexture2;
//        TextureRegion twoThirdHealth = resourceManager.HPBarTexture3;
//        village.setHPBarTextures(fullHealth, oneThirdHealth, twoThirdHealth);
        return village;
    }

    public static Background addBackground(int w, int h){
        Background background = new Background(w, h);
        background.setTexture(resourceManager.hard_coded_background);
        worldController.addObject(background);
        return background;
    }

    public static void clearWalls(){
        for(Object o : walls){
            worldController.getObjects().remove(o);
        }
        walls.clear();
    }

    public static void buildWalls(int w, int h){
        clearWalls();
        //horizontal bottom
        for(int i = 0; i < w*12; i++) {
            Tree tree = new Tree(pixelsToMeters(30 + 54*i), pixelsToMeters(-40),
                    pixelsToMeters(resourceManager.treeTexture_1.getRegionWidth()), pixelsToMeters(resourceManager.treeTexture_1.getRegionHeight()));
            tree.setDrawScale(scale);
            tree.setTexture(resourceManager.treeTexture_1);
            worldController.addObject(tree);
            walls.add(tree);
        }
        //horizontal top
        for(int i = 0; i < w*12; i++) {
            Tree tree = new Tree(pixelsToMeters(30 + 54*i), pixelsToMeters(h*410),
                    pixelsToMeters(resourceManager.treeTexture_1.getRegionWidth()), pixelsToMeters(resourceManager.treeTexture_1.getRegionHeight()));
            tree.setDrawScale(scale);
            tree.setTexture(resourceManager.treeTexture_1);
            worldController.addObject(tree);
            walls.add(tree);
        }
        //vertical left
        for(int i = 0; i < h*6; i++) {
            Tree tree = new Tree(pixelsToMeters(30), pixelsToMeters(i*75),
                    pixelsToMeters(resourceManager.treeTexture_1.getRegionWidth()), pixelsToMeters(resourceManager.treeTexture_1.getRegionHeight()));
            tree.setDrawScale(scale);
            tree.setTexture(resourceManager.treeTexture_1);
            worldController.addObject(tree);
            walls.add(tree);
        }
        //vertical right
        for(int i = 0; i < h*6; i++) {
            Tree tree = new Tree(pixelsToMeters(w*650), pixelsToMeters(i*75),
                    pixelsToMeters(resourceManager.treeTexture_1.getRegionWidth()), pixelsToMeters(resourceManager.treeTexture_1.getRegionHeight()));
            tree.setDrawScale(scale);
            tree.setTexture(resourceManager.treeTexture_1);
            worldController.addObject(tree);
            walls.add(tree);
        }
    }
}
