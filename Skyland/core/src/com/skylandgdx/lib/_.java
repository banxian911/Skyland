package com.skylandgdx.lib;

public class _
{
    static public String tr(String key)
    {
        return LanguagesManager.getInstance().getString(key);
    }

    static public String tr(String key, Object... args)
    {
        return String.format(tr(key), args);
    }
}
