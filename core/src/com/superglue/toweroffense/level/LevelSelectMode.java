package com.superglue.toweroffense.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.superglue.toweroffense.GDXRoot;
import com.superglue.toweroffense.GameCanvas;
import com.superglue.toweroffense.ResourceManager;
import com.superglue.toweroffense.WorldController;
import com.superglue.toweroffense.gameobjects.BoxGameObject;
import com.superglue.toweroffense.gameobjects.GameObject;
import com.superglue.toweroffense.util.Constants;
import com.superglue.toweroffense.util.SoundController;

import java.util.ArrayList;

import static com.superglue.toweroffense.util.Constants.RES_X;

public class LevelSelectMode extends WorldController {
    private static final int NUM_FLAGS = 15;
    private static final float FLAG_SPACING_X = 125f;
    private static final float FLAG_SPACING_Y = 100f;

    private GDXRoot parent;

    private BoxGameObject controls_text;

    private SoundController sounds = SoundController.getInstance();
    private BoxGameObject flag_outline;
    private BoxGameObject[] flags = new BoxGameObject[NUM_FLAGS];
    private boolean[] locked = new boolean[NUM_FLAGS];

    private BoxGameObject arrow1, arrow2;

    private int currentLevelIndex = 0;
    private int numLevels = NUM_FLAGS;
    private ArrayList<Integer> levelsPlayed = new ArrayList<>();
    private int lastLevelPlayed = 0;

    public LevelSelectMode(GDXRoot parent) {
        this.parent = parent;
        world.setGravity(new Vector2(0, 0));
    }

    CheatBackground background;

    @Override
    public void reset() {
        for (GameObject obj : objects) {
            obj.deactivatePhysics(world);
        }

        objects.clear();
        addQueue.clear();
        world.dispose();

        world = new World(new Vector2(0, 0), false);

        background = new CheatBackground(
            RES_X / 2f, Constants.RES_Y / 2f,
            Constants.RES_SCALE_X / 2f, Constants.RES_SCALE_Y / 2f
        );
        background.isBackground = true;
        background.setTexture(ResourceManager.level_select_screen);
        addObject(background);

        controls_text = new BoxGameObject(Constants.RES_X/2, Constants.RES_Y*5/9, 1, 1);
        controls_text.setDrawScale(1, 1);
        controls_text.setTexture(ResourceManager.level_select_controls);
        addObject(controls_text);

        flag_outline = new BoxGameObject(100, 100, 1, 1);
//        flag_outline.setDrawScale(Constants.RES_SCALE_X, Constants.RES_SCALE_Y);
        flag_outline.setDrawScale(1, 1);
        flag_outline.setTexture(ResourceManager.flag_outline);

        addObject(flag_outline);

        setFlags();
        setLocks();
        setColors();

        if(lastLevelPlayed < numLevels)
            currentLevelIndex = lastLevelPlayed;

        while(currentLevelIndex < numLevels && flags[currentLevelIndex].getX() > canvas.getWidth())
            for(BoxGameObject flag : flags){
                flag.setPosition(flag.getX() - 2*FLAG_SPACING_X, flag.getY());
            }

        arrow1 = new BoxGameObject(-1000, 50, 1, 1);
        arrow1.setDrawScale(1, 1);
        arrow1.setTexture(ResourceManager.ls_arrow);
        addObject(arrow1);

        arrow2 = new BoxGameObject(-1000, 50, 1, 1);
        arrow2.setDrawScale(1, 1);
        TextureRegion t = new TextureRegion(ResourceManager.ls_arrow);
        t.flip(true, false);
        arrow2.setTexture(t);
        addObject(arrow2);
    }

    public void setLastLevelPlayed(int levelNum){
        lastLevelPlayed = levelNum;
    }

