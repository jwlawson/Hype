package uk.co.jwlawson.hype.screen;

import uk.co.jwlawson.hype.actor.Goal;
import uk.co.jwlawson.hype.actor.Hacker;
import uk.co.jwlawson.hype.actor.Laser.LaserHitListener;
import uk.co.jwlawson.hype.actor.Overlay;
import uk.co.jwlawson.hype.actor.Underlay;
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

	public GameScreen() {
		mStage = new Stage(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, true);
		mWorld = new World(mStage);
	}

	public void load(String mapName, AssetManager assets) {

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
		System.out.println("Lasers loaded");

		Hacker hacker = new Hacker(atlas, mBox2d);
		Vector2 pos = mMap.findEntrance();
		hacker.setBounds(pos.x, pos.y, 16, 16);
		hacker.addFirstKeyDownListener(this);
		mStage.addActor(hacker);
		mStage.setKeyboardFocus(hacker);
		mWorld.lookAt(pos);

		Goal goal = new Goal(hacker);
		pos = mMap.findExit();
		goal.setBounds(pos.x, pos.y, 16, 16);
		goal.addWinListener(this);
		mStage.addActor(goal);

		mMoverManager = new ActorWorldMoverManager(mWorld, atlas);
		mMoverManager.setActor(hacker);

		mOverlay = new Overlay();
		mOverlay.setVisible(false);
		mMoverManager.addToOverlay(mOverlay);
		mWorld.addMoveListener(mOverlay);
		mStage.addActor(mOverlay);

		mWorld.addMoveListener(mUnderlay);

		mTimer = new Timer();
		mTimer.addTimeListener(mUnderlay);
		mTimer.addTimeListener(this);
		mStage.addActor(mTimer);

	}

	@Override
	public void render(float delta) {
		mStage.act(delta);
		mBox2d.act(delta);

		Gdx.gl.glClearColor((1 - mTR) * 0.8f, mTR * 0.2f, mTR * 0.7f + (1 - mTR) * 0.2f, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		mStage.draw();
		mBox2d.debugDraw(mStage.getCamera());
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

	private void gameOver() {
		Gdx.app.log("GAmeScreen", "Game Over");
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
		System.out.println("Lasers starting");
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
		gameOver();
	}

	@Override
	public void hackerWins(Hacker hacker) {
		mTimer.stop();
		Gdx.app.log("GameScreen", "Win!");
	}

	@Override
	public void hackerHitbyLaser(Hacker hacker) {
		gameOver();
	}
}
