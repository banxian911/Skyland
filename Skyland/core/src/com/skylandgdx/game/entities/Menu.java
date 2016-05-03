package com.skylandgdx.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.skylandgdx.game.GameCoreEntity;
import com.skylandgdx.game.entities.menu.*;
import com.skylandgdx.lib.*;
import com.skylandgdx.lib.input.GameButton;
import com.skylandgdx.main.SkylandUtils;

public class Menu extends GameEntitiesContainer
{
    public void backPressed()
    {
        currentMenu = CurrentMenu.MainMenu;
    }

    public enum CurrentMenu
    {
        CreditsMenu,
        HighScoresMenu,
        LevelsMenu,
        MainMenu,
        OptionsMenu, StartingScreen,
    }

    static public Menu instance = new Menu();
    protected final StartingScreen startingScreen;
    protected final CreditsMenu creditsMenu;
    protected final HighScoresMenu highScoresMenu;
    protected final LevelMenuButtons levelButtons;
    protected final MainMenuButtons menuButtons;
    protected final OptionsMenuButtons optionsMenuButtons;

    public CurrentMenu currentMenu = CurrentMenu.MainMenu;


    protected Menu()
    {
        startingScreen = new StartingScreen();
        creditsMenu = new CreditsMenu();
        highScoresMenu = new HighScoresMenu();
        levelButtons = new LevelMenuButtons();
        menuButtons = new MainMenuButtons();
        optionsMenuButtons = new OptionsMenuButtons();
        add(menuButtons);
    }

    public void filterAvailableLevels()
    {
        levelButtons.filterAvailableLevels();
    }

    @Override
    public GameTouchType handleTouch(float x, float y, GameTouchType previousTouchType, Integer activeTouchId)
    {
        if (GameCoreEntity.instance.getGameState() != GameCoreEntity.GameState.MainMenu)
        {
            return GameTouchType.NotIntercepted;
        }

        return getCurrentMenu().handleTouch(x, y, previousTouchType, activeTouchId);
    }

    protected GameEntityMenu getCurrentMenu()
    {
        switch (currentMenu)
        {
            case StartingScreen:
                return startingScreen;
            case CreditsMenu:
                return creditsMenu;
            case HighScoresMenu:
                return highScoresMenu;
            case LevelsMenu:
                return levelButtons;
            case MainMenu:
                return menuButtons;
            case OptionsMenu:
                return optionsMenuButtons;
            default:
                return null;
        }
    }

    @Override
    public void update(float delta)
    {
        if (GameCoreEntity.instance.getGameState() != GameCoreEntity.GameState.MainMenu)
        {
            return;
        }
        getCurrentMenu().update(delta);
    }

    @Override
    public void draw(GameLayers layer, SpriteBatch batch)
    {
        if (GameCoreEntity.instance.getGameState() != GameCoreEntity.GameState.MainMenu)
        {
            return;
        }

        if (layer == GameLayers.LayerHud)
        {
            float width =
                currentMenu == CurrentMenu.CreditsMenu || currentMenu == CurrentMenu.OptionsMenu
                ? GameSettings.getCameraWidth() - 80
                : GameButton.getStandardButtonWidth() + 50;
            float positionX = (GameSettings.getCameraWidth() - width) / 2;

            if (currentMenu != CurrentMenu.StartingScreen)
            {
                SkylandUtils.drawMenuBox(batch, "Skyland", positionX, 15, width, 450);
            }

            getCurrentMenu().draw(layer, batch);
        }
    }

}
