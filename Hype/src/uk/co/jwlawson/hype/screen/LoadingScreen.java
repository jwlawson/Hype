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

		manager.setLoader(String.class, "txt", new StringLoader(new InternalFileHandleResolver()));
		manager.load("maps/test.txt", String.class);
		manager.load("maps/terrain2.txt", String.class);
		manager.load("maps/1.txt", String.class);

		manager.load("fonts/test.fnt", BitmapFont.class);
		manager.load("fonts/calibri196.fnt", BitmapFont.class);
		manager.load("fonts/calibri108.fnt", BitmapFont.class);
	}

	@Override
	public void render(float delta) {
		if (manager.update()) {
			// we are done loading, let's move to another screen!
			game.finishedLoading(manager);
		}

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		// display loading information
		float progress = manager.getProgress();
		label.setText(progress + "%");

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
