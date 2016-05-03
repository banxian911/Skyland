package com.skylandgdx.lib;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.skylandgdx.main.SkylandUtils;

public class GameDebug
{
    static public void markPoint(SpriteBatch batch, Vector2 point, Color color)
    {
        batch.end();

        SkylandUtils.getShapeRenderer().setColor(color);
        SkylandUtils.getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);
        SkylandUtils.getShapeRenderer().circle(point.x, point.y, 4);
        SkylandUtils.getShapeRenderer().end();
        batch.begin();
    }

    static public void markPoint(SpriteBatch batch, Vector2 point)
    {
        markPoint(batch, point, Color.WHITE);
    }

    static public void markPolygon(SpriteBatch batch, Polygon polygon)
    {
        batch.end();
        SkylandUtils.getShapeRenderer().begin(ShapeRenderer.ShapeType.Line);
        SkylandUtils.getShapeRenderer().polygon(polygon.getTransformedVertices());
        SkylandUtils.getShapeRenderer().end();
        batch.begin();
    }

    static public void markRectangle(SpriteBatch batch, Rectangle rectangle)
    {
        batch.end();
        SkylandUtils.getShapeRenderer().begin(ShapeRenderer.ShapeType.Line);
        SkylandUtils.getShapeRenderer().rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        SkylandUtils.getShapeRenderer().end();
        batch.begin();
    }
}
