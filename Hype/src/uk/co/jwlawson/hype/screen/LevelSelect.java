package uk.co.jwlawson.hype.screen;

import uk.co.jwlawson.aniguffins.DrawableActor;
import uk.co.jwlawson.hype.MyGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class LevelSelect implements Screen {

	private Stage mStage;
	private Table mTable;
	private MyGame mGame;
	private int numberMaps = 6;
	private Actor background;

	public LevelSelect(MyGame game) {
		mGame = game;
		mStage = new Stage(320, 240, false);
		mTable = new Table();
		mStage.addActor(mTable);
		mTable.setFillParent(true);
	}

	public void setNumberMaps(int numberMaps) {
		this.numberMaps = numberMaps;
	}

	public void load(AssetManager assets) {
		Skin skin = new Skin();
		TextureAtlas atlas = assets.get("hype.pack");
		skin.add("up", atlas.createPatch("button"));
		skin.add("down", atlas.createPatch("button.click"));

		background = new DrawableActor(atlas.findRegion("hype"));
		float width = mStage.getWidth();
		float height = mStage.getWidth() / 320 * 135;
		float x = 10;
		float y = (mStage.getHeight() - height) / 2;
		background.setBounds(x, y, width, height);
		background.setColor(1f, 1f, 1f, 0.3f);
		mStage.addActor(background);
		background.toBack();

		ButtonStyle bStyle = new ButtonStyle();
		bStyle.up = skin.newDrawable("up");
		bStyle.down = skin.newDrawable("down");
		bStyle.over = skin.newDrawable("up", Color.CYAN);
		skin.add("default", bStyle);

		BitmapFont fontBig = assets.get("fonts/calibri64.fnt");
		fontBig.setScale(0.66f);
		skin.add("big", fontBig);

		LabelStyle lStyleBig = new LabelStyle();
		lStyleBig.font = fontBig;
		lStyleBig.fontColor = Color.WHITE;
		skin.add("big", lStyleBig);

		TextButtonStyle tbStyleBig = new TextButtonStyle();
		tbStyleBig.up = skin.newDrawable("up");
		tbStyleBig.down = skin.newDrawable("down");
		tbStyleBig.over = skin.newDrawable("up", Color.CYAN);
		tbStyleBig.font = fontBig;
		tbStyleBig.fontColor = Color.WHITE;
		skin.add("big", tbStyleBig);

		BitmapFont font = assets.get("fonts/calibri32.fnt");
		font.setScale(0.75f);
		skin.add("default", font);

		LabelStyle lStyle = new LabelStyle();
		lStyle.font = font;
		lStyle.fontColor = Color.WHITE;
		skin.add("default", lStyle);

		TextButtonStyle tbStyle = new TextButtonStyle();
		tbStyle.up = skin.newDrawable("up");
		tbStyle.down = skin.newDrawable("down");
		tbStyle.over = skin.newDrawable("up", Color.CYAN);
		tbStyle.font = font;
		tbStyle.fontColor = Color.WHITE;
		skin.add("default", tbStyle);

		Listener listener = new Listener();

		for (int i = 1; i <= numberMaps; i++) {
			String prefix = "map";
			skin.add(prefix + i, atlas.findRegion(prefix + i), TextureRegion.class);
			ImageButtonStyle imStyle = new ImageButtonStyle(tbStyle);
			imStyle.imageUp = skin.newDrawable(prefix + i, Color.WHITE);
			imStyle.imageDown = skin.newDrawable(prefix + i, Color.WHITE);
			skin.add(prefix + i, imStyle);
			ImageButton button = new ImageButton(skin, prefix + i);
			button.setName(String.valueOf(i));
			button.addListener(listener);
			mTable.add(button).pad(7).center();

			if (i % 4 == 0) mTable.row();
		}

	}

	@Override
	public void render(float delta) {
		mStage.act(delta);

		Gdx.gl.glClearColor(0.0f, 0.2f, 0.7f, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		mStage.draw();
	}

	@Override
	public void resize(int width, int height) {
		width /= 3;
		height /= 3;
		mStage.setViewport(width, height, true);
		float newwidth = mStage.getWidth();
		float newheight = mStage.getWidth() / 320 * 135;
		float x = 10;
		float y = (mStage.getHeight() - newheight) / 2;
		background.setBounds(x, y, newwidth, newheight);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(mStage);

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

	private class Listener extends ChangeListener {

		@Override
		public void changed(ChangeEvent event, Actor actor) {
			int level = Integer.parseInt(actor.getName());
			mGame.loadLevel(level);
		}

	}

}
