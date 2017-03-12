package com.gikdew.gameworld;

import Configuration.Configuration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.gikdew.gameobjects.Copter;
import com.gikdew.helpers.AssetLoader;
import com.gikdew.helpers.ScrollHandler;
import com.gikdew.swingcopters.ActionResolver;

public class GameWorld {

	public Copter copter;
	private ScrollHandler scroller;
	public float sW = Gdx.graphics.getWidth();
	public float sH = Gdx.graphics.getHeight();
	public float gameWidth = 136;
	public float gameHeight = sH / (sW / gameWidth);
	private Rectangle ground;

	private boolean isAlive = true;
	private static int score = 0;
	public GameState currentState = GameState.READY;
	private float runTime = 0;

	private ActionResolver actionResolver;

	public enum GameState {
		MENU, READY, RUNNING, GAMEOVER, HIGHSCORE
	}

	public boolean adIsShowed = false;
	public int adCounter = Configuration.INTERESTIAL_FREQ - 1;

	public GameWorld(int gameWidth, int gameHeight,
			ActionResolver actionResolver) {

		copter = new Copter(gameWidth / 2 - (12 / 2), gameHeight / 2 + 66 - 19,
				18, 20);
		scroller = new ScrollHandler(this, (gameWidth / 2) + 66);
		ground = new Rectangle(copter.getWidth() / 2, -copter.getHeight() / 2,
				gameWidth - (copter.getWidth()), gameHeight
						- copter.getHeight() / 2);
		currentState = GameState.MENU;
		this.actionResolver = actionResolver;
	}

	public void update(float delta) {

		runTime += delta;
		// Gdx.app.log("CurrentState", currentState.toString());
		switch (currentState) {
		case READY:
		case MENU:
			updateReady(delta);
			break;

		case RUNNING:
			updateRunning(delta);
			break;
		case HIGHSCORE:
			updateHighscore(delta);
			break;
		case GAMEOVER:
			updateGameOver(delta);
			break;
		default:
			updateRunning(delta);
			break;
		}

	}

	private void updateHighscore(float delta) {
		copter.update(delta);
		scroller.update(delta);
		scroller.stop();
		copter.die();
		copter.desacelerate();
	}

	private void updateReady(float delta) {
		scroller.updateReady(delta);
	}

	public void updateRunning(float delta) {
		if (delta > .15f) {
			delta = .15f;
		}

		copter.update(delta);
		scroller.update(delta);
		if (copter.isAlive() && scroller.collides(copter)) {
			onGameOver();
		}
		if (!Intersector.overlaps(copter.getRectangle(), ground)) {
			onGameOver();
		}
		if (Intersector.overlaps(scroller.getHammer1().getLCircle(),
				copter.getRectangle())
				|| Intersector.overlaps(scroller.getHammer1().getRCircle(),
						copter.getRectangle())
				|| Intersector.overlaps(scroller.getHammer2().getLCircle(),
						copter.getRectangle())
				|| Intersector.overlaps(scroller.getHammer2().getRCircle(),
						copter.getRectangle())
				|| Intersector.overlaps(scroller.getHammer3().getLCircle(),
						copter.getRectangle())
				|| Intersector.overlaps(scroller.getHammer3().getRCircle(),
						copter.getRectangle())) {
			onGameOver();
		}

	}

	private void updateGameOver(float delta) {
		copter.update(delta);
		scroller.update(delta);
		scroller.stop();
		copter.die();
		copter.desacelerate();
	}

	private void onGameOver() {
		scroller.stop();
		copter.die();
		copter.desacelerate();

		if (score > AssetLoader.getHighScore()) {
			if (Configuration.LEADERBOARDS) {
				actionResolver.submitScore(score);
			}
			AssetLoader.setHighScore(score);
			currentState = GameState.HIGHSCORE;
		} else {
			currentState = GameState.GAMEOVER;
		}

		// Ads every X gameovers!
		if (adCounter == Configuration.INTERESTIAL_FREQ) {
			if (!adIsShowed) {
				actionResolver.showOrLoadInterstital();
				adIsShowed = true;
				adCounter--;
			}
		} else if (adCounter == 0) {
			adCounter = Configuration.INTERESTIAL_FREQ;
		} else {
			adCounter--;
		}

	}

	public Copter getCopter() {
		return copter;
	}

	public ScrollHandler getScroller() {
		return scroller;
	}

	public int getScore() {
		return score;
	}

	public static void addScore(int increment) {
		score += increment;
	}

	public boolean isReady() {
		return currentState == GameState.READY;
	}

	public void restart() {
		currentState = GameState.READY;
		score = 0;
		copter.onRestart((int) gameWidth / 2 - (12 / 2),
				(int) gameHeight / 2 + 66 - 19);
		scroller.onRestart();
		currentState = GameState.READY;
		adIsShowed = false;
	}

	public boolean isGameOver() {
		return currentState == GameState.GAMEOVER;
	}

	public boolean isHighScore() {
		return currentState == GameState.HIGHSCORE;
	}

	public boolean isMenu() {
		return currentState == GameState.MENU;
	}

	public boolean isRunning() {
		return currentState == GameState.RUNNING;
	}

	public void start() {
		currentState = GameState.RUNNING;
	}

	public void ready() {
		currentState = GameState.READY;
	}
}
