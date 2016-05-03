package com.skylandgdx.game.entities.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.skylandgdx.game.entities.Menu;
import com.skylandgdx.lib.GameEntityMenu;
import com.skylandgdx.lib.GameLayers;
import com.skylandgdx.lib.GameSettings;
import com.skylandgdx.lib._;
import com.skylandgdx.lib.input.GameButton;
import com.skylandgdx.lib.input.IGameInput;
import com.skylandgdx.main.SkylandAssets;

public class CreditsMenu extends GameEntityMenu
{
    public float textHeight = 0;
    float scrolledStartY = 0;
    final float scrollSpeedY = 20f;

    final float startY = 420;
    final float endY = 100;
    final float marginX = 80;
    final float textWidth = GameSettings.getResolutionWidth() - 2 * marginX;

    final Rectangle area = new Rectangle(marginX, endY, textWidth, startY - endY);
    final Rectangle scissor = new Rectangle();

    final GameButton backButton;

    public CreditsMenu()
    {
        this.backButton = new GameButton(
            _.tr("menu.back"),
            (GameSettings.getResolutionWidth() - GameButton.getStandardButtonWidth()) / 2,
            20
        );
        buttons = new GameButton[]
        {
            backButton
        };
    }

    @Override
    protected void inputClicked(IGameInput button)
    {
        if (button == backButton)
        {
            Menu.instance.currentMenu = Menu.CurrentMenu.MainMenu;
        }
    }


    @Override
    public void draw(GameLayers layer, SpriteBatch batch)
    {
        calculateScissors(area, scissor, batch);
        batch.flush();
        ScissorStack.pushScissors(scissor);
        SkylandAssets.fontPrimeRegular.setColor(Color.WHITE);
        BitmapFont.TextBounds textBounds = SkylandAssets.fontPrimeRegular.drawWrapped(
                batch,
                SkylandAssets.creditsText,
                marginX,
                startY + scrolledStartY,
                textWidth,
                BitmapFont.HAlignment.CENTER
        );
        batch.flush();
        ScissorStack.popScissors();
        textHeight = textBounds.height + 100;

        super.draw(layer, batch);

    }

    public void calculateScissors(Rectangle area, Rectangle scissor, SpriteBatch batch)
    {
        ScissorStack.calculateScissors(
            GameSettings.getCamera(),
            0,
            0,
            GameSettings.getScreenWidth(),
            GameSettings.getScreenHeight(),
            batch.getTransformMatrix(),
            area,
            scissor
        );
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);


        float scrollLength = textHeight - (startY - endY);

        // po zakonczeniu scrollowania czekamy jeszcze 5 sekund przed repeatem
        if (scrolledStartY > scrollLength + .5f)
        {
            scrolledStartY = 0;
        }
        else if (scrolledStartY > scrollLength)
        {
            scrolledStartY += .1f * delta;
        }
        else
        {
            scrolledStartY += scrollSpeedY * delta;
        }
    }
}
