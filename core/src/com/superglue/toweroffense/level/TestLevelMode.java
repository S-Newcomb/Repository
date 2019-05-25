package com.superglue.toweroffense.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import com.superglue.toweroffense.GDXRoot;
import com.superglue.toweroffense.InputController;
import com.superglue.toweroffense.WorldController;
import com.superglue.toweroffense.enemies.*;
import com.superglue.toweroffense.collision.CollisionManager;
import com.superglue.toweroffense.ResourceManager;
import com.superglue.toweroffense.level.pathfinding.LevelGraph;
import com.superglue.toweroffense.levelEditor.LevelEditorController;
import com.superglue.toweroffense.obstacles.Rock;
import com.superglue.toweroffense.obstacles.Tree;
import com.superglue.toweroffense.player.*;
import com.superglue.toweroffense.gameobjects.GameObject;
import com.superglue.toweroffense.projectiles.Projectile;
import com.superglue.toweroffense.projectiles.ProjectilePool;
import com.superglue.toweroffense.util.SoundController;
import com.superglue.toweroffense.util.Updatable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;


/** This should just set up a bare background and the player. */

public class TestLevelMode extends WorldController {
    private GDXRoot parent;

    private static final SoundController sounds = SoundController.getInstance();
    private ResourceManager resourceManager;
    private PlayerRoomController playerRoomController;
    private PlayerRoom engineRoom;
    private PlayerRoom sacrificeRoom;
    private PlayerRoom lifeStealRoom;
    private PlayerRoom hardenRoom;
    private ArrayList<PlayerRoom> roomList;

    private boolean engineRoomActive = false;
    private boolean sacrificeRoomActive = false;
    private boolean lifeStealRoomActive = false;
    private boolean hardenRoomActive = false;

    private PlayerRoom room1;
    private PlayerRoom room2;
    private PlayerRoom room3;
    private PlayerRoom room4;

    /** LEVEL LOGIC */
    private static final float PIXELS_TO_METERS = 100f;

    // pathfinding graph
    private LevelGraph levelGraph;

    private HashSet<Updatable> updatables;
    private CollisionManager collisionManager;

    private Background background;
    private PlayerController playerController;
    private Player player;
    private boolean endGame;

    private EnemyController enemyController;
    private VillageController villageController;
    private ProjectilePool projectilePool;

    public LevelEditorController.JLevel level;

    public TestLevelMode(GDXRoot parent) {
        this.parent = parent;
        this.level = level;
        this.resourceManager = parent.resourceManager;

        levelGraph = new LevelGraph(3780/PIXELS_TO_METERS, 3456/PIXELS_TO_METERS);

        updatables = new HashSet<>();
        collisionManager = CollisionManager.getInstance();

        scale.scl(PIXELS_TO_METERS);

        setDebug(false);
        setComplete(false);
        setFailure(false);
        world.setContactListener(collisionManager);

        world.setGravity(new Vector2(0, 0));
        endGame = false;
    }

    private float pixelsToMeters(float pixels) {
        return pixels / scale.x;
    }

    /**
     * Resets the status of the game so that we can play again.
     * <p>
     * This method disposes of the world and creates a new one.
     */
    public void reset() { //TODO: make reset function for enemyController
        updatables.clear();

        // physics world stuff
        Vector2 gravity = new Vector2(world.getGravity());

        for (GameObject obj : objects) {
            obj.deactivatePhysics(world);
        }

        objects.clear();
        addQueue.clear();
        world.dispose();

        world = new World(gravity, false);
        world.setContactListener(collisionManager);
        // put everything back
        populateLevel();

        endGame = false;
    }

    private void populateLevel(){
        addBackground();

        addPlayer();

        addEnemies();

        addVillages();

        addObstacles();

        //canvas.createHud(player.getHealth(),player.getMaxHealth(), player.getPopulation(), engineRoom.getPopulation(), lifeStealRoom.getPopulation(), hardenRoom.getPopulation(), 0, room1.getName(), room2.getName(), room3.getName(), room4.getName());
    }

    void addBackground() {
        background = new Background(5, 6);
        //background.setTexture(resourceManager.backgroundTexture);
        background.addTextures(ResourceManager.backgroundTexture1, ResourceManager.backgroundTexture2, ResourceManager.backgroundTexture3);
        background.setTexture(ResourceManager.hard_coded_background);
        addObject(background);
    }

