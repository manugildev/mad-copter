package com.gikdew.gameobjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gikdew.helpers.AssetLoader;

public class Copter {
	private Vector2 position;
	private Vector2 velocity;
	private Vector2 acceleration;

	private float rotation; // For handling bird rotation
	private int width;
	private int height;

	private Rectangle rectangle;

	private boolean isAlive;
	private float rotationVel, rotationAcc;
	private int maxVelocity = 100;

	public Copter(float x, float y, int width, int height) {
		this.width = width;
		this.height = height;
		position = new Vector2(x, y);
		velocity = new Vector2(0, 0);
		acceleration = new Vector2(500, 0);
		rotation = -90;
		rotationVel = 400;
		rotationAcc = 900;
		rectangle = new Rectangle();
		isAlive = true;

	}

	public void update(float delta) {
		velocity.add(acceleration.cpy().scl(delta));

		if (velocity.x > maxVelocity) {
			velocity.x = maxVelocity;
			rotationVel -= rotationVel * delta;
			rotation -= rotationVel * delta;
			if (rotation < -110) {
				rotation = -110;
			}
		}

		if (velocity.x < -maxVelocity) {
			velocity.x = -maxVelocity;
			rotationVel += rotationVel * delta;
			rotation += rotationVel * delta;
			if (rotation > -70) {
				rotation = -70;
			}
		}

		if (isFalling()) {
			if (velocity.x > 0) {
				rotation -= rotationVel * 2.5 * delta;
			} else {
				rotation += rotationVel * 2.5 * delta;
			}
		}
		// Gdx.app.log("Rotation", getRotation() + "");
		position.add(velocity.cpy().scl(delta));
		rectangle.set(position.x, position.y, width, height);

	}

	private boolean isFalling() {
		return velocity.y > 0;
	}

	public void onClick() {
		if (isAlive) {
			AssetLoader.flap.play();
			acceleration.x *= -1;
		}

	}

	public float getX() {
		return position.x;
	}

	public float getY() {
		return position.y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public float getRotation() {
		return rotation;
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public void die() {
		if (isAlive()) {
			AssetLoader.dead.play();
		}

		if (velocity.x > 0) {
			velocity.x = 1;
		} else {
			velocity.x = -1;
		}
		isAlive = false;

		acceleration.x = 0;
		velocity.y = 100;

	}

	public void desacelerate() {
		acceleration.x = 0;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void onRestart(int x, int y) {
		rotation = 0;
		position.y = y;
		position.x = x;

		rotationVel = 400;
		rotationAcc = 900;
		velocity = new Vector2(0, 0);
		acceleration = new Vector2(350, 0);
		rotation = -90;
		isAlive = true;
	}

	public Vector2 getVelocity() {
		return velocity;
	}
}
