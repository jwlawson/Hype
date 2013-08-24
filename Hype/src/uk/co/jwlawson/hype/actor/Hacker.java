package uk.co.jwlawson.hype.actor;

import uk.co.jwlawson.hype.world.Box2dWorld;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class Hacker extends RectBox2dActor {

	private static final String NAME = "char";
	private State state = State.STOPPED;
	private boolean left, right;

	public Hacker(TextureAtlas atlas, Box2dWorld world) {
		super(atlas.findRegion(NAME), world);
		setId(NAME);
		load(atlas, "stand", "walkleft", "walkright");
		addListener(new ControlListener());
	}

	@Override
	public void act(float delta) {
		mBody.setLinearVelocity(state.getVelocity().x, mBody.getLinearVelocity().y);
		super.act(delta);
	}

	@Override
	protected BodyDef getBodyDef() {
		BodyDef def = super.getBodyDef();
		def.fixedRotation = true;
		return def;
	}

	private void setState(State nextState) {
		state = nextState;
		activateAnimation(state.getAnimation(), true);
	}

	private void jump() {
		if (Math.abs(mBody.getLinearVelocity().y) < 0.5) {
			mBody.applyLinearImpulse(0f, 0.2f, mBody.getWorldCenter().x, mBody.getWorldCenter().y,
					true);
		}
	}

	private class ControlListener extends InputListener {

		@Override
		public boolean keyDown(InputEvent event, int keycode) {
			switch (keycode) {
			case Keys.D:
			case Keys.RIGHT:
				right = true;
				setState(State.MOVING_RIGHT);
				return true;
			case Keys.A:
			case Keys.LEFT:
				left = true;
				setState(State.MOVING_LEFT);
				return true;
			case Keys.W:
			case Keys.UP:
			case Keys.SPACE:
				jump();
				return true;
			}
			return super.keyDown(event, keycode);
		}

		@Override
		public boolean keyUp(InputEvent event, int keycode) {
			switch (keycode) {
			case Keys.D:
			case Keys.RIGHT:
				right = false;
				if (state == State.MOVING_RIGHT) {
					if (left) {
						setState(State.MOVING_LEFT);
					} else {
						setState(State.STOPPED);
					}
				}
				return true;
			case Keys.A:
			case Keys.LEFT:
				left = false;
				if (state == State.MOVING_LEFT) {
					if (right) {
						setState(State.MOVING_RIGHT);
					} else {
						setState(State.STOPPED);
					}
				}
				return true;
			}
			return super.keyUp(event, keycode);
		}

	}

	private enum State {
		MOVING_LEFT("walkleft", new Vector2(-5f, 0)),
		MOVING_RIGHT("walkright", new Vector2(5f, 0)),
		STOPPED("stand", new Vector2(0, 0));

		private String animation;
		private Vector2 velocity;

		private State(String animation, Vector2 velocity) {
			this.animation = animation;
			this.velocity = velocity;
		}

		public String getAnimation() {
			return animation;
		}

		public Vector2 getVelocity() {
			return velocity;
		}
	}

}
