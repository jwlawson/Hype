package uk.co.jwlawson.hype.actor;

import uk.co.jwlawson.hype.timer.TimeListener;
import uk.co.jwlawson.hype.world.World;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class Underlay extends Group implements World.MoveListener, TimeListener {

	private static final String NAME = "underlay";
	private static final float PADDING = 0.25f;
	private Label timerLabel;
	private Skin skin;

	public Underlay() {
		setName(NAME);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		System.out.println(getX() + " " + getY() + " " + getWidth() + " " + getHeight());
	}

	public void load(AssetManager assets) {
		skin = new Skin();
		BitmapFont font = assets.get("fonts/test.fnt", BitmapFont.class);
		font.setUseIntegerPositions(false);
		LabelStyle style = new LabelStyle(font, Color.LIGHT_GRAY);
		skin.add("default", style);

		timerLabel = new Label("10", skin);
		timerLabel.setAlignment(Align.center, Align.center);
		addActor(timerLabel);
	}

	@Override
	public void setSize(float width, float height) {
		super.setSize(width, height);

		float newWidth = width - width * PADDING;
		float newHeight = height - height * PADDING;
		System.out.println("want: " + newWidth + " " + newHeight);

		float prefWidth = timerLabel.getPrefWidth();
		float prefHeight = timerLabel.getPrefHeight();
		System.out.println("current:" + prefWidth + " " + prefHeight);

		float scale = timerLabel.getFontScaleY();
		timerLabel.setFontScale(height / prefHeight);

		prefWidth = timerLabel.getPrefWidth();
		prefHeight = timerLabel.getPrefHeight();
		System.out.println("new:" + prefWidth + " " + prefHeight);

		float padX = width - prefWidth;
		float padY = height - prefHeight;
		timerLabel.setPosition(0, 0);
	}

	@Override
	public void worldMovingBy(float deltaX, float deltaY) {
		addAction(Actions.moveBy(deltaX, deltaY));
	}

	@Override
	public void secondsDropped(int secondsRemaining) {
	}

	@Override
	public void timeRemaining(float timeRemaining) {
	}

	@Override
	public void timeFinished() {
	}

}
