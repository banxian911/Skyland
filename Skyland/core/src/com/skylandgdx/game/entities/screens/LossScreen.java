package com.skylandgdx.game.entities.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.skylandgdx.game.GameCoreEntity;
import com.skylandgdx.game.enums.LossReason;
import com.skylandgdx.lib.*;
import com.skylandgdx.lib.input.GameButton;
import com.skylandgdx.lib.input.IGameInput;
import com.skylandgdx.main.SkylandAssets;
import com.skylandgdx.main.SkylandUtils;

public class LossScreen extends GameEntityMenu
{
    protected final GameButton restartButton;
    protected final GameButton mainMenuButton;
    protected final GameButton quitButton;
    private String lossReason;

    @Override
    public GameTouchType handleTouch(float x, float y, GameTouchType previousTouchType, Integer activeTouchId)
    {
        if (GameCoreEntity.instance.getGamePauseType() != GameCoreEntity.GamePauseType.JustLost)
        {
            return GameTouchType.NotIntercepted;
        }
        return super.handleTouch(x, y, previousTouchType, activeTouchId);
    }

    public LossScreen()
    {
        float positionX = (GameSettings.getCameraWidth() - GameButton.getStandardButtonWidth()) / 2;
        float positionY = 215;

        restartButton = new GameButton(_.tr("pause.restart"), positionX, positionY);
        positionY -= GameButton.getStandardButtonHeight() + 10;
        mainMenuButton = new GameButton(_.tr("pause.mainMenu"), positionX, positionY);
        positionY -= GameButton.getStandardButtonHeight() + 10;
        quitButton = new GameButton(_.tr("pause.quit"), positionX, positionY);

        buttons = new GameButton[] {
                restartButton,
                mainMenuButton,
                quitButton
        };
    }

    @Override
    protected void inputClicked(IGameInput button)
    {
        if (button == restartButton)
        {
            GameCoreEntity.instance.changeGameState(GameCoreEntity.GameState.GameActive);
            GameCoreEntity.instance.restartCurrentLevel();
        }
        else if (button == mainMenuButton)
        {
            GameCoreEntity.instance.showMainMenu();
        }
        else
        {
            Gdx.app.exit();
        }
    }

    @Override
    public void draw(GameLayers layer, SpriteBatch batch)
    {
        if (layer == GameLayers.LayerHud && GameCoreEntity.instance.getGamePauseType() == GameCoreEntity.GamePauseType.JustLost)
        {
            SkylandUtils.drawBlackingMask(batch);
            float width = GameButton.getStandardButtonWidth() + 50;
            SkylandUtils.drawMenuBox(batch, _.tr("game.loss"), (GameSettings.getCameraWidth() - width)
                                                                  / 2, 15, width, 450);
            SkylandAssets.fontPrimeRegular.setColor(new Color(.8f, .8f, .8f, .8f));
            SkylandAssets.fontPrimeRegular.drawWrapped(
                    batch,
                    lossReason,
                    (GameSettings.getCameraWidth() - width) / 2 + 10,
                    405,
                    width - 20,
                    BitmapFont.HAlignment.CENTER
            );
            SkylandAssets.fontPrimeRegular.setColor(Color.WHITE);
            super.draw(layer, batch);
        }
    }

    public void setLossReason(LossReason lossReason)
    {
        this.lossReason = lossReason.reason;
    }
}
