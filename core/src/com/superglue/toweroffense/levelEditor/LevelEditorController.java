package com.superglue.toweroffense.levelEditor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.superglue.toweroffense.GDXRoot;
import com.superglue.toweroffense.InputController;
import com.superglue.toweroffense.ResourceManager;
import com.superglue.toweroffense.WorldController;

import com.superglue.toweroffense.enemies.Enemy;
import com.superglue.toweroffense.enemies.Village;

import com.superglue.toweroffense.enemies.Village.EnemyType;
import com.superglue.toweroffense.enemies.VillageController;
import com.superglue.toweroffense.factories.LevelFactory;
import com.superglue.toweroffense.gameobjects.GameObject;
import com.superglue.toweroffense.level.Background;
import com.superglue.toweroffense.obstacles.Rock;
import com.superglue.toweroffense.obstacles.Tree;
import com.superglue.toweroffense.obstacles.Water;
import com.superglue.toweroffense.obstacles.Water2;
import com.superglue.toweroffense.player.PlayerConstants;
import com.superglue.toweroffense.util.Constants;
import com.superglue.toweroffense.util.SoundController;

import javax.annotation.Resource;
import javax.swing.*;
import java.nio.file.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

// TODO:
// controls do not work after loading and saving a file...
// camera movement
// Draw Villages
// Delete items
// GUI

public class LevelEditorController extends WorldController {
    private static String relativePath = "levelFiles";
    private static GDXRoot parent;
    private String currentFile = "testJSON.json"; // the full relative path of the file (i.e. levelFiles/testJSON.json)
    private JLevel level;       // the current JLevel object that we are creating/editing

    private static Scanner scanner;            // temporary console use
    // GameObject currentObject;
    private static OrthographicCamera camera;

    // Current object selected to place in level editor
    private ObjectType currentObject;
    private int currentObjectIndex;
    // GameObject to draw for preview before placing
    private GameObject objectPreview;
    // Last GameObject placed
    private GameObject lastObjectPlaced;
    // Current player spawn marker
    private GameObject currentPlayerSpawn;

    private ArrayList<GameObject> placedObjects;

    private static Background background;

    private static VillageController vc_dummy = new VillageController(null, null);

    private InputController inputController;
    float mouseX = 0, mouseY = 0, cameraDelta;

    /**
     * This constructor is for when we are creating Levels
     * @throws IOException
     */
    public LevelEditorController(GDXRoot parent) throws IOException {
        // create a new JLevel object and initialize all its lists
        this.parent = parent;

        placedObjects = new ArrayList<GameObject>();
        level = new JLevel();
        level.jvillages = new ArrayList<>();
        level.rockPositions = new ArrayList<>();
        level.treePositions = new ArrayList<>();
        level.waterPositions = new ArrayList<>();
        level.enemyPositions = new ArrayList<>();
        level.activeRooms = new ArrayList<>(Arrays.asList(0,0,0,0));

        scanner = new Scanner(System.in);
        LevelFactory.init(this, scale, parent.resourceManager);

        // initialize current object to place
        currentObjectIndex = 0;
        currentObject = ObjectType.values()[currentObjectIndex];

        inputController = InputController.getInstance();

        world.setGravity(new Vector2(0, 0));

        cameraDelta = 0;
    }

    //   /**
//     * This constructor is for when we are editing a saved Level
//     * @param filename
//     * @throws IOException
//     */
//    public LevelEditorController(GDXRoot parent, String filename) throws IOException {
//        this.parent = parent;
//        LevelFactory.init(this, scale, parent.resourceManager);
//        currentFile = relativePath + filename;
//        try {
//            // try to load the level from the file
//            level = loadLevel(this);
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//            System.out.println("Failed to load" + currentFile);
//        }
//    }

