package com.skylandgdx.game.entities.enemies;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.skylandgdx.lib.*;
import com.skylandgdx.main.SkylandAssets;

/**
 * User: trakos
 * Date: 10.11.13
 * Time: 21:31
 */
public class PlaneBomb extends GameEntity
{
    static final float verticalInitSpeed = -50f;
    static final float verticalAcceleration = -100f;
    static final float verticalMaxSpeed = -1000f;
    static int imageWidth;
    static int imageHeight;
    public Polygon bombPolygon = new Polygon(new float[8]);

    TVector2 velocityComponents;
    TVector2 position;
    TVector2 missileBottomTip;
    ParticleEffectPool.PooledEffect exhaustEffect;

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
    public float getHeight()
    {
        return imageHeight;
    }

    @Override
    public float getWidth()
    {
        return imageWidth;
    }

    public PlaneBomb(float initX, float initY, float planeSpeed, boolean planeDirection)
    {
        position = new TVector2(initX, initY);
        missileBottomTip = new TVector2(imageWidth / 2, 0);
        velocityComponents = new TVector2(planeSpeed * (planeDirection ? 1 : -1), verticalInitSpeed);

        imageWidth = SkylandAssets.textureBomb.getRegionWidth();
        imageHeight = SkylandAssets.textureBomb.getRegionHeight();

        bombPolygon.setVertices(new float[]{
                0, 0,
                0, imageHeight,
                imageWidth, 0,
                imageWidth, imageHeight,
        });

        exhaustEffect = SkylandAssets.particleEffectGrayExhaust.obtain();
    }

    @Override
    public Polygon getHitBox()
    {
        return bombPolygon;
    }

    @Override
    public void update(float delta)
    {
        position.x += velocityComponents.x * delta;
        position.y += velocityComponents.y * delta;
        // max bo to sa ujemne wartosci
        velocityComponents.y += Math.max(verticalAcceleration * delta, verticalMaxSpeed);

        alive = alive && position.x >= 0 && position.x <= GameSettings.getMapWidth() && position.y >= 0 && position.y <= GameSettings.getMapHeight();

        bombPolygon.setPosition(position.x, position.y);
        exhaustEffect.setPosition(position.x + bombPolygon.getBoundingRectangle().getWidth() / 2, position.y);
        exhaustEffect.update(delta);

        if (position.y <= GameSettings.groundPositionY)
        {
            makeBoom(false);
        }
    }

    public void makeBoom(boolean bigBoom)
    {
        GameFboParticle.instance.playParticleEffect(
                bigBoom ? SkylandAssets.particleEffectExplosion : SkylandAssets.particleEffectSmallExplosion,
                getX() + getWidth() / 2,
                getY() + getHeight() / 2);
        SkylandAssets.soundBomb.play(GameSettings.getSoundVolume() * (bigBoom ? 0.8f : 0.1f));
        alive = false;
    }

    @Override
    public void dispose()
    {
        exhaustEffect.dispose();
    }

    @Override
    public void draw(GameLayers layer, SpriteBatch batch)
    {
        if (!alive)
        {
            //noinspection UnnecessaryReturnStatement
            return;
        }
        else if (layer == GameLayers.LayerPrepareParticles)
        {
            GameFboParticle.instance.renderParticle(exhaustEffect);
        }
        else if (layer == GameLayers.LayerMain)
        {
            batch.draw(
                    SkylandAssets.textureBomb,
                    position.x,
                    position.y - SkylandAssets.textureBomb.getRegionHeight() / 2,
                    0,
                    SkylandAssets.textureBomb.getRegionHeight() / 2,
                    SkylandAssets.textureBomb.getRegionWidth(),
                    SkylandAssets.textureBomb.getRegionHeight(),
                    1,
                    1,
                    0);
        }
    }
}
