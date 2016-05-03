package com.skylandgdx.lib.input;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.skylandgdx.lib.GameLayers;
import com.skylandgdx.main.SkylandAssets;

public class GameButton extends GameInputElement
{
    String text;
    NinePatch textureRegion;
    NinePatch activeTextureRegion;

    public GameButton(String text, float x, float y)
    {
        width = getStandardButtonWidth();
        height = getStandardButtonHeight();

        textureRegion = SkylandAssets.textureHudButtonN;
        activeTextureRegion = SkylandAssets.textureHudButtonA;
        this.text = text;
        this.x = x;
        this.y = y;
    }


    static public float getStandardButtonWidth()
    {
        return 400;
    }

    static public float getStandardButtonHeight()
    {
        return 80;
    }

    @Override
    public void draw(GameLayers layer, SpriteBatch batch)
    {
        if (!visible) return;
        if (layer == GameLayers.LayerHud)
        {
            NinePatch ninePatch = active ? activeTextureRegion : textureRegion;
            ninePatch.draw(
                    batch,
                    getX(),
                    getY(),
                    getWidth(),
                    getHeight()
            );
            SkylandAssets.fontPrimeRegular.setColor(new Color(1, 1, 1, .8f));
            SkylandAssets.fontPrimeRegular.drawWrapped(
                    batch,
                    text.toUpperCase(),
                    getX(),
                    getY() + getHeight() / 2 + SkylandAssets.fontPrimeRegular.getLineHeight() / 2
                    + (active ? -2 : 0),
                    getWidth(),
                    BitmapFont.HAlignment.CENTER
            );
            SkylandAssets.fontPrimeRegular.setColor(Color.WHITE);
        }
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
