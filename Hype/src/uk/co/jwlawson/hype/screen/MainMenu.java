package uk.co.jwlawson.hype.screen;

import uk.co.jwlawson.hype.MyGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

public class MainMenu implements Screen {

	private Stage mStage;
	private Table mTable;
	private MyGame mGame;

	public MainMenu(MyGame game) {
		mGame = game;
		mStage = new Stage(320, 240, false);
		mTable = new Table();
		mStage.addActor(mTable);
		mTable.setFillParent(true);
	}

	public void load(AssetManager assets) {
		Skin skin = new Skin();
		TextureAtlas atlas = assets.get("hype.pack");
		skin.add("up", atlas.createPatch("button"));
		skin.add("down", atlas.createPatch("button.click"));

		ButtonStyle bStyle = new ButtonStyle();
		bStyle.up = skin.newDrawable("up");
		bStyle.down = skin.newDrawable("down");
		bStyle.over = skin.newDrawable("up", Color.CYAN);
		skin.add("default", bStyle);

		BitmapFont tiny = new BitmapFont();
		tiny.setScale(0.66f);
		tiny.setUseIntegerPositions(false);
		skin.add("tiny", tiny);

		LabelStyle lStyleTiny = new LabelStyle();
		lStyleTiny.font = tiny;
		lStyleTiny.fontColor = Color.WHITE;
		skin.add("tiny", lStyleTiny);

		BitmapFont fontBig = assets.get("fonts/calibri64.dr.fnt");
		fontBig.setUseIntegerPositions(false);
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
		tbStyleBig.checked = skin.newDrawable("down");
		tbStyleBig.font = fontBig;
		tbStyleBig.fontColor = Color.WHITE;
		skin.add("big", tbStyleBig);

		BitmapFont font = assets.get("fonts/calibri32.dr.fnt");
		font.setUseIntegerPositions(false);
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
		tbStyle.checked = skin.newDrawable("down");
		tbStyle.font = font;
		tbStyle.fontColor = Color.WHITE;
		skin.add("default", tbStyle);

		TextButton play = new TextButton("Play", skin, "default");
		Label playLabel = play.getLabel();
		playLabel.setAlignment(Align.left, Align.center);
		play.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				mGame.play();
				event.cancel();
			}
		});
		TextButton levelSelect = new TextButton("Level Select", skin, "default");
		levelSelect.getLabel().setAlignment(Align.left, Align.center);
		levelSelect.addListener(new ClickListener());
		levelSelect.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				mGame.levelSelect();
				event.cancel();
			}
		});

		Drawable draw = new TextureRegionDrawable(atlas.findRegion("hype"));
		Image img = new Image(draw, Scaling.none);

		Label label = new Label("A game made by John Lawson for Ludum Dare #27, 2013", skin, "tiny");

		mTable.add(img).pad(10).center();
		mTable.row();
		mTable.add(play).width(150).expandX().padLeft(20).padRight(20);
		mTable.row().pad(5);
		mTable.add(levelSelect).width(150).expandX().padLeft(20).padRight(20);
		mTable.row().expandY();
		mTable.add(label).left().pad(5);

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
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		mStage.dispose();
	}

}