    /** Reset this WorldController.
     *
     * Called when this WorldController is first created
     */
    public void reset() {
        SoundController.getInstance().getMusic("MenuTheme").stop();
        SoundController.getInstance().getMusic("MenuTheme").play();
        populateLevel(this.level);
        objectPreview = createObject();
    }

//    public void draw(float dt){
//        super.draw(dt);
//        if (currentObject != null) {
//            int x = Gdx.input.getX();
//            int y = -Gdx.input.getY();
//            parent.canvas.begin();
//            currentObject.setPosition(x, y);
//            currentObject.draw(parent.canvas);
//            parent.canvas.end();
//            canvas.draw(currentObject, Color.WHITE,
//                    currentObject.getRegionWidth()/2, 0, getX(), getY(), 0, drawScale.x/50f, drawScale.y/50f);
//        }else{
//            currentObject = LevelFactory.addRock(Gdx.input.getX(), Gdx.input.getY());
//        }
//    }
    /** Create the GameObject corresponding to ObjectType value of currentObject and add it to this WorldController
     *
     * @return the GameObject created based off of currentObject's value
     */
    private GameObject createObject() {
        GameObject object;

        float x = mouseX;
        float y = mouseY;

        switch(currentObject){
            case MELEE_ENEMY:
                object = LevelFactory.addEnemy(x, y, ResourceManager.meleeTexture, EnemyType.MELEE);
                break;
            case SPEAR_ENEMY:
                object = LevelFactory.addEnemy(x, y, ResourceManager.spearTexture, EnemyType.SPEAR);
                break;
            case ARCHER_ENEMY:
                object = LevelFactory.addEnemy(x, y, ResourceManager.defenderTexture, EnemyType.DEFENDER);
                break;
            case PLAYER:
                object = LevelFactory.addPlayerSpawn(x, y, ResourceManager.playerSpawnTexture);
                break;
            case DEFENDER_VILLAGE:
                object = LevelFactory.addVillage(vc_dummy, ObjectType.DEFENDER_VILLAGE, x, y, ResourceManager.blueVillageTexture);
                break;
            case MELEE_VILLAGE:
                object = LevelFactory.addVillage(vc_dummy, ObjectType.MELEE_VILLAGE, x, y, ResourceManager.redVillageTexture);
                break;
            case SPEAR_VILLAGE:
                object = LevelFactory.addVillage(vc_dummy, ObjectType.SPEAR_VILLAGE, x, y, ResourceManager.villageTexture);
                break;
            case TREE_1:
                object = LevelFactory.addTree(x, y, ResourceManager.treeTexture_1);
                break;
            case TREE_2:
                object = LevelFactory.addTree(x, y, ResourceManager.treeTexture_2);
                break;
            case TREE_3:
                object = LevelFactory.addTree(x, y, ResourceManager.treeTexture_3);
                break;
            case TREE_4:
                object = LevelFactory.addTree(x, y, ResourceManager.treeTexture_4);
                break;
            case ROCK_1:
                object = LevelFactory.addRock(x, y, ResourceManager.rockTexture_1);
                break;
            case ROCK_2:
                object = LevelFactory.addRock(x, y, ResourceManager.rockTexture_2);
                break;
            case ROCK_3:
                object = LevelFactory.addRock(x, y, ResourceManager.rockTexture_3);
                break;
            case ROCK_4:
                object = LevelFactory.addRock(x, y, ResourceManager.rockTexture_4);
                break;
            case WATER_1:
                object = LevelFactory.addWater(x, y, ResourceManager.water1);
                break;
            case WATER_2:
                object = LevelFactory.addWater2(x, y, ResourceManager.water1);
                break;
            default:
                object = LevelFactory.addRock(x, y, ResourceManager.rockTexture_1);
                break;
        }

        return object;
    }

