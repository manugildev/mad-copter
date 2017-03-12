package com.gikdew.gameobjects;

import java.util.Random;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.gikdew.gameworld.GameWorld;

public class Pipe extends Scrollable {

	private Random r;
	private int gap = 70;
	private GameWorld world;
	private Rectangle rRectangle, lRectangle;
	private boolean isScored = false;

	public Pipe(GameWorld world, float x, float y, int width, int height,
			float scrollSpeed) {
		super(world, x, y, width, height, scrollSpeed);
		this.world = world;
		r = new Random();
		this.width = r.nextInt((int) (world.gameWidth - gap - 10 - 10)) + 10;
		// Initialize a Random object for Random number generation

		lRectangle = new Rectangle(position.x, position.y, this.width, height);
		rRectangle = new Rectangle(position.x + this.width + gap, position.y,
				world.gameWidth - this.width - gap, height);
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		lRectangle = new Rectangle(position.x, position.y, width, height);
		rRectangle = new Rectangle(position.x + width + gap, position.y,
				world.gameWidth - width - gap, height);
	}

	@Override
	public void reset(float newY) {
		// Call the reset method in the superclass (Scrollable)
		super.reset(newY);
		width = r.nextInt((int) (world.gameWidth - gap - 10)) + 10;
		isScored = false;
		// Change the height to a random number
	}

	public boolean collides(Copter copter) {
		if (position.y < copter.getY() + copter.getWidth()) {
			return (Intersector.overlaps(copter.getRectangle(), rRectangle) || Intersector
					.overlaps(copter.getRectangle(), lRectangle));
		}
		return false;

	}

	public int getGap() {
		return gap;
	}

	public Rectangle getLRectangle() {
		return lRectangle;
	}

	public Rectangle getRRectangle() {
		return rRectangle;
	}

	public boolean isScored() {
		return isScored;
	}

	public void setScored(boolean b) {
		isScored = b;
	}

	public void onRestart(float y, int scrollSpeed) {
		isScored = false;
		velocity.y = scrollSpeed;

		reset(y);
		lRectangle = new Rectangle(position.x, y, this.width, height);
		rRectangle = new Rectangle(position.x + this.width + gap, y,
				world.gameWidth - width - gap, height);

	}
}
