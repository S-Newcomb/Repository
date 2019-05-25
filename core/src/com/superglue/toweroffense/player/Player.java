package com.superglue.toweroffense.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.graphics.Color;

import com.superglue.toweroffense.GameCanvas;
import com.superglue.toweroffense.ai.SteeringAgent;
import com.superglue.toweroffense.gameobjects.SimpleGameObject;
import com.superglue.toweroffense.player.states.PlayerState;
import com.superglue.toweroffense.collision.CollisionConstants;
import com.superglue.toweroffense.util.Constants;
import com.superglue.toweroffense.util.Direction;


/** This class defines what a player is. */
public class Player extends SimpleGameObject {
    private float fallAnimationOffset_X = 0;
    private float fallAnimationOffset_Y = 0;

    private float attackHitboxWidth;
    private float attackHitboxHeight;

    private Vector2 TEX_SCALE = new Vector2(1, 1);

    public SteeringAgent steering;

    Color tint;

    // player's max health
    private int maxHealth;

    // player's current health
    private float health;

    // player's overall total population//
    private int totalPopulation;

    // player's current unallocated population total
    private int population;

    private int enemiesKilled;
    //Used for lifeSteal
    private int oldPopulation;
    private int sacrificed;

    //Used to determine whether the player has enough ppl to crush rocks
    private int crushPower = PlayerConstants.DEFAULT_CRUSH_POWER;

    private int ammoCount = 10;

    private float defenseBuffScale;

    private float movementSpeedScale;

    private boolean isDead = false;
    // these vectors define the corners of the tower, which it uses to rotate around
    private Vector2 leftPivotPoint;
    private Vector2 rightPivotPoint;

    // the point around which the player rotates. By defualt this should be in its lowerleft corner,
    // but when it is attacking this should be on the edge
    private Vector2 pivotPoint;

    // This is true only at the moment the player touches the ground when slamming down, and false at all other times.
    private boolean justLanded;
    // This is true only at the moment the player releases the shoot key
    private boolean justShot;

    private boolean isInvincible = false;

    public PlayerCrosshair playerCrosshair;

    private PlayerAttackType attackType;

    private PlayerState state;

    private boolean dead = false;
    private boolean hasBeenDestroyed = false;

    private TextureRegion hpBarOutline;
    private TextureRegion hpBar;
    protected BitmapFont font;
    private static String FONT_FILE = "fonts/h.ttf";

    public Player(float x, float y, float width, float height){
        super(x, y);
        attackHitboxWidth = width;
        attackHitboxHeight = height;

        tint = Color.WHITE;

        maxHealth = PlayerConstants.DEFAULT_MAX_HEALTH;
        health = PlayerConstants.DEFAULT_MAX_HEALTH;

        movementSpeedScale = 1;
        defenseBuffScale = 1;

        // player starts with 0
        population = 0;
        enemiesKilled = 0;
        //Starting population + 10 in engine room
        oldPopulation = enemiesKilled + PlayerConstants.DEFAULT_PEOPLE_PER_CLICK;

        leftPivotPoint = new Vector2(0, 0);
        pivotPoint = leftPivotPoint;

        attackType = PlayerAttackType.SLAM;

        justLanded = false;

        state = PlayerState.WALKING;

        hpBarOutline = new TextureRegion(new Texture(Gdx.files.internal("level/hpBarOutline.png")));
        hpBar = new TextureRegion(new Texture(Gdx.files.internal("level/hpBar.png")));
        font = createFont(FONT_FILE, 32);
    }

    public void createAttackHitBoxes(Direction direction){
        if(body.getFixtureList().size > 1) return;

        //System.out.println("Attack hitboxes created");

        // collision filtering
        Filter filter = new Filter();
        // unlike the player, this can collide with enemies!
        filter.categoryBits = CollisionConstants.LAYER_ATTACK;
        filter.maskBits = CollisionConstants.MASK_DEFAULT;

        // parat 1
        createAttackHitBox1(direction, filter);
        if(direction == Direction.UP || direction == Direction.DOWN) return;
        // part 2
        createAttackHitBox2(direction, filter);
    }


