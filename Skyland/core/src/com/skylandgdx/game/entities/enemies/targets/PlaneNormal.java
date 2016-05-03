package com.skylandgdx.game.entities.enemies.targets;

import com.skylandgdx.main.SkylandAssets;

public class PlaneNormal extends AbstractTarget
{
    public PlaneNormal(float x, float y)
    {
        super(x, y, SkylandAssets.texturePlane2);
        speed = 250 + 50f * (float) Math.random();
    }

    @Override
    protected float getOutOfMapTimeout()
    {
        return 1;
    }

    @Override
    protected int getInitialHp()
    {
        return EnemyType.PlaneNormal.hitPoints;
    }

    @Override
    protected void addHitEffectParticle(int remainingHp)
    {

    }

    @Override
    protected float getNextBombDelay()
    {
        return 1f + .5f * (float)Math.random();
    }
}
