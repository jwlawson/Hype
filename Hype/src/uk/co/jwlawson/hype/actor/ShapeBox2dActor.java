package uk.co.jwlawson.hype.actor;

import uk.co.jwlawson.hype.world.Box2dWorld;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public abstract class ShapeBox2dActor extends Box2dActor {

	private float density = 1f;
	private float friction = 5f;
	private float restitution = 0f;

	public ShapeBox2dActor(TextureRegion tex, Box2dWorld world) {
		super(tex, world);
	}

	public void setDensity(float density) {
		this.density = density;
	}

	public void setFriction(float friction) {
		this.friction = friction;
	}

	public void setRestitution(float restitution) {
		this.restitution = restitution;
	}

	@Override
	public void setBounds(float x, float y, float width, float height) {
		super.setBounds(x, y, width, height);
		createBody();
	}

	@Override
	protected BodyDef getBodyDef() {
		BodyDef def = new BodyDef();
		def.position.set(getX(), getY());
		def.type = BodyType.DynamicBody;
		return def;
	}

	@Override
	protected FixtureDef getFixtureDef() {
		FixtureDef def = new FixtureDef();
		def.shape = getShape();
		def.density = density;
		def.friction = friction;
		def.restitution = restitution;
		return def;
	}

	protected abstract PolygonShape getShape();

}
