package com.skylandgdx.game.entities.enemies.targets;

import com.skylandgdx.lib.TParticle;
import com.skylandgdx.main.SkylandAssets;

public class Bomber extends AbstractTarget
{

    public Bomber(float x, float y)
    {
        super(x, y, SkylandAssets.textureBomber);
        speed = 300 + 10f * (float) Math.random();
    }

    @Override
    protected float getOutOfMapTimeout()
    {
        return (float) (Math.random() * 10);
    }

    @Override
    protected int getInitialHp()
    {
        return EnemyType.Bomber.hitPoints;
    }

    @Override
    protected void addHitEffectParticle(int remainingHp)
    {
        if (remainingHp == 2)
        {
            particles.add(new TParticle(SkylandAssets.particleEffectGrayExhaust.obtain(), getWidth() / 2, getHeight() / 2));
        }
        else if (remainingHp == 1)
        {
            particles.add(new TParticle(SkylandAssets.particleEffectExhaust.obtain(), getWidth() / 2, getHeight() / 2));
        }
    }

    @Override
    protected float getNextBombDelay()
    {
        return .5f;
    }
}
