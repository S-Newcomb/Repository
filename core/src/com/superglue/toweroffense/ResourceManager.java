package com.superglue.toweroffense;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.superglue.toweroffense.util.FilmStrip;
import com.superglue.toweroffense.util.SoundController;

/**
 * This is the Asset Manager class.
 */
public class ResourceManager {
    public static ResourceManager instance;
    public static AssetManager assetManager;
    private static SoundController sounds;

    public static ResourceManager getInstance(){
        if (instance == null){
            instance = new ResourceManager();
            assetManager = new AssetManager();
        }
        return instance;
    }

    // Music
//    public final String playingSong = "music/Rolemusic_-_pl4y1ng.mp3";

    // Skin
//    public final String skin = "skin/glassy-ui.json";

    // Textures
    // TODO: turn those images into atlas using TexturePacker
//    public final String loadingImages = "images/loading.atlas";
//    public final String playerImages = "images/player.atlas";
//    public final String levelImages = "images/level.atlas";
//    public final String tileImages = "images/tiles.atlas";

    public static final String PAUSE_TEXTURE = "misc/pause.png";
    public static final String RESUME_TEXTURE = "misc/resume.png";
    public static final String RETURN_TEXTURE = "misc/return_to_level_select.png";

    public static final String BACKGROUND_FILE = "loading/loading.png";
    public static final String PROGRESS_FILE = "loading/progressbar.png";
    public static final String PLAY_BTN_FILE = "loading/play.png";

    private static final String LEVEL_SELECT_SCREEN = "level_select/level_select.png";
    private static final String LEVEL_SELECT_CONTROLS = "level_select/controls.png";
    private static final String LEVEL_ONE_WASD = "level/wasd.png";
    private static final String LEVEL_ONE_SPACEBAR = "level/spacebar.png";
    private static final String LEFTCLICK = "misc/left_click_tut.png";
    private static final String RIGHTCLICK = "misc/right_click_tut.png";
    private static final String F_TEXT = "level/f.png";

    private static final String FLAG_OUTLINE = "level_select/flag_outline.png";
    private static final String FLAG_1 = "level_select/flag_1.png";
    private static final String FLAG_2 = "level_select/flag_2.png";
    private static final String FLAG_3 = "level_select/flag_3.png";
    private static final String FLAG_4 = "level_select/flag_4.png";
    private static final String FLAG_5 = "level_select/flag_5.png";
    private static final String FLAG_6 = "level_select/flag_6.png";
    private static final String FLAG_7 = "level_select/flag_7.png";
    private static final String FLAG_8 = "level_select/flag_8.png";
    private static final String FLAG_9 = "level_select/flag_9.png";
    private static final String FLAG_10 = "level_select/flag_10.png";
    private static final String FLAG_11 = "level_select/flag_11.png";
    private static final String FLAG_12 = "level_select/flag_12.png";
    private static final String FLAG_13 = "level_select/flag_13.png";
    private static final String FLAG_14 = "level_select/flag_14.png";
    private static final String FLAG_15 = "level_select/flag_15.png";
    private static final String LS_ARROW = "level_select/arrow.png";

    private static final String TOWER_CROSSHAIR_TEXTURE = "player/crosshair.png";
    private static final String TOWER_WALKING_SPRITE = "player/walking.png";
    private static final String TOWER_WALKING_DOWN_SPRITE = "player/walking_front.png";
    private static final String TOWER_WALKING_UP_SPRITE = "player/walking_back.png";

    private static final String TOWER_FALLING_SPRITE1 = "player/falling1.png";
    private static final String TOWER_FALLING_SPRITE2 = "player/falling2.png";
    private static final String TOWER_FALLING_UP = "player/falling_up.png";
    private static final String TOWER_FALLING_DOWN = "player/falling_down.png";

    private static final String SWORD_WALKING = "enemies/sword_walking.png";
    private static final String SPEAR_WALKING = "enemies/spear_walking.png";
    private static final String ARCHER_SHOOTING = "enemies/archer_shooting.png";

    private static final String TOWER_IDLE_SPRITE = "player/idle.png";
    private static final String TOWER_IDLE_UP_SPRITE = "player/idle_up.png";
    private static final String TOWER_IDLE_DOWN_SPRITE = "player/idle_down.png";

    private static final String BACKGROUND_TEXTURE1 = "tiles/grass1.png";
    private static final String BACKGROUND_TEXTURE2 = "tiles/grass2.png";
    private static final String BACKGROUND_TEXTURE3 = "tiles/grass3.png";
    private static final String HARD_CODED_BACKGROUND = "tiles/hard_coded_background.png";