    /** Add the coordinates of GameObject corresponding to ObjectType value of currentObject to list of objects
     * maintained by level so it will appear in the saved level
     *
     * @param x x-coordinate to save object at
     * @param y y-coordinate to save object at
     */
    private void placeObject(float x, float y) {
        switch(currentObject){
            case PLAYER:
                level.playerPos = new Vector2(x / 50, y / 50);
                break;
            case MELEE_ENEMY:
                level.enemyPositions.add(new PlacedObject(new Vector2(x, y), ResourceManager.MELEE_TEXTURE));
                break;
            case SPEAR_ENEMY:
                level.enemyPositions.add(new PlacedObject(new Vector2(x, y), ResourceManager.SPEAR_TEXTURE));
                break;
            case ARCHER_ENEMY:
                level.enemyPositions.add(new PlacedObject(new Vector2(x, y), ResourceManager.DEFENDER_TEXTURE));
                break;
            case DEFENDER_VILLAGE:
                level.jvillages.add(new JVillage(ObjectType.DEFENDER_VILLAGE, new Vector2(x, y), ResourceManager.BLUE_VILLAGE_TEXTURE));
                break;
            case MELEE_VILLAGE:
                level.jvillages.add(new JVillage(ObjectType.MELEE_VILLAGE, new Vector2(x, y), ResourceManager.RED_VILLAGE_TEXTURE));
                break;
            case SPEAR_VILLAGE:
                level.jvillages.add(new JVillage(ObjectType.SPEAR_VILLAGE, new Vector2(x, y), ResourceManager.VILLAGE_TEXTURE));
                break;
            case TREE_1:
                level.treePositions.add(new PlacedObject(new Vector2(x, y), ResourceManager.TREE_TEXTURE_1));
                break;
            case TREE_2:
                level.treePositions.add(new PlacedObject(new Vector2(x, y), ResourceManager.TREE_TEXTURE_2));
                break;
            case TREE_3:
                level.treePositions.add(new PlacedObject(new Vector2(x, y), ResourceManager.TREE_TEXTURE_3));
                break;
            case TREE_4:
                level.treePositions.add(new PlacedObject(new Vector2(x, y), ResourceManager.TREE_TEXTURE_4));
                break;
            case ROCK_1:
                level.rockPositions.add(new PlacedObject(new Vector2(x, y), ResourceManager.ROCK_TEXTURE_1));
                break;
            case ROCK_2:
                level.rockPositions.add(new PlacedObject(new Vector2(x, y), ResourceManager.ROCK_TEXTURE_2));
                break;
            case ROCK_3:
                level.rockPositions.add(new PlacedObject(new Vector2(x, y), ResourceManager.ROCK_TEXTURE_3));
                break;
            case ROCK_4:
                level.rockPositions.add(new PlacedObject(new Vector2(x, y), ResourceManager.ROCK_TEXTURE_4));
                break;
            case WATER_1:
                level.waterPositions.add(new PlacedObject(new Vector2(x, y), ResourceManager.WATER_1));
                break;
            case WATER_2:
                PlacedObject obj = new PlacedObject(new Vector2(x, y), ResourceManager.WATER_1);
                obj.alt = true;
                level.waterPositions.add(obj);
                break;
        }
    }

    /** Draw the currentObject selected to place. */
    public void draw(float dt) {

//        float x = camera.position.x/50.0f;
//        float y = camera.position.y/50.0f;

        super.draw(dt);
//        if (currentObject == ObjectType.PLAYER) {
//            parent.canvas.begin();
//            objectPreview.draw(parent.canvas);
//            parent.canvas.end();
//        }
        canvas.editorClearHud();
        canvas.drawHud();
        objectPreview.setPosition(mouseX/(50f * Constants.RES_SCALE_X), mouseY/(50f * Constants.RES_SCALE_Y));
    }

    /** Update this WorldController every frame and handle input. */
    public void update(float dt) {
        float offset_x = (inputController.getMousePos().x - PlayerConstants.CROSSHAIR_OFFSET_X);
        float offset_y = (inputController.getMousePos().y - PlayerConstants.CROSSHAIR_OFFSET_Y);
        mouseX = (camera.position.x + offset_x);
        mouseY = (camera.position.y - offset_y);

        int CAMERA_SPEED = 20;
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
            CAMERA_SPEED /= 3;
        }
        // Zoom camera out
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)){
            camera.zoom += 0.25;
            cameraDelta -= 0.25;
        }
        // Zoom camera in
        if (Gdx.input.isKeyJustPressed(Input.Keys.C)){
            camera.zoom -= 0.25;
            cameraDelta += 0.25;
        }
        // Reset camera zoom
        if (Gdx.input.isKeyJustPressed(Input.Keys.V)){
            camera.zoom += cameraDelta;
            cameraDelta = 0;
        }
