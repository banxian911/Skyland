package com.skylandgdx.lib;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.skylandgdx.main.SkylandAssets;

public abstract class GameAnimation extends GameEntity
{
    protected final Animation animation;
    private final int height;
    private final int width;
    protected float stateTime;
    protected boolean looping = true;
    protected float y;
    protected float x;

    public GameAnimation(TextureRegion animationRegion, int frameCols, int frameRows, float frameDuration)
    {
        TextureRegion animationSheet = SkylandAssets.textureHeliTail;
        TextureRegion[][] tmp = animationSheet.split(
                animationSheet.getRegionWidth() / frameCols,
                animationSheet.getRegionHeight() / frameRows
        );
        TextureRegion[] animationFrames = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                animationFrames[index++] = tmp[i][j];
            }
        }
        animation = new Animation(frameDuration, animationFrames);
        stateTime = 0f;
        width = animationRegion.getRegionWidth() / frameCols;
        height = animationRegion.getRegionHeight() / frameRows;
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

    @Override
    public void update(float delta)
    {
        stateTime += delta;
    }

    @Override
    public void draw(GameLayers layer, SpriteBatch batch)
    {
        if (layer == GameLayers.LayerMain)
        {
            batch.draw(animation.getKeyFrame(stateTime, looping), x, y);
        }
    }

    @Override
    public void dispose()
    {
    }
}
