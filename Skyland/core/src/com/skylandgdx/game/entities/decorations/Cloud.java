package com.skylandgdx.game.entities.decorations;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.skylandgdx.lib.GameEntity;
import com.skylandgdx.lib.GameLayers;
import com.skylandgdx.lib.GameSettings;
import com.skylandgdx.lib.TVector2;
import com.skylandgdx.main.SkylandAssets;

/**
 * User: trakos
 * Date: 15.11.13
 * Time: 08:01
 */
public class Cloud extends GameEntity
{
    public static Cloud randomizeNewCloud(float wind, boolean startOutsideScreen)
    {
        return new Cloud(
                CloudType.values()[MathUtils.random(0, CloudType.values().length - 1)],
                startOutsideScreen ? (wind < 0 ? GameSettings.getMapWidth(): 0) : MathUtils.random(0, GameSettings.getMapWidth()),
                100 + (GameSettings.getMapHeight() - 100) * (float)(1 - Math.pow(MathUtils.random(0f, 1f), 2)),
                MathUtils.random(0.4f, 0.7f),
                MathUtils.random(0.3f, 0.6f),
                MathUtils.random(1, 10) > 8,
                wind
        );
    }

    public enum CloudType { Cloud1, Cloud2, Cloud3, Cloud4, Cloud5, Cloud6, Cloud7 }

    final private float speed;
    private TVector2 position;
    final private TextureRegion textureRegion;
    final private float scale;
    final private boolean isForeground;
    final private float opacity;
    float wind;

    public Cloud(CloudType cloudType, float x, float y, float scale, float opacity, boolean isForeground, float wind)
    {
        this.speed = scale * 4 + 1;
        this.position = new TVector2(x, y);
        this.scale = scale;
        this.isForeground = isForeground;
        this.opacity = opacity;
        this.wind = wind;
        switch (cloudType)
        {
            case Cloud1:
                textureRegion = SkylandAssets.textureCloud1;
                break;
            case Cloud2:
                textureRegion = SkylandAssets.textureCloud2;
                break;
            case Cloud3:
                textureRegion = SkylandAssets.textureCloud3;
                break;
            case Cloud4:
                textureRegion = SkylandAssets.textureCloud4;
                break;
            case Cloud5:
                textureRegion = SkylandAssets.textureCloud5;
                break;
            case Cloud6:
                textureRegion = SkylandAssets.textureCloud6;
                break;
            case Cloud7:
                textureRegion = SkylandAssets.textureCloud7;
                break;
            default:
                textureRegion = SkylandAssets.textureCloud1;
        }

        if (position.x == 0)
        {
            position.x = -textureRegion.getRegionWidth();
        }
    }

    @Override
    public float getX()
    {
        return position.x;
    }

    @Override
    public float getY()
    {
        return position.y;
    }

    @Override
    public float getWidth()
    {
        return textureRegion.getRegionWidth();
    }

    @Override
    public float getHeight()
    {
        return textureRegion.getRegionHeight();
    }

    @Override
    public void update(float delta)
    {
        position.x += delta * speed * wind;
        if (position.x > GameSettings.getMapWidth() || position.x < -textureRegion.getRegionWidth())
        {
            alive = false;
        }
    }

    @Override
    public void draw(GameLayers layer, SpriteBatch batch)
    {
        if ((!isForeground && layer == GameLayers.LayerBackground) || (isForeground && layer == GameLayers.LayerForeground))
        {
            batch.setColor(1, 1, 1, opacity);
            batch.draw(textureRegion, position.x, position.y, textureRegion.getRegionWidth() * scale, textureRegion.getRegionHeight() * scale);
            batch.setColor(1, 1, 1, 1);
        }
    }

    @Override
    public void dispose()
    {
    }
}
