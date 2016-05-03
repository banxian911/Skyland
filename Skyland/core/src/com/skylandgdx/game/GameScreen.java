package com.skylandgdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.skylandgdx.game.entities.Hud;
import com.skylandgdx.game.entities.Menu;
import com.skylandgdx.lib.GameFboParticle;
import com.skylandgdx.lib.GameLayers;
import com.skylandgdx.lib.GameSettings;
import com.skylandgdx.lib.GameTouchType;
import com.skylandgdx.main.SkylandUtils;

public class GameScreen implements Screen
{
    SpriteBatch batch;
    GameCoreEntity gameCoreEntity;

    public GameScreen()
    {
        GameFboParticle.createOrResumeAll();
        GameSettings.getCamera().setToOrtho(false, GameSettings.getResolutionWidth(), GameSettings.getResolutionHeight());
        batch = new SpriteBatch();
        cameraMarginX = GameSettings.getCameraWidth() / 4;

        gameCoreEntity = GameCoreEntity.instance;
    }

    GameTouchType[] activeTouches = new GameTouchType[10];
    Integer[] activeTouchesIds = new Integer[10];
    int activeTouchNextId = 0;

    public void handleInput()
    {
        for (int k = 0; k < 10; k++)
        {
            if (Gdx.input.isTouched(k))
            {
                if (activeTouchesIds[k] == null)
                {
                    activeTouchesIds[k] = activeTouchNextId++;
                    if (activeTouchNextId == Integer.MAX_VALUE)
                    {
                        activeTouchNextId = 0;
                    }
                }

                Vector3 touchPos = new Vector3();
                touchPos.set(Gdx.input.getX(k), Gdx.input.getY(k), 0);
                GameSettings.getCamera().unproject(touchPos);

                GameTouchType touchType = gameCoreEntity.handleTouch(touchPos.x, touchPos.y, activeTouches[k], activeTouchesIds[k]);
                activeTouches[k] =
                    touchType != GameTouchType.NotIntercepted
                    ? touchType
                    : (activeTouches[k] == null ? GameTouchType.NotIntercepted : activeTouches[k]);


            }
            else
            {
                activeTouches[k] = null;
                activeTouchesIds[k] = null;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.BACK))
        {
            if (!isBackPressed)
            {
                if (!Hud.instance.backPressed())
                {
                    Menu.instance.backPressed();
                }
                isBackPressed = true;
            }
        }
        else
        {
            isBackPressed = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.MENU))
        {
            if (!isMenuPressed)
            {
                Hud.instance.menuPressed();
                isMenuPressed = true;
            }
        }
        else
        {
            isMenuPressed = false;
        }
    }
    boolean isBackPressed = false;
    boolean isMenuPressed = false;

    private float fpsDrawDelay = 1;

    public void update(float delta)
    {
        gameCoreEntity.update(delta);
        updateCameraPosition(GameSettings.getCameraStartX(), gameCoreEntity.getPlayerCameraX(), GameSettings.getCamera());

        /*if (fpsDrawDelay <= 0)
        {
            Gdx.app.log("fps", String.valueOf(Math.round(Gdx.graphics.getFramesPerSecond())));
            fpsDrawDelay = 1;
        }
        else
        {
            fpsDrawDelay -= delta;
        }*/
    }

    final float cameraMarginX;

    public void updateCameraPosition(float cameraStartX, float tankPositionX, OrthographicCamera camera)
    {
        float cameraStartMin = Math.max(tankPositionX - cameraMarginX, 0);
        float cameraStartMax = Math.min(tankPositionX - GameSettings.getCameraWidth() + cameraMarginX,
                GameSettings.getMapWidth() - GameSettings.getCameraWidth());
        float newPositionX = Math.min(cameraStartMin, Math.max(cameraStartMax, cameraStartX))
                             + GameSettings.getCameraWidth() / 2;
        if (newPositionX != camera.position.x)
        {
            camera.position.x = newPositionX;
            camera.update();
        }
    }

    public void draw()
    {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        //Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        GameFboParticle.instance.beginPreparing();
        drawHelper(GameLayers.LayerPrepareParticles);
        GameFboParticle.instance.endPreparing();

        GameFboParticle.foregroundInstance.beginPreparing();
        drawHelper(GameLayers.LayerPrepareParticlesForeground);
        GameFboParticle.foregroundInstance.endPreparing();

        batch.setProjectionMatrix(GameSettings.getCamera().combined);
        batch.begin();
        for (GameLayers layer : GameLayers.values())
        {
            if (layer == GameLayers.LayerPrepareParticles || layer == GameLayers.LayerPrepareParticlesForeground)
            {
                continue;
            }
            drawHelper(layer);
        }
        batch.end();
    }

    protected void drawHelper(GameLayers layer)
    {
        gameCoreEntity.draw(layer, batch);
    }

    @Override
    public void render(float delta)
    {
        handleInput();
        update(delta);
        draw();
    }

    @Override
    public void resize(int width, int height)
    {
        GameSettings.setScreenWidth(width);
        GameSettings.setScreenHeight(height);
    }

    @Override
    public void show()
    {
    }

    @Override
    public void hide()
    {
    }


    GameCoreEntity.GameState previousGameState;
    GameCoreEntity.GamePauseType previousGamePauseType;
    @Override
    public void pause()
    {
        if (Gdx.app.getType() == Application.ApplicationType.Android)
        {
            previousGameState = gameCoreEntity.getGameState();
            previousGamePauseType = gameCoreEntity.getGamePauseType();
            gameCoreEntity.changeGameState(GameCoreEntity.GameState.GamePaused, GameCoreEntity.GamePauseType.ByOS);
            GameFboParticle.disposeAll();
        }
    }

    @Override
    public void resume()
    {
        if (Gdx.app.getType() == Application.ApplicationType.Android)
        {
            GameFboParticle.createOrResumeAll();
            gameCoreEntity.changeGameState(previousGameState, previousGamePauseType);
            SkylandUtils.recreateShapeRenderer();

            GameSettings.getCamera().setToOrtho(false, GameSettings.getResolutionWidth(), GameSettings.getResolutionHeight());
            batch = new SpriteBatch();

            gameCoreEntity = GameCoreEntity.instance;
        }
    }

    @Override
    public void dispose()
    {
        GameFboParticle.disposeAll();
    }
}
