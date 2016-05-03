package com.skylandgdx.lib;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.skylandgdx.lib.input.IGameInput;

public abstract class GameEntityMenu extends GameEntitiesContainer implements IGameInput
{
    public IGameInput[] buttons;

    protected void drawButtons(GameLayers layer, SpriteBatch batch)
    {
        for (IGameInput button : buttons)
        {
            button.draw(layer, batch);
        }
        deactivateButtons();
    }

    @Override
    public void draw(GameLayers layer, SpriteBatch batch)
    {
        super.draw(layer, batch);
        drawButtons(layer, batch);
    }

    protected void deactivateButtons()
    {
        for (IGameInput button : buttons)
        {
            button.setActive(false);
        }
    }

    public GameTouchType handleTouch(float x, float y, GameTouchType previousTouchType, Integer activeTouchId)
    {
        GameTouchType gameTouchType = GameTouchType.NotIntercepted;
        for (IGameInput button : buttons)
        {
            gameTouchType = button.handleTouch(x, y, previousTouchType, activeTouchId);
            if (gameTouchType != GameTouchType.NotIntercepted)
            {
                inputClicked(button);
                break;
            }
        }
        return gameTouchType;
    }

    abstract protected void inputClicked(IGameInput button);

    @Override
    public boolean getActive()
    {
        return false;
    }

    @Override
    public void setActive(boolean value)
    {

    }

    @Override
    public boolean getVisible()
    {
        return false;
    }

    @Override
    public void setVisible(boolean value)
    {

    }
}