    void addPlayer() {
        // add player avatar
        Vector2 PLAYER_START_POS = new Vector2(37, 8);

//        float playerWidth = pixelsToMeters(ResourceManager.towerIdleSprite.getRegionWidth());
//        float playerHeight = pixelsToMeters(ResourceManager.towerIdleSprite.getRegionHeight());
        float playerWidth = PlayerConstants.HORIZONTAL_ATTACK_HITBOX_WIDTH;
        float playerHeight = PlayerConstants.HORIZONTAL_ATTACK_HITBOX_HEIGHT;

        player = new Player(PLAYER_START_POS.x, PLAYER_START_POS.y, playerWidth, playerHeight);
        player.setDrawScale(scale);
        player.setTexture(resourceManager.towerIdleSprite);
        addObject(player);

        // starts off negative so that it is placed off-screen
        Vector2 CROSSHAIR_START_POS = PlayerConstants.DEFAULT_CROSSHAIR_POS;

        final float CROSSHAIR_WIDTH = pixelsToMeters(resourceManager.crosshairTexture.getRegionWidth());
        final float CROSSHAIR_HEIGHT = pixelsToMeters(resourceManager.crosshairTexture.getRegionHeight());
        PlayerCrosshair playerCrosshair = new PlayerCrosshair(
                CROSSHAIR_START_POS.x, CROSSHAIR_START_POS.y,
                CROSSHAIR_WIDTH, CROSSHAIR_HEIGHT
        );
        playerCrosshair.setTexture(resourceManager.crosshairTexture);
        addObject(playerCrosshair);

        playerController = new PlayerController(player, playerCrosshair);

       // playerController.setCamera(canvas.getCamera(), 800, 2370, 370, 1720);

        updatables.add(playerController);

        //Initialize Rooms
        playerRoomController = new PlayerRoomController(player);
        engineRoom = playerRoomController.getRoom("Engine Room");
        sacrificeRoom = playerRoomController.getRoom("Sacrifice Room");
        lifeStealRoom = playerRoomController.getRoom("LifeSteal Room");
        hardenRoom = playerRoomController.getRoom("Harden Room");
        roomList = new ArrayList<>(Arrays.asList(engineRoom,sacrificeRoom,lifeStealRoom,hardenRoom));

        int roomPos = 0;
        for (Integer room : level.activeRooms){
            if(room == 1){
                if (room1 == null) {
                    room1 = roomList.get(roomPos);
                }
                else if (room2 == null){
                    room2 = roomList.get(roomPos);
                }
                else if (room3 == null) {
                    room3 = roomList.get(roomPos);
                }
                else if (room4 == null) {
                    room4 = roomList.get(roomPos);
                }
            }
            roomPos++;
        }
    }

    void addEnemies(){
        projectilePool = new ProjectilePool(1000, world);
        enemyController = new EnemyController(player, projectilePool);
        updatables.add(enemyController);
    }

