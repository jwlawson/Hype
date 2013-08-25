package uk.co.jwlawson.hype.actor;

import uk.co.jwlawson.hype.world.Box2dWorld;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class IrregularLaser extends TimedLaser {

	private float[] intervals;
	private int index = 0;

	public IrregularLaser(Box2dWorld world, TextureAtlas atlas, Position pos) {
		super(world, atlas, pos);
	}

	public void setIntervals(float... intervals) {
		this.intervals = intervals;
	}

	@Override
	protected float getNextInterval(boolean on) {
		if (intervals == null) {
			throw new IllegalStateException("No intervals set in " + toString());
		}
		if (index >= intervals.length) {
			index = 0;
		}
		return intervals[index++];

	}

}
