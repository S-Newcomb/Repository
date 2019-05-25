package com.superglue.toweroffense.enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.superglue.toweroffense.GameCanvas;
import com.superglue.toweroffense.gameobjects.BoxGameObject;
import com.superglue.toweroffense.collision.CollisionConstants;
import com.superglue.toweroffense.player.Player;
import com.superglue.toweroffense.util.Constants;
import com.superglue.toweroffense.util.SoundController;

import javax.xml.soap.Text;

public class Village extends BoxGameObject {
    private float timer;
    private float COOLDOWN_CONSTANT = 1.3f;
    private int DEFAULT_VILLAGE_HEALTH = 3;
    private float HEARING_LIMIT = 100;
    private static final String VILLAGE_FILE = "sounds/HighPitchVillage2.wav";

    private int numEnemies = 0;

    private EnemyType enemyType;
    private float enemyWidth, enemyHeight;
    private Vector2 enemyScale;
    private TextureRegion enemyTexture;
    private boolean didSpawnEnemy;
    private Enemy lastEnemySpawned;
    private int health;
    private Vector2 hearingDistance;

    private TextureRegion fullHealth;
    private TextureRegion oneThirdHealth;
    private TextureRegion twoThirdHealth;

    // TODO: GET RID OF THIS VARIABLE, JUST USE THE BODY POSITION
    private Vector2 position;
    protected Sound villageSound = SoundController.getInstance().makeSound(VILLAGE_FILE);
    private long villageSoundId;
    private boolean soundStarted = false;

//    SteeringAgent anchor;
//    DefensiveFormation defensiveFormation;

    public Village(float x, float y, float width, float height, EnemyType et, float ew, float eh, Vector2 es, TextureRegion etr){
        super(x, y, width, height);
        timer = 0;
        setBodyType(BodyDef.BodyType.StaticBody);
        enemyType = et;
        enemyWidth = ew;
        enemyHeight = eh;
        enemyScale = es;
        enemyTexture = etr;
        health = DEFAULT_VILLAGE_HEALTH;
        position = new Vector2(x, y);

        hearingDistance = new Vector2();
    }

    public enum EnemyType {
        MELEE, SPEAR, DEFENDER
    }

    public Enemy addEnemy(){
        Enemy enemy = null;
        switch(enemyType){
            case MELEE:
                enemy = new MeleeEnemy(getX()/drawScale.x, getY()/drawScale.y, enemyWidth/2, enemyHeight/2);

                break;
            case SPEAR:
                enemy = new SpearEnemy(getX()/drawScale.x, getY()/drawScale.y, enemyWidth/4, enemyHeight/4);

                break;
            case DEFENDER:
                enemy = new DefenderEnemy(getX()/drawScale.x, getY()/drawScale.y, enemyWidth/4, enemyHeight/4, getPosition());

                break;
        }

        enemy.setDrawScale(enemyScale);
        enemy.setTexture(enemyTexture);
        lastEnemySpawned = enemy;
        return enemy;
    }

    public boolean canSpawn(float dt){
        if(numEnemies > 50){
            didSpawnEnemy = false;
            return false;
        }

        didSpawnEnemy = false;
        timer += dt;
        if(timer >= COOLDOWN_CONSTANT){
            numEnemies++;
            didSpawnEnemy = true;
            timer = 0;
            return true;
        }

        return false;
    }

    public boolean didSpawnEnemy(){
        return didSpawnEnemy;
    }

    public void setHPBarTextures(TextureRegion t1, TextureRegion t2, TextureRegion t3){
        fullHealth = t1;
        twoThirdHealth = t2;
        oneThirdHealth = t3;
    }

    public int getHealth()  { return  health; }
    public int setHealth(int x) { return this.health = x; }

    public boolean getSoundStarted () {
        return soundStarted;
    }

    public TextureRegion getHealthBar() {
        if (health == 3) {
            return fullHealth;
        }
        if (health == 2) {
            return twoThirdHealth;
        }
        return oneThirdHealth;
    }

    public Enemy getLastEnemySpawned(){
        return lastEnemySpawned;
    }

    public void playVillageSound(Player player) {
        Vector2 playerPos = player.getPosition();
        hearingDistance.x = Math.abs(position.x - playerPos.x);
        hearingDistance.y = Math.abs(position.y - playerPos.y);
        if (hearingDistance.x > HEARING_LIMIT && hearingDistance.y > HEARING_LIMIT) {
            return;
        }

        villageSoundId = villageSound.loop(1/( hearingDistance.x + hearingDistance.y));
        SoundController.getInstance().setActive("HighPitchVillage", villageSound, villageSoundId, true);
        soundStarted = true;
    }

    public void updateVillageSound(Player player) {
        Vector2 playerPos = player.getPosition();
        hearingDistance.x = Math.abs(position.x - playerPos.x);
        hearingDistance.y = Math.abs(position.y - playerPos.y);
        if (hearingDistance.x > HEARING_LIMIT && hearingDistance.y > HEARING_LIMIT) {
            villageSound.setVolume(villageSoundId, 0.01f);
        }
        else{
            villageSound.setVolume(villageSoundId, 1/(hearingDistance.x + hearingDistance.y));
        }
    }

    @Override
    public boolean activatePhysics(World world){
        boolean returnVal = super.activatePhysics(world);

        Filter filter = new Filter();
        filter.categoryBits = CollisionConstants.LAYER_VILLAGE;
        filter.maskBits = CollisionConstants.MASK_DEFAULT;
        body.getFixtureList().get(0).setFilterData(filter);

        return returnVal;
    }

    @Override
    public void draw(GameCanvas canvas) {
        assert (getX() != 0 && getY() != 0);
        canvas.draw(texture, Color.WHITE, texture.getRegionWidth() / 2, 0,
                getX(), getY(), 0, 1.5f, 1.5f);
        canvas.draw(getHealthBar(), Color.WHITE, getHealthBar().getRegionWidth() / 2, 0,
                getX() - 32.5f*Constants.RES_SCALE_X, getY() + 150, 0, 1, 1);
    }
}
