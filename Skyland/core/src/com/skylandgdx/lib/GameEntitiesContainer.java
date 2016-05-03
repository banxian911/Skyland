package com.skylandgdx.lib;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * User: trakos
 * Date: 11.11.13
 * Time: 18:17
 */
public class GameEntitiesContainer extends GameEntity
{
    static private Hashtable<GameEntity, GameEntity[]> hitPairsList = new Hashtable<GameEntity, GameEntity[]>();

    static public Hashtable<GameEntity, GameEntity[]> getEntitiesHitByAnyOf(GameEntitiesContainer entityContainer1, GameEntitiesContainer entityContainer2)
    {
        hitPairsList.clear();
        for (GameEntity targetEntity : entityContainer1.entities)
        {
            GameEntity[] hitsHelper = entityContainer2.getEntitiesHitBy(targetEntity);
            if (hitsHelper != null)
            {
                hitPairsList.put(targetEntity, hitsHelper);
            }
        }
        return hitPairsList;
    }

    protected List<GameEntity> entities;
    protected List<IGameTouchHandler> touchHandlers;

    public GameEntitiesContainer()
    {
        entities = new ArrayList<GameEntity>();
        touchHandlers = new ArrayList<IGameTouchHandler>();
    }

    public GameEntitiesContainer(int initialCapacity)
    {
        entities = new ArrayList<GameEntity>(initialCapacity);
        touchHandlers = new ArrayList<IGameTouchHandler>(initialCapacity);
    }

    public void add(GameEntity entity)
    {
        entities.add(entity);
    }

    @Override
    public float getX()
    {
        return 0;
    }

    @Override
    public float getY()
    {
        return 0;
    }

    @Override
    public float getWidth()
    {
        return 0;
    }

    @Override
    public float getHeight()
    {
        return 0;
    }

    @Override
    public void update(float delta)
    {
        for (int i = 0; i < entities.size(); i++)
        {
            entities.get(i).update(delta);
            if (!entities.get(i).alive)
            {
                entities.remove(i);
                i--;
            }
        }
    }

    @Override
    public void draw(GameLayers layer, SpriteBatch batch)
    {
        for (GameEntity entity : entities)
        {
            entity.draw(layer, batch);
        }
    }

    public void clear()
    {
        for (int i = entities.size() - 1; i >= 0; i--)
        {
            entities.get(i).dispose();
            entities.remove(i);
        }
    }

    @Override
    public void dispose()
    {
        clear();
        entities = null;
    }

    private List<GameEntity> hitTargets = new ArrayList<GameEntity>();
    public GameEntity[] getEntitiesHitBy(GameEntity entity)
    {
        hitTargets.clear();
        for (GameEntity targetEntity : entities)
        {
            if (entity.checkIfHits(targetEntity))
            {
                hitTargets.add(targetEntity);
            }
        }
        return hitTargets.isEmpty() ? null : hitTargets.toArray(new GameEntity[hitTargets.size()]);
    }

    public int entitiesSize()
    {
        return entities.size();
    }

    @Override
    public void entityPause()
    {
        for (GameEntity entity : entities)
        {
            entity.entityPause();
        }
        super.entityPause();
    }

    GameTouchType touchInterception;
    public GameTouchType handleTouch(float x, float y, GameTouchType previousTouchType, Integer activeTouchId)
    {
        touchInterception = GameTouchType.NotIntercepted;
        for (IGameTouchHandler touchHandler : touchHandlers)
        {
            touchInterception = touchHandler.handleTouch(x, y, previousTouchType, activeTouchId);
            if (touchInterception != GameTouchType.NotIntercepted)
            {
                return touchInterception;
            }
        }
        return GameTouchType.NotIntercepted;
    }
}
