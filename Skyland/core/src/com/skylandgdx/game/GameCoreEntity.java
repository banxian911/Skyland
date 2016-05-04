package com.skylandgdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.skylandgdx.game.entities.*;
import com.skylandgdx.game.entities.enemies.targets.AbstractTarget;
import com.skylandgdx.game.entities.screens.LossScreen;
import com.skylandgdx.game.entities.screens.PauseScreen;
import com.skylandgdx.game.entities.screens.WinScreen;
import com.skylandgdx.game.levels.*;
import com.skylandgdx.game.var.WinPoints;
import com.skylandgdx.lib.*;
import com.skylandgdx.main.SkylandAssets;

import java.util.Hashtable;
import java.util.Map;

public class GameCoreEntity extends GameEntitiesContainer
{


    public enum GameState
    {
        MainMenu,
        GameActive,
        GamePaused
    }

    public enum GamePauseType
    {
        InPauseMenu,
        JustLost,
        JustWon,
        ByOS,
    }

    static AbstractLevel[] levels = new AbstractLevel[]
    {
            new LevelTutorial(),
            new Level(1),
            new Level(2),
            new Level(3),
            new Level(4),
            new Level(5),
            new Level(6),
            new Level(7),
            new Level(8),
            new Level(9),
            new Level(10),
            new Level(11),
            new Level(12),
            new Level(13),
            new Level(14),
            new Level(15),
            new Level(16),
            new Level(17),
            new Level(18),
            new Level(19),
            new Level(20),
    };
    static public GameCoreEntity instance = new GameCoreEntity();
    static public int getLevelsCount()
    {
        return levels.length;
    }

    GameState gameState = GameState.GameActive;
    GamePauseType gamePauseType = null;
    Menu menu;
    Background background;
    MlvAndMissiles MlvAndMissiles;
    TargetsAndBombs targetsAndBombs;
    AbstractLevel currentLevel;
    PauseScreen pauseScreen;
    LossScreen lossScreen;
    WinScreen winScreen;
    int currentLevelIndex = 0;

    protected GameCoreEntity()
    {
        super();

        menu = Menu.instance;
        background = new Background();
        MlvAndMissiles = new MlvAndMissiles();
        targetsAndBombs = new TargetsAndBombs();
        pauseScreen = new PauseScreen();
        lossScreen = new LossScreen();
        winScreen = new WinScreen();

        add(background);
        add(GameFboParticle.instance);
        add(MlvAndMissiles);
        add(targetsAndBombs);
        add(GameFboParticle.foregroundInstance);
        add(Hud.instance);
        add(menu);
        add(pauseScreen);
        add(lossScreen);
        add(winScreen);

        touchHandlers.add(menu);
        touchHandlers.add(lossScreen);
        touchHandlers.add(winScreen);
        touchHandlers.add(pauseScreen);
        touchHandlers.add(Hud.instance);
        touchHandlers.add(new IGameTouchHandler()
        {
            @Override
            public GameTouchType handleTouch(float x, float y, GameTouchType previousTouchType, Integer activeTouchId)
            {
                if (currentLevel != null)
                {
                    return currentLevel.handleTouch(x, y, previousTouchType, activeTouchId);
                }
                return GameTouchType.NotIntercepted;
            }
        });
        touchHandlers.add(MlvAndMissiles);
    }

    public GameState getGameState()
    {
        return gameState;
    }

    public void showRandomlyPlacedEnemiesForBackground()
    {
        resetGame(1);
        targetsAndBombs.targets.randomlyPlaceEnemiesForBackground();
        targetsAndBombs.targets.randomlyPlaceEnemiesForBackground();
    }

    public GamePauseType getGamePauseType()
    {
        return gamePauseType;
    }

    public void changeGameState(GameState newGameState)
    {
        changeGameState(newGameState, null);
    }

    public void changeGameState(GameState newGameState, GamePauseType newPauseType)
    {
        if (
            newGameState == GameState.GamePaused
        )
        {
            entityPause();
        }
        if (newGameState == GameState.GameActive)
        {
            SkylandAssets.music01spaceFighterLoop.pause();
            SkylandAssets.music02Hitman.play();
        }
        else
        {
            SkylandAssets.music02Hitman.pause();
            SkylandAssets.music01spaceFighterLoop.play();
        }
        gameState = newGameState;
        gamePauseType = newPauseType;
    }

    public void start()
    {
        showMainMenu();
        Menu.instance.currentMenu = Menu.CurrentMenu.StartingScreen;
    }

