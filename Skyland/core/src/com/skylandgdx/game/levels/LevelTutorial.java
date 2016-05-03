package com.skylandgdx.game.levels;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.skylandgdx.game.GameCoreEntity;
import com.skylandgdx.game.entities.Hud;
import com.skylandgdx.game.entities.enemies.targets.AbstractTarget;
import com.skylandgdx.game.entities.menu.ArrowExplanationIcon;
import com.skylandgdx.game.entities.menu.TapHereIcon;
import com.skylandgdx.game.var.WinPoints;
import com.skylandgdx.lib.GameLayers;
import com.skylandgdx.lib.GameSettings;
import com.skylandgdx.lib.GameTouchType;
import com.skylandgdx.lib._;

public class LevelTutorial extends AbstractLevel
{
    static enum TutorialStep
    {
        Step1TapSky,
        Step2TapGround,
        Step3Health,
        Step4TargetsLeft,
        Step5MissilesLeft,
        Step6ShootPlaneIntroduction,
        Step7ShootPlane,
        Step8Difficulty,
        TutorialDone
    }

    TutorialStep currentTutorialStep;
    TapHereIcon tapHereIcon = new TapHereIcon();
    ArrowExplanationIcon arrowExplanationIcon = new ArrowExplanationIcon();

    public LevelTutorial()
    {
        enemySpawns.add(new EnemySpawn(AbstractTarget.EnemyType.PlaneWeak, 0));
        sortSpawns();
    }


    @Override
    public void start()
    {
        super.start();
        setCurrentStep(TutorialStep.Step1TapSky);
    }

    boolean waitingForTap = false;

    @Override
    public WinPoints getMissionCompletePoints()
    {
        return null;
    }

    private void setCurrentStep(TutorialStep tutorialStep)
    {
        tapHereIcon.setVisible(false);
        arrowExplanationIcon.setVisible(false);
        switch (tutorialStep)
        {
            case Step1TapSky:

                tapHereIcon.setText(_.tr("tutorial.tapToShoot"));
                tapHereIcon.setInvert(false);
                tapHereIcon.setX(GameSettings.getCameraWidth() / 2);
                tapHereIcon.setY(GameSettings.getCameraHeight() / 2);
                tapHereIcon.setVisible(true);
                break;

            case Step2TapGround:

                tapHereIcon.setText(_.tr("tutorial.tapToMove"));
                tapHereIcon.setInvert(true);
                tapHereIcon.setX(GameSettings.getCameraWidth() / 4);
                tapHereIcon.setY(GameSettings.groundPositionY);
                tapHereIcon.setVisible(true);
                break;

            case Step3Health:

                arrowExplanationIcon.setText(_.tr("tutorial.hudHealth"));
                arrowExplanationIcon.setArrowVisible(true);
                arrowExplanationIcon.setVisible(true);
                arrowExplanationIcon.setTextAlignment(BitmapFont.HAlignment.LEFT);
                arrowExplanationIcon.setX(50);
                arrowExplanationIcon.setY(380);
                arrowExplanationIcon.setIsRotated(false);
                waitingForTap = true;
                break;

            case Step4TargetsLeft:

                arrowExplanationIcon.setText(_.tr("tutorial.hudTargets"));
                arrowExplanationIcon.setArrowVisible(true);
                arrowExplanationIcon.setVisible(true);
                arrowExplanationIcon.setTextAlignment(BitmapFont.HAlignment.RIGHT);
                arrowExplanationIcon.setX(600);
                arrowExplanationIcon.setY(380);
                arrowExplanationIcon.setIsRotated(false);
                waitingForTap = true;
                break;

            case Step5MissilesLeft:

                arrowExplanationIcon.setText(_.tr("tutorial.hudMissiles"));
                arrowExplanationIcon.setArrowVisible(true);
                arrowExplanationIcon.setVisible(true);
                arrowExplanationIcon.setTextAlignment(BitmapFont.HAlignment.RIGHT);
                arrowExplanationIcon.setX(720);
                arrowExplanationIcon.setY(380);
                arrowExplanationIcon.setIsRotated(false);
                waitingForTap = true;
                break;

            case Step6ShootPlaneIntroduction:

                arrowExplanationIcon.setText(_.tr("tutorial.enemyIncoming"));
                arrowExplanationIcon.setArrowVisible(true);
                arrowExplanationIcon.setVisible(true);
                arrowExplanationIcon.setTextAlignment(BitmapFont.HAlignment.LEFT);
                arrowExplanationIcon.setX(50);
                arrowExplanationIcon.setY(GameSettings.getCameraHeight() / 2);
                arrowExplanationIcon.setIsRotated(true);
                waitingForTap = true;
                break;

            case Step7ShootPlane:

                AbstractTarget.EnemyType enemyType = enemySpawns.get(nextSpawn).enemyType;
                GameCoreEntity.instance.addEnemy(enemyType, enemyType.minHeight + random.nextFloat() * (enemyType.maxHeight - enemyType.minHeight));
                nextSpawn++;
                nextSpawnTime = Float.MAX_VALUE;
                break;

            case Step8Difficulty:

                arrowExplanationIcon.setText(_.tr("tutorial.complete"));
                arrowExplanationIcon.setArrowVisible(false);
                arrowExplanationIcon.setVisible(true);
                arrowExplanationIcon.setTextAlignment(BitmapFont.HAlignment.CENTER);
                arrowExplanationIcon.setX(GameSettings.getCameraWidth() / 2);
                arrowExplanationIcon.setY(GameSettings.getCameraHeight() - 100);
                arrowExplanationIcon.setIsRotated(false);
                waitingForTap = true;
                break;

            case TutorialDone:
            default:
                break;
        }
        currentTutorialStep = tutorialStep;
    }