    private void createAttackHitBox1(Direction direction, Filter filter){
        // first hitbox
        FixtureDef hitBox = new FixtureDef();

        // shape of hitbox
        PolygonShape shape = new PolygonShape();
        if(direction == Direction.LEFT){
            shape.setAsBox(
                    attackHitboxWidth, attackHitboxHeight,
                    PlayerConstants.ATTACK_HITBOX_OFFSET_LEFT,
                    Constants.DEGS_TO_RADS * 180
            );
        } else if(direction == Direction.RIGHT){
            shape.setAsBox(
                    attackHitboxWidth, attackHitboxHeight,
                    PlayerConstants.ATTACK_HITBOX_OFFSET_RIGHT,
                    0
            );
        } else if(direction == Direction.UP){
             shape.setAsBox(2.25f, 1.5f, new Vector2(0, 2f), 90);
        } else if(direction == Direction.DOWN   ) {
            shape.setAsBox(2.25f, 1.5f, new Vector2(0, 0), 270);
        } else {
            return;
        }
        hitBox.shape = shape;
      //  shape.dispose();

        // worthless physics constants
        hitBox.isSensor = true;

//        ChainShape chainShape = new ChainShape();

        body.createFixture(hitBox);

        body.getFixtureList().get(1).setFilterData(filter);
    }

    private void createAttackHitBox2(Direction direction, Filter filter){
        FixtureDef feetHitBox = new FixtureDef();
        CircleShape shape = new CircleShape();

        shape.setRadius(PlayerConstants.FEET_ATTACK_HITBOX_RADIUS);
        shape.setPosition(Vector2.Zero);

        feetHitBox.shape = shape;
       // shape.dispose();

        feetHitBox.isSensor = true;
        body.createFixture(feetHitBox);

        body.getFixtureList().get(2).setFilterData(filter);
    }

    public void destroyAttackHitBox() {
        while(body.getFixtureList().size > 1) {
            body.destroyFixture(body.getFixtureList().get(1));
        }
    }

    public boolean activatePhysics(World world) {
        boolean return_val = super.activatePhysics(world);

        // set up collision filtering
        Filter filter = new Filter();
        // category bits are the layer this object is on
        filter.categoryBits = CollisionConstants.LAYER_PLAYER;
        // mask bits is what objects this object can collide with
        filter.maskBits = CollisionConstants.MASK_DEFAULT;
        body.getFixtureList().get(0).setFilterData(filter);

        steering = new SteeringAgent(this.body, true);

        return return_val;
    }

    @Override
    protected void createFixtures() {
        if(body == null) return;

        releaseFixtures();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(
                PlayerConstants.PLAYER_WIDTH, PlayerConstants.PLAYER_HEIGHT,
                PlayerConstants.REGULAR_HITBOX_OFFSET,
                0
        );

        fixture.shape = shape;
        body.createFixture(fixture);
        markDirty(false);

        if (body.isBullet()){
            for(Fixture fix : body.getFixtureList()){
                fix.setSensor(true);
            }
        }
    }

    @Override
    protected void releaseFixtures() {
        if(body != null) for(Fixture f : body.getFixtureList()) body.destroyFixture(f);
    }

    public void setTint(Color tint) { this.tint = tint; }

    public void setInvincible(){
        isInvincible = true;
    }

