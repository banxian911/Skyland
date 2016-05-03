package com.skylandgdx.game.entities.enemies.targets;

import com.skylandgdx.lib.TParticle;
import com.skylandgdx.main.SkylandAssets;

public class Zeppelin extends AbstractTarget
{

    public Zeppelin(float x, float y)
    {
        super(x, y, SkylandAssets.textureZeppelin);
        speed = 45 + 5f * (float) Math.random();
    }

    @Override
    protected float getOutOfMapTimeout()
    {
        return 1;
    }

    @Override
    protected int getInitialHp()
    {
        return EnemyType.Zeppelin.hitPoints;
    }

    @Override
    protected void addHitEffectParticle(int remainingHp)
    {
        switch (remainingHp)
        {
            case 4:
                particles.add(
                        new TParticle(
                                SkylandAssets.particleEffectGrayExhaust.obtain(),
                                (float) (getWidth() / 2 + (Math.random() - .5) * getWidth() / 3 * 2),
                                (float) (getHeight() / 2 + (Math.random() - .5) * getHeight() / 3 * 2)
                        )
                );
                break;
            case 3:
                particles.add(
                        new TParticle(
                                SkylandAssets.particleEffectGrayExhaust.obtain(),
                                (float) (getWidth() / 2 + (Math.random() - .5) * getWidth() / 3 * 2),
                                (float) (getHeight() / 2 + (Math.random() - .5) * getHeight() / 3 * 2)
                        )
                );
                break;
            case 2:
                particles.add(
                        new TParticle(
                                SkylandAssets.particleEffectGrayExhaust.obtain(),
                                (float) (getWidth() / 2 + (Math.random() - .5) * getWidth() / 3 * 2),
                                (float) (getHeight() / 2 + (Math.random() - .5) * getHeight() / 3 * 2)
                        )
                );
                break;
            case 1:
                particles.add(
                        new TParticle(
                                SkylandAssets.particleEffectExhaust.obtain(),
                                (float) (getWidth() / 2 + (Math.random() - .5) * getWidth() / 3 * 2),
                                (float) (getHeight() / 2 + (Math.random() - .5) * getHeight() / 3 * 2)
                        )
                );
                break;
        }
    }

    int bombBurstIndex = 0;
    @Override
    protected float getNextBombDelay()
    {
        if (bombBurstIndex >= 5)
        {
            bombBurstIndex = 0;
            return 3.5f + .5f * (float)Math.random();
        }
        bombBurstIndex++;
        return .75f;
    }
}
