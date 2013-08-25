package uk.co.jwlawson.hype.actor;

import uk.co.jwlawson.hype.world.Box2dWorld;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class RegularLaser extends TimedLaser {

	private float offInterval;
	private float onInterval;

	public RegularLaser(Box2dWorld world, TextureAtlas atlas, Position pos) {
		super(world, atlas, pos);
	}

	public void setInterval(float onInterval, float offInterval) {
		this.onInterval = onInterval;
		this.offInterval = offInterval;
	}

	@Override
	protected float getNextInterval(boolean on) {
		if (on) {
			return offInterval;
		} else {
			return onInterval;
		}
	}

}