    public int getMaxHealth() { return maxHealth; }
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }
    public float getHealth() { return health; }
    public void setHealth(int h){health = h;}
    public void addHealth(float amount) {
        if(isInvincible){
            return;
        }
        if(health + amount > maxHealth){
            health = maxHealth;
        }
        else {
            health += amount;
        }
    }

    public void addAmmo(int amount){
        this.ammoCount += amount;
    }

    public int getPopulation() { return population; }
    public int getEnemiesKilled() { return  enemiesKilled; }

    public int getTotalPopulation() { return totalPopulation; }
    public void setTotalPopulation(int amount) {totalPopulation = amount; }

    public int getSacrificed() { return  sacrificed; }
    public void setSacrificed(int amount) { this.sacrificed = amount; }

    public int getAmmo() {
        return ammoCount;
    }

    public int getCrushPower() { return crushPower; }
    public void setCrushPower(int x) { crushPower = x; }

    public int getOldPopulation() { return oldPopulation; }
    public void setOldPopulation(int amount) {this.oldPopulation = amount; }

    public PlayerAttackType getAttackType() { return attackType; }
    public void toggleAttackType() {
        if(attackType == PlayerAttackType.SLAM){
            attackType = PlayerAttackType.SHOOT;
        } else if(attackType == PlayerAttackType.SHOOT){
            attackType = PlayerAttackType.SLAM;
        }
    }

    public PlayerState getState() { return this.state; }
    public void setState(PlayerState state) { this.state = state; }

    public void addPopulation(int amount) {
        population += amount;
    }
    public void addEnemiesKilled(int amount) {
        enemiesKilled += amount;
    }


    public void togglePivotPoint() {
        if(pivotPoint.equals(leftPivotPoint)){
            pivotPoint = rightPivotPoint;
        } else if(pivotPoint.equals(rightPivotPoint)){
            pivotPoint = leftPivotPoint;
        }
    }

    public float getMovementSpeed(){ return PlayerConstants.DEFAULT_MOVEMENT_SPEED * movementSpeedScale; }
    public void setMovementSpeedScale(float scale) { this.movementSpeedScale = scale; }

    public float getDefenseBuffScale(){return defenseBuffScale;}
    public void setDefenseBuffScale(float scale) { this.defenseBuffScale = scale; }

    public boolean isDead() { return  isDead;}
    public void died() {isDead = true;}

    public boolean hasBeenDestroyed(){ return hasBeenDestroyed; }
    public void setHasBeenDestroyed(){ hasBeenDestroyed = true; }

    public boolean hasJustLanded() { return justLanded; }
    // this should be used to set justLanded to true only when the player has just touched the ground
    public void setJustLanded(boolean justLanded) { this.justLanded = justLanded; }

    public boolean hasJustShot() { return justShot; }
    public void setJustShot(boolean justShot) { this.justShot = justShot; }

    public Vector2 getShotLocation() {
        Vector2 returnVec = new Vector2();
        returnVec.set(playerCrosshair.getPosition()).scl(1/(50f * Constants.RES_SCALE_X));
        returnVec.x += 1 * Constants.RES_SCALE_X;
        return returnVec;
    }

    @Override
    public void setTexture(TextureRegion value){
        super.setTexture(value);
        rightPivotPoint = new Vector2(texture.getRegionWidth(), 0);
    }

    public void flipX(){
        TEX_SCALE.x *= -1;
    }

    public void setFallAnimationOffset_X(float f){
        fallAnimationOffset_X = f;
    }
    public void setFallAnimationOffset_Y(float f){
        fallAnimationOffset_Y = f;
    }


    public void draw(GameCanvas canvas) {
        canvas.draw(
                texture, tint,
                texture.getRegionWidth() / 2f + fallAnimationOffset_X, fallAnimationOffset_Y,
                getX(), getY() - 50f * Constants.RES_SCALE_X,
                0,
                TEX_SCALE.x, TEX_SCALE.y
        );
        if(!isInvincible) {
            canvas.draw(hpBarOutline, Color.WHITE,
                    hpBarOutline.getRegionWidth() / 2, hpBarOutline.getRegionHeight() / 2,
                    (getX() - hpBarOutline.getRegionWidth() * 2f) > 0 ?
                            getX() - hpBarOutline.getRegionWidth() * 1.5f :
                            getX() + hpBarOutline.getRegionWidth() * 1.5f,
                    getY() + 1f * hpBarOutline.getRegionHeight(),
                    0,
                    0.75f, 0.75f
            );
            canvas.draw(hpBar, Color.WHITE,
                    hpBar.getRegionWidth() / 2,
                    hpBar.getRegionHeight() / 2,
                    (getX() - hpBarOutline.getRegionWidth() * 2f) > 0 ?
                            getX() - hpBarOutline.getRegionWidth() * 1.5f :
                            getX() + hpBarOutline.getRegionWidth() * 1.5f,
                    getY() + 1f * hpBarOutline.getRegionHeight(),
                    0,
                    0.75f * health / maxHealth, 0.75f * health / maxHealth
            );
            float text_offset = 0;
            if (health < 100) text_offset = 0.006f * Constants.RES_X;
            if (health < 10) text_offset *= 2;
            canvas.drawText("" + (int) health, font, (getX() - hpBarOutline.getRegionWidth() * 2f) > 0 ?
                            getX() + text_offset - hpBarOutline.getRegionWidth() * 1.7f :
                            getX() + text_offset + hpBarOutline.getRegionWidth() * 1.3f,
                    getY() + 1.75f * hpBarOutline.getRegionHeight());
        }
    }

    private BitmapFont createFont(String font, int s) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(font));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = s;
        parameter.borderWidth =2;
        parameter.color =Color.WHITE;

        BitmapFont font24 = generator.generateFont(parameter);
        generator.dispose();
        return font24;
    }

    @Override
    public void drawDebug(GameCanvas canvas) {
    }
}
