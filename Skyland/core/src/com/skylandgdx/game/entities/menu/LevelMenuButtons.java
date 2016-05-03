package com.skylandgdx.game.entities.menu;

import com.skylandgdx.game.GameCoreEntity;
import com.skylandgdx.game.entities.Menu;
import com.skylandgdx.lib.GameEntityMenu;
import com.skylandgdx.lib.GameSettings;
import com.skylandgdx.lib._;
import com.skylandgdx.lib.input.GameButton;
import com.skylandgdx.lib.input.IGameInput;

public class LevelMenuButtons extends GameEntityMenu
{
    private final GameButton backButton;
    GameButton[] levelButtons = new GameButton[GameCoreEntity.getLevelsCount()];

    public LevelMenuButtons()
    {
        float positionX = (GameSettings.getCameraWidth() - GameButton.getStandardButtonWidth()) / 2;
        float positionY = 350;

        float levelWidth = 50;
        float levelHeight = 50;

        buttons = new GameButton[GameCoreEntity.getLevelsCount() + 1];
        float currentX = positionX;
        int rowCount = 5;
        for (int i = 0; i < GameCoreEntity.getLevelsCount(); i++)
        {
            GameButton levelButton = new GameButton(i == 0 ? "T" : Integer.toString(i), currentX, positionY);
            levelButton.setWidth(levelWidth);
            levelButton.setHeight(levelHeight);
            levelButtons[i] = levelButton;
            buttons[i] = levelButton;

            currentX += levelWidth + (GameButton.getStandardButtonWidth() - rowCount * levelWidth) / (rowCount  - 1);

            if (i % rowCount == 0)
            {
                currentX = positionX;
                positionY -= levelHeight + 10;
            }
        }

        positionY = 20;
        this.backButton = new GameButton(_.tr("menu.back"), positionX, positionY);
        buttons[GameCoreEntity.getLevelsCount()] = this.backButton;
    }

    public void filterAvailableLevels()
    {
        for (int i = 0; i < GameCoreEntity.getLevelsCount(); i++)
        {
            buttons[i].setVisible(i <= GameSettings.getReachedLevel(GameSettings.getGameDifficulty()));
        }
    }

    @Override
    protected void inputClicked(IGameInput button)
    {
        if (button == backButton)
        {
            Menu.instance.currentMenu = Menu.CurrentMenu.MainMenu;
        }
        else
        {
            for (int i = 0, levelButtonsLength = levelButtons.length; i < levelButtonsLength; i++)
            {
                GameButton levelButton = levelButtons[i];
                if (levelButton == button)
                {
                    GameCoreEntity.instance.startLevel(i);
                }
            }

        }

    }
}
