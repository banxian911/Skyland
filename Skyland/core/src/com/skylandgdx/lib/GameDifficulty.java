package com.skylandgdx.lib;

public enum GameDifficulty
{
    Easy(2f, 7, 1.5f, 1.5f, 0, "options.difficulty.easy"),
    Medium(1.7f, 5, 1f, 1f, 100, "options.difficulty.medium"),
    Hard(1.4f, 3, .9f, .9f, 200, "options.difficulty.hard");

    GameDifficulty(float missilesPerHitPoint, int maxHealth, float spawnTimeMultiplier, float bombTimeMultiplier, int difficultyBonusPoints, String name)
    {
        this.missilesPerHitPoint = missilesPerHitPoint;
        this.maxHealth = maxHealth;
        this.spawnTimeMultiplier = spawnTimeMultiplier;
        this.bombTimeMultiplier = bombTimeMultiplier;
        this.difficultyBonusPoints = difficultyBonusPoints;
        this.nameId = name;
    }

    public float missilesPerHitPoint = 1.7f;
    public int maxHealth = 5;
    public int difficultyBonusPoints;
    public float spawnTimeMultiplier = 1;
    public float bombTimeMultiplier = 1;
    public String nameId;

    public String getName()
    {
        return _.tr(nameId);
    }
}
