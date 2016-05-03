package com.skylandgdx.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.skylandgdx.game.entities.decorations.Cloud;
import com.skylandgdx.lib.*;
import com.skylandgdx.lib.input.IGameInput;
import com.skylandgdx.main.SkylandAssets;

public class StartingScreen extends GameEntityMenu
{
    public StartingScreen()
    {
        buttons = new IGameInput[0];
    }

    @Override
    protected void inputClicked(IGameInput button)
    {

    }

    float logoTransparency = 0;
    float transparency = 0;
    int direction = 1;
    @Override
    public void update(float delta)
    {
        if (logoTransparency < 1)
        {
            logoTransparency += delta * .5;
            if (logoTransparency > 1)
            {
                logoTransparency = 1;
            }
        }
        transparency += delta * direction;
        if (transparency > 1)
        {
            direction = -1;
            transparency = 1;
        }
        if (transparency < .2)
        {
            direction = 1;
            transparency = .4f;
        }
        super.update(delta);
    }

    @Override
    public GameTouchType handleTouch(float x, float y, GameTouchType previousTouchType, Integer activeTouchId)
    {
        Menu.instance.currentMenu = Menu.CurrentMenu.MainMenu;
        return GameTouchType.InterceptedByGame;
    }

    @Override
    public void draw(GameLayers layer, SpriteBatch batch)
    {
        if (layer == GameLayers.LayerHud)
        {
        
            batch.setColor(new Color(1, 1, 1, logoTransparency));
           /* batch.draw(
                    SkylandAssets.textureLogo,
                    (GameSettings.getCameraWidth() - SkylandAssets.textureLogo.getRegionWidth()) / 2,
                    (GameSettings.getCameraHeight() - SkylandAssets.textureLogo.getRegionHeight()) / 2 + 50,
                    SkylandAssets.textureLogo.getRegionWidth(),
                    SkylandAssets.textureLogo.getRegionHeight()
            );*/
            
            batch.draw(
                    SkylandAssets.textureLogo,
                    0,0,
                    GameSettings.getMapWidth(),
                    GameSettings.getCameraHeight()
                    
            );
            SkylandAssets.fontPrimeRegular.setColor(new Color(1, 1, 1, transparency));
            SkylandAssets.fontPrimeRegular.drawWrapped(
                    batch,
                    _.tr("game.start"),
                    0,
                    100,
                    GameSettings.getCameraWidth(),
                    BitmapFont.HAlignment.CENTER
            );
            SkylandAssets.fontPrimeRegular.setColor(Color.WHITE);
            batch.setColor(Color.WHITE);
        }
        super.draw(layer, batch);
    }
}
