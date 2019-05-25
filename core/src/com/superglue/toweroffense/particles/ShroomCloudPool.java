package com.superglue.toweroffense.particles;
// This is the particles class which controls all particle effects
// References to instances of this class should be used in PlayerAnimationController and CollisionManager

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.utils.Array;
import com.superglue.toweroffense.ResourceManager;

public class ShroomCloudPool {

    static ParticleEffectPool shroomEffectPool;
    static ParticleEffectPool rocksBlowPool;
    static ParticleEffectPool bigOrangePool;
    static ParticleEffectPool swordsMenPool;
    static ParticleEffectPool spearMenPool;
    static ParticleEffectPool archersPool;
    public static Array<ParticleEffectPool.PooledEffect> effects = new Array();

    // Creates the particle emitter
    public ShroomCloudPool(int initialCapacity, int maxCapacity) {


        // this one is the brown dust particles
        ParticleEffect shroomEffect = new ParticleEffect();
        shroomEffect.load(Gdx.files.internal("shtoom3"), ResourceManager.theAtlas);
        shroomEffect.setEmittersCleanUpBlendFunction(false);
        shroomEffectPool = new ParticleEffectPool(shroomEffect, initialCapacity, maxCapacity);

        // this one is the rock explosion thing
        ParticleEffect rocksBlowUp = new ParticleEffect();
        rocksBlowUp.load(Gdx.files.internal("wildFire"), ResourceManager.theAtlas);
        rocksBlowUp.setEmittersCleanUpBlendFunction(false);
        rocksBlowPool = new ParticleEffectPool(rocksBlowUp, initialCapacity, maxCapacity);

        // Addition particle files should be added similar to ^^^^

        ParticleEffect bigOrangeExplosion = new ParticleEffect();
        bigOrangeExplosion.load(Gdx.files.internal("orange"), ResourceManager.theAtlas);
        bigOrangeExplosion.setEmittersCleanUpBlendFunction(false);
        bigOrangePool = new ParticleEffectPool(bigOrangeExplosion, initialCapacity, maxCapacity);

        ParticleEffect swordsManExplosion = new ParticleEffect();
        swordsManExplosion.load(Gdx.files.internal("swordsman"), ResourceManager.theAtlas);
        swordsManExplosion.setEmittersCleanUpBlendFunction(false);
        swordsMenPool = new ParticleEffectPool(swordsManExplosion, initialCapacity, maxCapacity);

        ParticleEffect archers = new ParticleEffect();
        archers.load(Gdx.files.internal("archer"), ResourceManager.theAtlas);
        archers.setEmittersCleanUpBlendFunction(false);
        archersPool = new ParticleEffectPool(archers, initialCapacity, maxCapacity);

        ParticleEffect spearManExplosion = new ParticleEffect();
        spearManExplosion.load(Gdx.files.internal("spearMan"), ResourceManager.theAtlas);
        spearManExplosion.setEmittersCleanUpBlendFunction(false);
        spearMenPool = new ParticleEffectPool(spearManExplosion, initialCapacity, maxCapacity);

    }

    public void makeArcherExplode(float x, float y)
    {
        ParticleEffectPool.PooledEffect effect = archersPool.obtain();
        effect.setPosition(x, y);
        effect.start();
        effects.add(effect);
    }

    public void makeSwordsExplode(float x, float y)
    {
        ParticleEffectPool.PooledEffect effect = swordsMenPool.obtain();
        effect.setPosition(x, y);
        effect.start();
        effects.add(effect);
    }

    public void makeSpearsExplode(float x, float y)
    {
        ParticleEffectPool.PooledEffect effect = spearMenPool.obtain();
        effect.setPosition(x, y);
        effect.start();
        effects.add(effect);
    }

    // Makes the brown dust particles
    public void makeParticle(float x, float y) {
       // System.out.println("particle is being made");

        // ShroomCloud shroomCloud = new ShroomCloud();
        // shroomCloud.setPosition(Gdx.graphics.getWidth()*3/5,Gdx.graphics.getHeight()*1/5);
        // shroomCloud.addAction(Actions.repeat(-1,Actions.sequence(Actions.moveTo(Gdx.graphics.getWidth()*3/5,Gdx.graphics.getHeight()*3/5,2,
        // Interpolation.sine),Actions.moveTo(Gdx.graphics.getWidth()*3/5,Gdx.graphics.getHeight()*1/5,2,
        // Interpolation.sine))));
        //
        //
        ParticleEffectPool.PooledEffect effect = shroomEffectPool.obtain();
        effect.setPosition(x, y);
        effect.start();
        effects.add(effect);
    }

    // Makes the explosion particles
    public void makeFire(float x, float y) {
        ParticleEffectPool.PooledEffect effect = rocksBlowPool.obtain();
        effect.setPosition(x,  y);
        effect.start();
        effects.add(effect);
    }


    public void makeOrangeExplosion(float x, float y)
    {
        ParticleEffectPool.PooledEffect effect = bigOrangePool.obtain();
        effect.setPosition(x,  y);
        effect.start();
        effects.add(effect);
    }

    public void reset() {
        for (int i = effects.size - 1; i >= 0; i--)
            effects.get(i).free();
        effects.clear();

    }

}