package com.skylandgdx.lib.input;

import com.skylandgdx.lib.GameEntityMenu;

public class GameRadioGroup extends GameEntityMenu
{

    static public class Definition
    {
        public int id;
        public String text;
        public float x;
        public float y;

        public Definition(int id, String text, float x, float y)
        {
            this.id = id;
            this.text = text;
            this.x = x;
            this.y = y;
        }
    }

    public GameRadioGroup(Definition[] definitions)
    {
        buttons = new IGameInput[definitions.length];
        int k = 0;
        for (Definition definition : definitions)
        {
            GameRadio gameRadio = new GameRadio(definition.text, definition.x, definition.y);
            gameRadio.id = definition.id;
            buttons[k++] = gameRadio;
        }
    }

    @Override
    protected void inputClicked(IGameInput button)
    {
        for (IGameInput iGameInput : buttons)
        {
            if (iGameInput instanceof GameRadio)
            {
                ((GameRadio) iGameInput).checked = false;
            }
        }
        ((GameRadio) button).checked = true;
    }

    public Integer getCheckedId()
    {
        Integer checkedId = null;
        for (IGameInput iGameInput : buttons)
        {
            if (iGameInput instanceof GameRadio && ((GameRadio) iGameInput).checked)
            {
                checkedId = ((GameRadio) iGameInput).id;
            }
        }
        return checkedId;
    }
}