    private void advanceTutorial()
    {
        if (checkIfStepIsCompleted())
        {
            setCurrentStep(TutorialStep.values()[currentTutorialStep.ordinal() + 1]);
        }
    }

    @Override
    public GameTouchType handleTouch(float x, float y, GameTouchType previousTouchType, Integer activeTouchId)
    {
        if (GameCoreEntity.instance.getGameState() != GameCoreEntity.GameState.GameActive)
        {
            return GameTouchType.NotIntercepted;
        }
        else if (previousTouchType == null && waitingForTap)
        {
            waitingForTap = false;
            return GameTouchType.InterceptedByMenu;
        }
        return super.handleTouch(x, y, previousTouchType, activeTouchId);
    }

    private boolean checkIfStepIsCompleted()
    {
        switch (currentTutorialStep)
        {
            case Step1TapSky:
                return GameCoreEntity.instance.getAirborneMissilesCount() > 0;
            case Step2TapGround:
                return GameCoreEntity.instance.isTankMoving();
            case Step3Health:
            case Step4TargetsLeft:
            case Step5MissilesLeft:
            case Step6ShootPlaneIntroduction:
            case Step8Difficulty:
                return !waitingForTap;
            case Step7ShootPlane:
                return remainingHitPoints == 0;
            case TutorialDone:
            default:
                return false;
        }
    }

    @Override
    protected String getTitle()
    {
        return "";
    }

    @Override
    public void draw(GameLayers layer, SpriteBatch batch)
    {
        super.draw(layer, batch);
        if (layer == GameLayers.LayerHud)
        {
            tapHereIcon.draw(layer, batch);
            arrowExplanationIcon.draw(layer, batch);
        }
    }

    public void update(float delta)
    {
        timeTaken += delta;
        Hud.instance.enemiesLeft = enemySpawns.size() - nextSpawn + GameCoreEntity.instance.getTargetsSize();
        tapHereIcon.update(delta);
        arrowExplanationIcon.update(delta);
        advanceTutorial();
    }

    @Override
    public int getMissilesCount(int totalHitPoints)
    {
        return 100;
    }

    public boolean checkForWin()
    {
        return currentTutorialStep == TutorialStep.TutorialDone;
    }
}
