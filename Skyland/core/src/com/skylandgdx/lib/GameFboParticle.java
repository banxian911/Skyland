package com.skylandgdx.lib;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class GameFboParticle extends GameEntity
{
    static public GameFboParticle instance = new GameFboParticle(GameLayers.LayerParticles);
    static public GameFboParticle foregroundInstance = new GameFboParticle(GameLayers.LayerParticlesForeground);

    static public void createOrResumeAll()
    {
        instance.create();
        foregroundInstance.create();
    }

    static public void disposeAll()
    {
        instance.dispose();
        foregroundInstance.dispose();
    }


    protected final GameLayers drawOnLayer;
    public ArrayList<TParticle> pooledEffectList = new ArrayList<TParticle>();
    FrameBuffer particleFbo;
    SpriteBatch particleBatch;
    TextureRegion particleFboRegion;


    public GameFboParticle(GameLayers drawOnLayer)
    {
        this.drawOnLayer = drawOnLayer;
    }

    public void create()
    {
        particleFbo = new FrameBuffer(Pixmap.Format.RGBA4444, GameSettings.getResolutionWidth(), GameSettings.getResolutionHeight(), false);
        particleBatch = new SpriteBatch();
        particleFboRegion = new TextureRegion(particleFbo.getColorBufferTexture(), 0, 0, GameSettings.getResolutionWidth(), GameSettings.getResolutionHeight());
        particleFboRegion.flip(false, true);
    }

    @Override
    public float getX()
    {
        return 0;
    }

    @Override
    public float getY()
    {
        return 0;
    }

    @Override
    public float getWidth()
    {
        return 0;
    }

    @Override
    public float getHeight()
    {
        return 0;
    }

    @Override
    public void update(float delta)
    {
        for (int i = 0; i < pooledEffectList.size(); i++)
        {
            if (pooledEffectList.get(i).isComplete())
            {
                pooledEffectList.get(i).free();
                pooledEffectList.remove(i);
                i--;
                continue;
            }
            pooledEffectList.get(i).update(delta);
        }
    }

    public void playParticleEffect(ParticleEffectPool particleEffectPool, float x, float y)
    {
        ParticleEffectPool.PooledEffect pooledEffect = particleEffectPool.obtain();
        pooledEffectList.add(new TParticle(pooledEffect, x, y));
    }

    public void playParticleEffect(ParticleEffectPool particleEffectPool, float x, float y, GameEntity followEntity)
    {
        ParticleEffectPool.PooledEffect pooledEffect = particleEffectPool.obtain();
        pooledEffectList.add(new TParticle(pooledEffect, x, y, followEntity));
    }

    @Override
    public void draw(GameLayers layer, SpriteBatch batch)
    {
        if (particleFbo == null) return;
        if (
            drawOnLayer == GameLayers.LayerParticles && layer == GameLayers.LayerPrepareParticles
            || drawOnLayer == GameLayers.LayerParticlesForeground && layer == GameLayers.LayerPrepareParticlesForeground
        )
        {
            for (TParticle pooledEffect : pooledEffectList)
            {
                pooledEffect.draw(particleBatch);
            }
        }
        if (layer == drawOnLayer)
        {
            batch.setColor(1, 1, 1, 1);
            batch.draw(particleFboRegion, GameSettings.getCameraStartX(), GameSettings.getCameraStartY());
            batch.setColor(1, 1, 1, 1);
        }
    }

    public void beginPreparing()
    {
        if (particleFbo == null || particleBatch == null) return;
        particleBatch.setBlendFunction(-1, -1);
        particleFbo.begin();
        Gdx.gl20.glBlendFuncSeparate(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ONE, GL20.GL_ONE);
        particleBatch.setProjectionMatrix(GameSettings.getCamera().combined);
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        particleBatch.begin();
    }

    public void endPreparing()
    {
        if (particleFbo == null) return;
        particleBatch.end();
        particleFbo.end();
    }

    @Override
    public void dispose()
    {
        if (particleFbo != null)
            particleFbo.dispose();
        if (particleBatch != null)
            particleBatch.dispose();
        particleFbo = null;
        particleBatch = null;
    }

    public void renderParticle(ParticleEffectPool.PooledEffect exhaustEffect)
    {
        if (particleBatch != null)
            exhaustEffect.draw(particleBatch);
    }
}
