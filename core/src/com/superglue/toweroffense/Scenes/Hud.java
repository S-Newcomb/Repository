package com.superglue.toweroffense.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.superglue.toweroffense.GDXRoot;
import com.superglue.toweroffense.ResourceManager;
import com.superglue.toweroffense.level.LevelMode;
import com.superglue.toweroffense.player.PlayerConstants;
import com.superglue.toweroffense.util.Constants;
import com.superglue.toweroffense.util.FilmStrip;
import com.superglue.toweroffense.util.SoundController;

import java.util.ArrayList;
import java.util.Arrays;


public class Hud {
    private GDXRoot root;

    public Stage stage;
    private Viewport viewport;

    private float health;
    private float maxHealth;
    private int people;
    private int engine;
    private int lifesteal;
    private int harden;
    private int ammo;
    private float width;
    private float height;

    private String levelRoom1;
    private String levelRoom2;
    private String levelRoom3;
    private String levelRoom4;
    private ArrayList<String> levelRooms;
    private int roomCount;

    private FilmStrip glow_animation = null;
    private FilmStrip glow_animation2 = null;
    private boolean hasDisplayedGlow = false;
    private boolean shouldDisplayGlow2 = false;

    //Initialize Labels
//    Label healthLabel;
    Label peopleLabel;
    Label enginePpl;
    Label lifeStealPpl;
    Label hardenPpl;
    Label ammoLabel;

    //Used to determine room bounds
    private Vector2 button1Start;
    private Vector2 button1End;
    private Vector2 button2Start;
    private Vector2 button2End;
    private Vector2 button3Start;
    private Vector2 button3End;
    private Vector2 button4Start;
    private Vector2 button4End;

    private Table roomBG;

    //Initialize tables
    private Table healthPop;
    private Table rooms;
    private Table roomLabel;

    private final float HEALTH_RIGHT_PAD = 0.534375f * Constants.RES_X;
    private final float TEXT_HEALTH_TOP_PAD = 0.06666f * Constants.RES_Y;
    private final float TEXT_ROOM_TOP_PAD = 0.14444f * Constants.RES_Y;
    private final float TEXT_ROOM_TOP_PAD_2 = 0.0777f * Constants.RES_Y;

    private final float ROW_BUFFER = 0.045f * Constants.RES_Y;
    private final float ROOM_WIDTH = 0.125f * Constants.RES_X;
    private final float ROOM_HEIGHT = 0.1111f * Constants.RES_Y;
    private final float ROOM_X = 0.975f * 0.8875f * Constants.RES_X;
    private final float ROOM_Y = 0.2f * Constants.RES_Y;

    private final float HEALTH_FULL = 142;

    /**Font Stuff*/
    private static String FONT_FILE = "fonts/h.ttf";
    protected BitmapFont displayFont = createFont(FONT_FILE, 20);
    protected BitmapFont largeFont = createFont(FONT_FILE, 100);
    protected BitmapFont smallFont = createSmallFont(FONT_FILE);

    private BitmapFont createFont(String font, int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(font));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.borderWidth =1;
        parameter.color =Color.WHITE;
        parameter.shadowOffsetX =(int)(3*Constants.RES_RATIO);
        parameter.shadowOffsetY =(int)(3*Constants.RES_RATIO);
        parameter.shadowColor = new Color(0,0,0,0.75f);

