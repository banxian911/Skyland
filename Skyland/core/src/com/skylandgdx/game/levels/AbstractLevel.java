package com.skylandgdx.game.levels;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.skylandgdx.game.GameCoreEntity;
import com.skylandgdx.game.entities.Hud;
import com.skylandgdx.game.entities.enemies.targets.AbstractTarget;
import com.skylandgdx.game.enums.LossReason;
import com.skylandgdx.game.var.WinPoints;
import com.skylandgdx.lib.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public abstract class AbstractLevel implements IGameTouchHandler
{

    public float getTimeTaken()
    {
        return timeTaken;
    }

    public WinPoints getMissionCompletePoints()
    {
        GameDifficulty easy = GameSettings.getEasiestDifficulty();
        GameDifficulty current = GameSettings.getGameDifficulty();
        WinPoints winPoints = new WinPoints();
        winPoints.healthLeft = GameCoreEntity.instance.getTankHealth();
        winPoints.healthLessThanOnEasiest = easy.maxHealth - current.maxHealth;
        winPoints.missilesLeft = GameCoreEntity.instance.getTankMissilesLeft();
        winPoints.missilesLessThanOnEasiest = getMissilesCount(getTotalHitPoints(), easy) - getMissilesCount(getTotalHitPoints(), current);
        winPoints.timeTaken = timeTaken;
        winPoints.difficultyBonus = current.difficultyBonusPoints;
        winPoints.gameDifficulty = GameSettings.getGameDifficulty();
        for (EnemySpawn enemySpawn : enemySpawns)
        {
            if (winPoints.targetsShotDictionary.get(enemySpawn.enemyType) == null)
            {
                winPoints.targetsShotDictionary.put(enemySpawn.enemyType, 1);
            }
            else
            {
                winPoints.targetsShotDictionary.put(enemySpawn.enemyType,
                        winPoints.targetsShotDictionary.get(enemySpawn.enemyType) + 1);
            }
        }
        return winPoints;
    }

    class EnemySpawn
    {
        public AbstractTarget.EnemyType enemyType;
        public float time;

        EnemySpawn(AbstractTarget.EnemyType enemyType, float time)
        {
            this.enemyType = enemyType;
            this.time = time;
        }
    }

    float timeTaken = 0;
    float nextSpawnTime;
    int nextSpawn;
    int remainingHitPoints = 0;

    ArrayList<EnemySpawn> enemySpawns = new ArrayList<EnemySpawn>();
    Random random = new Random();

    protected void sortSpawns()
    {
        Collections.sort(enemySpawns, new Comparator<EnemySpawn>()
        {
            @Override
            public int compare(EnemySpawn p1, EnemySpawn p2)
            {
                return p1.time < p2.time ? -1 : (p1.time == p2.time ? 0 : 1);
            }

        });
    }

    public void start()
    {
        timeTaken = 0;
        nextSpawn = 0;
        nextSpawnTime = enemySpawns.get(0).time;
        remainingHitPoints = getTotalHitPoints();
        GameCoreEntity.instance.resetGame(getMissilesCount(remainingHitPoints));
        Hud.instance.showTitle(getTitle(), 2f);
    }

    public int getMissilesCount(int totalHitPoints)
    {
        return getMissilesCount(totalHitPoints, GameSettings.getGameDifficulty());
    }

    public int getMissilesCount(int totalHitPoints, GameDifficulty gameDifficulty)
    {
        return (int) Math.floor(totalHitPoints * gameDifficulty.missilesPerHitPoint);
    }

    public void registerHit(AbstractTarget targetHit)
    {
        remainingHitPoints--;
    }

    abstract protected String getTitle();

    public int getTotalHitPoints()
    {
        int totalHitPoints = 0;
        for (EnemySpawn enemySpawn : enemySpawns)
        {
            totalHitPoints += enemySpawn.enemyType.hitPoints;
        }
        return totalHitPoints;
    }

    public void update(float delta)
    {
        timeTaken += delta;
        if (timeTaken > nextSpawnTime * GameSettings.getGameDifficulty().spawnTimeMultiplier)
        {
            AbstractTarget.EnemyType enemyType = enemySpawns.get(nextSpawn).enemyType;
            GameCoreEntity.instance.addEnemy(enemyType, enemyType.minHeight + random.nextFloat() * (enemyType.maxHeight - enemyType.minHeight));
            nextSpawn++;
            if (nextSpawn >= enemySpawns.size())
            {
                nextSpawnTime = Float.MAX_VALUE;
            }
            else
            {
                nextSpawnTime = enemySpawns.get(nextSpawn).time;
            }
        }
        Hud.instance.enemiesLeft = enemySpawns.size() - nextSpawn + GameCoreEntity.instance.getTargetsSize();
    }

    public void draw(GameLayers layer, SpriteBatch batch)
    {

    }

    public GameTouchType handleTouch(float x, float y, GameTouchType previousTouchType, Integer activeTouchId)
    {
        return GameTouchType.NotIntercepted;
    }

    public boolean checkForWin()
    {
        return nextSpawn >= enemySpawns.size() && GameCoreEntity.instance.getTargetsCount() == 0;
    }

    public LossReason checkForLoss()
    {
        GameCoreEntity game = GameCoreEntity.instance;
        if (game.getTankHealth() <= 0)
        {
            return LossReason.OutOfHealth;
        }
        else if (game.getTankMissilesLeft() <= 0 && game.getAirborneMissilesCount() <= 0)
        {
            return LossReason.ZeroMissiles;
        }
        else if (game.getTankMissilesLeft() + game.getAirborneMissilesCount() < remainingHitPoints)
        {
            return LossReason.TooFewMissiles;
        }
        else
        {
            return null;
        }
    }
}
