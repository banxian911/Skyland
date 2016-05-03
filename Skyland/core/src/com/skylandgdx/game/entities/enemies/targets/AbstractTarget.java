package com.skylandgdx.game.entities.enemies.targets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.skylandgdx.lib.*;
import com.skylandgdx.main.SkylandAssets;

import java.util.ArrayList;

/**
 * User: trakos
 * Date: 11.11.13
 * Time: 03:30
 */
public abstract class AbstractTarget extends GameEntity
{

    public enum EnemyType
    {
        PlaneWeak(.2f, .7f, 1, SkylandAssets.texturePlane1),
        PlaneNormal(.4f, .9f, 1, SkylandAssets.texturePlane2),
        Heli(0, .6f, 1, SkylandAssets.textureHeli),
        Zeppelin(.7f, 1, 5, SkylandAssets.textureZeppelin),
        Bomber(.8f, 1, 3, SkylandAssets.textureBomber);

        public float minHeight;
        public float maxHeight;
        public int hitPoints;
        public TextureRegion textureRegion;

        EnemyType(float minHeight, float maxHeight, int hitPoints, TextureRegion textureRegion)
        {
            this.minHeight = minHeight;
            this.maxHeight = maxHeight;
            this.hitPoints = hitPoints;
            this.textureRegion = textureRegion;
        }
    }

    static public AbstractTarget instantiate(EnemyType enemyType, float y)
    {
        return instantiate(enemyType, 0, y);
    }

    public static AbstractTarget instantiate(EnemyType enemyType, float x, float y)
    {
        y = enemyType.minHeight + y * (enemyType.maxHeight - enemyType.minHeight);
        switch (enemyType)
        {
            case PlaneWeak:
                return new PlaneWeak(x, y);
            case PlaneNormal:
                return new PlaneNormal(x, y);
            case Heli:
                return new Heli(x, y);
            case Zeppelin:
                return new Zeppelin(x, y);
            case Bomber:
                return new Bomber(x, y);
            default:
                throw new RuntimeException("unknown enemy type: " + enemyType.toString());
        }
    }

    float speed = 300f;

    float positionX = 0;
    float positionY = 300f;
    int direction = 1;
    int hp;
    float timeout = 0;
    float nextBombIn;

    Polygon targetPolygon;
    TextureRegion texture;
    ArrayList<TParticle> particles = new ArrayList<TParticle>(4);

    abstract protected int getInitialHp();

    abstract protected void addHitEffectParticle(int remainingHp);

    protected abstract float getNextBombDelay();

    protected abstract float getOutOfMapTimeout();

    public AbstractTarget(float x, float y, TextureRegion texture)
    {
        this.texture = texture;
        hp = getInitialHp();
        nextBombIn = getNextBombDelay() * GameSettings.getGameDifficulty().bombTimeMultiplier;
        float minHeight = GameSettings.groundPositionY + 50;
        positionX = GameSettings.getMapWidth() * x - texture.getRegionWidth();
        positionY = (GameSettings.getMapHeight() - minHeight - 50) * y + minHeight;
        initPolygon();
    }


    public void initPolygon()
    {
        targetPolygon = new Polygon(new float[]{
                0, 0,
                0, texture.getRegionHeight(),
                texture.getRegionWidth(), texture.getRegionHeight(),
                texture.getRegionWidth(), 0
        });
    }

    public float getWidth()
    {
        return texture.getRegionWidth();
    }

    public float getHeight()
    {
        return texture.getRegionHeight();
    }

    @Override
    public float getX()
    {
        return positionX - (direction == 1 ? 0 : texture.getRegionWidth());
    }

    @Override
    public float getY()
    {
        return positionY;
    }

    @Override
    public void update(float delta)
    {
        if (timeout > 0)
        {
            timeout -= delta;
            return;
        }
        nextBombIn -= delta;
        if (direction == 1 && positionX > GameSettings.getMapWidth())
        {
            direction = -1;
            positionX = GameSettings.getMapWidth() + texture.getRegionWidth();
            timeout = getOutOfMapTimeout();
        }
        else if (direction == -1 && positionX < -texture.getRegionWidth())
        {
            direction = 1;
            timeout = getOutOfMapTimeout();
        }
        positionX += direction * speed * delta;

        targetPolygon.setPosition(positionX - (direction == 1 ? 0 : texture.getRegionWidth()), positionY);
        for (TParticle particle : particles)
        {
            particle.effect.setPosition(positionX + particle.x * direction, getY() + particle.y);
            particle.effect.update(delta);
        }
    }

    public boolean shouldDropBombNow()
    {
        if (nextBombIn < 0)
        {
            nextBombIn = getNextBombDelay() * GameSettings.getGameDifficulty().bombTimeMultiplier;
            return true;
        }
        return false;
    }


    @Override
    public void draw(GameLayers layer, SpriteBatch batch)
    {
        if (timeout > 0)
        {
            return;
        }
        if (layer == GameLayers.LayerPrepareParticlesForeground)
        {
            for (TParticle particle : particles)
            {
                GameFboParticle.foregroundInstance.renderParticle(particle.effect);
            }
        }
        if (layer == GameLayers.LayerMain)
        {
            batch.draw(
                    texture,
                    positionX,
                    positionY,
                    texture.getRegionWidth() * direction,
                    texture.getRegionHeight());
            //GameDebug.markPolygon(batch, targetPolygon);
        }
    }

    @Override
    public Polygon getHitBox()
    {
        return targetPolygon;
    }

    @Override
    public void dispose()
    {
        for (TParticle particle : particles)
        {
            particle.effect.dispose();
        }
        particles.clear();
    }

    public float getSpeed()
    {
        return speed;
    }

    public int getDirection()
    {
        return direction;
    }

    public void targetHit()
    {
        if (--hp <= 0)
        {
            alive = false;
        }
        else
        {
            addHitEffectParticle(hp);
        }
    }
}
