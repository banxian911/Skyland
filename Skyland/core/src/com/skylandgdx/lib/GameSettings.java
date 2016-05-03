package com.skylandgdx.lib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Json;
import com.skylandgdx.main.SkylandAssets;

/**
 * User: trakos
 * Date: 10.11.13
 * Time: 07:19
 */
public class GameSettings
{
    private static int resolutionWidth;
    private static int resolutionHeight;
    private static int screenWidth;
    private static int screenHeight;
    private static float musicVolume = 0;
    private static float soundVolume = .1f;
    private static GameDifficulty gameDifficulty = GameDifficulty.Medium;
    static private final OrthographicCamera camera = new OrthographicCamera();
    static public final int groundPositionY = 58;
    static Preferences preferences;
    static int reachedLevels[] = new int[GameDifficulty.values().length];
    static Json json = new Json();

    public static void setSoundVolume(float soundVolume)
    {
        GameSettings.soundVolume = soundVolume;
    }

    public static void setMusicVolume(float musicVolume)
    {
        GameSettings.musicVolume = musicVolume;
        refreshMusicVolume();
    }

    public static GameDifficulty getGameDifficulty()
    {
        return gameDifficulty;
    }

    public static void setGameDifficulty(GameDifficulty gameDifficulty)
    {
        GameSettings.gameDifficulty = gameDifficulty;
    }

    static public int getResolutionWidth()
    {
        return 800;
    }

    static public int getResolutionHeight()
    {
        return 480;
    }

    static public float getCameraWidth()
    {
        return camera.viewportWidth;
    }

    static public float getCameraHeight()
    {
        return camera.viewportHeight;
    }

    static public int getMapWidth()
    {
        return 800;
    }

    static public int getMapHeight()
    {
        return 480;
    }


    static public OrthographicCamera getCamera()
    {
        return camera;
    }

    static public float getCameraX()
    {
        return camera.position.x;
    }

    static public float getCameraY()
    {
        return camera.position.y;
    }

    static public float getCameraStartX()
    {
        return camera.position.x - getCameraWidth() / 2;
    }

    static public float getCameraStartY()
    {
        return camera.position.y - getCameraHeight() / 2;
    }

    public static float getMusicVolume()
    {
        return musicVolume;
    }

    public static float getSoundVolume()
    {
        return soundVolume;
    }

    public static void refreshMusicVolume()
    {
        SkylandAssets.music01spaceFighterLoop.setVolume(GameSettings.getMusicVolume() * 0.4f);
    }

    public static void setScreenWidth(int screenWidth)
    {
        GameSettings.screenWidth = screenWidth;
    }

    public static void setScreenHeight(int screenHeight)
    {
        GameSettings.screenHeight = screenHeight;
    }

    public static int getScreenWidth()
    {
        return screenWidth;
    }

    public static int getScreenHeight()
    {
        return screenHeight;
    }


    public static int getReachedLevel(GameDifficulty gameDifficulty)
    {
        return Math.max(0, reachedLevels[gameDifficulty.ordinal()]);
    }

    public static void setReachedLevel(GameDifficulty gameDifficulty, int reachedLevel)
    {
        for (int k = 0; k <= gameDifficulty.ordinal(); k++)
        {
            reachedLevels[k] = Math.max(reachedLevel, reachedLevels[k]);
        }
        saveOptions();
    }

    static Preferences getPreferences()
    {
        if (preferences == null)
        {
            preferences = Gdx.app.getPreferences("IronClouds");
        }
        return preferences;
    }

    static Preferences getHighScoresPreferences()
    {
        if (preferences == null)
        {
            preferences = Gdx.app.getPreferences("IronClouds.highScores");
        }
        return preferences;
    }

    public static Tuple<String, Integer>[] getHighScores()
    {
        return highScores;
    }

    public static boolean isScoreHigh(int value)
    {
        return highScores[9].b < value;
    }

    public static void addHighScore(String name, int value)
    {
        Tuple<String, Integer> prevScore = null;
        boolean added = false;
        for (int k = 0; k < 10; k++)
        {
            if (!added && highScores[k].b < value)
            {
                prevScore = highScores[k];
                highScores[k] = new Tuple<String, Integer>(name, value);
                added = true;
            }
            else if (added)
            {
                Tuple<String, Integer> temporary = highScores[k];
                highScores[k] = prevScore;
                prevScore = temporary;
            }
        }
        saveHighScores();
    }

    static Tuple<String, Integer>[] highScores;
    
    @SuppressWarnings({"unchecked"})
    public static void loadHighScores()
    {
        highScores = new Tuple[10];
        Preferences preferences = getHighScoresPreferences();
        for (int k = 0; k < 10; k++)
        {
            String name = preferences.getString("name" + Integer.toString(k));
            Integer value = preferences.getInteger("value" + Integer.toString(k));
            highScores[k] = new Tuple<String, Integer>(name, value);
        }
    }
    
    public static void saveHighScores()
    {
        Preferences preferences = getHighScoresPreferences();
        for (int k = 0; k < 10; k++)
        {
            preferences.putString("name" + Integer.toString(k), highScores[k].a);
            preferences.putInteger("value" + Integer.toString(k), highScores[k].b);
        }
        preferences.flush();
    }

    public static void saveOptions()
    {
        Preferences preferences = getPreferences();
        preferences.putString("reachedLevels", json.toJson(reachedLevels));
        preferences.putFloat("music", getMusicVolume());
        preferences.putFloat("sound", getSoundVolume());
        preferences.putInteger("difficulty", getGameDifficulty().ordinal());
        preferences.putString("lastUsedNick", lastUsedNick);
        preferences.putBoolean("initialized", true);
        preferences.flush();
    }

    public static void loadOptions()
    {
        Preferences preferences = getPreferences();
        if (preferences.getBoolean("initialized"))
        {
            try
            {
                reachedLevels = json.fromJson(int[].class, preferences.getString("reachedLevels"));
            }
            catch (Exception e)
            {
                reachedLevels = new int[GameDifficulty.values().length];
            }
            setMusicVolume(preferences.getFloat("music"));
            setSoundVolume(preferences.getFloat("sound"));
            setGameDifficulty(GameDifficulty.values()[Math.min(GameDifficulty.values().length, Math.max(0, preferences.getInteger("difficulty")))]);
            lastUsedNick = preferences.getString("lastUsedNick");
        }
        else
        {
            reachedLevels = new int[GameDifficulty.values().length];
            for (int i = 0; i < reachedLevels.length; i++)
            {
                reachedLevels[i] = 0;
            }
            setMusicVolume(1);
            setSoundVolume(1);
            setGameDifficulty(GameDifficulty.Medium);
            saveOptions();
            lastUsedNick = "";
        }
    }

    public static GameDifficulty getEasiestDifficulty()
    {
        return GameDifficulty.Easy;
    }

    public static void setLastUsedNick(String lastUsedNick)
    {
        GameSettings.lastUsedNick = lastUsedNick;
        saveOptions();
    }

    static String lastUsedNick;

    public static String getLastUsedNick()
    {
        return lastUsedNick;
    }
}
