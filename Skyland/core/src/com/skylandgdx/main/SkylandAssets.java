package com.skylandgdx.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Array;

/**
 * User: trakos
 * Date: 10.11.13
 * Time: 07:01
 */
public class SkylandAssets
{
    public static String creditsText;

    public static BitmapFont fontPrimeRegular;

    public static Music music01spaceFighterLoop;
    public static Music music02Hitman;
    public static Music soundTank;

    public static Sound soundTankShot;
    public static Sound soundHeli;
    public static Sound soundBomb;
    public static Sound soundSimpleExplosion;

    public static Texture textureGround;
    public static Texture textureBlank;

    public static TextureRegion textureBackground;
    public static TextureRegion textureLogo;

    public static NinePatch textureHudButtonA;
    public static NinePatch textureHudButtonN;
    public static NinePatch textureHudPanelDark;
    public static NinePatch textureHudPanelLight;

    public static Array<TextureAtlas.AtlasRegion> textureHudDigits;
    public static TextureRegion textureHudX;
    public static TextureRegion textureHudEqualSign;
    public static TextureRegion textureHudHeart;
    public static TextureRegion textureHudHeartEmpty;
    public static TextureRegion textureHudPause;
    public static TextureRegion textureHudRadioOff;
    public static TextureRegion textureHudRadioOn;
    public static TextureRegion textureHudFinger;
    public static TextureRegion textureHudArrow;

    public static TextureRegion textureGun;
    public static TextureRegion textureShell;
    public static TextureRegion textureTank;

    public static TextureRegion texturePlane1;
    public static TextureRegion texturePlane2;
    public static TextureRegion textureHeli;
    public static TextureRegion textureHeliTail;
    public static TextureRegion textureZeppelin;
    public static TextureRegion textureBomber;
    public static TextureRegion textureBomb;

    public static TextureRegion textureCloud1;
    public static TextureRegion textureCloud2;
    public static TextureRegion textureCloud3;
    public static TextureRegion textureCloud4;
    public static TextureRegion textureCloud5;
    public static TextureRegion textureCloud6;
    public static TextureRegion textureCloud7;

    public static ParticleEffectPool particleEffectExhaust;
    public static ParticleEffectPool particleEffectGrayExhaust;
    public static ParticleEffectPool particleEffectExplosion;
    public static ParticleEffectPool particleEffectSmallExplosion;


    public static void loadAssets()
    {
        creditsText = Gdx.files.internal("credits.txt").readString();

        // fonts
//        fontPrimeRegular = new BitmapFont(Gdx.files.internal("fonts/prime_regular.fnt"));
//        fontPrimeRegular.setFixedWidthGlyphs("0123456789");
        fontPrimeRegular = new BitmapFont(Gdx.files.internal("fonts/ChainFnt.fnt"));
        fontPrimeRegular.setFixedWidthGlyphs("0123456789");

        // music
        SkylandAssets.music01spaceFighterLoop = Gdx.audio.newMusic(Gdx.files.internal("audio/music01_space_fighter_loop.mp3"));
        SkylandAssets.music02Hitman = Gdx.audio.newMusic(Gdx.files.internal("audio/music02_hitman.mp3"));

        // sound as music objects
        SkylandAssets.soundTank = Gdx.audio.newMusic(Gdx.files.internal("audio/tank.mp3"));
        // sound
        SkylandAssets.soundTankShot = Gdx.audio.newSound(Gdx.files.internal("audio/tank_shot.wav"));
        SkylandAssets.soundHeli = Gdx.audio.newSound(Gdx.files.internal("audio/heli.wav"));
        SkylandAssets.soundBomb = Gdx.audio.newSound(Gdx.files.internal("audio/bomb.wav"));
        SkylandAssets.soundSimpleExplosion = Gdx.audio.newSound(Gdx.files.internal("audio/simple_explosion.wav"));

        // textures (non-atlas)
        SkylandAssets.textureGround = new Texture(Gdx.files.internal("images/ground.png"));
        SkylandAssets.textureGround.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        SkylandAssets.textureBlank = new Texture(Gdx.files.internal("images/blank.png"));
        SkylandAssets.textureBlank.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        // texture atlas
     //   TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("images/images.atlas"));
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("images/image.txt"));
        
        // textures
        SkylandAssets.textureBackground = atlas.findRegion("bg");
        SkylandAssets.textureLogo = atlas.findRegion("logo");

