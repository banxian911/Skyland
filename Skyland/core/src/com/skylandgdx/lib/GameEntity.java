package com.skylandgdx.lib;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.Disposable;

/**
 * User: trakos
 * Date: 10.11.13
 * Time: 02:40
 */
public abstract class GameEntity implements Disposable, IGameEntity
{
    public Polygon getHitBox()
    {
        return null;
    }

    public boolean alive = true;

    public boolean checkIfHits(GameEntity entity)
    {
        return checkIfHits(entity.getHitBox());
    }

    public boolean checkIfHits(Polygon polygon)
    {
        Polygon polygon2 = getHitBox();
        return polygon2 != null && Intersector.overlapConvexPolygons(polygon, polygon2);
    }

    public GameTouchType handleTouch(float x, float y, GameTouchType previousTouchType, Integer activeTouchId)
    {
        return GameTouchType.NotIntercepted;
    }

    @Override
    public void entityPause()
    {

    }
}
