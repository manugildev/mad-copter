package com.gikdew.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.gikdew.gameworld.GameWorld;

public class Scrollable {

	// Protected is similar to private, but allows inheritance by subclasses.
	protected Vector2 position;
	protected Vector2 velocity;
	protected int width;
	protected int height;
	protected boolean isScrolledDown;
	private GameWorld world;
	private Vector2 acceleration;
	private float scrollSpeed;

	public Scrollable(GameWorld world, float x, float y, int width, int height,
			float scrollSpeed) {
		position = new Vector2(x, y);
		this.scrollSpeed = scrollSpeed;
		velocity = new Vector2(0, scrollSpeed);
		acceleration = new Vector2(0, 10);
		this.width = width;
		this.height = height;
		this.world = world;
		isScrolledDown = false;
	}

	public void update(float delta) {
		velocity.add(acceleration.cpy().scl(delta));
		if (velocity.y < 0.20) {
			velocity.y = 0;
		}
		position.add(velocity.cpy().scl(delta));

		if (velocity.y > scrollSpeed) {
			velocity.y = scrollSpeed;
		}
		if (position.y > world.gameHeight + 10) {
			// Gdx.app.log("ScrolledDown", "true");
			isScrolledDown = true;
		}
	}

	public void reset(float newY) {
		isScrolledDown = false;
		position.y = newY;
		acceleration = new Vector2(0, 0);
	}

	public void stop() {
		velocity.y = 0;
		// acceleration = new Vector2(0, -500);
	}

	public boolean isScrolledDown() {
		return isScrolledDown;
	}

	public float getTailY() {
		return position.y;
	}

	public float getX() {
		return position.x;
	}

	public float getY() {
		return position.y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}