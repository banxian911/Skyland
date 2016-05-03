package com.skylandgdx.lib.input;

import com.skylandgdx.lib.IGameEntity;

public interface IGameInput extends IGameEntity
{
    public boolean getActive();
    public void setActive(boolean value);
    public boolean getVisible();
    public void setVisible(boolean value);
}
