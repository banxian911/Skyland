package com.skylandgdx.game.entities.enemies;

import com.skylandgdx.lib.GameEntitiesContainer;

public class BombsContainer extends GameEntitiesContainer
{
    public BombsContainer()
    {
    }

    public void add(float initX, float initY, float planeSpeed, boolean planeDirection)
    {
        add(new PlaneBomb(initX, initY, planeSpeed, planeDirection));
    }
}
