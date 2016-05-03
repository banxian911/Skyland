package com.skylandgdx.game.entities.enemies.targets;

import com.skylandgdx.main.SkylandAssets;

public class PlaneWeak extends AbstractTarget
{

    public PlaneWeak(float x, float y)
    {
        super(x, y, SkylandAssets.texturePlane1);
        speed = 150 + 50f * (float) Math.random();
    }

    @Override
    protected int getInitialHp()
    {
        return EnemyType.PlaneWeak.hitPoints;
    }

    @Override
    protected void addHitEffectParticle(int remainingHp)
    {
    }

    @Override
    protected float getNextBombDelay()
    {
        return 2.5f + .5f * (float)Math.random();
    }

    @Override
    protected float getOutOfMapTimeout()
    {
        return 1;
    }
}
