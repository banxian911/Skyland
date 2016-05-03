package com.skylandgdx.lib;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.skylandgdx.main.SkylandAssets;

public class TGradient
{
    static float[] vertices = new float[20];

    static TextureRegion textureRegion;
    private static void drawGradient(SpriteBatch batch, float x, float y, float width, float height, Color a, Color b, boolean horizontal)
    {
        if (textureRegion == null)
        {
            textureRegion = new TextureRegion(SkylandAssets.textureBlank);
        }
        float ca = a.toFloatBits();
        float cb = b.toFloatBits();

        int idx = 0;
        vertices[idx++] = x;
        vertices[idx++] = y;
        vertices[idx++] = horizontal ? ca : cb; // bottom left
        vertices[idx++] = textureRegion.getU(); //NOTE: texture coords origin is top left
        vertices[idx++] = textureRegion.getV2();

        vertices[idx++] = x;
        vertices[idx++] = y + height;
        vertices[idx++] = ca; // top left
        vertices[idx++] = textureRegion.getU();
        vertices[idx++] = textureRegion.getV();

        vertices[idx++] = x + width;
        vertices[idx++] = y + height;
        vertices[idx++] = horizontal ? cb : ca; // top right
        vertices[idx++] = textureRegion.getU2();
        vertices[idx++] = textureRegion.getV();

        vertices[idx++] = x + width;
        vertices[idx++] = y;
        vertices[idx++] = cb; // bottom right
        vertices[idx++] = textureRegion.getU2();
        vertices[idx++] = textureRegion.getV2();

        batch.draw(textureRegion.getTexture(), vertices, 0, vertices.length);
    }

    private static float currentPos;
    private static float dPos;
    private static void drawGradientVertical(SpriteBatch batch, float x, float y, float width, float height, Color[] colors, float[] positions)
    {
        currentPos = y;
        for (int i = 1; i < positions.length; i++)
        {
            dPos = (positions[i] - positions[i - 1]) * height;
            drawGradient(batch, x, currentPos, width, dPos, colors[i], colors[i - 1], false);
            currentPos += dPos;
        }
    }

    private static void drawGradientHorizontal(SpriteBatch batch, float x, float y, float width, float height, Color[] colors, float[] positions)
    {
        currentPos = x;
        for (int i = 1; i < positions.length; i++)
        {
            dPos = (positions[i] - positions[i - 1]) * width;
            drawGradient(batch, currentPos, y, dPos, height, colors[i - 1], colors[i], true);
            currentPos += dPos;
        }
    }

    private Color[] colors;
    private float[] positions;

    public TGradient(Color[] colors, float[] positions)
    {
        if (positions.length != colors.length || positions.length < 2 || positions[0] != 0 || positions[positions.length - 1] != 1)
        {
            throw new IllegalArgumentException();
        }
        this.colors = colors;
        this.positions = positions;
    }

    public void drawHorizontal(SpriteBatch batch, float x, float y, float w, float h)
    {
        drawGradientHorizontal(batch, x, y, w, h, colors, positions);
    }

    public void drawVertical(SpriteBatch batch, float x, float y, float w, float h)
    {
        drawGradientVertical(batch, x, y, w, h, colors, positions);
    }
}
