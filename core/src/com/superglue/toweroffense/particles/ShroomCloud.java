package com.superglue.toweroffense.particles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.superglue.toweroffense.GDXRoot;
import com.superglue.toweroffense.GameCanvas;
import com.superglue.toweroffense.ResourceManager;
import com.superglue.toweroffense.util.Constants;

public class ShroomCloud extends Image
{

    ParticleEffect effect;

    public ShroomCloud()
    {
        super(new Texture("particles/shrooomCoud2.png"));
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("shroomcloud2"), ResourceManager.blacksmokeatlas);
        effect.start();
        effect.setPosition(this.getWidth()/2+this.getX(),this.getHeight()/2+this.getY());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        effect.draw(batch);
    }


    public void update(float delta) {
        //System.out.println("ShroomCloud Update==========================");
        super.act(delta);
        effect.setPosition(this.getWidth()/2+this.getX(),this.getHeight()/2+this.getY());
        effect.update(delta);

    }

}
