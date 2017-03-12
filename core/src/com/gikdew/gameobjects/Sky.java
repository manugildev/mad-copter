package com.gikdew.gameobjects;

import com.gikdew.gameworld.GameWorld;

public class Sky extends Scrollable {

	public Sky(GameWorld world, float x, float y, int gameWidth, int height,
			float scrollSpeed) {
		super(world, x, y, gameWidth, height, scrollSpeed);

	}

	public void update(float delta) {
		super.update(delta);
		// Gdx.app.log("Sky vel", velocity.toString());
	}

	@Override
	public void reset(float newY) {
		super.reset(newY);
	}

	public void onRestart(float newY, int scrollSpeed) {
		velocity.y = scrollSpeed;
		reset(newY);
	}

}
