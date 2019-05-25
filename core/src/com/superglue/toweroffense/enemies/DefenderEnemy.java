package com.superglue.toweroffense.enemies;

import com.badlogic.gdx.ai.fma.FormationMember;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.steer.behaviors.LookWhereYouAreGoing;
import com.badlogic.gdx.ai.steer.behaviors.ReachOrientation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import com.superglue.toweroffense.GameCanvas;
import com.superglue.toweroffense.ResourceManager;
import com.superglue.toweroffense.ai.Position;
import com.superglue.toweroffense.collision.CollisionConstants;
import com.superglue.toweroffense.enemies.enemystates.DefenderEnemyState;
import com.superglue.toweroffense.projectiles.Projectile;
import com.superglue.toweroffense.projectiles.ProjectilePool;
import com.superglue.toweroffense.util.Constants;
import com.superglue.toweroffense.util.FilmStrip;
import com.superglue.toweroffense.util.Updatable;

public class DefenderEnemy extends Enemy implements Updatable, FormationMember<Vector2> {
    private static final float SPEED_CONSTANT = 0.1f;
    Position defendPoint;
    float randx = (float)Math.random()*2 - 1;
    float randy = (float)Math.random()*2 - 1;
    Vector2 movementVector;
    public StateMachine<DefenderEnemy, DefenderEnemyState> stateMachine;
    public ReachOrientation<Vector2> reachOrientationSB;
    public LookWhereYouAreGoing<Vector2> lookWhereYouAreGoingSB;

    private boolean standStill = false;

    float animationTimer;

    public DefenderEnemy(float x, float y, float width, float height, Vector2 dPoint){
        super(x, y, width, height);
        movementVector = new Vector2();
        defendPoint = new Position(dPoint);
        stateMachine = new DefaultStateMachine<>(this, DefenderEnemyState.SPAWNING);

        animationTimer = 0;
    }

    @Override
    public Position getTargetLocation() {
        return defendPoint;
    }

    public void setStandStill(){
        standStill = true;
    }

    @Override
    public boolean activatePhysics(World world){
        boolean returnVal = super.activatePhysics(world);

        Filter filter = new Filter();
        filter.categoryBits = CollisionConstants.LAYER_ENEMY;
        filter.maskBits = CollisionConstants.MASK_ENEMY & ~CollisionConstants.LAYER_ENEMY;
        body.getFixtureList().get(0).setFilterData(filter);

        return returnVal;
    }

    @Override
    public void update(float dt){
        if(target == null) return;

        if(getTexture().equals(ResourceManager.archerShootingAnimation)){
            animationTimer += dt;
            if (animationTimer >= 1f) {
                ((FilmStrip) getTexture()).setFrame((((FilmStrip) getTexture()).getFrame() + 1) % ((FilmStrip) getTexture()).getSize());
                animationTimer = 0;
            }
        }

        if(standStill) return;

        Vector2 playerPos = this.getTargetLocation().getPosition();
        float xDifD = drawScale.x*(defendPoint.getPosition().x - getPosition().x);
        float yDifD = drawScale.y*(defendPoint.getPosition().y - getPosition().y);

        float xDifP = drawScale.x*(playerPos.x - getPosition().x);

        float distance = (float)Math.sqrt((xDifD*xDifD)+(yDifD*yDifD));

        if(distance > 200) {
            movementVector.set(xDifD, yDifD);
        }
        else if (distance < 190){
            movementVector.set(randx, randy);
        }
        else {
            //movementVector.set(-(float) Math.cos((float) Math.atan2(xDifD, yDifD)), (float) Math.sin((float) Math.atan2(xDifD, yDifD)));
            movementVector.set(0,0);
        }

        movementVector.nor();
        movementVector.set(movementVector.x * SPEED_CONSTANT * drawScale.x, movementVector.y * SPEED_CONSTANT * drawScale.y);
        setLinearVelocity(movementVector);
        //stateMachine.update();
    }

    public void draw(GameCanvas canvas) {
        float y = getY();
        if(getTexture().equals(ResourceManager.archerShootingAnimation)) y = getY() - 5f*Constants.RES_SCALE_Y;

        if (isDead == true)
            return;

        float revScale = 1;
        if (target != null){ revScale = (target.getPosition().x - getPosition().x) < 0 ? -1 : 1;}
        canvas.draw(texture, Color.WHITE, texture.getRegionWidth()/2, 0, getX(), y, 0, revScale, 1);
    }
}
