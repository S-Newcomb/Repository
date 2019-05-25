/*
 * GDXRoot.java
 *
 * This is the primary class file for running the game.  It is the "static main" of
 * LibGDX.  In the first lab, we extended ApplicationAdapter.  In previous lab
 * we extended Game.  This is because of a weird graphical artifact that we do not
 * understand.  Transparencies (in 3D only) is failing when we use ApplicationAdapter.
 * There must be some undocumented OpenGL code in setScreen.
 *
 * Author: Walker M. White
 * Based on original PhysicsDemo Lab by Don Holden, 2007
 * LibGDX version, 2/6/2015
 */
package com.superglue.toweroffense;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.*;
import com.badlogic.gdx.assets.loaders.*;
import com.badlogic.gdx.assets.loaders.resolvers.*;

import com.google.gson.Gson;
import com.superglue.toweroffense.factories.LevelFactory;
import com.superglue.toweroffense.level.*;
import com.superglue.toweroffense.util.*;
import com.superglue.toweroffense.levelEditor.LevelEditorController;

import java.nio.file.Files;
import java.util.ArrayList;

/**
 * Root class for a LibGDX.
 *
 * This class is technically not the ROOT CLASS. Each platform has another class above
 * this (e.g. PC games use DesktopLauncher) which serves as the true root.  However,
 * those classes are unique to each platform, while this class is the same across all
 * plaforms. In addition, this functions as the root class all intents and purposes,
 * and you would draw it as a root class in an architecture specification.
 */
public class GDXRoot extends Game implements ScreenListener {
    /** AssetManager to load game assets (textures, sounds, etc.) */
    public ResourceManager resourceManager;
    /** Drawing context to display graphics (VIEW CLASS) */
    public GameCanvas canvas;
    /** Player mode for the asset loading screen (CONTROLLER CLASS) */
    private LoadingMode loading;
    /** Player mode for the the game proper (CONTROLLER CLASS) */
    private int current;
    /** List of all WorldControllers */
    private WorldController[] controllers;

    private LevelEditorController.JLevel[] loadedLevels;

    private ArrayList<Integer> levelsPlayed = new ArrayList<Integer>();
    private int lastLevelPlayed = 0;

    /**
     * Creates a new game from the configuration settings.
     *
     * This method configures the asset manager, but does not load any assets
     * or assign any screen.
     */
    public GDXRoot() {
        // Start loading with the asset manager
        this.resourceManager = ResourceManager.getInstance();
        resourceManager.loadLoadingContent();
        FileHandleResolver resolver = new InternalFileHandleResolver();
        resourceManager.assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        resourceManager.assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
    }

    /**
     * Called when the Application is first created.
     *
     * This is method immediately loads assets for the loading screen, and prepares
     * the asynchronous loader for all other assets.
     */
    public void create() {
        canvas  = new GameCanvas();
        loading = new LoadingMode(canvas,resourceManager,1);
        final int WORLD_COUNT = 18;

        controllers = new WorldController[WORLD_COUNT];
        controllers[0] = new LevelSelectMode(this);

        loadedLevels = new LevelEditorController.JLevel[15];
        Gson gson = new Gson();
        try {
            loadedLevels[0] = gson.fromJson(new String(Files.readAllBytes(Gdx.files.internal("levelFiles/level1.json").file().toPath())), LevelEditorController.JLevel.class);
            loadedLevels[1] = gson.fromJson(new String(Files.readAllBytes(Gdx.files.internal("levelFiles/level2.json").file().toPath())), LevelEditorController.JLevel.class);
            loadedLevels[2] = gson.fromJson(new String(Files.readAllBytes(Gdx.files.internal("levelFiles/level3.json").file().toPath())), LevelEditorController.JLevel.class);
            loadedLevels[3] = gson.fromJson(new String(Files.readAllBytes(Gdx.files.internal("levelFiles/level4.json").file().toPath())), LevelEditorController.JLevel.class);
            loadedLevels[4] = gson.fromJson(new String(Files.readAllBytes(Gdx.files.internal("levelFiles/level5.json").file().toPath())), LevelEditorController.JLevel.class);
            loadedLevels[5] = gson.fromJson(new String(Files.readAllBytes(Gdx.files.internal("levelFiles/noRoomsandrocks.json").file().toPath())), LevelEditorController.JLevel.class);
            loadedLevels[6] = gson.fromJson(new String(Files.readAllBytes(Gdx.files.internal("levelFiles/Gauntlet2.json").file().toPath())), LevelEditorController.JLevel.class);
            loadedLevels[7] = gson.fromJson(new String(Files.readAllBytes(Gdx.files.internal("levelFiles/ChooseWisely.json").file().toPath())), LevelEditorController.JLevel.class);
            loadedLevels[8] = gson.fromJson(new String(Files.readAllBytes(Gdx.files.internal("levelFiles/rockLevel2.json").file().toPath())), LevelEditorController.JLevel.class);
            loadedLevels[9] = gson.fromJson(new String(Files.readAllBytes(Gdx.files.internal("levelFiles/level10.json").file().toPath())), LevelEditorController.JLevel.class);

            loadedLevels[10] = gson.fromJson(new String(Files.readAllBytes(Gdx.files.internal("levelFiles/boulder1.json").file().toPath())), LevelEditorController.JLevel.class);
            loadedLevels[11] = gson.fromJson(new String(Files.readAllBytes(Gdx.files.internal("levelFiles/boulder2.json").file().toPath())), LevelEditorController.JLevel.class);
            loadedLevels[12] = gson.fromJson(new String(Files.readAllBytes(Gdx.files.internal("levelFiles/boulder3.json").file().toPath())), LevelEditorController.JLevel.class);
            loadedLevels[13] = gson.fromJson(new String(Files.readAllBytes(Gdx.files.internal("levelFiles/SacrificeTutorial.json").file().toPath())), LevelEditorController.JLevel.class);
            loadedLevels[14] = gson.fromJson(new String(Files.readAllBytes(Gdx.files.internal("levelFiles/spiraltest.json").file().toPath())), LevelEditorController.JLevel.class);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to load level");
        }

        controllers[1] = new LevelMode(this, loadedLevels[0]);
        controllers[2] = new LevelMode(this, loadedLevels[5]);
        controllers[3] = new LevelMode(this, loadedLevels[10]);
        controllers[4] = new LevelMode(this, loadedLevels[1]);
        controllers[5] = new LevelMode(this, loadedLevels[8]);
        controllers[6] = new LevelMode(this, loadedLevels[2]);
        controllers[7] = new LevelMode(this, loadedLevels[6]);
        controllers[8] = new LevelMode(this, loadedLevels[13]);
        controllers[9] = new LevelMode(this, loadedLevels[9]);
        controllers[10] = new LevelMode(this, loadedLevels[11]);

        controllers[11] = new LevelMode(this, loadedLevels[12]);
        controllers[12] = new LevelMode(this, loadedLevels[3]);
        controllers[13] = new LevelMode(this, loadedLevels[14]);
        controllers[14] = new LevelMode(this, loadedLevels[7]);
        controllers[15] = new LevelMode(this, loadedLevels[4]);

        try {
            controllers[16] = new LevelEditorController(this);
        } catch (java.io.IOException exception) {
            System.out.println("Failed to create new LevelEditorController");
        }
        controllers[17] = new LevelMode(this, null);
        current = 0;
        loading.setScreenListener(this);
        setScreen(loading);
    }


