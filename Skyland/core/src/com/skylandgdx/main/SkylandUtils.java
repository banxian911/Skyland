package com.skylandgdx.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.skylandgdx.lib.GameSettings;

public class SkylandUtils
{
    static ShapeRenderer shapeRenderer = new ShapeRenderer();

    static public void recreateShapeRenderer()
    {
        Gdx.app.log("fps", "shaperenderer");
        if (shapeRenderer != null)
        {
            shapeRenderer.dispose();
            shapeRenderer = null;
        }
        shapeRenderer = new ShapeRenderer();
    }

    static public ShapeRenderer getShapeRenderer()
    {
        return shapeRenderer;
    }

    static public void drawMenuBox(SpriteBatch batch, String title, float positionX, float positionY, float width, float height)
    {
        SkylandAssets.textureHudPanelDark.setColor(new Color(0, 0, 0, .7f));
        SkylandAssets.textureHudPanelDark.draw(
                batch,
                positionX,
                positionY,
                width,
                height
        );
        SkylandAssets.textureHudPanelLight.setColor(new Color(0, 0, 0, .3f));
        SkylandAssets.textureHudPanelLight.draw(
                batch,
                positionX,
                positionY,
                width,
                height - 40
        );
        SkylandAssets.fontPrimeRegular.setColor(new Color(.8f, .8f, .8f, .8f));
        SkylandAssets.fontPrimeRegular.drawWrapped(
                batch,
                title,
                positionX,
                positionY + height - 11,
                width,
                BitmapFont.HAlignment.CENTER
        );
        SkylandAssets.fontPrimeRegular.setColor(Color.WHITE);
    }

    public static void drawBlackingMask(SpriteBatch batch)
    {
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(GameSettings.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0, 0, 0, .5f));
        shapeRenderer.rect(0, 0, GameSettings.getCameraWidth(), GameSettings.getCameraHeight());
        shapeRenderer.end();
        batch.begin();
    }

    public static float typeNumber(SpriteBatch batch, int number, int digitCount, float positionX, float positionY, Color color)
    {
        number = Math.min(Math.max(number, 0), (int)Math.pow(10, digitCount) - 1);
        batch.setColor(color);
        for (int k = digitCount - 1; k >= 0; k--)
        {
            int digit = (int) Math.floor(number / Math.pow(10, k));
            batch.draw(
                    SkylandAssets.textureHudDigits.get(digit),
                    positionX,
                    positionY - 8,
                    SkylandAssets.textureHudDigits.get(digit).getRegionWidth(),
                    SkylandAssets.textureHudDigits.get(digit).getRegionHeight()
            );
            positionX += 24;
            number -= (int)(digit * Math.pow(10, k));
        }
        batch.setColor(Color.WHITE);
        return positionX;
    }

    public static void typeWrapped(SpriteBatch batch, String text, float positionX, float positionY, float width, BitmapFont.HAlignment alignment, boolean makeDarker)
    {
        if (makeDarker)
            SkylandAssets.fontPrimeRegular.setColor(new Color(.8f, .8f, .8f, .8f));
        SkylandAssets.fontPrimeRegular.drawWrapped(
                batch,
                text,
                positionX,
                positionY + 10,
                width,
                alignment
        );
        if (makeDarker)
            SkylandAssets.fontPrimeRegular.setColor(Color.WHITE);
    }
}
