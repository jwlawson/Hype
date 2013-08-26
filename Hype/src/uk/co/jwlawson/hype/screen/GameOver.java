package uk.co.jwlawson.hype.screen;

import uk.co.jwlawson.aniguffins.DrawableActor;
import uk.co.jwlawson.hype.MyGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class GameOver implements Screen {

	private Stage mStage;
	private Table mTable;
	private MyGame mGame;
	private Label mMessageLabel;
	private boolean restart;
	private float timeBuffer;
	private Actor background;

	public GameOver(MyGame game) {
		mGame = game;
		mStage = new Stage(512, 340, true);
		mTable = new Table();
		mStage.addActor(mTable);
		mTable.setFillParent(true);
		mStage.addListener(new Listener());
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

		mMessageLabel = new Label("", skin, "big");
		mMessageLabel.setAlignment(Align.center, Align.center);

		Label instructions = new Label("Press Esc to go to the menu.\nPress any key to play.", skin);
		instructions.setAlignment(Align.center, Align.center);

		mTable.add(mMessageLabel).expandX().center().fillX().pad(30);
		mTable.row().spaceBottom(0);
		mTable.add(instructions).expandX().center().fillX().pad(30);

	}

	public void setMessage(Message message) {
		mMessageLabel.setText(message.getMessage());
		restart = message.isRestart();
	}

	@Override
	public void render(float delta) {
		mStage.act(delta);

		Gdx.gl.glClearColor(0.0f, 0.2f, 0.7f, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		mStage.draw();

		timeBuffer -= delta;
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
		timeBuffer = 0.1f;
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

	private class Listener extends InputListener {
		@Override
		public boolean keyDown(InputEvent event, int keycode) {
			if (timeBuffer < 0) {
				switch (keycode) {
				case Keys.ESCAPE:
					mGame.showMenu();
					return true;
				default:
					if (restart) {
						mGame.restartLevel();
					} else {
						mGame.nextLevel();
					}
				}
			}
			return super.keyDown(event, keycode);
		}
	}

	public enum Message {
		WIN("Awesome", false),
		TIME_UP("I have to go quick", true),
		LASERED("Lasers hurt", true),
		QUIT("Who would pause this", true);

		private String message;
		private boolean restart;

		private Message(String message, boolean restart) {
			this.message = message;
			this.restart = restart;
		}

		public String getMessage() {
			return message;
		}

		public boolean isRestart() {
			return restart;
		}
	}

}
