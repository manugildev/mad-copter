package com.gikdew.gameworld;

import java.util.ArrayList;

import Configuration.Configuration;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.gikdew.gameobjects.Copter;
import com.gikdew.gameobjects.Floor;
import com.gikdew.gameobjects.Hammer;
import com.gikdew.gameobjects.Pipe;
import com.gikdew.gameobjects.Sky;
import com.gikdew.helpers.AssetLoader;
import com.gikdew.helpers.InputHandler;
import com.gikdew.helpers.ScrollHandler;
import com.gikdew.tweenaccessors.Value;
import com.gikdew.tweenaccessors.ValueAccessor;
import com.gikdew.ui.SimpleButton;

public class GameRenderer {

	private GameWorld world;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batcher;

	private int midPointY;
	private int gameHeight;

	private Copter copter;

	private TextureRegion bg, grass;
	private Animation birdAnimation;
	private TextureRegion birdMid, birdDown, birdUp, hammer;
	private TextureRegion skullUp, skullDown, bar, barUp, barDown, sky, sign,
			sign1;

	private Pipe pipe1, pipe2, pipe3;
	private Hammer hammer1, hammer2, hammer3;
	private ScrollHandler scroller;
	private Floor floor;
	private Sky topSky, botSky;

	private TweenManager manager;
	private Value alpha = new Value();

	private ArrayList<SimpleButton> menuButtons;

	public GameRenderer(GameWorld world, int gameHeight, int midPointY) {
		this.world = world;
		this.gameHeight = gameHeight;
		this.midPointY = midPointY;
		this.menuButtons = ((InputHandler) Gdx.input.getInputProcessor())
				.getMenuButtons();
		cam = new OrthographicCamera();
		cam.setToOrtho(true, 136, 204);
		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(cam.combined);

		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);

