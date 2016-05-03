package com.skylandgdx.game.entities.menu;

import com.badlogic.gdx.Gdx;
import com.skylandgdx.game.GameCoreEntity;
import com.skylandgdx.game.entities.Menu;
import com.skylandgdx.lib.GameEntityMenu;
import com.skylandgdx.lib.GameSettings;
import com.skylandgdx.lib._;
import com.skylandgdx.lib.input.GameButton;
import com.skylandgdx.lib.input.IGameInput;

public class MainMenuButtons extends GameEntityMenu
{
    protected final GameButton playButton;
    protected final GameButton levelButton;
    protected final GameButton optionsButton;
    protected final GameButton highScoresButton;
    protected final GameButton creditsButton;
    protected final GameButton quitButton;


    public MainMenuButtons()
    {
        float positionX = (GameSettings.getCameraWidth() - GameButton.getStandardButtonWidth()) / 2;
        float positionY = 415;

        float mainButtonHeight = 80;
        float otherButtonHeight = 50;

        positionY -= mainButtonHeight + 10;
        this.playButton = new GameButton(_.tr("menu.play"), positionX, positionY);
        playButton.setHeight(mainButtonHeight);
        positionY -= otherButtonHeight + 10;
        this.levelButton = new GameButton(_.tr("menu.levels"), positionX, positionY);
        levelButton.setHeight(otherButtonHeight);
        positionY -= otherButtonHeight + 10;
        this.optionsButton = new GameButton(_.tr("menu.settings"), positionX, positionY);
        optionsButton.setHeight(otherButtonHeight);
        positionY -= otherButtonHeight + 10;
        this.highScoresButton = new GameButton(_.tr("menu.highScores"), positionX, positionY);
        highScoresButton.setHeight(otherButtonHeight);
        positionY -= otherButtonHeight + 10;
        this.creditsButton = new GameButton(_.tr("menu.credits"), positionX, positionY);
        creditsButton.setHeight(otherButtonHeight);
        positionY -= otherButtonHeight + 10;
        this.quitButton = new GameButton(_.tr("menu.quit"), positionX, positionY);
        quitButton.setHeight(otherButtonHeight);

        buttons = new GameButton[]{
                playButton,
                levelButton,
                optionsButton,
                highScoresButton,
                creditsButton,
                quitButton
        };
    }


    @Override
    protected void inputClicked(IGameInput button)
    {
        if (button == playButton)
        {
            int reachedLevel = GameSettings.getReachedLevel(GameSettings.getGameDifficulty());
            GameCoreEntity.instance.startLevel(reachedLevel >= GameCoreEntity.getLevelsCount() ? 0 : reachedLevel);
        }
        else if (button == levelButton)
        {
            Menu.instance.currentMenu = Menu.CurrentMenu.LevelsMenu;
            Menu.instance.filterAvailableLevels();
        }
        else if (button == optionsButton)
        {
            Menu.instance.currentMenu = Menu.CurrentMenu.OptionsMenu;
        }
        else if (button == highScoresButton)
        {
            Menu.instance.currentMenu = Menu.CurrentMenu.HighScoresMenu;
        }
        else if (button == creditsButton)
        {
            Menu.instance.currentMenu = Menu.CurrentMenu.CreditsMenu;
        }
        else if (button == quitButton)
        {
            Gdx.app.exit();
        }
    }
}
