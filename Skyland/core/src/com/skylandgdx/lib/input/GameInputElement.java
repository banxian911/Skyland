package com.skylandgdx.lib.input;

import com.skylandgdx.lib.GameEntity;
import com.skylandgdx.lib.GameTouchType;

public abstract class GameInputElement extends GameEntity implements IGameInput
{

    public boolean active = false;
    public boolean visible = true;
    float x;
    float y;
    float width;
    float height;

    @Override
    public boolean getActive()
    {
        return active;
    }

    @Override
    public void setActive(boolean value)
    {
        active = value;
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

    public void setX(float x)
    {
        this.x = x;
    }
    public void setY(float y)
    {
        this.y = y;
    }

    @Override
    public float getWidth()
    {
        return width;
    }

    @Override
    public float getHeight()
    {
        return height;
    }

    public void setWidth(float width)
    {
        this.width = width;
    }

    public void setHeight(float height)
    {
        this.height = height;
    }

    @Override
    public void update(float delta)
    {

    }

    @Override
    public void dispose()
    {

    }

    @Override
    public GameTouchType handleTouch(float x, float y, GameTouchType previousTouchType, Integer activeTouchId)
    {
        if (
            visible
            && x > getX()
            && y > getY()
            && x < getX() + getWidth()
            && y < getY() + getHeight()
        )
        {
            setActive(true);
            if (previousTouchType == null || previousTouchType == GameTouchType.NotIntercepted)
            {
                return GameTouchType.InterceptedByMenu;
            }
        }
        return GameTouchType.NotIntercepted;
    }

    public boolean getVisible()
    {
        return visible;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }
}
