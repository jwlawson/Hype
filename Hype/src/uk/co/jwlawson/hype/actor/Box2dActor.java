package uk.co.jwlawson.hype.actor;

import uk.co.jwlawson.aniguffins.LoadAnimatableActor;
import uk.co.jwlawson.hype.world.Box2dWorld;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public abstract class Box2dActor extends LoadAnimatableActor {

	protected Body mBody = null;
	private Box2dWorld mWorld;

	public Box2dActor(TextureRegion tex, Box2dWorld world) {
		super(tex);
		this.mWorld = world;
	}

	public void createBody() {
		BodyDef bdef = getBodyDef();
		FixtureDef fdef = getFixtureDef();

		if (mBody != null) {
			mWorld.removeBodySafely(mBody);
		}
		mBody = mWorld.addBody(bdef);
		Fixture fix = mBody.createFixture(fdef);

		bodyCreated(mBody, fix);
	}

	protected abstract BodyDef getBodyDef();

	protected abstract FixtureDef getFixtureDef();

	protected abstract void bodyCreated(Body body, Fixture fixture);

	@Override
	public void act(float delta) {
		super.act(delta);
		setRotation(radiansToDegrees(mBody.getAngle()));
		setPosition(mBody.getPosition().x * Box2dWorld.PIXELS_PER_METER - getWidth() / 2,
				mBody.getPosition().y * Box2dWorld.PIXELS_PER_METER - getHeight() / 2);
	}

	public void dispose() {
		mWorld.removeBodySafely(mBody);
	}

	public static float radiansToDegrees(float rad) {
		return (float) (rad * 360 / (2 * Math.PI));
	}

}