    void addVillages() {
        villageController = new VillageController(enemyController, player);

        updatables.add(villageController);

        Village meleeVillage = villageController.createVillage(
                pixelsToMeters(2900), pixelsToMeters(500),
                pixelsToMeters(resourceManager.villageTexture.getRegionWidth()), pixelsToMeters(resourceManager.villageTexture.getRegionHeight()),
                Village.EnemyType.MELEE,
                pixelsToMeters(resourceManager.meleeTexture.getRegionWidth()), pixelsToMeters(resourceManager.meleeTexture.getRegionHeight()),
                scale, resourceManager.meleeTexture);

        Village meleeVillage2 = villageController.createVillage(
                pixelsToMeters(2900), pixelsToMeters(800),
                pixelsToMeters(resourceManager.villageTexture.getRegionWidth()), pixelsToMeters(resourceManager.villageTexture.getRegionHeight()),
                Village.EnemyType.MELEE,
                pixelsToMeters(resourceManager.meleeTexture.getRegionWidth()), pixelsToMeters(resourceManager.meleeTexture.getRegionHeight()),
                scale, resourceManager.meleeTexture);

        Village meleeVillage3 = villageController.createVillage(
                pixelsToMeters(2900), pixelsToMeters(200),
                pixelsToMeters(resourceManager.villageTexture.getRegionWidth()), pixelsToMeters(resourceManager.villageTexture.getRegionHeight()),
                Village.EnemyType.MELEE,
                pixelsToMeters(resourceManager.meleeTexture.getRegionWidth()), pixelsToMeters(resourceManager.meleeTexture.getRegionHeight()),
                scale, resourceManager.meleeTexture);

        Village spearVillage = villageController.createVillage(
                pixelsToMeters(400), pixelsToMeters(800),
                pixelsToMeters(resourceManager.villageTexture.getRegionWidth()), pixelsToMeters(resourceManager.villageTexture.getRegionHeight()),
                Village.EnemyType.SPEAR,
                pixelsToMeters(resourceManager.spearTexture.getRegionWidth()), pixelsToMeters(resourceManager.spearTexture.getRegionHeight()),
                scale, resourceManager.spearTexture);

        Village spearVillage2 = villageController.createVillage(
                pixelsToMeters(400), pixelsToMeters(500),
                pixelsToMeters(resourceManager.villageTexture.getRegionWidth()), pixelsToMeters(resourceManager.villageTexture.getRegionHeight()),
                Village.EnemyType.SPEAR,
                pixelsToMeters(resourceManager.spearTexture.getRegionWidth()), pixelsToMeters(resourceManager.spearTexture.getRegionHeight()),
                scale, resourceManager.spearTexture);

        Village defenderVillage = villageController.createVillage(
                pixelsToMeters(2200), pixelsToMeters(1675),
                pixelsToMeters(resourceManager.villageTexture.getRegionWidth()), pixelsToMeters(resourceManager.villageTexture.getRegionHeight()),
                Village.EnemyType.DEFENDER,
                pixelsToMeters(resourceManager.defenderTexture.getRegionWidth()), pixelsToMeters(resourceManager.defenderTexture.getRegionHeight()),
                scale, resourceManager.defenderTexture);

        Village defenderVillage2 = villageController.createVillage(
                pixelsToMeters(400), pixelsToMeters(1675),
                pixelsToMeters(resourceManager.villageTexture.getRegionWidth()), pixelsToMeters(resourceManager.villageTexture.getRegionHeight()),
                Village.EnemyType.DEFENDER,
                pixelsToMeters(resourceManager.defenderTexture.getRegionWidth()), pixelsToMeters(resourceManager.defenderTexture.getRegionHeight()),
                scale, resourceManager.defenderTexture);

        meleeVillage.setDrawScale(scale);
        meleeVillage.setTexture(resourceManager.villageTexture);
        meleeVillage.setHPBarTextures(resourceManager.HPBarTexture1, resourceManager.HPBarTexture2, resourceManager.HPBarTexture3);
        meleeVillage2.setDrawScale(scale);
        meleeVillage2.setTexture(resourceManager.villageTexture);
        meleeVillage2.setHPBarTextures(resourceManager.HPBarTexture1, resourceManager.HPBarTexture2, resourceManager.HPBarTexture3);
        meleeVillage3.setDrawScale(scale);
        meleeVillage3.setTexture(resourceManager.villageTexture);
        meleeVillage3.setHPBarTextures(resourceManager.HPBarTexture1, resourceManager.HPBarTexture2, resourceManager.HPBarTexture3);

        spearVillage.setDrawScale(scale);
        spearVillage.setTexture(resourceManager.villageTexture);
        spearVillage.setHPBarTextures(resourceManager.HPBarTexture1, resourceManager.HPBarTexture2, resourceManager.HPBarTexture3);
        spearVillage2.setDrawScale(scale);
        spearVillage2.setTexture(resourceManager.villageTexture);
        spearVillage2.setHPBarTextures(resourceManager.HPBarTexture1, resourceManager.HPBarTexture2, resourceManager.HPBarTexture3);

        defenderVillage.setDrawScale(scale);
        defenderVillage.setTexture(resourceManager.villageTexture);
        defenderVillage.setHPBarTextures(resourceManager.HPBarTexture1, resourceManager.HPBarTexture2, resourceManager.HPBarTexture3);
        defenderVillage2.setDrawScale(scale);
        defenderVillage2.setTexture(resourceManager.villageTexture);
        defenderVillage2.setHPBarTextures(resourceManager.HPBarTexture1, resourceManager.HPBarTexture2, resourceManager.HPBarTexture3);

        addObject(meleeVillage);
        addObject(meleeVillage2);
        addObject(meleeVillage3);
        addObject(spearVillage);
        addObject(spearVillage2);
        addObject(defenderVillage);
        addObject(defenderVillage2);
    }

