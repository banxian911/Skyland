package com.skylandgdx.game.entities.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.skylandgdx.lib.GameLayers;
import com.skylandgdx.lib.GameSettings;
import com.skylandgdx.lib.input.GameInputElement;
import com.skylandgdx.main.SkylandAssets;

public class ArrowExplanationIcon extends GameInputElement
{
    TextureRegion hudArrow = SkylandAssets.textureHudArrow;

    float currentDiffY = 0;
    int direction = 1;
    String text = "tap here to shoot";
    BitmapFont.HAlignment hAlignment = BitmapFont.HAlignment.LEFT;
    final float amplitude = 20f;
    final float speed = 20f;
    float textPositionX;
    private boolean isRotated;
    private boolean arrowVisible;

    public void setTextAlignment(BitmapFont.HAlignment hAlignment)
    {
        this.hAlignment = hAlignment;
    }

    @Override
    public void update(float delta)
    {
        currentDiffY += speed * delta * direction;
        if (currentDiffY > amplitude)
        {
            direction = -1;
        }
        else if (currentDiffY <= 0)
        {
            direction = 1;
        }
    }

    final float width = 600;
    @Override
    public void draw(GameLayers layer, SpriteBatch batch)
    {
        if (!visible)
        {
            return;
        }
        if (arrowVisible)
        {
            batch.draw(
                hudArrow,
                getX() - hudArrow.getRegionWidth() / 2 + (isRotated ? currentDiffY : 0),
                getY() + (isRotated ? 0 : currentDiffY),
                hudArrow.getRegionWidth() / 2,
                hudArrow.getRegionHeight() / 2,
                hudArrow.getRegionWidth(),
                hudArrow.getRegionHeight(),
                1,
                1,
                isRotated ? 90 : 0
            );
        }
        SkylandAssets.fontPrimeRegular.setColor(Color.WHITE);
        switch (hAlignment)
        {
            case LEFT:
                textPositionX = 20;
                break;
            case CENTER:
                textPositionX = getX() - width / 2;
                break;
            case RIGHT:
                textPositionX = GameSettings.getCameraWidth() - width - 20;
                break;
        }
        SkylandAssets.fontPrimeRegular.drawWrapped(
                batch,
                text,
                textPositionX,
                getY() - 10,
                width,
                hAlignment
        );
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public void setIsRotated(boolean isRotated)
    {
        this.isRotated = isRotated;
    }

    public void setArrowVisible(boolean arrowVisible)
    {
        this.arrowVisible = arrowVisible;
    }
}