		initGameObjects();
		initAssets();
		setupTweens();
	}

	public void render(float delta, float runTime) {
		Gdx.gl.glClearColor(61 / 255f, 186 / 255f, 234 / 255f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		shapeRenderer.begin(ShapeType.Filled);

		// Draw Background color
		shapeRenderer.setColor(Color.valueOf("3dbaea"));
		shapeRenderer.rect(0, 0, 136, world.gameHeight);

		// Draw Grass
		// shapeRenderer.setColor(111 / 255.0f, 186 / 255.0f, 45 / 255.0f, 1);
		// shapeRenderer.rect(0, midPointY + 66, 136, 11);

		// Draw Dirt
		// shapeRenderer.setColor(147 / 255.0f, 80 / 255.0f, 27 / 255.0f, 1);
		// shapeRenderer.rect(0, midPointY + 77, 136, 52);

		// End ShapeRenderer
		shapeRenderer.end();

		// Begin SpriteBatch
		batcher.begin();

		// Disable transparency
		// This is good for performance when drawing images that do not require

		// transparency.
		batcher.disableBlending();
		batcher.draw(sky, botSky.getX(), botSky.getY(), 136, botSky.getHeight());
		batcher.draw(sky, topSky.getX(), topSky.getY(), 136, topSky.getHeight());
		batcher.draw(grass, floor.getX(), floor.getY(), 136, floor.getHeight());
		batcher.enableBlending();
		batcher.draw(AssetLoader.bg, 0, floor.getY() - 43, 136, 43);
		batcher.enableBlending();
		drawHammers();
		drawPipes();

		// The bird needs transparency, so we enable that again.
		batcher.enableBlending();

		// Draw bird at its coordinates. Retrieve the Animation object from
		// AssetLoader
		// Pass in the runTime variable to get the current frame.

		// End SpriteBatch
		if (world.isRunning()) {
			drawBird(runTime);
			drawScore();
		} else if (world.isReady()) {
			drawBird(runTime);
			drawScore();
		} else if (world.isMenu()) {
			drawBirdCentered(runTime);
			drawMenuUI();
		} else if (world.isGameOver()) {
			drawBird(runTime);
			drawGameOver();

		} else if (world.isHighScore()) {
			drawBird(runTime);
			drawHighScore();

		}
		batcher.draw(AssetLoader.grassy, 0, floor.getY() - 4, 136, 4);
		batcher.end();
		drawTransition(delta);

		shapeRenderer.begin(ShapeType.Filled);
		// shapeRenderer.setColor(Color.RED);
		// shapeRenderer.rect(floor.getX(), floor.getY(), floor.getWidth(),
		// floor.getHeight());
		shapeRenderer.setColor(Color.valueOf("858585"));
		shapeRenderer.rect(floor.getX(), floor.getY() + floor.getHeight(),
				floor.getWidth(), gameHeight - floor.getY());
		// shapeRenderer.rect(copter.getRectangle().x, copter.getRectangle().y,
		// copter.getRectangle().width, copter.getRectangle().height,
		// copter.getRectangle().width / 2,
		// copter.getRectangle().height / 2, copter.getRotation());
		// shapeRenderer.setColor(Color.GREEN);
		// // DRAWING THE SHIT ON THE SCROLLER
		// shapeRenderer.rect(pipe1.getLRectangle().x, pipe1.getLRectangle().y,
		// pipe1.getLRectangle().width, pipe1.getLRectangle().height);
		// shapeRenderer.rect(pipe1.getRRectangle().x, pipe1.getRRectangle().y,
		// pipe1.getRRectangle().width, pipe1.getRRectangle().height);
		//
		// shapeRenderer.rect(pipe2.getLRectangle().x, pipe2.getLRectangle().y,
		// pipe2.getLRectangle().width, pipe2.getLRectangle().height);
		// shapeRenderer.rect(pipe2.getRRectangle().x, pipe2.getRRectangle().y,
		// pipe2.getRRectangle().width, pipe2.getRRectangle().height);
		//
		// shapeRenderer.rect(pipe3.getLRectangle().x, pipe3.getLRectangle().y,
		// pipe3.getLRectangle().width, pipe3.getLRectangle().height);
		// shapeRenderer.rect(pipe3.getRRectangle().x, pipe3.getRRectangle().y,
		// pipe3.getRRectangle().width, pipe3.getRRectangle().height);
		// shapeRenderer.setColor(1, 0, 1, 0.5f);

		// shapeRenderer.rect(hammer1.getLRectangle().x,
		// hammer1.getLRectangle().y, hammer1.getLRectangle().width,
		// hammer1.getLRectangle().height, hammer1.getOrigin().x,
		// hammer1.getOrigin().y, hammer1.getRotation());
		//
		// shapeRenderer.rect(hammer1.getRRectangle().x,
		// hammer1.getRRectangle().y, hammer1.getRRectangle().width,
		// hammer1.getRRectangle().height, hammer1.getOrigin().x,
		// hammer1.getOrigin().y, hammer1.getRotation());
		// shapeRenderer.setColor(1, 0, 1, 0.5f);
		// shapeRenderer.rect(hammer2.getLRectangle().x,
		// hammer2.getLRectangle().y, hammer2.getLRectangle().width,
		// hammer2.getLRectangle().height, hammer2.getOrigin().x,
		// hammer2.getOrigin().y, hammer2.getRotation());
		//
		// shapeRenderer.rect(hammer2.getRRectangle().x,
		// hammer2.getRRectangle().y, hammer2.getRRectangle().width,
		// hammer2.getRRectangle().height, hammer2.getOrigin().x,
		// hammer2.getOrigin().y, hammer2.getRotation());
		// shapeRenderer.setColor(1, 0, 1, 0.5f);
		// shapeRenderer.rect(hammer3.getLRectangle().x,
		// hammer3.getLRectangle().y, hammer3.getLRectangle().width,
		// hammer3.getLRectangle().height, hammer3.getOrigin().x,
		// hammer3.getOrigin().y, hammer3.getRotation());
		//
		// shapeRenderer.rect(hammer3.getRRectangle().x,
		// hammer3.getRRectangle().y, hammer3.getRRectangle().width,
		// hammer3.getRRectangle().height, hammer3.getOrigin().x,
		// hammer3.getOrigin().y, hammer3.getRotation());
		//
		// shapeRenderer.setColor(0, 1, 1, 1);
		// shapeRenderer.circle(hammer1.getLCircle().x, hammer1.getLCircle().y,
		// hammer1.getLCircle().radius);
		// shapeRenderer.circle(hammer1.getRCircle().x, hammer1.getRCircle().y,
		// hammer1.getRCircle().radius);
		//
		// shapeRenderer.circle(hammer2.getLCircle().x, hammer2.getLCircle().y,
		// hammer2.getLCircle().radius);
		// shapeRenderer.circle(hammer2.getRCircle().x, hammer2.getRCircle().y,
		// hammer2.getRCircle().radius);
		//
		// shapeRenderer.circle(hammer3.getLCircle().x, hammer3.getLCircle().y,
		// hammer3.getLCircle().radius);
		// shapeRenderer.circle(hammer3.getRCircle().x, hammer3.getRCircle().y,
		// hammer3.getRCircle().radius);

		shapeRenderer.end();

		// batcher.begin();
		// drawHammers();
		// batcher.end();

	}

	private void drawHammers() {

		batcher.draw(hammer, hammer1.getLRectangle().x,
				hammer1.getLRectangle().y, hammer1.getOrigin().x,
				hammer1.getOrigin().y, hammer1.getWidth(), hammer1.getHeight(),
				1, 1, hammer1.getRotation());
		batcher.draw(hammer, hammer1.getRRectangle().x,
				hammer1.getRRectangle().y, hammer1.getOrigin().x,
				hammer1.getOrigin().y, hammer1.getWidth(), hammer1.getHeight(),
				1, 1, hammer1.getRotation());

		batcher.draw(hammer, hammer2.getLRectangle().x,
				hammer2.getLRectangle().y, hammer2.getOrigin().x,
				hammer2.getOrigin().y, hammer2.getWidth(), hammer2.getHeight(),
				1, 1, hammer2.getRotation());
		batcher.draw(hammer, hammer2.getRRectangle().x,
				hammer2.getRRectangle().y, hammer2.getOrigin().x,
				hammer2.getOrigin().y, hammer2.getWidth(), hammer2.getHeight(),
				1, 1, hammer2.getRotation());

		batcher.draw(hammer, hammer3.getLRectangle().x,
				hammer3.getLRectangle().y, hammer3.getOrigin().x,
				hammer3.getOrigin().y, hammer3.getWidth(), hammer3.getHeight(),
				1, 1, hammer3.getRotation());
		batcher.draw(hammer, hammer3.getRRectangle().x,
				hammer3.getRRectangle().y, hammer3.getOrigin().x,
				hammer3.getOrigin().y, hammer3.getWidth(), hammer3.getHeight(),
				1, 1, hammer3.getRotation());

	}

	private void drawBirdCentered(float runTime) {
		batcher.draw(AssetLoader.birdAnimationf.getKeyFrame(runTime),
				copter.getX(), copter.getY(), copter.getWidth() / 2.0f,
				copter.getHeight() / 2.0f, copter.getWidth(),
				copter.getHeight(), 1, 1, copter.getRotation());
	}

	private void drawBird(float runTime) {
		if (copter.getVelocity().x > 0) {
			batcher.draw(AssetLoader.birdAnimation.getKeyFrame(runTime),
					copter.getX(), copter.getY(), copter.getWidth() / 2.0f,
					copter.getHeight() / 2.0f, copter.getWidth(),
					copter.getHeight(), 1, 1, copter.getRotation());

		} else {
			batcher.draw(AssetLoader.birdAnimationf.getKeyFrame(runTime),
					copter.getX(), copter.getY(), copter.getWidth() / 2.0f,
					copter.getHeight() / 2.0f, copter.getWidth(),
					copter.getHeight(), 1, 1, copter.getRotation());
		}

	}

	private void drawMenuUI() {
		batcher.draw(AssetLoader.zbLogo, world.gameWidth / 2
				- (AssetLoader.zbLogo.getRegionWidth() / 2), midPointY - 20,
				AssetLoader.zbLogo.getRegionWidth(),
				AssetLoader.zbLogo.getRegionHeight());

		menuButtons.get(0).draw(batcher);
		if (Configuration.LEADERBOARDS) {
			menuButtons.get(1).draw(batcher);
		}

	}

	private void drawScore() {
		int length = ("" + world.getScore()).length();
		AssetLoader.shadow.draw(batcher, "" + world.getScore(),
				68 - (3 * length), midPointY - 82);
		AssetLoader.font.draw(batcher, "" + world.getScore(),
				68 - (3 * length), midPointY - 83);
		AssetLoader.font.draw(batcher, "" + world.getScore(),
				68 - (3 * length), midPointY - 83);
	}

	private void drawHighScore() {
		int length = ("" + world.getScore()).length();

		batcher.draw(sign1, world.gameWidth / 2 - sign.getRegionWidth() / 2,
				world.gameHeight / 2 - AssetLoader.sign.getRegionHeight() / 2,
				82, 42);
		AssetLoader.font.draw(batcher, "" + world.getScore(),
				68 - (3 * length), midPointY);
		menuButtons.get(2).draw(batcher);
		if (Configuration.LEADERBOARDS) {
			menuButtons.get(3).draw(batcher);
		}

	}

	private void drawGameOver() {
		int length = ("       " + world.getScore()).length();
		int length1 = ("           " + AssetLoader.getHighScore()).length();

		batcher.draw(sign, world.gameWidth / 2 - sign.getRegionWidth() / 2,
				world.gameHeight / 2 - AssetLoader.sign.getRegionHeight() / 2,
				82, 42);
		AssetLoader.font1.draw(batcher, "       " + world.getScore(),
				68 - (1.5f * length) + 12, midPointY - 2);
		AssetLoader.font1.draw(batcher,
				"           " + AssetLoader.getHighScore(),
				68 - (1.5f * length1) + 5, midPointY + 8);
		menuButtons.get(2).draw(batcher);
		if (Configuration.LEADERBOARDS) {
			menuButtons.get(3).draw(batcher);
		}

	}

	private void drawPipes() {
		// Temporary code! Sorry about the mess :)
		// We will fix this when we finish the Pipe class.
		batcher.enableBlending();
		batcher.draw(bar, pipe1.getX(), pipe1.getY(), pipe1.getWidth(),
				pipe1.getHeight());
		batcher.draw(barUp, pipe1.getX() + pipe1.getWidth(), pipe1.getY(), 3,
				pipe1.getHeight());

		batcher.draw(bar, pipe1.getX() + pipe1.getWidth() + pipe1.getGap(),
				pipe1.getY(),
				world.gameWidth - pipe1.getWidth() - pipe1.getGap(),
				pipe1.getHeight());
		batcher.draw(barDown, pipe1.getX() + pipe1.getWidth() + pipe1.getGap()
				- 3, pipe1.getY(), 3, pipe1.getHeight());

		batcher.draw(bar, pipe2.getX(), pipe2.getY(), pipe2.getWidth(),
				pipe2.getHeight());
		batcher.draw(barUp, pipe2.getX() + pipe2.getWidth(), pipe2.getY(), 3,
				pipe2.getHeight());

		batcher.draw(bar, pipe2.getX() + pipe2.getWidth() + pipe2.getGap(),
				pipe2.getY(),
				world.gameWidth - pipe2.getWidth() - pipe2.getGap(),
				pipe2.getHeight());
		batcher.draw(barDown, pipe2.getX() + pipe2.getWidth() + pipe2.getGap()
				- 3, pipe2.getY(), 3, pipe2.getHeight());

		batcher.draw(bar, pipe3.getX(), pipe3.getY(), pipe3.getWidth(),
				pipe3.getHeight());
		batcher.draw(barUp, pipe3.getX() + pipe3.getWidth(), pipe3.getY(), 3,
				pipe3.getHeight());

		batcher.draw(bar, pipe3.getX() + pipe3.getWidth() + pipe3.getGap(),
				pipe3.getY(),
				world.gameWidth - pipe3.getWidth() - pipe3.getGap(),
				pipe3.getHeight());
		batcher.draw(barDown, pipe3.getX() + pipe3.getWidth() + pipe3.getGap()
				- 3, pipe3.getY(), 3, pipe3.getHeight());
	}

	private void initGameObjects() {

		copter = world.getCopter();
		scroller = world.getScroller();
		pipe1 = scroller.getPipe1();
		pipe2 = scroller.getPipe2();
		pipe3 = scroller.getPipe3();
		hammer1 = scroller.getHammer1();
		hammer2 = scroller.getHammer2();
		hammer3 = scroller.getHammer3();
		floor = scroller.getFloor();
		topSky = scroller.getTopSky();
		botSky = scroller.getBotSky();

	}

	private void initAssets() {
		bg = AssetLoader.bg;
		grass = AssetLoader.grass;
		birdAnimation = AssetLoader.birdAnimation;
		birdMid = AssetLoader.bird;
		birdDown = AssetLoader.birdDown;
		birdUp = AssetLoader.birdUp;
		skullUp = AssetLoader.skullUp;
		skullDown = AssetLoader.skullDown;
		bar = AssetLoader.bar;
		hammer = AssetLoader.hammer;
		barUp = AssetLoader.barUp;
		barDown = AssetLoader.barDown;
		sky = AssetLoader.sky;
		sign = AssetLoader.sign;
		sign1 = AssetLoader.sign1;
	}

	private void setupTweens() {
		Tween.registerAccessor(Value.class, new ValueAccessor());
		manager = new TweenManager();
		Tween.to(alpha, -1, 0.5f).target(0).ease(TweenEquations.easeOutQuad)
				.start(manager);
	}

	private void drawTransition(float delta) {
		if (alpha.getValue() > 0) {
			manager.update(delta);
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(0, 0, 0, alpha.getValue());
			shapeRenderer.rect(0, 0, 136, 300);
			shapeRenderer.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);

		}
	}
}