    /**
     * Called when the Application is destroyed.
     *
     * This is preceded by a call to pause().
     */
    public void dispose() {
        // Call dispose on our children
        setScreen(null);

        canvas.dispose();
        canvas = null;

        // Unload all of the resources
        resourceManager.assetManager.clear();
        resourceManager.assetManager.dispose();
        super.dispose();
    }

    /**
     * Called when the Application is resized.
     *
     * This can happen at any point during a non-paused state but will never happen
     * before a call to create().
     *
     * @param width  The new width in pixels
     * @param height The new height in pixels
     */
    public void resize(int width, int height) {
        canvas.resize();
        super.resize(width,height);
    }

    /**
     * The given screen has made a request to exit its player mode.
     *
     * The value exitCode can be used to implement menu options.
     *
     * @param screen   The screen requesting to exit
     * @param exitCode The state of the screen upon exit
     */
    public void exitScreen(Screen screen, int exitCode) {
        if (screen == loading) {
            for(int ii = 0; ii < controllers.length; ii++) {
                controllers[ii].setScreenListener(this);
                controllers[ii].setCanvas(canvas);
                controllers[ii].setCamera(canvas.getCamera());
            }
            controllers[current].reset();
            setScreen(controllers[current]);

            loading.dispose();
            loading = null;
        } else if (exitCode == WorldController.EXIT_QUIT) {
            // We quit the main application
            Gdx.app.exit();
        } else if (exitCode == WorldController.EXIT_NEXT) {
            current = 16;
            controllers[current].reset();
            setScreen(controllers[current]);
        }
    }

    public void exitToLevel(int levelNum){
        LevelFactory.init(controllers[levelNum], null, resourceManager);
        current = levelNum;

        if(levelNum == 1) ((LevelMode) controllers[current]).setIsLevelOne();
        else if(levelNum == 3) ((LevelMode) controllers[current]).setIsLevelThree();
        else if(levelNum == 4) ((LevelMode) controllers[current]).setIsLevelFour();
        else if(levelNum == 8) ((LevelMode) controllers[current]).setIsLevelEight();



        controllers[current].reset();

        lastLevelPlayed = levelNum;
        setScreen(controllers[current]);
    }

    /**
     * Call this method to switch controller to the level specified by level
     * @param level
     */
    public void exitToLevel(LevelEditorController.JLevel level){
        SoundController.getInstance().getMusic("MenuTheme").stop();
        current = 17;
        LevelFactory.init(controllers[current], null, resourceManager);
        ((LevelMode) controllers[17]).level = level;
        controllers[current].reset();
        setScreen(controllers[current]);
    }

    public void exitToLevelSelect(boolean didWin){
        if(didWin){
            levelsPlayed.add(lastLevelPlayed);
            ((LevelSelectMode) controllers[0]).setLastLevelPlayed(lastLevelPlayed);
        }
        else{
            ((LevelSelectMode) controllers[0]).setLastLevelPlayed(lastLevelPlayed - 1);
        }

        current = 0;
        ((LevelSelectMode) controllers[current]).setLevelsPlayed(levelsPlayed);
        controllers[current].reset();
        controllers[current].camera.position.set(Constants.RES_X / 2f, Constants.RES_Y / 2f, 0);
        setScreen(controllers[current]);
        SoundController.getInstance().stopAll();
        Music menuTheme = SoundController.getInstance().getMusic("MenuTheme");
        menuTheme.setLooping(true);
        menuTheme.setVolume(1);
        menuTheme.play();
    }

    public WorldController getLevelMode(){
        return controllers[17];
    }

    public WorldController getCurrentLevel(){
        return controllers[current];
    }
}
