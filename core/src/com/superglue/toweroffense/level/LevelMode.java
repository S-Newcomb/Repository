package com.superglue.toweroffense.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.superglue.toweroffense.GDXRoot;
import com.superglue.toweroffense.InputController;
import com.superglue.toweroffense.ResourceManager;
import com.superglue.toweroffense.WorldController;
import com.superglue.toweroffense.collision.CollisionManager;
import com.superglue.toweroffense.enemies.*;
import com.superglue.toweroffense.factories.LevelFactory;
import com.superglue.toweroffense.gameobjects.BoxGameObject;
import com.superglue.toweroffense.gameobjects.GameObject;
import com.superglue.toweroffense.gameobjects.GameObjectComparator;
import com.superglue.toweroffense.levelEditor.LevelEditorController;
import com.superglue.toweroffense.obstacles.Rock;
import com.superglue.toweroffense.particles.ShroomCloudPool;
import com.superglue.toweroffense.player.*;
import com.superglue.toweroffense.projectiles.Projectile;
import com.superglue.toweroffense.projectiles.ProjectilePool;
import com.superglue.toweroffense.util.Constants;
import com.superglue.toweroffense.util.SoundController;
import com.superglue.toweroffense.util.Updatable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class LevelMode extends WorldController {
    private static final SoundController sounds = SoundController.getInstance();
    private GDXRoot parent;
    private ResourceManager resourceManager;
    private PlayerRoomController playerRoomController;
    private PlayerRoom engineRoom;
    private PlayerRoom sacrificeRoom;
    private PlayerRoom lifeStealRoom;
    private PlayerRoom hardenRoom;
    private ArrayList<PlayerRoom> roomList;

    private boolean paused;

    private boolean isLevelOne;
    private boolean isLevelThree;
    private boolean isLevelFour;
    private boolean isLevelEight;

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
    public ShroomCloudPool shroomCloudPool;
    public ShroomCloudPool firePool;

    public LevelMode(GDXRoot parent, LevelEditorController.JLevel level) {
        this.parent = parent;
        this.level = level;

        this.resourceManager = parent.resourceManager;
        updatables = new HashSet<>();
        collisionManager = CollisionManager.getInstance();

        scale.scl(PIXELS_TO_METERS);

        setDebug(false);
        setComplete(false);
        setFailure(false);
        world.setContactListener(collisionManager);

        world.setGravity(new Vector2(0, 0));

        paused = false;
        endGame = false;

        shroomCloudPool= new ShroomCloudPool(200, 2000);
        collisionManager.scp = shroomCloudPool;

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

        for (GameObject obj : objects) {
            obj.deactivatePhysics(world);
        }

        objects.clear();
        addQueue.clear();
        world.dispose();

        world = new World(new Vector2(0, 0), false);
        world.setContactListener(collisionManager);

        engineRoomActive = false;
        sacrificeRoomActive = false;
        lifeStealRoomActive = false;
        hardenRoomActive = false;

        room1 = null;
        room2 = null;
        room3 = null;
        room4 = null;

        PlayerConstants.ANIMATION_FRAME_RATE = 15f;

        populateLevel(this.level);

        sounds.stopAll();
        sounds.getMusic("Groovin Drums3").stop();
        sounds.getMusic("Groovin Drums3").setVolume(1);
        sounds.getMusic("Groovin Drums3").setLooping(true);
        sounds.getMusic("Groovin Drums3").play();

        paused = false;
        endGame = false;

        if(isLevelOne){
            BoxGameObject wasd_text = new BoxGameObject(600, 700, 1, 1);
            wasd_text.setTexture(ResourceManager.level_one_wasd);
            wasd_text.setDrawScale(1, 1);
            addObject(wasd_text);

            BoxGameObject spacebar_text = new BoxGameObject(1575, 700, 1, 1);
            spacebar_text.setTexture(ResourceManager.level_one_spacebar);
            spacebar_text.setDrawScale(1, 1);
            addObject(spacebar_text);
        }
        else if(isLevelThree){
            BoxGameObject f_text = new BoxGameObject(1020, 500, 1, 1);
            f_text.setTexture(ResourceManager.f_text);
            f_text.setDrawScale(1, 1);
            addObject(f_text);
        }
    }

    private void populateLevel(LevelEditorController.JLevel level){
        background = LevelFactory.addBackground(level.backgroundWidth, level.backgroundHeight);
        LevelFactory.buildWalls(level.backgroundWidth, level.backgroundHeight);
        if (level != null){
            if(level.playerPos != null) {
                playerController = LevelFactory.addPlayer(level.playerPos.x, level.playerPos.y);
                addPlayer();
            }
            addEnemies();
            if(level.enemyPositions != null)
                for (LevelEditorController.PlacedObject obj : level.enemyPositions){
                    Enemy enemy = LevelFactory.addEnemy(obj.position.x, obj.position.y, LevelEditorController.getTexture(obj.texture), LevelEditorController.getEnemyType(obj.texture));
                    enemyController.addEnemy(enemy);
                    if(LevelEditorController.getEnemyType(obj.texture) == Village.EnemyType.DEFENDER){
                        ((DefenderEnemy)enemy).setStandStill();
                    }
                }
            for (LevelEditorController.PlacedObject obj : level.rockPositions){
                LevelFactory.addRock(obj.position.x, obj.position.y, LevelEditorController.getTexture(obj.texture));
            }
            for(LevelEditorController.PlacedObject obj : level.treePositions){
                LevelFactory.addTree(obj.position.x, obj.position.y, LevelEditorController.getTexture(obj.texture));
            }
            for (LevelEditorController.PlacedObject obj : level.waterPositions){
                if (!obj.alt){
                    LevelFactory.addWater(obj.position.x, obj.position.y, LevelEditorController.getTexture(obj.texture));
                }else{
                    LevelFactory.addWater2(obj.position.x, obj.position.y, LevelEditorController.getTexture(obj.texture));
                }
            }

            if (level.activeRooms.get(0) == 1) engineRoomActive = true;
            if (level.activeRooms.get(1) == 1) sacrificeRoomActive = true;
            if (level.activeRooms.get(2) == 1) lifeStealRoomActive = true;
            if (level.activeRooms.get(3) == 1) hardenRoomActive = true;

        }

        villageController = new VillageController(enemyController, player);
        updatables.add(villageController);

        addVillages();

        canvas.createHud(
                player.getHealth(),
                player.getMaxHealth(),
                player.getPopulation(),
                engineRoom.getPopulation(),
                lifeStealRoom.getPopulation(),
                hardenRoom.getPopulation(),
                player.getAmmo(),
                name(room1),
                name(room2),
                name(room3),
                name(room4),
                parent
        );
    }


    void addPlayer() {
        this.player = playerController.getPlayer();

        playerController.setCamera(shroomCloudPool, canvas.getCamera(), 800, 658*(background.getWidthFactor() - 1) - 142, 370, 425*(background.getHeightFactor() - 1) - 110);
        playerController.getCamera().position.set(camera.position.x < 800 ? 800 : camera.position.x, camera.position.y < 450 ? 450 : camera.position.y, 0);
        playerController.getCamera().position.set(camera.position.x > 658*(background.getWidthFactor() - 1) - 142 ?
                658*(background.getWidthFactor() - 1) - 142 : camera.position.x, camera.position.y < 425*(background.getHeightFactor() - 1) - 110 ?
                425*(background.getHeightFactor() - 1) - 110 : camera.position.y, 0);

//        updatables.add(playerController);
        if (isLevelEight) {player.setHealth(20);}

        //Initialize Rooms
        playerRoomController = new PlayerRoomController(player);
        engineRoom = playerRoomController.getRoom("Engine Room");
        sacrificeRoom = playerRoomController.getRoom("Sacrifice Room");
        lifeStealRoom = playerRoomController.getRoom("LifeSteal Room");
        hardenRoom = playerRoomController.getRoom("Harden Room");
        roomList = new ArrayList<>(Arrays.asList(engineRoom,sacrificeRoom,lifeStealRoom,hardenRoom));

        int roomPos = 0;
        for (Integer room : level.activeRooms) {
            if (room == 1) {
                if (room1 == null) {
                    room1 = roomList.get(roomPos);
                } else if (room2 == null) {
                    room2 = roomList.get(roomPos);
                } else if (room3 == null) {
                    room3 = roomList.get(roomPos);
                } else if (room4 == null) {
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
        for(LevelEditorController.JVillage village : level.jvillages){
            LevelFactory.addVillage(villageController, village.objectType, village.position.x, village.position.y,
                    LevelEditorController.getTexture(village.texture));
        }
    }

    void playLandingSound() {
        Sound thwack = sounds.getSound("DeeperThwack");
        thwack.play(.8f);
    }

    public void setIsLevelOne(){
        isLevelOne = true;
    }
    public void setIsLevelThree(){
        isLevelThree = true;
    }
    public void setIsLevelFour(){
        isLevelFour = true;
    }
    public boolean isLevelFour(){
        return isLevelFour;
    }
    public void setIsLevelEight(){
        isLevelEight = true;
    }
    public boolean isLevelEight(){
        return isLevelEight;
    }
    public void unpause(){
        paused = false;
    }

    public boolean preUpdate(float dt) {
        if (InputController.getInstance().didPause()){
            paused = !paused;
        }

        if(paused){
            canvas.pauseScreen();
            return false;
        }

        InputController input = InputController.getInstance();
        input.readInput();
        if (listener == null) {
            return true;
        }

        // Now it is time to maybe switch screens.
        if (input.didExit()) {
            listener.exitScreen(this, EXIT_QUIT);
            return false;
        }
        return true;
    }

    public void update(float dt) {
        // perform time step for all updatable objects
        // TODO: good candidates for threads.?
        super.update(dt);

        for (Updatable u : updatables) {
            u.update(dt);
        }

        // perform time step for villages
        for (Village v : villageController.getVillageList()) {
            if (v.didSpawnEnemy()) {
                addObject(v.getLastEnemySpawned());
            }
        }

        if (enemyController.getShoot()) {
            for (Projectile p : enemyController.getLastProjectilesSpawned()) {
                p.setTexture(resourceManager.arrowTexture);
                p.setDrawScale(scale);
                addObject(p);
                updatables.add(p);
            }
        }
    }

    private void updatePlayer(){
        // check if reset pressed
        if(InputController.getInstance().didReset()){
            reset();
        }

        //Player allocation stuff
        Vector2 mousePos = InputController.getInstance().getMousePos();
        if (!endGame) {
            checkRoom1(mousePos.x, mousePos.y);
            checkRoom2(mousePos.x, mousePos.y);
            checkRoom3(mousePos.x, mousePos.y);
            checkRoom4(mousePos.x, mousePos.y);
        }

        player.setTotalPopulation(player.getEnemiesKilled() + player.getPopulation() + engineRoom.getPopulation() + lifeStealRoom.getPopulation() + player.getSacrificed() + hardenRoom.getPopulation());
        if(player.hasJustLanded()){
            if (!player.isDead()) playLandingSound();
            float delay = .05f;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    int newPop = player.getTotalPopulation() - player.getOldPopulation();
                    //playerRoomController.lifeStealRoomCheck(newPop);
                    player.setOldPopulation(player.getTotalPopulation());
                    if (newPop > 0  &&  newPop < 20) sounds.scream();
                    else if (newPop >= 20) sounds.smallGroupScream();
                }
            }, delay);
        }

        //check if player is dead
        if(player.isDead() && !player.hasBeenDestroyed()){
            player.setHasBeenDestroyed();
            collisionManager.getObjectsToDestroy().add(player);
        }

        if (!endGame){
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

    @Override
    public void postUpdate(float dt){
        super.postUpdate(dt);
        destroyBodies();
        destroyArrows();

        playerController.update(dt);

        updatePlayer();

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
//                player.setOldPopulation(player.getTotalPopulation());
                //canvas.updateHud(player.getHealth(), player.getPopulation(), engineRoom.getPopulation(), lifeStealRoom.getPopulation(), hardenRoom.getPopulation());
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
            playerBoulder.setDrawScale((50 * Constants.RES_SCALE_X), (50 * Constants.RES_SCALE_Y));
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
                                parent.exitToLevelSelect( true);
                            }

                        }, 4.0f);
                    }
                }
            }
            if (o instanceof Player) {
                if (!endGame) {
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
            if (engineRoomActive) {
                engineRoom();
                room1 = engineRoom;
            }
            else if (sacrificeRoomActive){
                sacrificeRoom();
                room1 = sacrificeRoom;
            }
            else if (lifeStealRoomActive){
                lifeStealRoom();
                room1 = lifeStealRoom;
            }
            else if (hardenRoomActive){
                hardenRoom();
                room1 = hardenRoom;
            }
        }
    }

    public void checkRoom2 (float x, float y) {
        if (canvas.inRoom2Region(x, y) && Gdx.input.justTouched()) {
            if (sacrificeRoomActive && room1 != sacrificeRoom){
                sacrificeRoom();
            }
            else if (lifeStealRoomActive && room1 != lifeStealRoom){
                lifeStealRoom();
            }
            else if (hardenRoomActive && room1 != hardenRoom){
                hardenRoom();
            }

        }
    }

    public void checkRoom3 (float x, float y) {
        if (canvas.inRoom3Region(x, y) && Gdx.input.justTouched()) {
            if (lifeStealRoomActive && room2 != lifeStealRoom && room1 != lifeStealRoom) {
                lifeStealRoom();
            }
            else if (hardenRoomActive && room2 != hardenRoom && room1 != hardenRoom) {
                hardenRoom();
            }
        }
    }


    public void checkRoom4 (float x, float y) {
        if (canvas.inRoom4Region(x, y) && Gdx.input.justTouched()) {
            if (hardenRoomActive && room3 != hardenRoom && room2 != hardenRoom && room1 != hardenRoom) {
                hardenRoom();
            }
        }
    }

    public void engineRoom() {
        //Add population to room if left clicked in bounds
        if (InputController.getInstance().leftClick()) {
            playerRoomController.addToRoom(engineRoom, 10);
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
        playerRoomController.engineRoomCheck();
        //Remove population from room if right clicked in bounds
        if (InputController.getInstance().rightClick()) {
            playerRoomController.removeFromRoom(engineRoom, 10);
            canvas.updateHud(
                    player.getHealth(),
                    player.getMaxHealth(),
                    player.getPopulation(),
                    engineRoom.getPopulation(),
                    lifeStealRoom.getPopulation(),
                    hardenRoom.getPopulation(),
                    player.getAmmo()
            );
            playerRoomController.engineRoomCheck();
        }
    }

    public void lifeStealRoom () {
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

    public void sacrificeRoom() {
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

    public void hardenRoom() {
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

    public String name(PlayerRoom room){
        if (room == null) return "null";
        return room.getName();
    }


    @Override
    public void render(float delta) {
        super.render(delta);
        canvas.drawHud();
    }

    @Override
    public void draw(float delta) {
        canvas.clear();

        canvas.begin();
        objects.sort(GameObjectComparator.getInstance());
        for(GameObject obj : objects) {
            obj.draw(canvas);
        }
        canvas.drawParticles(shroomCloudPool);
        if(paused){
            Background b = new Background(50, 50);
            b.setTexture(ResourceManager.pauseTexture);
            b.draw(canvas);
        }

        canvas.end();
     }
}
