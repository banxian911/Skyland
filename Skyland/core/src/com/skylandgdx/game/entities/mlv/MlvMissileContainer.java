package com.skylandgdx.game.entities.mlv;

import com.skylandgdx.lib.GameEntitiesContainer;

public class MlvMissileContainer extends GameEntitiesContainer
{
    static final int maxMissileCount = 4;

    public MlvMissileContainer()
    {
        super(maxMissileCount);
    }

    public void add(float initX, float initY, float destX, float destY)
    {
        entities.add(new MlvMissile(initX, initY, destX, destY));
    }

    public boolean canSpawnAdditionalMissile()
    {
        return entities.size() < maxMissileCount;
    }
}
