package com.skylandgdx.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.skylandgdx.game.GameCoreEntity;
import com.skylandgdx.game.GameScreen;
import com.skylandgdx.lib.GameSettings;


public class SkylandMain extends Game
{
    static Screen gameScreen;
    static boolean first = true;

    public SkylandMain()
    {

    }

    @Override
    public void create()
    {
        SkylandAssets.loadAssets();
        SkylandAssets.music01spaceFighterLoop.setLooping(true);
        SkylandAssets.music02Hitman.setLooping(true);
        GameSettings.refreshMusicVolume();
        SkylandAssets.music01spaceFighterLoop.play();
        if (first)
        {
            GameSettings.loadOptions();
            GameSettings.loadHighScores();
            Gdx.input.setCatchBackKey(true);
            Gdx.input.setCatchMenuKey(true);
            gameScreen = new GameScreen();

            first = false;
        }
        gameScreen.resume();
        setScreen(gameScreen);
        GameCoreEntity.instance.start();
    }

    @Override
    public void resume()
    {
        /*IronCloudsAssets.loadAssets();
        IronCloudsAssets.music01spaceFighterLoop.setLooping(true);
        GameSettings.refreshMusicVolume();
        IronCloudsAssets.music01spaceFighterLoop.play();*/

        super.resume();
    }

    @Override
    public void pause()
    {
        /*IronCloudsAssets.music01spaceFighterLoop.stop();
        IronCloudsAssets.unloadAssets();*/

        super.pause();
    }
}
