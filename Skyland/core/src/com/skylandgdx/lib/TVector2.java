package com.skylandgdx.lib;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class TVector2 extends Vector2
{
    public TVector2(float x, float y)
    {
        super(x, y);
    }

    public float distance(Vector2 vector2)
    {
        return dst(vector2);
    }

    public float distance(float x, float y)
    {
        return dst(x, y);
    }

    public TVector2 rotate(float originX, float originY, float degrees)
    {
        x -= originX;
        y -= originY;
        rotate(degrees);
        x += originX;
        y += originY;

        return this;
    }
}