    public static final String MELEE_TEXTURE = "enemies/swordsman.png";
    public static final String SPEAR_TEXTURE = "enemies/spearman.png";
    public static final String VILLAGE_TEXTURE = "enemies/village.png";
    public static final String RED_VILLAGE_TEXTURE = "enemies/Village2.png";
    public static final String BLUE_VILLAGE_TEXTURE = "enemies/Village3.png";
    public static final String BIG_VILLAGE_TEXTURE = "enemies/big_village.png";
    public static final String DEFENDER_TEXTURE = "enemies/archer.png";
    private static final String ARROW_TEXTURE = "enemies/arrow.png";

    public static final String TREE_TEXTURE_1 = "obstacles/tree1.png";
    public static final String TREE_TEXTURE_2 = "obstacles/tree2.png";
    public static final String TREE_TEXTURE_3 = "obstacles/tree3.png";
    public static final String TREE_TEXTURE_4 = "obstacles/tree_stump.png";
    public static final String ROCK_TEXTURE_1 = "obstacles/rock4.png";
    public static final String ROCK_TEXTURE_2 = "obstacles/rock1.png";
    public static final String ROCK_TEXTURE_3 = "obstacles/rock2.png";
    public static final String ROCK_TEXTURE_4 = "obstacles/rock3.png";
    public static final String BOULDER_TEXTURE = "obstacles/boulder.png";
    public static final String WATER_1 = "obstacles/water1.png";

    public static final String PLAYER_SPAWN_TEXTURE = "player/player_spawn_marker.png";

    private static final String HP_BAR_TEXTURE_1 = "enemies/FullHealthBar.png";
    private static final String HP_BAR_TEXTURE_2 = "enemies/TwoThirdsHealthBar.png";
    private static final String HP_BAR_TEXTURE_3 = "enemies/OneThirdHealthBar.png";

    public static TextureRegion pauseTexture;
    public static TextureRegion resumeTexture;
    public static TextureRegion returnTexture;

    public static TextureRegion backgroundTexture1;
    public static TextureRegion backgroundTexture2;
    public static TextureRegion backgroundTexture3;
    public static TextureRegion hard_coded_background;

    public static TextureRegion level_select_screen;
    public static TextureRegion level_select_controls;
    public static TextureRegion level_one_wasd;
    public static TextureRegion level_one_spacebar;
    public static TextureRegion f_text;
    public static FilmStrip left_click;
    public static FilmStrip right_click;

    public static TextureRegion flag_outline;
    public static TextureRegion flag_1;
    public static TextureRegion flag_2;
    public static TextureRegion flag_3;
    public static TextureRegion flag_4;
    public static TextureRegion flag_5;
    public static TextureRegion flag_6;
    public static TextureRegion flag_7;
    public static TextureRegion flag_8;
    public static TextureRegion flag_9;
    public static TextureRegion flag_10;
    public static TextureRegion flag_11;
    public static TextureRegion flag_12;
    public static TextureRegion flag_13;
    public static TextureRegion flag_14;
    public static TextureRegion flag_15;
    public static TextureRegion ls_arrow;

    public static TextureRegion water1;

    public static TextureRegion crosshairTexture;
    public static TextureRegion meleeTexture;
    public static TextureRegion spearTexture;
    public static TextureRegion villageTexture;
    public static TextureRegion redVillageTexture;
    public static TextureRegion blueVillageTexture;
    public static TextureRegion bigVillageTexture;
    public static TextureRegion defenderTexture;
    public static TextureRegion arrowTexture;
    public static TextureRegion treeTexture_1;
    public static TextureRegion treeTexture_2;
    public static TextureRegion treeTexture_3;
    public static TextureRegion treeTexture_4;
    public static TextureRegion rockTexture_1;
    public static TextureRegion rockTexture_2;
    public static TextureRegion rockTexture_3;
    public static TextureRegion rockTexture_4;
    public static TextureRegion boulderTexture;
    public static TextureRegion HPBarTexture1;
    public static TextureRegion HPBarTexture2;
    public static TextureRegion HPBarTexture3;

    public static TextureRegion playerSpawnTexture;

