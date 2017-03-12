package com.gikdew.helpers;

import java.util.ArrayList;

import Configuration.Configuration;

import com.badlogic.gdx.InputProcessor;
import com.gikdew.gameworld.GameWorld;
import com.gikdew.swingcopters.ActionResolver;
import com.gikdew.ui.SimpleButton;

public class InputHandler implements InputProcessor {
	private GameWorld world;

	private ArrayList<SimpleButton> menuButtons;

	private SimpleButton playButton, playButton1, rankButton, rankButton1;

	private float scaleFactorX;
	private float scaleFactorY;

	private ActionResolver actionResolver;

	public InputHandler(GameWorld world, float scaleFactorX,
			float scaleFactorY, ActionResolver actionResolver) {
		this.world = world;
		this.scaleFactorX = scaleFactorX;
		this.scaleFactorY = scaleFactorY;
		this.actionResolver = actionResolver;

		menuButtons = new ArrayList<SimpleButton>();
		if (!Configuration.LEADERBOARDS) {
			playButton = new SimpleButton(world,
					136 / 2 - (AssetLoader.playButtonUp.getRegionWidth() / 2),
					world.gameHeight / 2 + 10, 34, 22,
					AssetLoader.playButtonUp, AssetLoader.playButtonDown);
			playButton1 = new SimpleButton(world,
					136 / 2 - (AssetLoader.playButtonUp.getRegionWidth() / 2),
					world.gameHeight / 2 + 25, 34, 22,
					AssetLoader.playButtonUp, AssetLoader.playButtonDown);
		} else {
			playButton = new SimpleButton(world,
					136 / 2 - (AssetLoader.playButtonUp.getRegionWidth() + 2),
					world.gameHeight / 2 + 10, 34, 22,
					AssetLoader.playButtonUp, AssetLoader.playButtonDown);
			playButton1 = new SimpleButton(world,
					136 / 2 - (AssetLoader.playButtonUp.getRegionWidth() + 2),
					world.gameHeight / 2 + 25, 34, 22,
					AssetLoader.playButtonUp, AssetLoader.playButtonDown);
		}

		rankButton = new SimpleButton(world, 136 / 2 + 4,
				world.gameHeight / 2 + 10, 34, 22, AssetLoader.rankButtonUp,
				AssetLoader.rankButtonDown);

		rankButton1 = new SimpleButton(world, 136 / 2 + 4,
				world.gameHeight / 2 + 25, 34, 22, AssetLoader.rankButtonUp,
				AssetLoader.rankButtonDown);
		menuButtons.add(playButton);
		menuButtons.add(rankButton);
		menuButtons.add(playButton1);
		menuButtons.add(rankButton1);
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screenX = scaleX(screenX);
		screenY = scaleY(screenY);
		// System.out.println(screenX + " " + screenY);
		if (world.isMenu()) {
			playButton.isTouchDown(screenX, screenY);
			rankButton.isTouchDown(screenX, screenY);
		} else if (world.isReady()) {
			world.start();
		} else if (world.isRunning()) {
			world.getCopter().onClick();
		}

		if (world.isGameOver() || world.isHighScore()) {
			playButton1.isTouchDown(screenX, screenY);
			rankButton1.isTouchDown(screenX, screenY);
		}

		return true;

	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenX = scaleX(screenX);
		screenY = scaleY(screenY);

		if (world.isMenu()) {
			if (playButton.isTouchUp(screenX, screenY)) {
				world.ready();
				return true;
			} else if (rankButton.isTouchUp(screenX, screenY)) {
				if (Configuration.LEADERBOARDS) {
					actionResolver.showScores();
					if (actionResolver.isSignedIn())
						actionResolver.showScores();
					else
						actionResolver.signIn();
					return true;
				}
			}
		}
		if (world.isGameOver() || world.isHighScore()) {
			if (playButton1.isTouchUp(screenX, screenY)) {
				world.restart();
				// actionResolver.showOrLoadInterstital();
				return true;
			} else if (rankButton1.isTouchUp(screenX, screenY)) {
				if (Configuration.LEADERBOARDS) {
					if (actionResolver.isSignedIn())
						actionResolver.showScores();
					else
						actionResolver.signIn();
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	private int scaleX(int screenX) {
		return (int) (screenX / scaleFactorX);
	}

	private int scaleY(int screenY) {
		return (int) (screenY / scaleFactorY - ((world.gameHeight - 204) / 2));
	}

	public ArrayList<SimpleButton> getMenuButtons() {
		return menuButtons;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
