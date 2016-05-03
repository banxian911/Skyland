package com.skylandgdx.game.entities.menu;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.skylandgdx.game.entities.Menu;
import com.skylandgdx.lib.*;
import com.skylandgdx.lib.input.*;
import com.skylandgdx.main.SkylandAssets;

public class OptionsMenuButtons extends GameEntityMenu
{
    final GameButton backButton;
    final GameRadioGroup difficultyRadio;
    final GameSlider soundVolumeSlider;
    final GameSlider musicVolumeSlider;

    public OptionsMenuButtons()
    {
        this.backButton = new GameButton(
                _.tr("menu.back"),
                (GameSettings.getResolutionWidth() - GameButton.getStandardButtonWidth()) / 2,
                20
        );
        difficultyRadio = new GameRadioGroup(new GameRadioGroup.Definition[]
        {
                new GameRadioGroup.Definition(GameDifficulty.Easy.ordinal(), _.tr("options.difficulty.easy"), 80, 300),
                new GameRadioGroup.Definition(GameDifficulty.Medium.ordinal(), _.tr("options.difficulty.medium"), 80, 220),
                new GameRadioGroup.Definition(GameDifficulty.Hard.ordinal(), _.tr("options.difficulty.hard"), 80, 140)
        });
        ((GameRadio)difficultyRadio.buttons[GameSettings.getGameDifficulty().ordinal()]).checked = true;
        soundVolumeSlider = new GameSlider(_.tr("options.soundVolume") + ":", 400, 400);
        musicVolumeSlider = new GameSlider(_.tr("options.musicVolume") + ":", 400, 250);
        soundVolumeSlider.value = GameSettings.getSoundVolume();
        musicVolumeSlider.value = GameSettings.getMusicVolume();
        buttons = new IGameInput[]
        {
                backButton,
                difficultyRadio,
                soundVolumeSlider,
                musicVolumeSlider
        };
    }

    @Override
    protected void inputClicked(IGameInput button)
    {
        if (button == backButton)
        {
            Menu.instance.currentMenu = Menu.CurrentMenu.MainMenu;
        }
        else if (button == difficultyRadio)
        {
            GameSettings.setGameDifficulty(GameDifficulty.values()[difficultyRadio.getCheckedId()]);
            GameSettings.saveOptions();
        }
        else if (button == soundVolumeSlider)
        {
            soundVolumeSlider.value = Math.round(soundVolumeSlider.value * 10) / 10f;
            GameSettings.setSoundVolume(soundVolumeSlider.value);
            GameSettings.saveOptions();
        }
        else if (button == musicVolumeSlider)
        {
            musicVolumeSlider.value = Math.round(musicVolumeSlider.value * 10) / 10f;
            GameSettings.setMusicVolume(musicVolumeSlider.value);
            GameSettings.saveOptions();
        }
    }


    @Override
    public void draw(GameLayers layer, SpriteBatch batch)
    {
        SkylandAssets.fontPrimeRegular.draw(
            batch,
            _.tr("options.difficulty") + ":",
            80,
            400
        );
        super.draw(layer, batch);
    }
}