        BitmapFont font24 = generator.generateFont(parameter); // font size 24 pixels
        generator.dispose();
        return font24;
    }


    private BitmapFont createSmallFont(String font) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(font));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        parameter.borderWidth = 1;
        parameter.color = Color.WHITE;

        BitmapFont font24 = generator.generateFont(parameter);
        generator.dispose();
        return font24;
    }

    //Images for people allocation
    private Texture engineTexture = new Texture(Gdx.files.internal("misc/engine.png"));
    private TextureRegion engineRegion = new TextureRegion(engineTexture);
    private TextureRegionDrawable engineDrawable = new TextureRegionDrawable(engineRegion);

    private Texture sacrificeTexture = new Texture(Gdx.files.internal("misc/sacrifice.png"));
    private TextureRegion sacrificeRegion = new TextureRegion(sacrificeTexture);
    private TextureRegionDrawable sacrificeDrawable = new TextureRegionDrawable(sacrificeRegion);

    private Texture toughnessTexture = new Texture(Gdx.files.internal("misc/toughness.png"));
    private TextureRegion toughnessRegion = new TextureRegion(toughnessTexture);
    private TextureRegionDrawable toughnessDrawable = new TextureRegionDrawable(toughnessRegion);

    private Texture magnetTexture = new Texture(Gdx.files.internal("misc/magnet.png"));
    private TextureRegion magnetRegion = new TextureRegion(magnetTexture);
    private TextureRegionDrawable magnetDrawable = new TextureRegionDrawable(magnetRegion);

    // Create the room buttons
    private ImageButton engineRoom =  new ImageButton(engineDrawable);
    private ImageButton sacrificeRoom = new ImageButton(sacrificeDrawable);
    private ImageButton toughnessRoom = new ImageButton(toughnessDrawable);
    private ImageButton lifestealRoom = new ImageButton(magnetDrawable);

    //Create other buttons
    private ImageButton resumeButton = new ImageButton(new TextureRegionDrawable(ResourceManager.resumeTexture));
    private ImageButton returnButton = new ImageButton(new TextureRegionDrawable(ResourceManager.returnTexture));

    private Texture roomBG_0 = new Texture(Gdx.files.internal("misc/ui_0.png"));
    private TextureRegionDrawable roomBG_0Drawable = new TextureRegionDrawable(new TextureRegion(roomBG_0));
    private Texture roomBG_1 = new Texture(Gdx.files.internal("misc/ui_1.png"));
    private TextureRegionDrawable roomBG_1Drawable = new TextureRegionDrawable(new TextureRegion(roomBG_1));
    private Texture roomBG_2 = new Texture(Gdx.files.internal("misc/ui_2.png"));
    private TextureRegionDrawable roomBG_2Drawable = new TextureRegionDrawable(new TextureRegion(roomBG_2));
    private Texture roomBG_3 = new Texture(Gdx.files.internal("misc/ui_3.png"));
    private TextureRegionDrawable roomBG_3Drawable = new TextureRegionDrawable(new TextureRegion(roomBG_3));


    public Hud(PolygonBatch s, float width, float height, float hlth, int maxh, int ppl, int eng, int life, int hard, int ammo, String r1, String r2, String r3, String r4){
        this.health = hlth;
        people = ppl;
        maxHealth = maxh;
        this.ammo = ammo;

        this.width = width;
        this.height = height;
        engine = eng;
        lifesteal = life;
        harden =  hard;

        levelRoom1 = r1;
        levelRoom2 = r2;
        levelRoom3 = r3;
        levelRoom4 = r4;
        levelRooms = new ArrayList<>(Arrays.asList(r1,r2,r3,r4));
        for (String room : levelRooms){
            if (room != "null") roomCount++;
        }
        //System.out.println(levelRooms);

        button1Start = new Vector2 (ROOM_X*1.02f, ROOM_Y*.9f);
        button1End = new Vector2 (ROOM_X*1.02f + ROOM_WIDTH*.87f, ROOM_Y + ROOM_HEIGHT);
        button2Start = new Vector2 (ROOM_X*1.02f, button1End.y*.91f + ROW_BUFFER);
        button2End = new Vector2 (ROOM_X*1.02f + ROOM_WIDTH*.87f, button2Start.y*1.07f + ROOM_HEIGHT);
        button3Start = new Vector2(ROOM_X*1.02f, button2End.y*.95f + ROW_BUFFER);
        button3End = new Vector2(ROOM_X*1.02f + ROOM_WIDTH*.87f, button3Start.y*1.04f + ROOM_HEIGHT);
        button4Start = new Vector2(ROOM_X*1.02f, button3End.y*.96f + ROW_BUFFER);
        button4End = new Vector2(ROOM_X*1.02f + ROOM_WIDTH*.87f, button4Start.y*1.04f + ROOM_HEIGHT);


        // Initialize Camera and create stage
        viewport = new FitViewport(width,height, new OrthographicCamera());
        stage = new Stage(viewport, s);
        Gdx.input.setInputProcessor(stage);
        stage.act();

        createLabels();
        createTables();

        // Add tables to stage
        stage.addActor(rooms);
        stage.addActor(healthPop);
        stage.addActor(roomLabel);

        stage.addActor(roomBG);
    }

    /** Creates all the text Labels to be added to the stage*/
    public void createLabels(){
//        healthLabel = new Label ((int)health + "/" + (int)maxHealth, new Label.LabelStyle(displayFont, Color.WHITE));
        peopleLabel = new Label ("Pop:"  + people, new Label.LabelStyle(displayFont, Color.WHITE));
        enginePpl = new Label(Integer.toString(engine), new Label.LabelStyle(smallFont, Color.WHITE));
        lifeStealPpl = new Label (Integer.toString(lifesteal), new Label.LabelStyle(smallFont, Color.WHITE));
        hardenPpl = new Label (Integer.toString(harden), new Label.LabelStyle(smallFont, Color.WHITE));
        ammoLabel = new Label("Ammo:" + ammo, new Label.LabelStyle(displayFont, Color.WHITE));
    }

    /** Creates all the tables to be added to the Stage */
    public void createTables(){
        roomBG = new Table();
        roomBG.top();
        roomBG.setFillParent(true);
        if(roomCount == 0) roomBG.add(new ImageButton(roomBG_0Drawable)).expandX().padTop(ROOM_HEIGHT).padLeft(ROOM_X);
        else if(roomCount == 1) roomBG.add(new ImageButton(roomBG_1Drawable)).expandX().padTop(ROOM_HEIGHT).padLeft(ROOM_X);
        else if(roomCount == 2) roomBG.add(new ImageButton(roomBG_2Drawable)).expandX().padTop(ROOM_HEIGHT).padLeft(ROOM_X);
        else if(roomCount == 3) roomBG.add(new ImageButton(roomBG_3Drawable)).expandX().padTop(ROOM_HEIGHT).padLeft(ROOM_X);

        // Add Table containing health and population labels
        healthPop = new Table();
        healthPop.top();
        healthPop.setFillParent(true);

//        healthPop.add(healthLabel).expandX().padTop(TEXT_HEALTH_TOP_PAD * 1.15f);
        //healthPop.add(peopleLabel).expandX().padTop(TEXT_HEALTH_TOP_PAD*3f).padLeft(0.9f * ROOM_X);


        //Add the table containing the room pictures
        rooms = new Table();
        rooms.top().right();
        rooms.setFillParent(true);
        if (roomCount >=1) {
            if (levelRoom1 == "Engine Room") {
                rooms.add(engineRoom).expandX().padTop(ROOM_HEIGHT*1.7f).padLeft(ROOM_X*1.8f).size(ROOM_WIDTH, ROOM_HEIGHT);
                rooms.row();
            } else if (levelRoom1 == "Sacrifice Room") {
                rooms.add(sacrificeRoom).expandX().padTop(ROOM_HEIGHT*1.7f).padLeft(ROOM_X*1.8f).size(ROOM_WIDTH, ROOM_HEIGHT);
                rooms.row();
            } else if (levelRoom1 == "LifeSteal Room") {
                rooms.add(lifestealRoom).expandX().padTop(ROOM_HEIGHT*1.7f).padLeft(ROOM_X*1.8f).size(ROOM_WIDTH, ROOM_HEIGHT);
                rooms.row();
            } else if (levelRoom1 == "Harden Room") {
                rooms.add(toughnessRoom).expandX().padTop(ROOM_HEIGHT*1.7f).padLeft(ROOM_X*1.8f).size(ROOM_WIDTH, ROOM_HEIGHT);
                rooms.row();
            }
        }
        if (roomCount >=2) {
            if (levelRoom2 == "Sacrifice Room" && levelRoom1 != "Sacrifice Room") {
                rooms.add(sacrificeRoom).expandX().padTop(ROW_BUFFER).padLeft(ROOM_X*1.8f).size(ROOM_WIDTH, ROOM_HEIGHT);
                rooms.row();
            } else if (levelRoom2 == "LifeSteal Room" && levelRoom1 != "LifeSteal Room") {
                rooms.add(lifestealRoom).expandX().padTop(ROW_BUFFER).padLeft(ROOM_X*1.8f).size(ROOM_WIDTH, ROOM_HEIGHT);
                rooms.row();
            } else if (levelRoom2 == "Harden Room" && levelRoom1 != "Harden Room") {
                rooms.add(toughnessRoom).expandX().padTop(ROW_BUFFER).padLeft(ROOM_X*1.8f).size(ROOM_WIDTH, ROOM_HEIGHT);
                rooms.row();
            }
        }
        if (roomCount >=3) {
            if (levelRoom3 == "LifeSteal Room" && levelRoom2 != "LifeSteal Room") {
                rooms.add(lifestealRoom).expandX().padTop(ROW_BUFFER*.95f).padLeft(ROOM_X*1.8f).size(ROOM_WIDTH, ROOM_HEIGHT);
                rooms.row();
            }
            else if (levelRoom3 == "Harden Room" && levelRoom2 != "Harden Room") {
                rooms.add(toughnessRoom).expandX().padTop(ROW_BUFFER*.95f).padLeft(ROOM_X*1.8f).size(ROOM_WIDTH, ROOM_HEIGHT);
                rooms.row();
            }
        }
        if (roomCount >=4) {
            if (levelRoom4 == "Harden Room" && levelRoom3 != "Harden Room") {
                rooms.add(toughnessRoom).expandX().padTop(ROW_BUFFER).padLeft(ROOM_X*1.8f).size(ROOM_WIDTH, ROOM_HEIGHT);
                rooms.row();
            }
        }

        //Add Table containing the labels for the rooms
        roomLabel = new Table();
        roomLabel.top().right();
        roomLabel.setFillParent(true);
        //Room 1
        if (levelRoom1 =="Engine Room") {
            roomLabel.add(enginePpl).expandX().padTop(ROW_BUFFER*5.25f).padLeft(0.7f * Constants.RES_X);
            roomLabel.row();
        }
        else if (levelRoom1 == "Sacrifice Room") {
            roomLabel.add(new Label("", new Label.LabelStyle(smallFont, Color.WHITE))).expandX().padTop(ROW_BUFFER*5.25f).padLeft(0.7f * Constants.RES_X);
            roomLabel.row();
        }
        else if (levelRoom1 == "LifeSteal Room") {
            roomLabel.add(lifeStealPpl).expandX().padTop(ROW_BUFFER*5.25f).padLeft(0.7f * Constants.RES_X);
            roomLabel.row();
        }
        else if (levelRoom1 == "Harden Room") {
            roomLabel.add(hardenPpl).expandX().padTop(ROW_BUFFER*5.25f).padLeft(0.7f * Constants.RES_X);
            roomLabel.row();
        }
        //Room 2
        if (levelRoom2 == "Sacrifice Room" && levelRoom1 != "Sacrifice Room") {
            roomLabel.add(new Label("", new Label.LabelStyle(smallFont, Color.WHITE))).expandX().padTop(ROW_BUFFER*3f).padLeft(0.7f * Constants.RES_X);
            roomLabel.row();
        }
        else if (levelRoom2 == "LifeSteal Room" && levelRoom1 != "LifeSteal Room") {
            roomLabel.add(lifeStealPpl).expandX().padTop(ROW_BUFFER*3f).padLeft(0.7f * Constants.RES_X);
            roomLabel.row();
        }
        else if (levelRoom2 == "Harden Room" && levelRoom1 != "Harden Room") {
            roomLabel.add(hardenPpl).expandX().padTop(ROW_BUFFER*3f).padLeft(0.7f * Constants.RES_X);
            roomLabel.row();
        }
        //Room 3
        if (levelRoom3 == "LifeSteal Room" && levelRoom2 != "LifeSteal Room") {
            roomLabel.add(lifeStealPpl).expandX().padTop(ROW_BUFFER*3f).padLeft(0.7f * Constants.RES_X);
            roomLabel.row();
        }
        else if (levelRoom3 == "Harden Room" && levelRoom2 != "Harden Room") {
            roomLabel.add(hardenPpl).expandX().padTop(ROW_BUFFER*3f).padLeft(0.7f * Constants.RES_X);
            roomLabel.row();
        }
        //Room 4
        if (levelRoom4 == "Harden Room" && levelRoom3 != "Harden Room") {
            roomLabel.add(hardenPpl).expandX().padTop(ROW_BUFFER*2.9f).padLeft(0.7f * Constants.RES_X);
            roomLabel.row();
        }
        if(roomCount == 0) {
            roomLabel.add(peopleLabel).expandX().padTop(TEXT_HEALTH_TOP_PAD * 2.7f).padLeft(ROOM_X);
            roomLabel.row();
            roomLabel.add(ammoLabel).expandX().padTop(ROW_BUFFER * 1.25f).padLeft(ROOM_X);
        }
        else if(roomCount == 3){
            roomLabel.add(peopleLabel).expandX().padTop(TEXT_HEALTH_TOP_PAD*1.2f).padLeft(ROOM_X);
            roomLabel.row();
            roomLabel.add(ammoLabel).expandX().padTop(ROW_BUFFER).padLeft(ROOM_X);
        }
        else{
            roomLabel.add(peopleLabel).expandX().padTop(TEXT_HEALTH_TOP_PAD*1.25f).padLeft(ROOM_X);
            roomLabel.row();
            roomLabel.add(ammoLabel).expandX().padTop(ROW_BUFFER).padLeft(ROOM_X);
        }
    }

    /** Clears the Hud of all tables and labels */
    public void clearHud(){
        roomBG.remove();
        healthPop.remove();
        roomLabel.remove();
        rooms.remove();

        for (int i = 0; i < stage.getActors().size; i++) {
            stage.getActors().items[i].remove();
        }
    }

    /** Clears non essential elements for level editor use */
    public void editorClearHud(){
        healthPop.remove();
    }

    /**Updates the Hud with new values for people, health and rooms */
    public void updateHud(float h, int maxh, int p, int eng, int life, int hard, int ammo){
        int old_hardness = harden;

        clearHud();
        this.health = h;
        this.maxHealth = maxh;
        this.people = p;
        this.engine = eng;
        this.lifesteal = life;
        this.harden = hard;
        this.ammo = ammo;

        createLabels();
        createTables();
        this.stage.addActor(roomBG);
        this.stage.addActor(rooms);
        this.stage.addActor(healthPop);
        this.stage.addActor(roomLabel);

        if(hard > 0 && !hasDisplayedGlow){
            hasDisplayedGlow = true;
            shouldDisplayGlow2 = true;
        }

        if(shouldDisplayGlow2 && old_hardness > hard){
            shouldDisplayGlow2 = false;
        }

        if(p >= 10 && !hasDisplayedGlow) {
            if (glow_animation != null) {
                glow_animation.setFrame((glow_animation.getFrame() + 1) % (glow_animation.getSize()));
                roomBG.clear();
                roomBG.add(new ImageButton(new TextureRegionDrawable(glow_animation))).expandX().padTop(ROOM_HEIGHT*0.88f).padLeft(ROOM_X/1.162f);
            }
        }

        if(shouldDisplayGlow2){
            if (glow_animation2 != null) {
                glow_animation2.setFrame((glow_animation2.getFrame() + 1) % (glow_animation2.getSize()));
                roomBG.clear();
                roomBG.add(new ImageButton(new TextureRegionDrawable(glow_animation2))).expandX().padTop(ROOM_HEIGHT*0.88f).padLeft(ROOM_X/1.162f);
            }
        }
    }

    public void victoryScreen(){
      //  System.out.println("Victory");
        clearHud();
        Label victory = new Label("VICTORY" , new Label.LabelStyle(largeFont, Color.WHITE));
        victory.setPosition(width/2 + PlayerConstants.VICTORY_LABEL_OFFSET_X,height/2);
        stage.addActor(victory);
        SoundController.getInstance().stopAll();
        Music victorySound = SoundController.getInstance().getMusic("BrassyVictory");
        victorySound.setVolume(1);
        victorySound.play();
    }

    public void defeatScreen(){
        //System.out.println("Defeat");
        clearHud();
        Label defeat = new Label("DEFEAT" , new Label.LabelStyle(largeFont, Color.RED));
        Label restart_text = new Label("Press R to restart" , new Label.LabelStyle(createFont(FONT_FILE, 60), Color.RED));

        defeat.setPosition(width/2 + PlayerConstants.DEFEAT_LABEL_OFFSET_X,height/2);
        restart_text.setPosition(width*7/27, height*3/8);

        stage.addActor(defeat);
        stage.addActor(restart_text);

        SoundController.getInstance().stopAll();
        Music defeatSound = SoundController.getInstance().getMusic("Defeat");
        defeatSound.setVolume(1);
        defeatSound.play();
    }

    public void pauseScreen(){
        clearHud();
        Label paused = new Label("PAUSED", new Label.LabelStyle(createFont(FONT_FILE,80), Color.WHITE));
        paused.setPosition(width/2 -150*Constants.RES_SCALE_X,height*2/3);
        stage.addActor(paused);

        ClickListener resumeListener = new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                    if((root.getCurrentLevel().getClass()).equals(LevelMode.class))
                        ((LevelMode)root.getCurrentLevel()).unpause();
                    clearHud();
                }
        };

        ClickListener returnListener = new ClickListener(){
           public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    SoundController.getInstance().stopAll();
                    clearHud();
                    root.exitToLevelSelect(false);
            }
        };

        resumeButton.setPosition(width*2/3 - 150*Constants.RES_SCALE_X, height/3);
        returnButton.setPosition(width*(2/3) + 400*Constants.RES_SCALE_X, height/3);
        resumeButton.addListener(resumeListener);
        returnButton.addListener(returnListener);


        stage.addActor(resumeButton);
        stage.addActor(returnButton);
      //  levelSelectButton.setPosition(width/2-150, height/5-50);
    //    levelSelectButton.setSize(400,100);
     //   stage.addActor(levelSelectButton);


    }

    /** Returns True if x and y are within the area*/
    public boolean inRegion (float x, float y, float startX, float startY, float endX, float endY){
        float xRange = endX - startX;
        float yRange = endY - startY;
        float xPos = endX - x;
        float yPos = endY - y;
        if ((xPos >= 0 && xPos <= xRange) &&(yPos >= 0 && yPos <= yRange)) return true;
        return false;
    }

    /** Returns True if x and y are within the area of room1 */
    public boolean inRoom1Region (float x, float y){
        return inRegion(x,y, button1Start.x, button1Start.y, button1End.x, button1End.y);
    }

    /** Returns True if x and y are within the area of room2 */
    public boolean inRoom2Region (float x, float y) {
        return inRegion(x, y, button2Start.x, button2Start.y, button2End.x, button2End.y);
    }

    /** Returns True if x and y are within the area of room3 */
    public boolean inRoom3Region (float x, float y){
        return inRegion(x,y, button3Start.x, button3Start.y, button3End.x, button3End.y);
    }

    /** Returns True if x and y are within the area of room3 */
    public boolean inRoom4Region (float x, float y){
        return inRegion(x,y, button4Start.x, button4Start.y, button4End.x, button4End.y);
    }

    public void setRoot(GDXRoot root){
        this.root = root;
        if(((LevelMode)root.getCurrentLevel()).isLevelFour()){
            glow_animation = ResourceManager.left_click;
            glow_animation2 = ResourceManager.right_click;
        }
    }
}


