package com.skylandgdx.game.entities.mlv;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.skylandgdx.game.GameCoreEntity;
import com.skylandgdx.game.entities.Hud;
import com.skylandgdx.lib.GameEntity;
import com.skylandgdx.lib.GameLayers;
import com.skylandgdx.lib.GameSettings;
import com.skylandgdx.lib.TVector2;
import com.skylandgdx.main.SkylandAssets;

/**
 * User: trakos
 * Date: 10.11.13
 * Time: 07:38
 */
public class MissileLaunchingVehicle extends GameEntity
{

    final int tankWidth;
    final int tankHeight;
    final int gunWidth;
    final int gunHeight;
    final Polygon tankPolygon;

    final float shootingDelay = .7f;
    final float maxSpeedX = 250f;


    TVector2 tankPos = new TVector2(0, GameSettings.groundPositionY);
    TVector2 destinationPos = new TVector2(0, GameSettings.groundPositionY);
    TVector2 gunOriginPos;
    TVector2 gunMuzzlePos = new TVector2(0, 0);
    TVector2 aimPos = new TVector2(GameSettings.getCameraWidth() / 2, GameSettings.groundPositionY);

    private float currentShootingDelay = 0;
    private float deltaX;
    private boolean turnedForward = false;
    private TextureRegion tankRegion;
    private TextureRegion gunRegion;

    protected int missilesLeft = 50;
    protected int healthLeft = 5;

    public MissileLaunchingVehicle()
    {
        tankRegion = new TextureRegion(SkylandAssets.textureTank);
        gunRegion = new TextureRegion(SkylandAssets.textureGun);

        tankWidth = tankRegion.getRegionWidth()/2;
        tankHeight = tankRegion.getRegionHeight()/2;
        gunWidth = gunRegion.getRegionWidth()*0;
        gunHeight = gunRegion.getRegionHeight()*0;
        tankPolygon = new Polygon(new float[]{
                0, 0,
                tankWidth, 0,
                tankWidth, tankHeight,
                0, tankHeight
        });
        gunOriginPos = new TVector2(0, GameSettings.groundPositionY + tankHeight - 10);

        SkylandAssets.soundTank.setLooping(true);
        SkylandAssets.soundTank.setVolume(0.8f * GameSettings.getSoundVolume());
        SkylandAssets.soundTank.pause();
    }

    public void setMaxHealth(int health)
    {
        Hud.instance.maxHealth = health;
    }

    public void setHealth(int health)
    {
        healthLeft = health;
        Hud.instance.health = health;
    }
    public void setMissiles(int missiles)
    {
        missilesLeft = missiles;
        Hud.instance.missiles = missiles;
    }

    @Override
    public Polygon getHitBox()
    {
        return tankPolygon;
    }

    boolean isPlayingEngineSound = false;

    public void ensureEngineSoundIs(boolean stateToggle)
    {
        if (stateToggle != isPlayingEngineSound)
        {
            if (stateToggle)
            {
                SkylandAssets.soundTank.setVolume(.7f * GameSettings.getSoundVolume());
                SkylandAssets.soundTank.play();
            }
            else
            {
                SkylandAssets.soundTank.pause();
            }
            isPlayingEngineSound = stateToggle;
        }
    }

    @Override
    public void entityPause()
    {
        super.entityPause();

        ensureEngineSoundIs(false);
    }

