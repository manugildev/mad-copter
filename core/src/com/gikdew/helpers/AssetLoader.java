package com.gikdew.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader {

	public static Texture texture, logoTexture;
	public static TextureRegion logo, zbLogo, bg, grass, bird, birdDown,
			birdUp, skullUp, skullDown, bar, playButtonUp, playButtonDown,
			hammer, barUp, barDown, sky, grassy, bird1, birdf, birdDownf,
			birdUpf, bird1f, sign, sign1, rankButtonUp, rankButtonDown;
	public static Animation birdAnimation, birdAnimationf;
	public static Sound dead, flap, coin;
	public static BitmapFont font, shadow, font1;
	private static Preferences prefs;

	public static void load() {

		logoTexture = new Texture(Gdx.files.internal("data/logo.png"));
		logoTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		logo = new TextureRegion(logoTexture, 0, 0, 512, 114);

		texture = new Texture(Gdx.files.internal("data/texture.png"));
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		sign = new TextureRegion(texture, 131, 60, 81, 42);
		sign.flip(false, true);

		sign1 = new TextureRegion(texture, 212, 60, 81, 42);
		sign1.flip(false, true);

		playButtonUp = new TextureRegion(texture, 0, 83, 34, 22);
		playButtonDown = new TextureRegion(texture, 36, 83, 34, 22);
		playButtonUp.flip(false, true);
		playButtonDown.flip(false, true);

		rankButtonUp = new TextureRegion(texture, 137, 120, 34, 22);
		rankButtonDown = new TextureRegion(texture, 173, 120, 34, 22);
		rankButtonUp.flip(false, true);
		rankButtonDown.flip(false, true);

		zbLogo = new TextureRegion(texture, 0, 60, 100, 16);
		zbLogo.flip(false, true);

		bg = new TextureRegion(texture, 0, 0, 136, 43);
		bg.flip(false, true);

		grassy = new TextureRegion(texture, 137, 116, 136, 4);
		grassy.flip(false, true);

		sky = new TextureRegion(texture, 0, 117, 136, 255);
		sky.flip(false, true);

		grass = new TextureRegion(texture, 0, 43, 136, 11);
		grass.flip(false, true);

		birdDown = new TextureRegion(texture, 135, 0, 18, 20);
		birdDown.flip(false, true);

		bird = new TextureRegion(texture, 153, 0, 18, 20);
		bird.flip(false, true);

		birdUp = new TextureRegion(texture, 171, 0, 18, 20);
		birdUp.flip(false, true);

		bird1 = new TextureRegion(texture, 189, 0, 18, 20);
		bird1.flip(false, true);

		hammer = new TextureRegion(texture, 172, 19, 18, 34);
		hammer.flip(false, true);

		birdDownf = new TextureRegion(texture, 135, 0, 18, 20);
		birdDownf.flip(false, false);

		birdf = new TextureRegion(texture, 153, 0, 18, 20);
		birdf.flip(false, false);

		birdUpf = new TextureRegion(texture, 171, 0, 18, 20);
		birdUpf.flip(false, false);

		bird1f = new TextureRegion(texture, 189, 0, 18, 20);
		bird1f.flip(false, false);

		TextureRegion[] birds = { birdDown, bird, birdUp, bird1 };
		birdAnimation = new Animation(0.06f, birds);
		birdAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

		TextureRegion[] birdsf = { birdDownf, birdf, birdUpf, bird1f };
		birdAnimationf = new Animation(0.06f, birdsf);
		birdAnimationf.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

		skullUp = new TextureRegion(texture, 192, 0, 24, 14);
		// Create by flipping existing skullUp
		skullDown = new TextureRegion(skullUp);
		skullDown.flip(false, true);

		barUp = new TextureRegion(texture, 136 + 22, 20, 3, 10);
		barUp.flip(false, true);
		barDown = new TextureRegion(barUp);
		barDown.flip(true, false);

		bar = new TextureRegion(texture, 136, 20, 22, 10);
		bar.flip(false, true);

		dead = Gdx.audio.newSound(Gdx.files.internal("data/dead.wav"));
		flap = Gdx.audio.newSound(Gdx.files.internal("data/flap.wav"));
		coin = Gdx.audio.newSound(Gdx.files.internal("data/coin.wav"));

		font = new BitmapFont(Gdx.files.internal("data/text.fnt"));
		font.setScale(.25f, -.25f);
		shadow = new BitmapFont(Gdx.files.internal("data/shadow.fnt"));
		shadow.setScale(.25f, -.25f);

		font1 = new BitmapFont(Gdx.files.internal("data/font1.fnt"));
		font1.setScale(0.4f, -0.4f);

		// Create (or retrieve existing) preferences file
		prefs = Gdx.app.getPreferences("ZombieBird");

		if (!prefs.contains("highScore")) {
			prefs.putInteger("highScore", 0);
		}
	}

	public static void dispose() {
		// We must dispose of the texture when we are finished.
		texture.dispose();
		dead.dispose();
		flap.dispose();
		font.dispose();
		shadow.dispose();
		coin.dispose();
	}

	public static void setHighScore(int val) {

		prefs.putInteger("highScore", val);
		prefs.flush();
	}

	// Retrieves the current high score
	public static int getHighScore() {
		return prefs.getInteger("highScore");
	}
}
