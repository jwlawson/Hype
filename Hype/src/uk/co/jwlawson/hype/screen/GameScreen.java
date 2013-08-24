package uk.co.jwlawson.hype.screen;

import uk.co.jwlawson.hype.actor.Hacker;
import uk.co.jwlawson.hype.actor.Overlay;
import uk.co.jwlawson.hype.world.ActorWorldMoverManager;
import uk.co.jwlawson.hype.world.Box2dWorld;
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
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GameScreen implements Screen {

	private Stage mStage;
	private World mWorld;
	private WorldMap mMap;
	private Overlay mOverlay;
	private ActorWorldMoverManager mMoverManager;
	private Box2dWorld mBox2d;

	public GameScreen() {
		mStage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		Gdx.input.setInputProcessor(mStage);
		mWorld = new World(mStage);
	}

	public void load(String mapName, AssetManager assets) {
		if (!mapName.endsWith(".tmx")) {
			mapName = "maps/" + mapName + ".tmx";
		}
		mMap = new WorldMap(mapName, assets, mStage);
		mWorld.setBounds(mMap.getPixWidth(), mMap.getPixHeight());

		TextureAtlas atlas = assets.get("hype.pack", TextureAtlas.class);

		String collisionFile = mapName.replace("tmx", "txt");

		mBox2d = new Box2dWorld(mMap);
		mBox2d.loadCollisions(collisionFile, assets);

		Hacker hacker = new Hacker(atlas, mBox2d);
		Vector2 pos = new Vector2(32, 32);// mMap.findEntrance();
		hacker.setBounds(pos.x, pos.y, 16, 16);
		mStage.addActor(hacker);

		mMoverManager = new ActorWorldMoverManager(mWorld, atlas);

		mOverlay = new Overlay();
		mMoverManager.addToOverlay(mOverlay);
		mStage.addActor(mOverlay);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		mStage.act(delta);
		mBox2d.act(delta);
		mMap.draw();
		mStage.draw();
		mBox2d.debugDraw(mStage.getCamera());
	}

	@Override
	public void resize(int width, int height) {
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

		// We fix the top left corner, but overlay has coords in bottom left.
		// This means need to shift the overlay to new coord frame.
		Group root = mStage.getRoot();
		Overlay over = (Overlay) root.findActor("Overlay");
		over.setPosition(x, y - newHeight);

		mMoverManager.setSize(newWidth, newHeight);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
