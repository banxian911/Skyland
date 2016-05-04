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

public class MissileLaunchingVehicle extends GameEntity
{

    final int MlvWidth;
    final int MlvHeight;
    final int gunWidth;
    final int gunHeight;
    final Polygon MlvPolygon;

    final float shootingDelay = .7f;
    final float maxSpeedX = 250f;


    TVector2 MlvPos = new TVector2(0, GameSettings.groundPositionY);
    TVector2 destinationPos = new TVector2(0, GameSettings.groundPositionY);
    TVector2 gunOriginPos;
    TVector2 gunMuzzlePos = new TVector2(0, 0);
    TVector2 aimPos = new TVector2(GameSettings.getCameraWidth() / 2, GameSettings.groundPositionY);

    private float currentShootingDelay = 0;
    private float deltaX;
    private boolean turnedForward = false;
    private TextureRegion MlvRegion;
    private TextureRegion gunRegion;

    protected int missilesLeft = 50;
    protected int healthLeft = 5;

    public MissileLaunchingVehicle()
    {
        MlvRegion = new TextureRegion(SkylandAssets.textureTank);
        gunRegion = new TextureRegion(SkylandAssets.textureGun);

        MlvWidth = MlvRegion.getRegionWidth()/2;
        MlvHeight = MlvRegion.getRegionHeight()/2;
        gunWidth = gunRegion.getRegionWidth()*0;
        gunHeight = gunRegion.getRegionHeight()*0;
        MlvPolygon = new Polygon(new float[]{
                0, 0,
                MlvWidth, 0,
                MlvWidth, MlvHeight,
                0, MlvHeight
        });
        gunOriginPos = new TVector2(0, GameSettings.groundPositionY + MlvHeight - 10);

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
        return MlvPolygon;
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
        if (Math.abs(this.destinationPos.x - this.MlvPos.x) < deltaX)
        {
            this.MlvPos.x = this.destinationPos.x;
            ensureEngineSoundIs(false);
        }
        else if (this.destinationPos.x < this.MlvPos.x)
        {
            this.MlvPos.x -= deltaX;
            ensureEngineSoundIs(true);
        }
        else
        {
            this.MlvPos.x += deltaX;
            ensureEngineSoundIs(true);
        }

        turnedForward = aimPos.x >= gunOriginPos.x;
        gunOriginPos.x = MlvPos.x + MlvWidth*(turnedForward ? 0 : 1);//设置导弹起始点X坐标
        gunMuzzlePos.x = gunOriginPos.x + gunWidth * (turnedForward ? 1 : -1);
        gunMuzzlePos.y = gunOriginPos.y;

        gunMuzzlePos.rotate(gunOriginPos.x, gunOriginPos.y, (float) getAimAngle() * (turnedForward ? 1 : -1));

        if (currentShootingDelay >= 0)
        {
            currentShootingDelay -= delta;
        }
        MlvPolygon.setPosition(MlvPos.x, MlvPos.y);
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
                    MlvRegion,
                    turnedForward ? this.MlvPos.x : (this.MlvPos.x + MlvWidth),
                    GameSettings.groundPositionY,
                    0,
                    0,
                    MlvWidth,
                    MlvHeight,
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
        return MlvWidth;
    }

    @Override
    public float getHeight()
    {
        return MlvHeight;
    }

    public float getTankX()
    {
        return MlvPos.x;
    }

    public float getTankY()
    {
        return MlvPos.y;
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
        MlvPos.x = x;
    }

    public boolean isTankMoving()
    {
        return Math.abs(this.destinationPos.x - this.MlvPos.x) > 1f;
    }
}
