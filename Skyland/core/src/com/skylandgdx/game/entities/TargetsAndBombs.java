package com.skylandgdx.game.entities;

import com.skylandgdx.game.entities.enemies.BombsContainer;
import com.skylandgdx.game.entities.enemies.PlaneBomb;
import com.skylandgdx.game.entities.enemies.TargetsContainer;
import com.skylandgdx.game.entities.enemies.targets.AbstractTarget;
import com.skylandgdx.game.entities.mlv.MissileLaunchingVehicle;
import com.skylandgdx.lib.GameEntitiesContainer;
import com.skylandgdx.lib.GameEntity;

public class TargetsAndBombs extends GameEntitiesContainer
{
    public final TargetsContainer targets;
    public final BombsContainer bombs;

    public TargetsAndBombs()
    {
        targets = new TargetsContainer();
        bombs = new BombsContainer();

        add(targets);
        add(bombs);
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);

        for (AbstractTarget target : targets)
        {
            if (target.shouldDropBombNow())
            {
                bombs.add(target.getX() + target.getWidth() / 2, target.getY(), .5f * target.getSpeed(), target.getDirection() == 1);
            }
        }
    }

    public boolean isMlvHit(MissileLaunchingVehicle mlv)
    {
        GameEntity[] bombHits = bombs.getEntitiesHitBy(mlv);
        if (bombHits == null)
        {
            return false;
        }
        for (GameEntity bombHit : bombHits)
        {
            if (bombHit instanceof PlaneBomb)
            {
                ((PlaneBomb) bombHit).makeBoom(true);
            }
        }
        return true;
    }
}
