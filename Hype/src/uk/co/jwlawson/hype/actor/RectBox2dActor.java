package uk.co.jwlawson.hype.actor;

import uk.co.jwlawson.hype.world.Box2dWorld;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class RectBox2dActor extends ShapeBox2dActor {

	private PolygonShape shape;

	public RectBox2dActor(TextureRegion tex, Box2dWorld world) {
		super(tex, world);
	}

	@Override
	protected PolygonShape getShape() {
		// Boxes are defined by their "half width" and "half height", hence the
		// 2 multiplier.
		shape = new PolygonShape();
		shape.setAsBox(getWidth() / (4 * Box2dWorld.PIXELS_PER_METER), getHeight()
				/ (2 * Box2dWorld.PIXELS_PER_METER));
		return shape;
	}

	@Override
	protected void bodyCreated() {
		shape.dispose();
	}

}
