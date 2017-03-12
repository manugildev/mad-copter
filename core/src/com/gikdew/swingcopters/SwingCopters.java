package com.gikdew.swingcopters;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.gikdew.helpers.AssetLoader;
import com.gikdew.screens.SplashScreen;

public class SwingCopters extends Game {
	private ActionResolver actionResolver;

	public SwingCopters(ActionResolver actionResolver) {
		this.actionResolver = actionResolver;

	}

	@Override
	public void create() {
		Gdx.app.log("Game", "Created");
		AssetLoader.load();
		setScreen(new SplashScreen(this, actionResolver));

	}

	@Override
	public void dispose() {
		super.dispose();
		AssetLoader.dispose();
	}

}