    public void startLevel(int levelNumber)
    {
        changeGameState(GameState.GameActive);
        if (levelNumber > levels.length)
        {
            throw new RuntimeException("Level number too high!");
        }
        currentLevelIndex = levelNumber;
        currentLevel = levels[currentLevelIndex];
        currentLevel.start();
    }
    public boolean isTankMoving()
    {
        return MlvAndMissiles.Mlv.isTankMoving();
    }

    public void showMainMenu()
    {
       // showRandomlyPlacedEnemiesForBackground();//设置背景，显示随机飞机数量
        menu.currentMenu = Menu.CurrentMenu.MainMenu;
        changeGameState(GameState.MainMenu);
    }

    public void addEnemy(AbstractTarget.EnemyType enemyType, float y)
    {
        targetsAndBombs.targets.addEnemy(enemyType, y);
    }

    public void resetGame(int missilesLeft)
    {
        MlvAndMissiles.missiles.clear();
        targetsAndBombs.targets.clear();
        targetsAndBombs.bombs.clear();
        MlvAndMissiles.Mlv.setPositionX(GameSettings.getMapWidth() / 2);
        MlvAndMissiles.Mlv.setDestinationX(GameSettings.getMapWidth() / 2);
        MlvAndMissiles.Mlv.setMaxHealth(GameSettings.getGameDifficulty().maxHealth);
        MlvAndMissiles.Mlv.setHealth(GameSettings.getGameDifficulty().maxHealth);
        MlvAndMissiles.Mlv.setMissiles(missilesLeft);
    }

    @Override
    public void update(float delta)
    {
        if (gameState != GameState.GameActive)
        {
            if (gameState == GameState.MainMenu)
            {
                Menu.instance.update(delta);
            }
            else if (gamePauseType == GamePauseType.InPauseMenu)
            {
                Hud.instance.update(delta);
            }
            return;
        }
        super.update(delta);

        Hashtable<GameEntity, GameEntity[]> entitiesHitByAnyOf = GameEntitiesContainer.getEntitiesHitByAnyOf(MlvAndMissiles.missiles, targetsAndBombs.targets);
        for (Map.Entry<GameEntity, GameEntity[]> entry : entitiesHitByAnyOf.entrySet())
        {
            entry.getKey().alive = false;
            AbstractTarget targetHit = (AbstractTarget) entry.getValue()[0];
            targetHit.targetHit();
            currentLevel.registerHit(targetHit);
            GameFboParticle.instance.playParticleEffect(
                    targetHit.alive ? SkylandAssets.particleEffectSmallExplosion : SkylandAssets.particleEffectExplosion,
                    (targetHit.alive ? 0 : targetHit.getX()) + entry.getValue()[0].getWidth() / 2,
                    (targetHit.alive ? 0 : targetHit.getY()) + entry.getValue()[0].getHeight() / 2,
                    targetHit.alive ? targetHit : null);
            SkylandAssets.soundSimpleExplosion.play(GameSettings.getSoundVolume() * 0.7f);
        }

        if (targetsAndBombs.isMlvHit(MlvAndMissiles.Mlv))
        {
            MlvAndMissiles.hitMlv();
        }

        currentLevel.update(delta);

        if (currentLevel.checkForWin())
        {
            GameSettings.setReachedLevel(GameSettings.getGameDifficulty(), currentLevelIndex + 1);
            GameSettings.saveOptions();

            Integer nextLevel = currentLevelIndex + 1 < levels.length ? currentLevelIndex + 1 : null;
            WinPoints winPoints = currentLevel.getMissionCompletePoints();
            winScreen.setData(winPoints, nextLevel);
            changeGameState(GameState.GamePaused, GamePauseType.JustWon);
        }
        else if (currentLevel.checkForLoss() != null)
        {
            changeGameState(GameState.GamePaused, GamePauseType.JustLost);
            lossScreen.setLossReason(currentLevel.checkForLoss());
        }
    }

    public void restartCurrentLevel()
    {
        currentLevel.start();
    }

    @Override
    public void draw(GameLayers layer, SpriteBatch batch)
    {
        if (gameState == GameState.GameActive)
        {
            currentLevel.draw(layer, batch);
        }
        super.draw(layer, batch);
    }

    public float getPlayerCameraX()
    {
        return MlvAndMissiles.getPlayerTankX();
    }

    public int getTargetsCount()
    {
        return targetsAndBombs.targets.entitiesSize();
    }

    public int getAirborneMissilesCount()
    {
        return MlvAndMissiles.missiles.entitiesSize();
    }

    public int getTankHealth()
    {
        return MlvAndMissiles.Mlv.getHealth();
    }

    public int getTankMissilesLeft()
    {
        return MlvAndMissiles.Mlv.getMissilesCount();
    }

    public int getTargetsSize()
    {
        return targetsAndBombs.targets.entitiesSize();
    }
}
