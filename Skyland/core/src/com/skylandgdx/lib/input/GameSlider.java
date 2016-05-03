package com.skylandgdx.lib.input;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.skylandgdx.lib.GameLayers;
import com.skylandgdx.lib.GameSettings;
import com.skylandgdx.lib.GameTouchType;
import com.skylandgdx.main.SkylandAssets;
import com.skylandgdx.main.SkylandUtils;

public class GameSlider extends GameInputElement
{
    String text;
    TextureRegion radioCircleOff = SkylandAssets.textureHudRadioOff;
    public float value;
    protected Vector3 vectorStart = new Vector3();
    protected Vector3 vectorEnd = new Vector3();

    public GameSlider(String text, float x, float y)
    {
        width = getStandardButtonWidth();
        height = getStandardButtonHeight();

        this.text = text;
        this.x = x;
        this.y = y;
    }


    static public float getStandardButtonWidth()
    {
        return 300;
    }

    static public float getStandardButtonHeight()
    {
        return 36;
    }

    final float radius = 3f;
    float handleTouchX;
    float handleTouchY;
    @Override
    public void draw(GameLayers layer, SpriteBatch batch)
    {
        if (!visible) return;
        if (layer == GameLayers.LayerHud)
        {
            batch.end();

            fillVectors();
            ShapeRenderer shapeRenderer = SkylandUtils.getShapeRenderer();
            shapeRenderer.setProjectionMatrix(GameSettings.getCamera().combined);
            shapeRenderer.setColor(new Color(.5f, .5f, .5f, 1));

            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.line(vectorStart, vectorEnd);
            shapeRenderer.end();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(vectorStart.x, vectorStart.y, radius);
            shapeRenderer.circle(vectorEnd.x, vectorEnd.y, radius);
            shapeRenderer.end();

            batch.begin();
            handleTouchX = vectorStart.x + (vectorEnd.x - vectorStart.x) * value - radioCircleOff.getRegionWidth() / 2;
            handleTouchY = vectorStart.y - radioCircleOff.getRegionHeight() / 2;
            batch.draw(
                    radioCircleOff,
                    handleTouchX,
                    handleTouchY,
                    radioCircleOff.getRegionWidth(),
                    radioCircleOff.getRegionHeight()
            );

            SkylandAssets.fontPrimeRegular.draw(
                    batch,
                    getText(),
                    getX(),
                    getY()
            );
        }
    }

    void fillVectors()
    {
        vectorStart.set(getX(), getY() + radioCircleOff.getRegionHeight() / 2 - 80, 0);
        vectorEnd.set(getX() + getWidth(), getY() + radioCircleOff.getRegionHeight() / 2 - 80, 0);
    }

    Integer currentTouchId = -1;
    @Override
    public GameTouchType handleTouch(float x, float y, GameTouchType previousTouchType, Integer activeTouchId)
    {
        if (!visible) return GameTouchType.NotIntercepted;
        if
        (
            (
                (previousTouchType == GameTouchType.NotIntercepted || previousTouchType == null)
                && (
                    x > handleTouchX && x < handleTouchX + radioCircleOff.getRegionWidth() &&
                    y > handleTouchY && y < handleTouchY + radioCircleOff.getRegionHeight()
                )
            )
            || currentTouchId.equals(activeTouchId)
        )
        {
            value = (x - vectorStart.x) / (vectorEnd.x - vectorStart.x);
            value = Math.max(0, Math.min(value, 1));
            currentTouchId = activeTouchId;
            return GameTouchType.InterceptedByDragging;
        }
        else
        {
            currentTouchId = -1;
        }
        return super.handleTouch(x, y, previousTouchType, activeTouchId);
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }
}
