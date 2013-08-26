package uk.co.jwlawson.hype.screen;

import uk.co.jwlawson.hype.MyGame;
import uk.co.jwlawson.hype.actor.Goal;
import uk.co.jwlawson.hype.actor.Hacker;
import uk.co.jwlawson.hype.actor.Laser.LaserHitListener;
import uk.co.jwlawson.hype.actor.Overlay;
import uk.co.jwlawson.hype.actor.Underlay;
import uk.co.jwlawson.hype.screen.GameOver.Message;
import uk.co.jwlawson.hype.timer.TimeListener;
import uk.co.jwlawson.hype.timer.Timer;
import uk.co.jwlawson.hype.world.ActorWorldMoverManager;
import uk.co.jwlawson.hype.world.Box2dWorld;
import uk.co.jwlawson.hype.world.LaserLoader;
import uk.co.jwlawson.hype.world.World;
import uk.co.jwlawson.hype.world.WorldMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GameScreen implements Screen, Hacker.FirstKeyDownListener, TimeListener,
		Goal.WinListener, LaserHitListener {

	private MyGame mGame;
	private Stage mStage;
	private World mWorld;
	private WorldMap mMap;
	private Underlay mUnderlay;
	private Overlay mOverlay;
	private ActorWorldMoverManager mMoverManager;
	private Box2dWorld mBox2d;
	private Timer mTimer;
	private float mTR = 1;
	private LaserLoader mLaserLoader;

	public GameScreen(MyGame game) {
		mGame = game;
		mStage = new Stage(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, true);
	}

	public void clear() {
		mWorld = null;
		mMap = null;
		mUnderlay = null;
		mOverlay = null;
		mMoverManager = null;
		mBox2d = null;
		mTimer = null;
		mTR = 1;
		mLaserLoader = null;
	}

	public void load(String mapName, AssetManager assets) {

		mStage.clear();
		clear();

		mWorld = new World(mStage);
		mTR = 1;
		TextureAtlas atlas = assets.get("hype.pack", TextureAtlas.class);

		mUnderlay = new Underlay(assets, atlas);
		mUnderlay.setVisible(true);
		mStage.addActor(mUnderlay);

		String mapFileName = "maps/" + mapName + ".tmx";
		mMap = new WorldMap(mapFileName, assets, mStage);
		mStage.addActor(mMap);
		mWorld.setBounds(mMap.getPixWidth(), mMap.getPixHeight());

		String collisionFile = "maps/terrain2.txt";
		mBox2d = new Box2dWorld(mMap);
		mBox2d.loadCollisions(collisionFile, assets);

		String laserFileName = "maps/" + mapName + ".txt";
		mLaserLoader = new LaserLoader(mMap);
		mLaserLoader.load(laserFileName, assets, atlas, mBox2d);
		mLaserLoader.addToStage(mStage);
		mLaserLoader.addLaserHitListener(this);

		Hacker hacker = new Hacker(atlas, mBox2d);
		Vector2 entrance = mMap.findEntrance();
		hacker.setBounds(entrance.x, entrance.y, 16, 16);
		hacker.addFirstKeyDownListener(this);
		mStage.addActor(hacker);
		mStage.setKeyboardFocus(hacker);

		Goal goal = new Goal(hacker);
		Vector2 exit = mMap.findExit();
		goal.setBounds(exit.x, exit.y, 16, 16);
		goal.addWinListener(this);
		mStage.addActor(goal);

		mMoverManager = new ActorWorldMoverManager(mWorld, atlas);
		mMoverManager.setActor(hacker);

		mOverlay = new Overlay();
		mOverlay.setVisible(false);
		mMoverManager.addToOverlay(mOverlay);
		mStage.addActor(mOverlay);

		mTimer = new Timer();
		mTimer.addTimeListener(mUnderlay);
		mTimer.addTimeListener(this);
		mStage.addActor(mTimer);

		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		mWorld.lookAt(entrance);
		mWorld.addMoveListener(mOverlay);
		mWorld.addMoveListener(mUnderlay);
	}

	@Override
	public void render(float delta) {
		mStage.act(delta);
		mBox2d.act(delta);

		Gdx.gl.glClearColor((1 - mTR) * 0.8f, mTR * 0.2f, mTR * 0.7f + (1 - mTR) * 0.2f, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		mStage.draw();
//		mBox2d.debugDraw(mStage.getCamera());
	}

	@Override
	public void resize(int width, int height) {
		width /= 3;
		height /= 3;
		Camera cam = mStage.getCamera();
		Vector3 pos = cam.position;
		float x = pos.x - cam.viewportWidth / 2;
		float y = pos.y + cam.viewportHeight / 2;

		int newWidth = width;
		int newHeight = height;

		// setViewport moves the camera back to the origin, but we move the camera,
		// so need to reset the position to where it should be
		mStage.setViewport(newWidth, newHeight, true);
		cam.position.set(x + newWidth / 2, y - newHeight / 2, 0);

		mWorld.resize(newWidth, newHeight);
		mMoverManager.setSize(newWidth, newHeight);
		mUnderlay.setSize(newWidth, newHeight);

		// We fix the top left corner, but overlay has coords in bottom left.
		// This means need to shift the overlay to new coord frame.
		mOverlay.setPosition(x, y - newHeight);
		mUnderlay.setPosition(x, y - newHeight);

	}

	private void gameOver(Message message) {
		mGame.gameOver(message);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(mStage);
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {
		mTimer.stop();
	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		mStage.dispose();
	}

	@Override
	public void onFirstKeyDown() {
		mTimer.start();
		mLaserLoader.startFiring();
	}

	@Override
	public void secondsDropped(int secondsRemaining) {
	}

	@Override
	public void timeRemaining(float timeRemaining) {
		mTR = timeRemaining / 10;
	}

	@Override
	public void timeFinished() {
		gameOver(Message.TIME_UP);
	}

	@Override
	public void hackerWins(Hacker hacker) {
		mTimer.stop();
		gameOver(Message.WIN);
	}

	@Override
	public void hackerHitbyLaser(Hacker hacker) {
		gameOver(Message.LASERED);
	}

	@Override
	public void onEscKeyDown() {
		gameOver(Message.QUIT);
	}
}
