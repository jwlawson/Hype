package uk.co.jwlawson.hype.screen;

import uk.co.jwlawson.hype.MyGame;
import uk.co.jwlawson.hype.assets.StringLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class LoadingScreen implements Screen {

	private static final int WIDTH = 320;
	private static final int HEIGHT = 240;
	private static final int LABEL_WIDTH = 50;
	private static final int LABEL_HEIGHT = 24;

	private MyGame game;
	private AssetManager manager;
	private Label label;
	private Stage stage;

	public LoadingScreen(MyGame game) {
		this.game = game;
		manager = new AssetManager();
		manager.load("hype.pack", TextureAtlas.class);
		manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		manager.load("maps/test.tmx", TiledMap.class);
		manager.load("maps/1.tmx", TiledMap.class);
		manager.load("maps/2.tmx", TiledMap.class);
		manager.load("maps/3.tmx", TiledMap.class);
		manager.load("maps/4.tmx", TiledMap.class);
		manager.load("maps/5.tmx", TiledMap.class);
		manager.load("maps/6.tmx", TiledMap.class);
		manager.load("maps/7.tmx", TiledMap.class);
		manager.load("maps/8.tmx", TiledMap.class);
		manager.load("maps/9.tmx", TiledMap.class);
		manager.load("maps/10.tmx", TiledMap.class);
		manager.load("maps/11.tmx", TiledMap.class);
		manager.load("maps/12.tmx", TiledMap.class);
		manager.load("maps/13.tmx", TiledMap.class);
		manager.load("maps/14.tmx", TiledMap.class);
		manager.load("maps/15.tmx", TiledMap.class);
		manager.load("maps/16.tmx", TiledMap.class);

		manager.setLoader(String.class, "txt", new StringLoader(new InternalFileHandleResolver()));
		manager.load("maps/test.txt", String.class);
		manager.load("maps/terrain2.txt", String.class);
		manager.load("maps/1.txt", String.class);
		manager.load("maps/2.txt", String.class);
		manager.load("maps/3.txt", String.class);
		manager.load("maps/4.txt", String.class);
		manager.load("maps/5.txt", String.class);
		manager.load("maps/6.txt", String.class);
		manager.load("maps/7.txt", String.class);
		manager.load("maps/8.txt", String.class);
		manager.load("maps/9.txt", String.class);
		manager.load("maps/10.txt", String.class);
		manager.load("maps/11.txt", String.class);
		manager.load("maps/12.txt", String.class);
		manager.load("maps/13.txt", String.class);
		manager.load("maps/14.txt", String.class);
		manager.load("maps/15.txt", String.class);
		manager.load("maps/16.txt", String.class);

		manager.load("fonts/test.fnt", BitmapFont.class);
		manager.load("fonts/calibri196.fnt", BitmapFont.class);
		manager.load("fonts/calibri108.fnt", BitmapFont.class);
		manager.load("fonts/calibri64.fnt", BitmapFont.class);
		manager.load("fonts/calibri32.fnt", BitmapFont.class);
		manager.load("fonts/calibri64.dr.fnt", BitmapFont.class);
		manager.load("fonts/calibri32.dr.fnt", BitmapFont.class);
	}

	@Override
	public void render(float delta) {
		if (manager.update()) {
			// we are done loading, let's move to another screen!
			game.finishedLoading(manager);
		}

		Gdx.gl.glClearColor(0.0f, 0.2f, 0.7f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		// display loading information
		float progress = manager.getProgress();
		label.setText(progress * 100 + "%");

		stage.act(delta);
		stage.draw();

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		stage = new Stage(WIDTH, HEIGHT, true);
		Label.LabelStyle style = new LabelStyle(new BitmapFont(), Color.WHITE);
		label = new Label("00%", style);
		label.setBounds((WIDTH - LABEL_WIDTH) / 2, (HEIGHT - LABEL_HEIGHT) / 2, LABEL_WIDTH,
				LABEL_HEIGHT);

		stage.addActor(label);
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
