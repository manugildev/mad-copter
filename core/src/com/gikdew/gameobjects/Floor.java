package com.gikdew.gameobjects;

import com.badlogic.gdx.math.Rectangle;
import com.gikdew.gameworld.GameWorld;

public class Floor extends Scrollable {
	private Rectangle rectangle;
	private GameWorld world;

	public Floor(GameWorld world, float x, float y, int width, int height,
			float scrollSpeed) {
		super(world, x, y, width, height, scrollSpeed - 20);
		this.world = world;
		rectangle = new Rectangle(x, y, width, height);
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	@Override
	public void update(float delta) {
		position.add(velocity.cpy().scl(delta));
		if (position.y > world.gameHeight + 100) {
			// Gdx.app.log("ScrolledDown", "true");
			isScrolledDown = true;
		}

	}

	@Override
	public void reset(float newY) {
		velocity.y = 0;
		super.reset(newY);

	};

	public void onRestart(float y, int scrollSpeed) {

		reset(y);
		this.velocity.y = scrollSpeed - 20;
	}
}
