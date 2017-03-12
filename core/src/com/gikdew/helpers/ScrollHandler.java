package com.gikdew.helpers;

import com.gikdew.gameobjects.Copter;
import com.gikdew.gameobjects.Floor;
import com.gikdew.gameobjects.Hammer;
import com.gikdew.gameobjects.Pipe;
import com.gikdew.gameobjects.Sky;
import com.gikdew.gameworld.GameWorld;

public class ScrollHandler {
	// ScrollHandler will create all five objects that we need.
	private Pipe pipe1, pipe2, pipe3;
	public static final int SCROLL_SPEED = 60;
	public static final int PIPE_GAP = 100;
	private Hammer hammer1, hammer2, hammer3;
	private Floor floor;
	private Sky topSky, botSky;
	private GameWorld world;

	// Constructor receives a float that tells us where we need to create our
	// Grass and Pipe objects.
	public ScrollHandler(GameWorld world, float xPos) {
		this.world = world;
		pipe1 = new Pipe(world, 0, 40, 60, 10, SCROLL_SPEED);
		pipe2 = new Pipe(world, 0, pipe1.getTailY() - PIPE_GAP, 60, 10,
				SCROLL_SPEED);
		pipe3 = new Pipe(world, 0, pipe2.getTailY() - PIPE_GAP, 60, 10,
				SCROLL_SPEED);
		hammer1 = new Hammer(world, pipe1.getLRectangle().width - 18,
				pipe1.getLRectangle().y + 3.5f, 18, 34, SCROLL_SPEED, pipe1);

		hammer2 = new Hammer(world, pipe2.getLRectangle().width - 18,
				pipe2.getLRectangle().y + 3.5f, 18, 34, SCROLL_SPEED, pipe2);

		hammer3 = new Hammer(world, pipe3.getLRectangle().width - 18,
				pipe3.getLRectangle().y + 3.5f, 18, 34, SCROLL_SPEED, pipe3);
		floor = new Floor(world, 0, world.gameHeight / 2 + 66, 136, 11,
				SCROLL_SPEED);

		botSky = new Sky(world, 0, 0, (int) world.gameWidth, 256,
				SCROLL_SPEED - 20);
		topSky = new Sky(world, 0, botSky.getTailY() - 256,
				(int) world.gameWidth, 257, SCROLL_SPEED - 20);

	}

	public void update(float delta) {
		pipe1.update(delta);
		pipe2.update(delta);
		pipe3.update(delta);
		hammer1.update(delta);
		hammer2.update(delta);
		hammer3.update(delta);
		floor.update(delta);
		botSky.update(delta);
		topSky.update(delta);

		// Gdx.app.log("Hammer1", hammer1.getRotation() + "");
		// Gdx.app.log("Fps", pipe1.getTailY() + "");
		if (floor.isScrolledDown()) {
			floor.reset(world.gameHeight + 100);
		}
		if (pipe1.isScrolledDown()) {
			pipe1.reset(pipe3.getTailY() - PIPE_GAP);
			hammer1.reset(pipe3.getTailY() - PIPE_GAP);
			hammer1.setX(pipe1.getLRectangle().width - 5);
		} else if (pipe2.isScrolledDown()) {
			pipe2.reset(pipe1.getTailY() - PIPE_GAP);
			hammer2.reset(pipe1.getTailY() - PIPE_GAP);
			hammer2.setX(pipe2.getLRectangle().width - 5);

		} else if (pipe3.isScrolledDown()) {
			pipe3.reset(pipe2.getTailY() - PIPE_GAP);
			hammer3.reset(pipe2.getTailY() - PIPE_GAP);
			hammer3.setX(pipe3.getLRectangle().width - 5);
		}

		if (botSky.isScrolledDown()) {
			botSky.reset(topSky.getTailY() - 256);
		} else if (topSky.isScrolledDown()) {
			topSky.reset(botSky.getTailY() - 256);
		}

	}

	public void stop() {
		pipe1.stop();
		pipe2.stop();
		pipe3.stop();
		hammer1.stop();
		hammer2.stop();
		hammer3.stop();
		floor.stop();
		botSky.stop();
		topSky.stop();

	}

	// Return true if ANY pipe hits the bird.
	public boolean collides(Copter bird) {
		if (!pipe1.isScored()
				&& pipe1.getY() + (pipe1.getHeight() / 2) > bird.getY()
						+ bird.getWidth()) {
			addScore(1);
			pipe1.setScored(true);
			AssetLoader.coin.play();
		} else if (!pipe2.isScored()
				&& pipe2.getY() + (pipe2.getWidth() / 2) > bird.getY()
						+ bird.getWidth()) {
			addScore(1);
			pipe2.setScored(true);
			AssetLoader.coin.play();

		} else if (!pipe3.isScored()
				&& pipe3.getY() + (pipe3.getWidth() / 2) > bird.getY()
						+ bird.getWidth()) {
			addScore(1);
			pipe3.setScored(true);
			AssetLoader.coin.play();

		}

		return (pipe1.collides(bird) || pipe2.collides(bird) || pipe3
				.collides(bird));

	}

	private void addScore(int increment) {
		GameWorld.addScore(increment);
	}

	public Pipe getPipe1() {
		return pipe1;
	}

	public Pipe getPipe2() {
		return pipe2;
	}

	public Pipe getPipe3() {
		return pipe3;
	}

	public Hammer getHammer1() {
		return hammer1;
	}

	public Hammer getHammer2() {
		return hammer2;
	}

	public Hammer getHammer3() {
		return hammer3;
	}

	public Floor getFloor() {
		return floor;
	}

	public Sky getTopSky() {
		return topSky;
	}

	public Sky getBotSky() {
		return botSky;
	}

	public void onRestart() {
		pipe1.onRestart(40, SCROLL_SPEED);
		pipe2.onRestart(pipe1.getTailY() - PIPE_GAP, SCROLL_SPEED);
		pipe3.onRestart(pipe2.getTailY() - PIPE_GAP, SCROLL_SPEED);
		hammer1.onRestart(0 - 40, SCROLL_SPEED);
		hammer2.onRestart(pipe1.getTailY() - PIPE_GAP, SCROLL_SPEED);
		hammer3.onRestart(pipe2.getTailY() - PIPE_GAP, SCROLL_SPEED);
		floor.onRestart(world.gameHeight / 2 + 66, SCROLL_SPEED);
		botSky.onRestart(0, SCROLL_SPEED - 20);
		topSky.onRestart(botSky.getTailY() - 256, SCROLL_SPEED - 20);
	}

	public void updateReady(float delta) {
		hammer1.update(delta);
	}

}