    @Override
    public void update(float delta)
    {
        deltaX = maxSpeedX * delta;
        if (Math.abs(this.destinationPos.x - this.tankPos.x) < deltaX)
        {
            this.tankPos.x = this.destinationPos.x;
            ensureEngineSoundIs(false);
        }
        else if (this.destinationPos.x < this.tankPos.x)
        {
            this.tankPos.x -= deltaX;
            ensureEngineSoundIs(true);
        }
        else
        {
            this.tankPos.x += deltaX;
            ensureEngineSoundIs(true);
        }

        turnedForward = aimPos.x >= gunOriginPos.x;
        gunOriginPos.x = tankPos.x + tankWidth*(turnedForward ? 0 : 1);//设置导弹起始点X坐标
        gunMuzzlePos.x = gunOriginPos.x + gunWidth * (turnedForward ? 1 : -1);
        gunMuzzlePos.y = gunOriginPos.y;

        gunMuzzlePos.rotate(gunOriginPos.x, gunOriginPos.y, (float) getAimAngle() * (turnedForward ? 1 : -1));

        if (currentShootingDelay >= 0)
        {
            currentShootingDelay -= delta;
        }
        tankPolygon.setPosition(tankPos.x, tankPos.y);
    }

    @Override
    public void draw(GameLayers layer, SpriteBatch batch)
    {
        if (layer == GameLayers.LayerMain)
        {
            if (GameCoreEntity.instance.getGameState() == GameCoreEntity.GameState.MainMenu)
            {
                return;
            }
            batch.draw(
                    gunRegion,
                    gunOriginPos.x,
                    gunOriginPos.y - gunHeight / 2,
                    0,
                    gunHeight / 2,
                    gunWidth,
                    gunHeight,
                    turnedForward ? 1 : -1,
                    1,
                    (float)getAimAngle() * (turnedForward ? 1 : -1));
            batch.draw(
                    tankRegion,
                    turnedForward ? this.tankPos.x : (this.tankPos.x + tankWidth),
                    GameSettings.groundPositionY,
                    0,
                    0,
                    tankWidth,
                    tankHeight,
                    turnedForward ? 1 : -1,
                    1,
                    0);
        }
    }

    @Override
    public void dispose()
    {
    }

    private double cathetus;
    private double hypotenuse;
    public double getAimAngle()
    {
        cathetus = aimPos.distance(aimPos.x, gunOriginPos.y);
        hypotenuse = aimPos.distance(gunOriginPos);
        if (hypotenuse == 0) return 90;
        return (Math.asin(cathetus / hypotenuse) / Math.PI) * 180;
    }

    public void registerShot()
    {
        currentShootingDelay = shootingDelay;
        missilesLeft--;
        Hud.instance.missiles = missilesLeft;
    }

    public boolean isReadyToShoot()
    {
        return currentShootingDelay < 0 && missilesLeft > 0;
    }

    public void aimAt(float x, float y)
    {
        aimPos.x = x;
        aimPos.y = Math.max(y, gunOriginPos.y);
    }

    public float getDestinationX()
    {
        return this.destinationPos.x;
    }

    public void setDestinationX(float destinationX)
    {
        this.destinationPos.x = Math.max(0, Math.min(destinationX, GameSettings.getMapWidth()));
    }

    public float getAimX()
    {
        return aimPos.x;
    }

    public float getAimY()
    {
        return aimPos.y;
    }

    @Override
    public float getX()
    {
        return getTankX();
    }

    @Override
    public float getY()
    {
        return getTankY();
    }

    @Override
    public float getWidth()
    {
        return tankWidth;
    }

    @Override
    public float getHeight()
    {
        return tankHeight;
    }

    public float getTankX()
    {
        return tankPos.x;
    }

    public float getTankY()
    {
        return tankPos.y;
    }

    public float getTankMuzzleX()
    {
        return gunMuzzlePos.x;
    }

    public float getTankMuzzleY()
    {
        return gunMuzzlePos.y;
    }

    public float getTankGunOriginX()
    {
        return gunOriginPos.x;
    }

    public float getTankGunOriginY()
    {
        return gunOriginPos.y;
    }

    public void hit()
    {
        healthLeft--;
        Hud.instance.health = healthLeft;
    }

    public int getHealth()
    {
        return healthLeft;
    }

    public int getMissilesCount()
    {
        return missilesLeft;
    }

    public void setPositionX(int x)
    {
        tankPos.x = x;
    }

    public boolean isTankMoving()
    {
        return Math.abs(this.destinationPos.x - this.tankPos.x) > 1f;
    }
}
