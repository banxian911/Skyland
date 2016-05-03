package com.skylandgdx.game.entities.enemies.targets.heli;

import com.skylandgdx.lib.GameAnimation;
import com.skylandgdx.main.SkylandAssets;

public class Tail extends GameAnimation
{
    public Tail()
    {
        super(SkylandAssets.textureHeli, 9, 1, .025f);
        looping = true;
    }

    @Override
    public float getX()
    {
        return x;
    }

    @Override
    public float getY()
    {
        return y;
    }

    public void setX(float value)
    {
        x = value;
    }

    public void setY(float value)
    {
        y = value;
    }
}
