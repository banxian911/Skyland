package com.skylandgdx.lib;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TParticle
{
    public GameEntity followEntity = null;
    public ParticleEffectPool.PooledEffect effect;
    public float x;
    public float y;

    public TParticle(ParticleEffectPool.PooledEffect effect, float x, float y)
    {
        this.x = x;
        this.y = y;
        this.effect = effect;
    }

    public TParticle(ParticleEffectPool.PooledEffect effect, float x, float y, GameEntity followEntity)
    {
        this(effect, x, y);
        this.followEntity = followEntity;
    }

    public boolean isComplete()
    {
        return effect.isComplete();
    }

    public void free()
    {
        effect.free();
    }

    public void update(float delta)
    {
        if (followEntity == null)
        {
            effect.setPosition(x, y);
        }
        else
        {
            effect.setPosition(followEntity.getX() + x, followEntity.getY() + y);
        }
        effect.update(delta);
    }

    public void draw(SpriteBatch particleBatch)
    {
        effect.draw(particleBatch);
    }
}