    public static FilmStrip towerWalkingAnimation;
    public static FilmStrip towerWalkingUpAnimation;
    public static FilmStrip towerWalkingDownAnimation;
    public static FilmStrip towerFallingAnimation1;
    public static FilmStrip towerFallingAnimation2;
    public static FilmStrip towerFallingUpAnimation;
    public static FilmStrip towerFallingDownAnimation;
    public static FilmStrip towerIdleSprite;
    public static FilmStrip towerIdleUpSprite;
    public static FilmStrip towerIdleDownSprite;

    public static FilmStrip swordWalkingAnimation;
    public static FilmStrip spearWalkingAnimation;
    public static FilmStrip archerShootingAnimation;

    public static TextureAtlas blacksmokeatlas;
    public static TextureAtlas theAtlas;

    // sounds
    public static final String EPIC_THEME_FILE = "sounds/WorkingSoundtrack v2.wav";
    public static final String GROOVY_THEME_FILE = "sounds/Groovin Drums3.mp3";
    public static final String THWACK_FILE = "sounds/DeeperThwack.wav";
    public static final String VICTORY_THEME_FILE = "sounds/BrassyVictory.wav";
    public static final String DEFEAT_FILE = "sounds/Defeat.wav";
    public static final String SCREAM1_FILE = "sounds/MaleScream1.mp3";
    public static final String SCREAM2_FILE = "sounds/MaleScream2.mp3";
    public static final String SCREAM3_FILE = "sounds/NoScream.mp3";
    public static final String SCREAM4_FILE = "sounds/WilhelmScream.wav";
    public static final String GROUP_SCREAM1_FILE = "sounds/GroupScream1.mp3";
    public static final String GROUP_SCREAM2_FILE = "sounds/GroupScream2.mp3";


    // Fonts
    private static String FONT_FILE = "fonts/h.ttf";

    /**
     * All the assets that go on the loading screen get loaded here so that we can call this first
     * so that we don't need to stare at black screen for 20 hours.
     */
    public void loadLoadingContent(){
        assetManager.load(BACKGROUND_FILE, Texture.class);
        assetManager.load(PROGRESS_FILE, Texture.class);
        assetManager.load(PLAY_BTN_FILE, Texture.class);
        theAtlas = new TextureAtlas();
    }

