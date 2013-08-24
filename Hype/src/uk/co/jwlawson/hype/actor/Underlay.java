package uk.co.jwlawson.hype.actor;

import uk.co.jwlawson.aniguffins.NinePatchActor;
import uk.co.jwlawson.hype.timer.TimeListener;
import uk.co.jwlawson.hype.world.World;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
	NinePatchActor patch;

	public Underlay(AssetManager assets, TextureAtlas atlas) {
		setName(NAME);

		patch = new NinePatchActor(atlas.createPatch("green"));
		patch.setBounds(0, 0, getWidth(), getHeight());
		patch.setVisible(false);
		addActor(patch);

		skin = new Skin();
		BitmapFont font = assets.get("fonts/calibri196.fnt", BitmapFont.class);
		LabelStyle style = new LabelStyle(font, Color.LIGHT_GRAY);
		skin.add("default", style);

		timerLabel = new Label("10", skin);
		timerLabel.setAlignment(Align.center, Align.center);
		addActor(timerLabel);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}

	@Override
	public void setSize(float width, float height) {
		super.setSize(width, height);
		patch.setBounds(0, 0, getWidth(), getHeight());

		float prefWidth = timerLabel.getPrefWidth();
		float prefHeight = timerLabel.getPrefHeight();

		float padX = width - prefWidth;
		float padY = height - prefHeight;
		timerLabel.setPosition(padX / 2, padY / 2);
	}

	@Override
	public void worldMovingBy(float deltaX, float deltaY) {
		addAction(Actions.moveBy(deltaX, deltaY));
	}

	@Override
	public void secondsDropped(int secondsRemaining) {
		timerLabel.setText("" + secondsRemaining);
	}

	@Override
	public void timeRemaining(float timeRemaining) {
	}

	@Override
	public void timeFinished() {
	}

}