//        if (Gdx.input.isKeyJustPressed(Input.Keys.Y)){
//            prompt();
//        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)){
            saveLevel(level);
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.O)){
            this.level = loadLevel(this);
            populateLevel(this.level);
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.I)){
            openLevel();
        }
        else {
            if (Gdx.input.isKeyPressed(Input.Keys.W)){
                camera.position.y += CAMERA_SPEED;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)){
                camera.position.y -= CAMERA_SPEED;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)){
                camera.position.x -= CAMERA_SPEED;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                camera.position.x += CAMERA_SPEED;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
            currentObjectIndex = (currentObjectIndex+1) % ObjectType.values().length;
            currentObject = ObjectType.values()[currentObjectIndex];
            objectPreview.setActive(false);
            //objectPreview.getBody().setActive(false);
            objects.remove(objectPreview);
            objectPreview = createObject();
            lastObjectPlaced = null;
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
            currentObjectIndex = currentObjectIndex == 0 ? ObjectType.values().length-1 : currentObjectIndex-1;
            currentObject = ObjectType.values()[currentObjectIndex];
            objectPreview.setActive(false);
            //objectPreview.getBody().setActive(false);
            objects.remove(objectPreview);
            objectPreview = createObject();
            lastObjectPlaced = null;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            if (lastObjectPlaced == null) {
                lastObjectPlaced = createObject();
                handleSpawnPlacement(lastObjectPlaced);
                placeObject(mouseX, mouseY);

                placedObjects.add(lastObjectPlaced);
            }
            else if (lastObjectPlaced.getPosition().dst(objectPreview.getPosition()) >= getSpacing()){
                lastObjectPlaced = createObject();
                handleSpawnPlacement(lastObjectPlaced);
                placeObject(mouseX, mouseY);

                placedObjects.add(lastObjectPlaced);
            }
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            if(placedObjects.size() > 0) {
                GameObject o = placedObjects.remove(placedObjects.size() - 1);
                objects.remove(o);

                if(o.getBody().getUserData().getClass().equals(Tree.class)) level.treePositions.remove(level.treePositions.size() - 1);
                if(o.getBody().getUserData().getClass().equals(Rock.class)) level.rockPositions.remove(level.rockPositions.size() - 1);
                if(o.getBody().getUserData().getClass().equals(Water.class) || o.getBody().getUserData().getClass().equals(Water2.class)){
                    level.waterPositions.remove(level.waterPositions.size() - 1);
                }
                if(o.getBody().getUserData().getClass().equals(Village.class)) level.jvillages.remove(level.jvillages.size() - 1);
                if(o.getBody().getUserData() instanceof Enemy) level.enemyPositions.remove(level.enemyPositions.size() - 1);
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
            background.setWidthFactor(background.getWidthFactor() + 1);
            level.backgroundWidth++;
            LevelFactory.buildWalls(level.backgroundWidth, level.backgroundHeight);
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)){
            if(background.getWidthFactor() > 1) {
                background.setWidthFactor(background.getWidthFactor() - 1);
                level.backgroundWidth--;
                LevelFactory.buildWalls(level.backgroundWidth, level.backgroundHeight);
            }
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
            background.setHeightFactor(background.getHeightFactor() + 1);
            level.backgroundHeight++;
            LevelFactory.buildWalls(level.backgroundWidth, level.backgroundHeight);
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)){
            if(background.getHeightFactor() > 1) {
                background.setHeightFactor(background.getHeightFactor() - 1);
                level.backgroundHeight--;
                LevelFactory.buildWalls(level.backgroundWidth, level.backgroundHeight);
            }
        }

        //Position of the mouse on the SCREEN
        Vector2 mousePos = InputController.getInstance().getMousePos();
        if (Gdx.input.justTouched()){
            if (canvas.inRoom1Region(mousePos.x, mousePos.y)){
                if (level.activeRooms.get(0) == 0) {
                    level.activeRooms.set(0, 1);
                    System.out.println("Engine Room Active");
                    canvas.updateHud(0, 0, 0, 1, level.activeRooms.get(2), level.activeRooms.get(3), 0);
                }
                else {
                    level.activeRooms.set(0, 0);
                    System.out.println("Engine Room Inactive");
                    canvas.updateHud(0, 0, 0, 0, level.activeRooms.get(2), level.activeRooms.get(3), 0);
                }
            }
            if (canvas.inRoom2Region(mousePos.x, mousePos.y)) {
                if (level.activeRooms.get(1) == 0) {
                    level.activeRooms.set(1, 1);
                    System.out.println("Sacrifice Room Active");
                }
                else {
                    level.activeRooms.set(1, 0);
                    System.out.println("Sacrifice Room Inactive");
                }
            }
            if (canvas.inRoom3Region(mousePos.x, mousePos.y)) {
                if (level.activeRooms.get(2) == 0) {
                    level.activeRooms.set(2, 1);
                    System.out.println("Life Steal Room Active");
                    canvas.updateHud(0, 0, 0, level.activeRooms.get(0), 1, level.activeRooms.get(3), 0);
                }
                else{
                    level.activeRooms.set(2, 0);
                    System.out.println("Life Steal Room Inactive");
                    canvas.updateHud(0, 0, 0, level.activeRooms.get(0), 0, level.activeRooms.get(3), 0);
                }
            }
            if (canvas.inRoom4Region(mousePos.x, mousePos.y)) {
                if (level.activeRooms.get(3) == 0) {
                    level.activeRooms.set(3, 1);
                    System.out.println("Toughness Room Active");
                    canvas.updateHud(0, 0, 0, level.activeRooms.get(0), level.activeRooms.get(2), 1, 0);
                }
                else {
                    level.activeRooms.set(3, 0);
                    System.out.println("Toughness Room Inactive");
                    canvas.updateHud(0, 0, 0, level.activeRooms.get(0), level.activeRooms.get(2), 0, 0);
                }
            }
        }
    }

    private void handleSpawnPlacement(GameObject object) {
        if (currentObject == ObjectType.PLAYER) {
            if (currentPlayerSpawn != null) {
                currentPlayerSpawn.setActive(false);
                objects.remove(currentPlayerSpawn);
                currentPlayerSpawn = object;
            } else {
                currentPlayerSpawn = object;
            }
        }
    }

    /** Get the spacing between adjacent objects when space is held down for the GameObject corresponding to
     *  CurrentObject.
     *
     * @return the float value for spacing between adjacent objects for CurrentObject
     **/
    private float getSpacing() {
        float spacing = 1.0f;
        float scale = (50f * Constants.RES_SCALE_X);

        switch(currentObject){
            case PLAYER:
                // can't place multiple players so do nothing here
                break;
            case ARCHER_ENEMY:
                spacing = 1.0f;
                break;
            case MELEE_ENEMY:
                spacing = 1.0f;
                break;
            case SPEAR_ENEMY:
                spacing = 1.0f;
                break;
            case DEFENDER_VILLAGE:
                //spacing = parent.resourceManager.villageTexture.getRegionWidth()/scale;
                spacing = 3.5f;
                break;
            case MELEE_VILLAGE:
                //spacing = parent.resourceManager.villageTexture.getRegionWidth()/scale;
                spacing = 3.5f;
                break;
            case SPEAR_VILLAGE:
                //spacing = parent.resourceManager.villageTexture.getRegionWidth()/scale;
                spacing = 3.5f;
                break;
            case TREE_1:
                //spacing = parent.resourceManager.treeTexture.getRegionWidth()/scale;
                spacing = 2.6f;
                break;
            case TREE_2:
                spacing = 2.6f;
                break;
            case TREE_3:
                spacing = 1.6f;
                break;
            case TREE_4:
                spacing = 1.7f;
                break;
            case ROCK_1:
                //spacing = parent.resourceManager.rockTexture.getRegionWidth()/scale;
                spacing = 1.1f;
                break;
            case ROCK_2:
                spacing = 0.8f;
                break;
            case ROCK_3:
                spacing = 0.8f;
                break;
            case ROCK_4:
                spacing = 1.4f;
                break;
            default:
                spacing = 1.0f;
                break;
        }

        return spacing;
    }

    public static void populateLevel(JLevel level) {
        background = LevelFactory.addBackground(level.backgroundWidth, level.backgroundHeight);
        LevelFactory.buildWalls(level.backgroundWidth, level.backgroundHeight);
        if (level != null){
            if (level.playerPos != null) {
                LevelFactory.addPlayer(level.playerPos.x, level.playerPos.y);
            }
            for (PlacedObject obj : level.enemyPositions){
                LevelFactory.addEnemy(obj.position.x, obj.position.y, getTexture(obj.texture), getEnemyType(obj.texture));
            }
            for (PlacedObject obj : level.rockPositions){
                LevelFactory.addRock(obj.position.x, obj.position.y, getTexture(obj.texture));
            }
            for(PlacedObject obj : level.treePositions){
                LevelFactory.addTree(obj.position.x, obj.position.y, getTexture(obj.texture));
            }
            for(PlacedObject obj : level.waterPositions){
                if (!obj.alt){
                    LevelFactory.addWater(obj.position.x, obj.position.y, LevelEditorController.getTexture(obj.texture));
                }else{
                    LevelFactory.addWater2(obj.position.x, obj.position.y, LevelEditorController.getTexture(obj.texture));
                }
            }
            for(JVillage village : level.jvillages){
                LevelFactory.addVillage(vc_dummy, village.objectType, village.position.x, village.position.y, getTexture(village.texture));
            }
        }
        camera = parent.canvas.getCamera();
        parent.canvas.createEditorHud(0,0,0,0,0,0,0, null,null,null,null);
    }

    /**
     * Static method that returns a JLevel from the file specified by filename
     *
     * @param worldController - the worldController that we should load this level on
     * @return JLevel loaded from filename or null
     */
    public static JLevel loadLevel (WorldController worldController){
        JFrame parentFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser(new File("levelFiles"));
        fileChooser.setDialogTitle("Open File");
        int userSelection = fileChooser.showOpenDialog(parentFrame);
        File openFile;
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            openFile = fileChooser.getSelectedFile();
            try {
                String data = new String(Files.readAllBytes(openFile.toPath()));
                Gson gson = new Gson();
                // parse the string into a level
                JLevel level = gson.fromJson(data, JLevel.class);
                return level;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Failed to load level: " + openFile.toString());
            }
        }
        return null;
    }

    public static void openLevel(){
        LevelFactory.init(parent.getLevelMode(), null, parent.resourceManager);
        JLevel level = loadLevel(parent.getLevelMode());
        scanner.close();
        parent.exitToLevel(level);
    }

    /**
     * Save the level under the current filename
     *
     * @param level the JLevel object that was created
     */
    private void saveLevel(JLevel level) {
        JFrame parentFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser(new File("levelFiles"));
        fileChooser.setDialogTitle("Save file as");
        int userSelection = fileChooser.showSaveDialog(parentFrame);
        File saveFile;
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            saveFile = fileChooser.getSelectedFile();
            try (Writer writer = new FileWriter(saveFile)) {
                Gson gson = new GsonBuilder().create();
                gson.toJson(level, writer);
                writer.flush();
                writer.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Failed to write level to file:" + saveFile.toString());
            }
        }
    }

