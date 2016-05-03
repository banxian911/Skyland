package com.skylandgdx.game.entities.tank;

import com.skylandgdx.lib.GameEntitiesContainer;

/**
 * User: trakos
 * Date: 10.11.13
 * Time: 22:12
 */
public class TankMissileContainer extends GameEntitiesContainer
{
    static final int maxMissileCount = 4;

    public TankMissileContainer()
    {
        super(maxMissileCount);
    }

    public void add(float initX, float initY, float destX, float destY)
    {
        entities.add(new TankMissile(initX, initY, destX, destY));
    }

    public boolean canSpawnAdditionalMissile()
    {
        return entities.size() < maxMissileCount;
    }
}
