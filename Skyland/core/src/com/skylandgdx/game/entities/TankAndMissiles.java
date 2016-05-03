package com.skylandgdx.game.entities;

import com.skylandgdx.game.GameCoreEntity;
import com.skylandgdx.game.entities.tank.Tank;
import com.skylandgdx.game.entities.tank.TankMissileContainer;
import com.skylandgdx.lib.GameEntitiesContainer;
import com.skylandgdx.lib.GameSettings;
import com.skylandgdx.lib.GameTouchType;
import com.skylandgdx.main.SkylandAssets;


public class TankAndMissiles extends GameEntitiesContainer
{
    public Tank tank;
    public TankMissileContainer missiles;
    final int missileWidth;
    final int missileHeight;

    public TankAndMissiles()
    {
        tank = new Tank();
        missiles = new TankMissileContainer();
        add(tank);
        add(missiles);

        missileWidth = SkylandAssets.textureShell.getRegionWidth();
        missileHeight = SkylandAssets.textureShell.getRegionHeight();
    }

    public GameTouchType handleTouch(float touchPosX, float touchPosY, GameTouchType previousTouchType, Integer activeTouchId)
    {
        if (GameCoreEntity.instance.getGameState() != GameCoreEntity.GameState.GameActive)
        {
            return GameTouchType.NotIntercepted;
        }
        if (previousTouchType == GameTouchType.InterceptedByMenu)
        {
            return GameTouchType.NotIntercepted;
        }
        if (touchPosY < tank.getTankGunOriginY())
        {
            tank.setDestinationX(touchPosX - 20);
        }
        else
        {
            tank.aimAt(touchPosX, touchPosY);
            if (tank.isReadyToShoot() && missiles.canSpawnAdditionalMissile())
            {
                missiles.add(tank.getTankGunOriginX(), tank.getTankGunOriginY(), tank.getAimX(), tank.getAimY());
                tank.registerShot();
                SkylandAssets.soundTankShot.play(.4f * GameSettings.getSoundVolume());
            }
        }
        return GameTouchType.InterceptedByGame;
    }

    public float getPlayerTankX()
    {
        return tank.getTankX();
    }

    public void hitTank()
    {
        tank.hit();
    }
}