    public void preLoadContent() {
        sounds = SoundController.getInstance();
        //Load Music
        sounds.makeMusic(EPIC_THEME_FILE);
        sounds.makeMusic(GROOVY_THEME_FILE);
        sounds.makeMusic(VICTORY_THEME_FILE);
        sounds.makeMusic(DEFEAT_FILE);
        //Load SFX
        sounds.makeSound(THWACK_FILE);
        sounds.makeSound(SCREAM1_FILE);
        sounds.makeSound(SCREAM2_FILE);
        sounds.makeSound(SCREAM3_FILE);
        sounds.makeSound(SCREAM4_FILE);
        sounds.makeSound(GROUP_SCREAM1_FILE);
        sounds.makeSound(GROUP_SCREAM2_FILE);


        //sounds.makeSound(VILLAGE_FILE);

        assetManager.load(PAUSE_TEXTURE, Texture.class);
        assetManager.load(RESUME_TEXTURE, Texture.class);
        assetManager.load(RETURN_TEXTURE, Texture.class);
        assetManager.load(BACKGROUND_TEXTURE1, Texture.class);
        assetManager.load(BACKGROUND_TEXTURE2, Texture.class);
        assetManager.load(BACKGROUND_TEXTURE3, Texture.class);
        assetManager.load(HARD_CODED_BACKGROUND, Texture.class);
        assetManager.load(TOWER_CROSSHAIR_TEXTURE, Texture.class);
        assetManager.load(MELEE_TEXTURE, Texture.class);
        assetManager.load(SPEAR_TEXTURE, Texture.class);
        assetManager.load(VILLAGE_TEXTURE, Texture.class);
        assetManager.load(RED_VILLAGE_TEXTURE, Texture.class);
        assetManager.load(BLUE_VILLAGE_TEXTURE, Texture.class);
        assetManager.load(BIG_VILLAGE_TEXTURE, Texture.class);
        assetManager.load(DEFENDER_TEXTURE, Texture.class);
        assetManager.load(ARROW_TEXTURE, Texture.class);
        assetManager.load(TOWER_WALKING_SPRITE, Texture.class);
        assetManager.load(TOWER_FALLING_SPRITE1, Texture.class);
        assetManager.load(TOWER_FALLING_SPRITE2, Texture.class);
        assetManager.load(TOWER_FALLING_UP, Texture.class);
        assetManager.load(TOWER_FALLING_DOWN, Texture.class);
        assetManager.load(TOWER_IDLE_SPRITE, Texture.class);
        assetManager.load(TREE_TEXTURE_1, Texture.class);
        assetManager.load(TREE_TEXTURE_2, Texture.class);
        assetManager.load(TREE_TEXTURE_3, Texture.class);
        assetManager.load(TREE_TEXTURE_4, Texture.class);
        assetManager.load(ROCK_TEXTURE_1, Texture.class);
        assetManager.load(ROCK_TEXTURE_2, Texture.class);
        assetManager.load(ROCK_TEXTURE_3, Texture.class);
        assetManager.load(ROCK_TEXTURE_4, Texture.class);
        assetManager.load(BOULDER_TEXTURE, Texture.class);
        assetManager.load(HP_BAR_TEXTURE_1, Texture.class);
        assetManager.load(HP_BAR_TEXTURE_2, Texture.class);
        assetManager.load(HP_BAR_TEXTURE_3, Texture.class);
        assetManager.load(TOWER_WALKING_SPRITE, Texture.class);
        assetManager.load(TOWER_WALKING_UP_SPRITE, Texture.class);
        assetManager.load(TOWER_WALKING_DOWN_SPRITE, Texture.class);
        assetManager.load(TOWER_IDLE_SPRITE, Texture.class);
        assetManager.load(TOWER_IDLE_UP_SPRITE, Texture.class);
        assetManager.load(TOWER_IDLE_DOWN_SPRITE, Texture.class);
        assetManager.load(LEVEL_SELECT_SCREEN, Texture.class);
        assetManager.load(LEVEL_SELECT_CONTROLS, Texture.class);
        assetManager.load(LEVEL_ONE_WASD, Texture.class);
        assetManager.load(LEVEL_ONE_SPACEBAR, Texture.class);
        assetManager.load(LEFTCLICK, Texture.class);
        assetManager.load(RIGHTCLICK, Texture.class);

        assetManager.load(FLAG_OUTLINE, Texture.class);
        assetManager.load(FLAG_1, Texture.class);
        assetManager.load(FLAG_2, Texture.class);
        assetManager.load(FLAG_3, Texture.class);
        assetManager.load(FLAG_4, Texture.class);
        assetManager.load(FLAG_5, Texture.class);
        assetManager.load(FLAG_6, Texture.class);
        assetManager.load(FLAG_7, Texture.class);
        assetManager.load(FLAG_8, Texture.class);
        assetManager.load(FLAG_9, Texture.class);
        assetManager.load(FLAG_10, Texture.class);
        assetManager.load(FLAG_11, Texture.class);
        assetManager.load(FLAG_12, Texture.class);
        assetManager.load(FLAG_13, Texture.class);
        assetManager.load(FLAG_14, Texture.class);
        assetManager.load(FLAG_15, Texture.class);
        assetManager.load(LS_ARROW, Texture.class);
        assetManager.load(PLAYER_SPAWN_TEXTURE, Texture.class);
        assetManager.load(WATER_1, Texture.class);

        assetManager.load(F_TEXT, Texture.class);

        assetManager.load(SWORD_WALKING, Texture.class);
        assetManager.load(SPEAR_WALKING, Texture.class);
        assetManager.load(ARCHER_SHOOTING, Texture.class);

        theAtlas.addRegion("whitePuff22", new TextureRegion(new Texture("particles/White puff/whitePuff22.png")));
        theAtlas.addRegion("whitePuff09", new TextureRegion(new Texture("particles/White puff/whitePuff09.png")));
        theAtlas.addRegion("blackSmoke08", new TextureRegion(new Texture("particles/Black smoke/blackSmoke08.png")));
        theAtlas.addRegion("flash05", new TextureRegion(new Texture("particles/Flash/flash05.png")));
        theAtlas.addRegion("whitePuff24", new TextureRegion(new Texture("particles/White Puff/whitePuff24.png")));

    }

    public static void preLoadContentLevelEditor() {
        //assetManager.load(BACKGROUND_TEXTURE, Texture.class);
    }