//    private void prompt(){
//        // ask listener for whether we have placed an object
//
//        // temporary method to see if stuff works
//        System.out.print("What would you like to add? [ROCK, TREE, VILLAGE]: ");
//        String inputString = scanner.nextLine();
//        int x, y;
//        switch(inputString)
//        {
//            case "PLAYER":
//                System.out.print("Enter x position: ");
//                x = scanner.nextInt();
//                scanner.nextLine();
//                System.out.print("Enter y position: ");
//                y = scanner.nextInt();
//                scanner.nextLine();
//                level.playerPos = LevelFactory.addPlayer(x, y).getPlayer().getPosition();
//                break;
//            case "ROCK":
//                System.out.print("Enter x position: ");
//                x = scanner.nextInt();
//                scanner.nextLine();
//                System.out.print("Enter y position: ");
//                y = scanner.nextInt();
//                scanner.nextLine();
//                // add rock to level
//                level.rockPositions.add(new Vector2(x, y));
//                LevelFactory.addRock(x, y);
//                break;
//            case "TREE":
//                System.out.print("Enter x position: ");
//                x = scanner.nextInt();
//                scanner.nextLine();
//                System.out.print("Enter y position: ");
//                y = scanner.nextInt();
//                scanner.nextLine();
//                // add tree to level
//                level.treePositions.add(new Vector2(x, y));
//                LevelFactory.addTree(x,y);
//                break;
//            case "VILLAGE":
//                System.out.print("Enter enemy type [MELEE, DEFENDER, SPEAR]: ");
//                String enemyType = scanner.nextLine();
//                System.out.print("Enter x position: ");
//                x = scanner.nextInt();
//                scanner.nextLine();
//                System.out.print("Enter y position: ");
//                y = scanner.nextInt();
//                scanner.nextLine();
//                JVillage myvillage = new JVillage(null, null);
//                myvillage.position = new Vector2(x, y);
//                switch (enemyType)
//                {
//                    case "MELEE":
//                        myvillage.objectType = ObjectType.MELEE_VILLAGE;
//                        break;
//
//                    case "DEFENDER":
//                        myvillage.objectType = ObjectType.DEFENDER_VILLAGE;
//                        break;
//
//                    case "SPEAR":
//                        myvillage.objectType = ObjectType.SPEAR_VILLAGE;
//                        break;
//                    default:
//                        System.out.println("I couldn't understand what you said");
//                        System.out.println("Enter MELEE, DEFENDER, or SPEAR");
//                }
//
//                // add village to level
//                level.jvillages.add(myvillage);
//                LevelFactory.addVillage(vc_dummy, myvillage.objectType, x, y);
//                break;
//            default:
//                System.out.println("I couldn't understand what you said");
//                System.out.println("Input PLAYER, ROCK, TREE, or VILLAGE");
//                break;
//        }
//   }

    /**
     * The JSON object needed to create a single village
     * Note - this is nested inside JLevel and not saved to a files
     */
    public class JVillage{
        public ObjectType objectType;        // the enemy type that this village should spawn
        public Vector2 position;           // where we should place this village
        public String texture;

        public JVillage(ObjectType objectType, Vector2 position, String tex){
            this.objectType = objectType;
            this.position = position;
            this.texture = tex;
        }
    }

    public class PlacedObject{
        public Vector2 position;
        public String texture;
        // true if alternate version/texture of this object should be used, false otherwise
        public boolean alt;

        PlacedObject(Vector2 pos, String tex){
            position = pos;
            texture = tex;
            alt = false;
        }
    }

    public static EnemyType getEnemyType(String texture){
        EnemyType type = null;
        switch(texture) {
            case ResourceManager.MELEE_TEXTURE:
                type = EnemyType.MELEE;
                break;
            case ResourceManager.SPEAR_TEXTURE:
                type = EnemyType.SPEAR;
                break;
            case ResourceManager.DEFENDER_TEXTURE:
                type = EnemyType.DEFENDER;
                break;
        }
        return type;
    }

    public static TextureRegion getTexture(String texture){
        TextureRegion tex;
        switch(texture){
            case ResourceManager.ROCK_TEXTURE_1:
                tex = ResourceManager.rockTexture_1;
                break;
            case ResourceManager.ROCK_TEXTURE_2:
                tex = ResourceManager.rockTexture_2;
                break;
            case ResourceManager.ROCK_TEXTURE_3:
                tex = ResourceManager.rockTexture_3;
                break;
            case ResourceManager.ROCK_TEXTURE_4:
                tex = ResourceManager.rockTexture_4;
                break;
            case ResourceManager.TREE_TEXTURE_1:
                tex = ResourceManager.treeTexture_1;
                break;
            case ResourceManager.TREE_TEXTURE_2:
                tex = ResourceManager.treeTexture_2;
                break;
            case ResourceManager.TREE_TEXTURE_3:
                tex = ResourceManager.treeTexture_3;
                break;
            case ResourceManager.TREE_TEXTURE_4:
                tex = ResourceManager.treeTexture_4;
                break;
            case ResourceManager.WATER_1:
                tex = ResourceManager.water1;
                break;
            case ResourceManager.MELEE_TEXTURE:
                tex = ResourceManager.swordWalkingAnimation;
                break;
            case ResourceManager.SPEAR_TEXTURE:
                tex = ResourceManager.spearWalkingAnimation;
                break;
            case ResourceManager.DEFENDER_TEXTURE:
                tex = ResourceManager.archerShootingAnimation;
                break;
            case ResourceManager.RED_VILLAGE_TEXTURE:
                tex = ResourceManager.redVillageTexture;
                break;
            case ResourceManager.BLUE_VILLAGE_TEXTURE:
                tex = ResourceManager.blueVillageTexture;
                break;
            case ResourceManager.VILLAGE_TEXTURE:
                tex = ResourceManager.villageTexture;
                break;
            default:
                tex = ResourceManager.rockTexture_1;
        }

        return tex;
    }

    /**
     * The JSON Level object
     */
    public class JLevel{
        public Vector2 playerPos;

        public ArrayList<PlacedObject> rockPositions;    // all the locations where we should put rocks
        public ArrayList<PlacedObject> treePositions;
        public ArrayList<PlacedObject> waterPositions;
        public ArrayList<PlacedObject> enemyPositions;
        public ArrayList<JVillage> jvillages;
        public ArrayList<Integer> activeRooms;

        public int backgroundWidth = 1, backgroundHeight = 1;
    }
}
