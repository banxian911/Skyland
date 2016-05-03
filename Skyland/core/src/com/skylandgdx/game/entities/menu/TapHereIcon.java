package com.skylandgdx.game.entities.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.skylandgdx.lib.GameLayers;
import com.skylandgdx.lib.GameSettings;
import com.skylandgdx.lib.input.GameInputElement;
import com.skylandgdx.main.SkylandAssets;
import com.skylandgdx.main.SkylandUtils;

public class TapHereIcon extends GameInputElement
{
    TextureRegion hudFinger = SkylandAssets.textureHudFinger;

    float currentDiffY = 0;
    int direction = 1;
    boolean invert = true;
    String text = "tap here to shoot";
    final float amplitude = 20f;
    final float speed = 20f;

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

    @Override
    public void draw(GameLayers layer, SpriteBatch batch)
    {
        if (!visible)
        {
            return;
        }
        batch.end();
        ShapeRenderer shapeRenderer = SkylandUtils.getShapeRenderer();
        shapeRenderer.setProjectionMatrix(GameSettings.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0xA91818FF));
        shapeRenderer.circle(getX(), getY(), 24f);
        shapeRenderer.setColor(new Color(0xE02727FF));
        shapeRenderer.circle(getX(), getY(), 16f);
        shapeRenderer.end();
        batch.begin();
        batch.draw(
            hudFinger,
            getX() - hudFinger.getRegionWidth() / 2,
            getY() + (invert ? currentDiffY :  - currentDiffY - hudFinger.getRegionHeight()),
            hudFinger.getRegionWidth() / 2,
            hudFinger.getRegionHeight() / 2,
            hudFinger.getRegionWidth(),
            hudFinger.getRegionHeight(),
            1,
            1,
            invert ? 180 : 0
        );
        SkylandAssets.fontPrimeRegular.setColor(new Color(0xE02727FF));
        SkylandAssets.fontPrimeRegular.drawWrapped(
            batch,
            text,
            getX() - 300,
            getY() + 40 * (invert ? -.8f : 1.3f),
            600,
            BitmapFont.HAlignment.CENTER
        );
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public void setInvert(boolean invert)
    {
        this.invert = invert;
    }
}