        /*IronCloudsAssets.textureHudDigits = atlas.findRegions("hud/digit");
        IronCloudsAssets.textureHudX = atlas.findRegion("hud/x");
        IronCloudsAssets.textureHudEqualSign = atlas.findRegion("hud/=");
        IronCloudsAssets.textureHudHeart = atlas.findRegion("hud/heart");
        IronCloudsAssets.textureHudHeartEmpty = atlas.findRegion("hud/empty_heart");
        IronCloudsAssets.textureHudPause = atlas.findRegion("hud/osc_pause");
        IronCloudsAssets.textureHudRadioOff = atlas.findRegion("hud/osc_radio_off");
        IronCloudsAssets.textureHudRadioOn = atlas.findRegion("hud/osc_radio_on");
        IronCloudsAssets.textureHudFinger = atlas.findRegion("hud/finger");
        IronCloudsAssets.textureHudArrow = atlas.findRegion("hud/arrow");
        IronCloudsAssets.textureHudButtonN = new NinePatch(atlas.findRegion("hud/osc_button_neutral"),
            6, 6, 7, 10
        );
        IronCloudsAssets.textureHudButtonA = new NinePatch(atlas.findRegion("hud/osc_button_active"),
            6, 6, 7, 10
        );
        IronCloudsAssets.textureHudPanelLight = new NinePatch(atlas.findRegion("hud/osc_panel_light"),
            10, 10, 10, 10
        );
        IronCloudsAssets.textureHudPanelDark = new NinePatch(atlas.findRegion("hud/osc_panel_dark"),
            10, 10, 10, 10
        );

        IronCloudsAssets.textureGun = atlas.findRegion("tank/gun");
        IronCloudsAssets.textureShell = atlas.findRegion("tank/shell");
        IronCloudsAssets.textureTank = atlas.findRegion("tank/tank");

        IronCloudsAssets.texturePlane1 = atlas.findRegion("enemies/plane1");
        IronCloudsAssets.texturePlane2 = atlas.findRegion("enemies/plane2");
        IronCloudsAssets.textureHeli = atlas.findRegion("enemies/heli");
        IronCloudsAssets.textureHeliTail = atlas.findRegion("enemies/heli_tail");
        IronCloudsAssets.textureZeppelin = atlas.findRegion("enemies/zeppelin");
        IronCloudsAssets.textureBomber = atlas.findRegion("enemies/bomber");
        IronCloudsAssets.textureBomb = atlas.findRegion("enemies/bomb1");

        IronCloudsAssets.textureCloud1 = atlas.findRegion("clouds/cloud1");
        IronCloudsAssets.textureCloud2 = atlas.findRegion("clouds/cloud2");
        IronCloudsAssets.textureCloud3 = atlas.findRegion("clouds/cloud3");
        IronCloudsAssets.textureCloud4 = atlas.findRegion("clouds/cloud4");
        IronCloudsAssets.textureCloud5 = atlas.findRegion("clouds/cloud5");
        IronCloudsAssets.textureCloud6 = atlas.findRegion("clouds/cloud6");
        IronCloudsAssets.textureCloud7 = atlas.findRegion("clouds/cloud7");*/
        
        SkylandAssets.textureHudDigits = atlas.findRegions("digit");
        SkylandAssets.textureHudX = atlas.findRegion("x");
        SkylandAssets.textureHudEqualSign = atlas.findRegion("=");
        SkylandAssets.textureHudHeart = atlas.findRegion("heart");
        SkylandAssets.textureHudHeartEmpty = atlas.findRegion("empty_heart");
        SkylandAssets.textureHudPause = atlas.findRegion("osc_pause");
        SkylandAssets.textureHudRadioOff = atlas.findRegion("osc_radio_off");
        SkylandAssets.textureHudRadioOn = atlas.findRegion("osc_radio_on");
        SkylandAssets.textureHudFinger = atlas.findRegion("finger");
        SkylandAssets.textureHudArrow = atlas.findRegion("arrow");
        SkylandAssets.textureHudButtonN = new NinePatch(atlas.findRegion("osc_button_neutral"),
            6, 6, 7, 10
        );
        SkylandAssets.textureHudButtonA = new NinePatch(atlas.findRegion("osc_button_active"),
            6, 6, 7, 10
        );
        SkylandAssets.textureHudPanelLight = new NinePatch(atlas.findRegion("osc_panel_light"),
            10, 10, 10, 10
        );
        SkylandAssets.textureHudPanelDark = new NinePatch(atlas.findRegion("osc_panel_dark"),
            10, 10, 10, 10
        );

        SkylandAssets.textureGun = atlas.findRegion("gun");
        SkylandAssets.textureShell = atlas.findRegion("shell");
        SkylandAssets.textureTank = atlas.findRegion("tank");

        SkylandAssets.texturePlane1 = atlas.findRegion("plane1");
        SkylandAssets.texturePlane2 = atlas.findRegion("plane2");
        SkylandAssets.textureHeli = atlas.findRegion("heli");
        SkylandAssets.textureHeliTail = atlas.findRegion("heli_tail");
        SkylandAssets.textureZeppelin = atlas.findRegion("zeppelin");
        SkylandAssets.textureBomber = atlas.findRegion("bomber");
        SkylandAssets.textureBomb = atlas.findRegion("bomb1");

        SkylandAssets.textureCloud1 = atlas.findRegion("cloud1");
        SkylandAssets.textureCloud2 = atlas.findRegion("cloud2");
        SkylandAssets.textureCloud3 = atlas.findRegion("cloud3");
        SkylandAssets.textureCloud4 = atlas.findRegion("cloud4");
        SkylandAssets.textureCloud5 = atlas.findRegion("cloud5");
        SkylandAssets.textureCloud6 = atlas.findRegion("cloud6");
        SkylandAssets.textureCloud7 = atlas.findRegion("cloud7");

        // particles
        ParticleEffect exhaustEffect = new ParticleEffect();
        exhaustEffect.load(Gdx.files.internal("particles/exhaust.particle"), atlas);
        SkylandAssets.particleEffectExhaust = new ParticleEffectPool(exhaustEffect, 1, 1);
        ParticleEffect grayExhaustEffect = new ParticleEffect();
        grayExhaustEffect.load(Gdx.files.internal("particles/exhaust_gray.particle"), atlas);
        SkylandAssets.particleEffectGrayExhaust = new ParticleEffectPool(grayExhaustEffect, 1, 1);
        ParticleEffect explosionEffect = new ParticleEffect();
        explosionEffect.load(Gdx.files.internal("particles/explosion.particle"), atlas);
        SkylandAssets.particleEffectExplosion = new ParticleEffectPool(explosionEffect, 1, 1);
        ParticleEffect smallExplosionEffect = new ParticleEffect();
        smallExplosionEffect.load(Gdx.files.internal("particles/small_explosion.particle"), atlas);
        SkylandAssets.particleEffectSmallExplosion = new ParticleEffectPool(smallExplosionEffect, 1, 1);
    }
}