    public void loadContent(){
        assetManager.finishLoading();
        //backgroundTexture = createTexture(assetManager, BACKGROUND_TEXTURE, true);
        pauseTexture = createTexture(assetManager, PAUSE_TEXTURE, false);
        resumeTexture = createTexture(assetManager, RESUME_TEXTURE, false);
        returnTexture = createTexture(assetManager, RETURN_TEXTURE, false);
        backgroundTexture1 = createTexture(assetManager, BACKGROUND_TEXTURE1, true);
        backgroundTexture2 = createTexture(assetManager, BACKGROUND_TEXTURE2, true);
        backgroundTexture3 = createTexture(assetManager, BACKGROUND_TEXTURE3, true);
        hard_coded_background = createTexture(assetManager, HARD_CODED_BACKGROUND, false);
        crosshairTexture = createTexture(assetManager, TOWER_CROSSHAIR_TEXTURE, false);
        meleeTexture = createTexture(assetManager, MELEE_TEXTURE, false);
        spearTexture = createTexture(assetManager, SPEAR_TEXTURE, false);
        villageTexture = createTexture(assetManager, VILLAGE_TEXTURE, false);
        redVillageTexture = createTexture(assetManager, RED_VILLAGE_TEXTURE, false);
        blueVillageTexture = createTexture(assetManager, BLUE_VILLAGE_TEXTURE, false);
        bigVillageTexture = createTexture(assetManager, BIG_VILLAGE_TEXTURE, false);
        defenderTexture = createTexture(assetManager, DEFENDER_TEXTURE, false);
        arrowTexture = createTexture(assetManager, ARROW_TEXTURE, false);
        treeTexture_1 = createTexture(assetManager, TREE_TEXTURE_1, false);
        treeTexture_2 = createTexture(assetManager, TREE_TEXTURE_2, false);
        treeTexture_3 = createTexture(assetManager, TREE_TEXTURE_3, false);
        treeTexture_4 = createTexture(assetManager, TREE_TEXTURE_4, false);
        rockTexture_1 = createTexture(assetManager, ROCK_TEXTURE_1, false);
        rockTexture_2 = createTexture(assetManager, ROCK_TEXTURE_2, false);
        rockTexture_3 = createTexture(assetManager, ROCK_TEXTURE_3, false);
        rockTexture_4 = createTexture(assetManager, ROCK_TEXTURE_4, false);
        boulderTexture = createTexture(assetManager, BOULDER_TEXTURE, false);
        playerSpawnTexture = createTexture(assetManager, PLAYER_SPAWN_TEXTURE, false);

        HPBarTexture1 = createTexture(assetManager, HP_BAR_TEXTURE_1, false);
        HPBarTexture2 = createTexture(assetManager, HP_BAR_TEXTURE_2, false);
        HPBarTexture3 = createTexture(assetManager, HP_BAR_TEXTURE_3, false);

        towerWalkingAnimation = createFilmStrip(assetManager, TOWER_WALKING_SPRITE, 1, 9, 9);
        towerWalkingUpAnimation = createFilmStrip(assetManager, TOWER_WALKING_UP_SPRITE, 1, 8, 8);
        towerWalkingDownAnimation = createFilmStrip(assetManager, TOWER_WALKING_DOWN_SPRITE, 1, 8, 8);
        towerFallingAnimation1 = createFilmStrip(assetManager, TOWER_FALLING_SPRITE1, 1, 18, 18);
        towerFallingAnimation2 = createFilmStrip(assetManager, TOWER_FALLING_SPRITE2, 1, 18, 18);
        towerFallingUpAnimation = createFilmStrip(assetManager, TOWER_FALLING_UP, 1, 35, 35);
        towerFallingDownAnimation = createFilmStrip(assetManager, TOWER_FALLING_DOWN, 1, 35, 35);
        towerIdleSprite = createFilmStrip(assetManager, TOWER_IDLE_SPRITE, 1, 1, 1);
        towerIdleUpSprite = createFilmStrip(assetManager, TOWER_IDLE_UP_SPRITE, 1, 1, 1);
        towerIdleDownSprite = createFilmStrip(assetManager, TOWER_IDLE_DOWN_SPRITE, 1, 1, 1);

        swordWalkingAnimation = createFilmStrip(assetManager, SWORD_WALKING, 1, 4, 4);
        spearWalkingAnimation = createFilmStrip(assetManager, SPEAR_WALKING, 1, 4, 4);
        archerShootingAnimation = createFilmStrip(assetManager, ARCHER_SHOOTING, 1, 4, 4);

        level_select_screen = createTexture(assetManager, LEVEL_SELECT_SCREEN, false);
        level_select_controls = createTexture(assetManager, LEVEL_SELECT_CONTROLS, false);
        level_one_wasd = createTexture(assetManager, LEVEL_ONE_WASD, false);
        level_one_spacebar = createTexture(assetManager, LEVEL_ONE_SPACEBAR, false);
        f_text = createTexture(assetManager, F_TEXT, false);
        left_click = createFilmStrip(assetManager, LEFTCLICK, 1, 30, 30);
        right_click = createFilmStrip(assetManager, RIGHTCLICK, 1, 30, 30);

        flag_outline = createTexture(assetManager, FLAG_OUTLINE, false);
        flag_1 = createTexture(assetManager, FLAG_1, false);
        flag_2 = createTexture(assetManager, FLAG_2, false);
        flag_3 = createTexture(assetManager, FLAG_3, false);
        flag_4 = createTexture(assetManager, FLAG_4, false);
        flag_5 = createTexture(assetManager, FLAG_5, false);
        flag_6 = createTexture(assetManager, FLAG_6, false);
        flag_7 = createTexture(assetManager, FLAG_7, false);
        flag_8 = createTexture(assetManager, FLAG_8, false);
        flag_9 = createTexture(assetManager, FLAG_9, false);
        flag_10 = createTexture(assetManager, FLAG_10, false);
        flag_11 = createTexture(assetManager, FLAG_11, false);
        flag_12 = createTexture(assetManager, FLAG_12, false);
        flag_13 = createTexture(assetManager, FLAG_13, false);
        flag_14 = createTexture(assetManager, FLAG_14, false);
        flag_15 = createTexture(assetManager, FLAG_15, false);
        ls_arrow = createTexture(assetManager, LS_ARROW, false);

        water1 = createTexture(assetManager, WATER_1, false);
    }

