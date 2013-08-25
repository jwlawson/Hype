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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenu implements Screen {

	private Stage mStage;
	private Table mTable;
	private MyGame mGame;

	public MainMenu(MyGame game) {
		mGame = game;
		mStage = new Stage(1024, 680, false);
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

		BitmapFont fontBig = assets.get("fonts/calibri196.fnt");
		skin.add("big", fontBig);

		LabelStyle lStyleBig = new LabelStyle();
		lStyleBig.font = fontBig;
		lStyleBig.fontColor = Color.LIGHT_GRAY;
		skin.add("big", lStyleBig);

		TextButtonStyle tbStyleBig = new TextButtonStyle();
		tbStyleBig.up = skin.newDrawable("up");
		tbStyleBig.down = skin.newDrawable("down");
		tbStyleBig.over = skin.newDrawable("up", Color.CYAN);
		tbStyleBig.checked = skin.newDrawable("down");
		tbStyleBig.font = fontBig;
		tbStyleBig.fontColor = Color.LIGHT_GRAY;
		skin.add("big", tbStyleBig);

		BitmapFont font = assets.get("fonts/calibri108.fnt");
		skin.add("default", font);

		LabelStyle lStyle = new LabelStyle();
		lStyle.font = font;
		lStyle.fontColor = Color.LIGHT_GRAY;
		skin.add("default", lStyle);

		TextButtonStyle tbStyle = new TextButtonStyle();
		tbStyle.up = skin.newDrawable("up");
		tbStyle.down = skin.newDrawable("down");
		tbStyle.over = skin.newDrawable("up", Color.CYAN);
		tbStyle.checked = skin.newDrawable("down");
		tbStyle.font = font;
		tbStyle.fontColor = Color.LIGHT_GRAY;
		skin.add("default", tbStyle);

		TextButton play = new TextButton("Play", skin, "default");
		Label playLabel = play.getLabel();
		playLabel.setAlignment(Align.left, Align.center);
		play.pad(20);
		play.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println("Play");
				mGame.play();
				event.cancel();
			}
		});
		TextButton levelSelect = new TextButton("Level Select", skin, "default");
		levelSelect.getLabel().setAlignment(Align.left, Align.center);
		levelSelect.pad(20);
		levelSelect.addListener(new ClickListener());
		levelSelect.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println("ls");
				mGame.levelSelect();
				event.cancel();
			}
		});

		mTable.add(play).width(600).expandX().left().pad(20);
		mTable.row().pad(20);
		mTable.add(levelSelect).width(600).expandX().left().pad(20);

	}

	@Override
	public void render(float delta) {
		mStage.act(delta);

		Gdx.gl.glClearColor(0.0f, 0.1f, 0.3f, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		mStage.draw();
	}

	@Override
	public void resize(int width, int height) {
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