    void addObstacles(){
        for(int i = 0; i < 31; i++) {
            Tree tree = new Tree(pixelsToMeters(54*i + 1476), pixelsToMeters(1100),
                    pixelsToMeters(resourceManager.treeTexture_1.getRegionWidth()), pixelsToMeters(resourceManager.treeTexture_1.getRegionHeight()));
            tree.setDrawScale(scale);
            tree.setTexture(resourceManager.treeTexture_1);
            addObject(tree);
        }
        for(int i = 0; i < 58; i++) {
            Tree tree = new Tree(pixelsToMeters(54*i), pixelsToMeters(2150),
                    pixelsToMeters(resourceManager.treeTexture_1.getRegionWidth()), pixelsToMeters(resourceManager.treeTexture_1.getRegionHeight()));
            tree.setDrawScale(scale);
            tree.setTexture(resourceManager.treeTexture_1);
            addObject(tree);
        }
        for(int i = 0; i < 57; i++) {
            Tree tree = new Tree(pixelsToMeters(54*i + 40), pixelsToMeters(-50),
                    pixelsToMeters(resourceManager.treeTexture_1.getRegionWidth()), pixelsToMeters(resourceManager.treeTexture_1.getRegionHeight()));
            tree.setDrawScale(scale);
            tree.setTexture(resourceManager.treeTexture_1);
            addObject(tree);
        }
        for(int i = 0; i < 40; i++) {
            Tree tree = new Tree(pixelsToMeters(3150), pixelsToMeters(75*i - 50),
                    pixelsToMeters(resourceManager.treeTexture_1.getRegionWidth()), pixelsToMeters(resourceManager.treeTexture_1.getRegionHeight()));
            tree.setDrawScale(scale);
            tree.setTexture(resourceManager.treeTexture_1);
            addObject(tree);
        }
        for(int i = 0; i < 40; i++) {
            Tree tree = new Tree(pixelsToMeters(0), pixelsToMeters(75*i - 50),
                    pixelsToMeters(resourceManager.treeTexture_1.getRegionWidth()), pixelsToMeters(resourceManager.treeTexture_1.getRegionHeight()));
            tree.setDrawScale(scale);
            tree.setTexture(resourceManager.treeTexture_1);
            addObject(tree);
        }
        for(int i = 0; i < 20; i++) {
            if(i >= 12 && i <= 15) continue;
            Rock rock = new Rock(pixelsToMeters(1476), pixelsToMeters(54*i + 20),
                    pixelsToMeters(resourceManager.rockTexture_1.getRegionWidth()), pixelsToMeters(resourceManager.rockTexture_1.getRegionHeight()));
            rock.setDrawScale(scale);
            rock.setTexture(resourceManager.rockTexture_1);
            addObject(rock);
        }
        for(int i = 0; i < 20; i++) {
            if(i >= 12 && i <= 15) continue;
            Rock rock = new Rock(pixelsToMeters(1536), pixelsToMeters(54*i + 20),
                    pixelsToMeters(resourceManager.rockTexture_1.getRegionWidth()), pixelsToMeters(resourceManager.rockTexture_1.getRegionHeight()));
            rock.setDrawScale(scale);
            rock.setTexture(resourceManager.rockTexture_1);
            addObject(rock);
        }
        for(int i = 0; i < 20; i++) {
            if(i >= 12 && i <= 15) continue;
            Rock rock = new Rock(pixelsToMeters(1596), pixelsToMeters(54*i + 20),
                    pixelsToMeters(resourceManager.rockTexture_1.getRegionWidth()), pixelsToMeters(resourceManager.rockTexture_1.getRegionHeight()));
            rock.setDrawScale(scale);
            rock.setTexture(resourceManager.rockTexture_1);
            addObject(rock);
        }
    }

    void playLandingSound() {
        Sound thwack = sounds.getSound("DeeperThwack");
        thwack.play(.8f);
    }

    /**
     * The core gameplay loop of this world.
     * <p>
     * This method contains the specific update code for this mini-game. It does
     * not handle collisions, as those are managed by the parent class WorldController.
     * This method is called after input is read, but before collisions are resolved.
     * The very last thing that it should do is apply forces to the appropriate objects.
     *
     * @param dt Number of seconds since last animation frame
     */
    public void update(float dt) {
        // perform time step for all updatable objects
        for (Updatable u : updatables) {
            u.update(dt);
        }

        // perform time step for villages
        for (Village v : villageController.getVillageList()) {
            if (v.didSpawnEnemy()) {
                addObject(v.getLastEnemySpawned());
            }
        }
        if(enemyController.getShoot()){
            for (Projectile p : enemyController.getLastProjectilesSpawned()) {
                    p.setTexture(resourceManager.arrowTexture);
                    p.setDrawScale(scale);
                    addObject(p);
                    updatables.add(p);
            }
        }

        updatePlayer();
    }