    public void loadContentLevelEditor () {
        assetManager.finishLoading();
      //  backgroundTexture = createTexture(assetManager, BACKGROUND_TEXTURE, true);
    }

    /**
     * Returns a newly loaded texture region for the given file.
     *
     * This helper methods is used to set texture settings (such as scaling, and
     * whether or not the texture should repeat) after loading.
     *
     * @param manager 	Reference to global asset manager.
     * @param file		The texture (region) file
     * @param repeat	Whether the texture should be repeated
     *
     * @return a newly loaded texture region for the given file.
     */
    protected TextureRegion createTexture(AssetManager manager, String file, boolean repeat) {
        if (manager.isLoaded(file)) {
            Texture t = manager.get(file, Texture.class);
            TextureRegion region = new TextureRegion(t);
            if (repeat) { //if the texture is a tile meant to be repeated, i.e. a background a tile
                region.getTexture().setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            }
            else{
                region.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            }
            return region;
        }
        return null;
    }

    /**
     * Returns a newly loaded filmstrip for the given file.
     *
     * This helper methods is used to set texture settings (such as scaling, and
     * the number of animation frames) after loading.
     *
     * @param manager 	Reference to global asset manager.
     * @param file		The texture (region) file
     * @param rows 		The number of rows in the filmstrip
     * @param cols 		The number of columns in the filmstrip
     * @param size 		The number of frames in the filmstrip
     *
     * @return a newly loaded texture region for the given file.
     */
    protected FilmStrip createFilmStrip(AssetManager manager, String file, int rows, int cols, int size) {
        if (manager.isLoaded(file)) {
            FilmStrip strip = new FilmStrip(manager.get(file, Texture.class),rows,cols,size);
            strip.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            return strip;
        }
        return null;
    }

    /**
     * Unloads the assets for this game.
     *
     * This method erases the static variables.  It also deletes the associated textures
     * from the asset manager. If no assets are loaded, this method does nothing.
     *
     * @param manager Reference to global asset manager.
     */
    public void unloadContent(ResourceManager manager) {
        // TODO: assetManager.unload everything
    }

//    public void queueAddFonts(){
//
//    }
//
//    public void queueAddImages(){
//        assetManager.load(playerImages, TextureAtlas.class);
//        assetManager.load(tileImages, TextureAtlas.class);
//        assetManager.load(levelImages, TextureAtlas.class);
//    }
//
//    public void queueAddLoadingImages(){
//        assetManager.load(loadingImages, TextureAtlas.class);
//    }
//
//    public void queueAddSkin(){
//        SkinParameter params = new SkinParameter("skin/glassy-ui.atlas");
//        assetManager.load(skin, Skin.class, params);
//    }
//
//    public void queueAddMusic(){
//        assetManager.load(playingSong, Music.class);
//    }
//
//    public void queueAddSounds(){
//        assetManager.load(deeperThwack, Sound.class);
//        assetManager.load(menuTheme, Sound.class);
//    }

}
