package com.gikdew.gameobjects;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gikdew.gameworld.GameWorld;

public class Hammer extends Scrollable {

	private GameWorld world;
	private Rectangle rRectangle, lRectangle;
	private Circle lCircle, rCircle;
	private float rotation, rotationVel, rotationAcc;
	private int maximumRotation = 100;
	private Pipe pipe;
	private int circleRadius = 8;
	float radius;

	public Hammer(GameWorld world, float x, float y, int width, int height,
			float scrollSpeed, Pipe pipe) {
		super(world, x, y, width, height, scrollSpeed);
		this.world = world;
		lRectangle = new Rectangle(pipe.getLRectangle().width - width / 2 - 5f,
				pipe.getLRectangle().y + pipe.getLRectangle().height / 2,
				width, height);
		rRectangle = new Rectangle(world.gameWidth - pipe.getRRectangle().width
				- width / 2 + 5f, getLRectangle().y, width, height);
		lCircle = new Circle(calculatePoint(lRectangle), circleRadius);
		rCircle = new Circle(calculatePoint(rRectangle), circleRadius);
		rotation = -maximumRotation / 2;
		rotationAcc = 120;
		rotationVel = 0;
		this.pipe = pipe;
		float y2 = getHeight() - getOrigin().y - (circleRadius / 2);
		radius = (float) Math.sqrt((getOrigin().x - getOrigin().x)
				* (getOrigin().x - getOrigin().x) + (getOrigin().y - y2)
				* (getOrigin().y - y2));
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		rotationVel += rotationAcc * delta;
		rotation += rotationVel * delta;

		if (rotation < -maximumRotation / 2) {
			rotation = -maximumRotation / 2;
		} else if (rotation > maximumRotation / 2) {
			rotation = maximumRotation / 2;
		}

		if (rotationVel > maximumRotation) {
			rotationAcc *= -1;
		} else if (rotationVel < -maximumRotation) {
			rotationAcc *= -1;
		}

		lRectangle = new Rectangle(pipe.getLRectangle().width - width / 2 - 5f,
				pipe.getLRectangle().y + pipe.getLRectangle().height / 2,
				width, height);

		rRectangle = new Rectangle(world.gameWidth - pipe.getRRectangle().width
				- width / 2 + 5f, getLRectangle().y, width, height);

		lCircle = new Circle(calculatePoint(lRectangle), circleRadius);
		rCircle = new Circle(calculatePoint(rRectangle), circleRadius);
	}

	private Vector2 calculatePoint(Rectangle rectangle) {
		float cx = rectangle.x + getWidth() / 2;
		float cy = rectangle.y + getHeight() / 2 - getOrigin().y - circleRadius
				- 5;
		return new Vector2((float) (cx + radius
				* Math.sin(Math.toRadians(-rotation))), (float) (cy + radius
				* Math.cos(Math.toRadians(-rotation))));
	}

	@Override
	public void reset(float newY) {
		// Call the reset method in the superclass (Scrollable)
		super.reset(newY);
		rotation = -maximumRotation / 2;
		rotationAcc = 100;
		rotationVel = 0;
		// Change the height to a random number
	}

	public boolean collides(Copter bird) {
		if (position.x < bird.getX() + bird.getWidth()) {
			return (Intersector.overlaps(bird.getRectangle(), rRectangle) || Intersector
					.overlaps(bird.getRectangle(), lRectangle));
		}
		return false;
	}

	public void stop() {
		velocity.y = 0;
		// acceleration = new Vector2(0, -500);
	}

	public Rectangle getLRectangle() {
		return lRectangle;
	}

	public Rectangle getRRectangle() {
		return rRectangle;
	}

	public float getRotation() {
		return rotation;
	}

	public void onRestart(float y, int scrollSpeed) {
		reset(y);
		velocity.y = scrollSpeed;

		lRectangle = new Rectangle(pipe.getLRectangle().width - width / 2, y,
				width, height);
		rRectangle = new Rectangle(world.gameWidth - pipe.getRRectangle().width
				- width / 2, y, width, height);

		lCircle = new Circle(calculatePoint(lRectangle), circleRadius);
		rCircle = new Circle(calculatePoint(rRectangle), circleRadius);

	}

	public void setX(float f) {
		lRectangle.x = f;
	}

	public Vector2 getOrigin() {
		return new Vector2(getRRectangle().width / 2, 3.5f);
	}

	public Circle getLCircle() {
		return lCircle;
	}

	public Circle getRCircle() {
		return rCircle;
	}
}