    private void updatePlayer() {
        // check if reset pressed
        if(InputController.getInstance().didReset()){
            reset();
        }

        //Player allocation stuff
        if(player.isDead()) {
            if(!player.hasBeenDestroyed()){
                player.setHasBeenDestroyed();
                collisionManager.getObjectsToDestroy().add(player);
            }

            return;
        }

        Vector2 mousePos = InputController.getInstance().getMousePos();
        checkRoom1(mousePos.x, mousePos.y);
        checkRoom2(mousePos.x, mousePos.y);
        checkRoom3(mousePos.x, mousePos.y);
        checkRoom4(mousePos.x, mousePos.y);

        player.setTotalPopulation(player.getPopulation() + engineRoom.getPopulation() + lifeStealRoom.getPopulation() + player.getSacrificed() + hardenRoom.getPopulation());

        if(player.hasJustLanded()){
            playLandingSound();
            float delay = .05f;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    //LifeSteal Code
                    int newPop = player.getTotalPopulation() - player.getOldPopulation();
                    playerRoomController.lifeStealRoomCheck(newPop);
                    player.setOldPopulation(player.getTotalPopulation());
                    if (newPop > 0 && newPop < 20) sounds.scream();
                    else if (newPop >= 20) sounds.smallGroupScream();
                }
            }, delay);
        }

        canvas.updateHud(
                player.getHealth(),
                player.getMaxHealth(),
                player.getPopulation(),
                engineRoom.getPopulation(),
                lifeStealRoom.getPopulation(),
                hardenRoom.getPopulation(),
                player.getAmmo()
        );
    }

    @Override
    public void postUpdate(float dt){
        super.postUpdate(dt);
        destroyBodies();
        destroyArrows();

        postUpdatePlayer();
    }

    private void postUpdatePlayer(){
        if(player.hasJustShot()){

            float delay = .05f;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    //LifeSteal Code
                    int newPop = player.getTotalPopulation() - player.getOldPopulation();
                    playerRoomController.lifeStealRoomCheck(newPop);
                    player.setOldPopulation(player.getTotalPopulation());
                    //canvas.updateHud(player.getHealth(),player.getMaxHealth(), player.getPopulation(), engineRoom.getPopulation(), lifeStealRoom.getPopulation(), hardenRoom.getPopulation());
                }

            }, delay);

            // spawn boulder at location
            Vector2 shotLocation = player.getShotLocation();
            Rock playerBoulder = new Rock(
                    shotLocation.x + PlayerConstants.BOULDER_OFFSET_X,
                    shotLocation.y + PlayerConstants.BOULDER_OFFSET_Y,
                    pixelsToMeters(ResourceManager.boulderTexture.getRegionWidth()),
                    pixelsToMeters(ResourceManager.boulderTexture.getRegionHeight())
            );
            playerBoulder.setDrawScale(scale);
            playerBoulder.setTexture(ResourceManager.boulderTexture);
            addObject(playerBoulder);
        }
    }

    private void destroyBodies() {
        if(collisionManager.getObjectsToDestroy().isEmpty()) return;

        // destroy bodies that are now dead
        for(GameObject o : collisionManager.getObjectsToDestroy()){
            Body b = o.getBody();
            o.setActive(false);
            b.setActive(false);
            objects.remove(o);
            updatables.remove(o);
            if(o instanceof Enemy){
                enemyController.removeEnemy((Enemy) o);
            }
            if(o instanceof Village){
                villageController.removeVillage((Village) o);
                if(villageController.getVillageList().isEmpty()) {
                    if (!endGame) {
                        canvas.victoryScreen();
                        endGame = true;
                        player.setInvincible();

                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                //switch back to level select screen
                                parent.exitToLevelSelect(true);

                            }

                        }, 4.0f);
                    }
                }
            }
            if (o instanceof Player) {
                if(!endGame) {
                    canvas.defeatScreen();
                    endGame = true;
                }

            }

        }

        // don't want to remove the same body twice!
        collisionManager.clearObjectsToDestroy();
    }

    private void destroyArrows() {
        if(projectilePool.getDeadProjectiles().isEmpty()) return;

        // destroy bodies that are now dead
        for(Projectile p : projectilePool.getDeadProjectiles()){
            objects.remove(p);
            updatables.remove(p);
            projectilePool.free(p);
        }

        // don't want to remove the same body twice!
        projectilePool.clearDeadProjectiles();
    }

    public void checkRoom1 (float x, float y) {
        if (canvas.inRoom1Region(x, y) && Gdx.input.justTouched()) {
            //Add population to room if left clicked in bounds
            if (InputController.getInstance().leftClick())
                playerRoomController.addToRoom(engineRoom, 10);
            canvas.updateHud(player.getHealth(),player.getMaxHealth(), player.getPopulation(), engineRoom.getPopulation(), lifeStealRoom.getPopulation(), hardenRoom.getPopulation(), player.getAmmo());
            playerRoomController.engineRoomCheck();
            //Remove population from room if right clicked in bounds
            if (InputController.getInstance().rightClick()) {
                playerRoomController.removeFromRoom(engineRoom, 10);
                canvas.updateHud(player.getHealth(),player.getMaxHealth(), player.getPopulation(), engineRoom.getPopulation(), lifeStealRoom.getPopulation(), hardenRoom.getPopulation(), player.getAmmo());
                playerRoomController.engineRoomCheck();
            }
        }
    }

    public void checkRoom2 (float x, float y) {
        if (canvas.inRoom2Region(x, y) && Gdx.input.justTouched()) {
            //Add population to room if left clicked in bounds
            if (InputController.getInstance().leftClick()) {
                playerRoomController.addToRoom(sacrificeRoom, 10);
                playerRoomController.sacrificeRoomCheck();
                canvas.updateHud(
                        player.getHealth(),
                        player.getMaxHealth(),
                        player.getPopulation(),
                        engineRoom.getPopulation(),
                        lifeStealRoom.getPopulation(),
                        hardenRoom.getPopulation(),
                        player.getAmmo()
                );
                playerRoomController.hardenRoomCheck();

            }
        }
    }

    public void checkRoom3 (float x, float y) {
        if (canvas.inRoom3Region(x, y) && Gdx.input.justTouched()) {
            //Add population to room if left clicked in bounds
            if (InputController.getInstance().leftClick())
                playerRoomController.addToRoom(lifeStealRoom, 10);
            canvas.updateHud(
                    player.getHealth(),
                    player.getMaxHealth(),
                    player.getPopulation(),
                    engineRoom.getPopulation(),
                    lifeStealRoom.getPopulation(),
                    hardenRoom.getPopulation(),
                    player.getAmmo()
            );
            //Remove population from room if right clicked in bounds
            if (InputController.getInstance().rightClick()) {
                playerRoomController.removeFromRoom(lifeStealRoom, 10);
                canvas.updateHud(
                        player.getHealth(),
                        player.getMaxHealth(),
                        player.getPopulation(),
                        engineRoom.getPopulation(),
                        lifeStealRoom.getPopulation(),
                        hardenRoom.getPopulation(),
                        player.getAmmo()
                );
            }
        }
    }

    public void checkRoom4 (float x, float y) {
        if (canvas.inRoom4Region(x, y) && Gdx.input.justTouched()) {
            //Add population to room if left clicked in bounds
            if (InputController.getInstance().leftClick())
                playerRoomController.addToRoom(hardenRoom, 10);
            canvas.updateHud(
                    player.getHealth(),
                    player.getMaxHealth(),
                    player.getPopulation(),
                    engineRoom.getPopulation(),
                    lifeStealRoom.getPopulation(),
                    hardenRoom.getPopulation(),
                    player.getAmmo()
            );
            playerRoomController.hardenRoomCheck();
            //Remove population from room if right clicked in bounds
            if (InputController.getInstance().rightClick()) {
                playerRoomController.removeFromRoom(hardenRoom, 10);
                canvas.updateHud(
                        player.getHealth(),
                        player.getMaxHealth(),
                        player.getPopulation(),
                        engineRoom.getPopulation(),
                        lifeStealRoom.getPopulation(),
                        hardenRoom.getPopulation(),
                        player.getAmmo()
                );
                playerRoomController.hardenRoomCheck();
            }
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        canvas.drawHud();
    }
}