    // places flags
    public void setFlags(){
        // for each flag
        for(int i = 0; i < NUM_FLAGS; i++){
            // construct
            flags[i] = new BoxGameObject(
                    FLAG_SPACING_X * i * 2, FLAG_SPACING_Y,
                    1, 1
            );

            // scale based on resolution
//            flags[i].setDrawScale(Constants.RES_SCALE_X, Constants.RES_SCALE_Y);
            flags[i].setDrawScale(1, 1);

            // set the texture based on which flag this is
            TextureRegion flagTex = null;
            if(i == 0) flagTex = ResourceManager.flag_1;
            else if(i == 1) flagTex = ResourceManager.flag_2;
            else if(i == 2) flagTex = ResourceManager.flag_3;
            else if(i == 3) flagTex = ResourceManager.flag_4;
            else if(i == 4) flagTex = ResourceManager.flag_5;
            else if(i == 5) flagTex = ResourceManager.flag_6;
            else if(i == 6) flagTex = ResourceManager.flag_7;
            else if(i == 7) flagTex = ResourceManager.flag_8;
            else if(i == 8) flagTex = ResourceManager.flag_9;
            else if(i == 9) flagTex = ResourceManager.flag_10;
            else if(i == 10) flagTex = ResourceManager.flag_11;
            else if(i == 11) flagTex = ResourceManager.flag_12;
            else if(i == 12) flagTex = ResourceManager.flag_13;
            else if(i == 13) flagTex = ResourceManager.flag_14;
            else if(i == 14) flagTex = ResourceManager.flag_15;

            flags[i].setTexture(flagTex);
            flags[i].setBodyType(BodyDef.BodyType.StaticBody);
            addObject(flags[i]);
        }
    }
    //Set locked[i] true to false to unlock
    public void setLocks(){
        locked[0] = false;
        for (int i = 1; i < flags.length; i++){
            locked[i] = false;
        }
        for (int i = 0; i < levelsPlayed.size(); i++){
            if(levelsPlayed.get(i) == numLevels) break;
            locked[levelsPlayed.get(i)] = false;
        }
    }

    public void setColors(){
        for (int i = 0; i < flags.length; i++){
            //make locked flags black:
            if (locked[i]) flags[i].setColor(Color.BLACK);
            //make locked flags grayed out:
            //if (locked[i]) flags[i].setColor(new Color(0x111111ff));
            //make flags gold-ish when beaten:
            //else if (levelsPlayed.contains(i)) flags[i].setColor(new Color(0xcc9005ff));
        }
    }

    public void setLevelsPlayed(ArrayList<Integer> levelsPlayed){
        this.levelsPlayed = levelsPlayed;
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        camera.position.x = Constants.RES_X / 2f;
        camera.position.y = Constants.RES_Y / 2f;

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)){
            if (currentLevelIndex < NUM_FLAGS - 1 && !locked[(currentLevelIndex+1)]) {
                currentLevelIndex = (currentLevelIndex + 1);
                if(currentLevelIndex < numLevels && flags[currentLevelIndex].getX() > canvas.getWidth())
                    for(BoxGameObject flag : flags){
                        flag.setPosition(flag.getX() - 2*FLAG_SPACING_X, flag.getY());
                    }
            }
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.A)){
            if (currentLevelIndex > 0) {
                currentLevelIndex = currentLevelIndex - 1;
                if(currentLevelIndex >= 0 && flags[currentLevelIndex].getX() < 0)
                    for(BoxGameObject flag : flags){
                        flag.setPosition(flag.getX() + 2*FLAG_SPACING_X, flag.getY());
                }
            }
        }

        else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            Music menuTheme = sounds.getMusic("MenuTheme");
            menuTheme.stop();
            //   sounds.play("Groovin Drums3", true, 0.5f);
            parent.exitToLevel(currentLevelIndex + 1);
        }

        else if (Gdx.input.isKeyJustPressed(Input.Keys.EQUALS)){
            parent.exitScreen(this, EXIT_NEXT);
        }

        flag_outline.setPosition(flags[currentLevelIndex].getX() - 1, flags[currentLevelIndex].getY() + 2);

        boolean anyFlagsOffScreenRight = false;
        boolean anyFlagsOffScreenLeft = false;
        for(BoxGameObject f: flags){
            if (f.getX() > canvas.getWidth())
                anyFlagsOffScreenRight = true;
            if(f.getX() < 0)
                anyFlagsOffScreenLeft = true;
        }
        if(anyFlagsOffScreenRight){
            arrow1.setX(canvas.getWidth()*1.05f);
        }
        else{
            arrow1.setX(-1000);
        }

        if(anyFlagsOffScreenLeft){
            arrow2.setX(-100*Constants.RES_SCALE_X);
        }
        else{
            arrow2.setX(-1000);
        }
    }
}