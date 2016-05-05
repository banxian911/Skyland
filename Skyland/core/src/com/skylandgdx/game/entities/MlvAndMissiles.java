package com.skylandgdx.game.entities;

import com.skylandgdx.game.GameCoreEntity;
import com.skylandgdx.game.entities.mlv.MissileLaunchingVehicle;
import com.skylandgdx.game.entities.mlv.MlvMissileContainer;
import com.skylandgdx.lib.GameEntitiesContainer;
import com.skylandgdx.lib.GameSettings;
import com.skylandgdx.lib.GameTouchType;
import com.skylandgdx.main.SkylandAssets;


public class MlvAndMissiles extends GameEntitiesContainer
{
    public MissileLaunchingVehicle Mlv;
    public MlvMissileContainer missiles;
    final int missileWidth;
    final int missileHeight;

    public MlvAndMissiles()
    {
        Mlv = new MissileLaunchingVehicle();
        missiles = new MlvMissileContainer();
        add(Mlv);
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
        if (touchPosY < Mlv.getTankGunOriginY())
        {
            Mlv.setDestinationX(touchPosX - 20);
        }
        else
        {
            Mlv.aimAt(touchPosX, touchPosY);
            if (Mlv.isReadyToShoot() && missiles.canSpawnAdditionalMissile())
            {
            	//发射导弹，设置导弹的起始坐标和目标坐标
                missiles.add(Mlv.getTankGunOriginX(), Mlv.getTankGunOriginY(), Mlv.getAimX(), Mlv.getAimY());
                Mlv.registerShot();
                SkylandAssets.soundTankShot.play(.4f * GameSettings.getSoundVolume());
            }
        }
        return GameTouchType.InterceptedByGame;
    }

    public float getPlayerTankX()
    {
        return Mlv.getTankX();
    }

    public void hitMlv()
    {
        Mlv.hit();
    }
}
