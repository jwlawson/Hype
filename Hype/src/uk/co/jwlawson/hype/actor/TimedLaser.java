package uk.co.jwlawson.hype.actor;

import uk.co.jwlawson.hype.world.Box2dWorld;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public abstract class TimedLaser extends Laser {

	private static final float PREPARE_TIME = 0.7f;
	private float timeRemaining;
	private float overlap;
	private boolean firing = false;
	private boolean prepared = false;
	private boolean on = false;

	public TimedLaser(Box2dWorld world, TextureAtlas atlas, Position pos) {
		super(world, atlas, pos);
	}

	public void start() {
		firing = true;
		timeRemaining = getNextInterval(false);
		on = true;
		if (timeRemaining > 0) {
			fireLaser();
		}
	}

	protected abstract float getNextInterval(boolean on);

	@Override
	public void act(float delta) {
		super.act(delta);
		if (firing) {
			timeRemaining -= delta;
			if (timeRemaining < PREPARE_TIME && !prepared) {
				prepareToFire();
				prepared = true;
			}
			if (timeRemaining < 0) {
				overlap = timeRemaining;
				if (on) {
					timeRemaining = getNextInterval(on) - overlap;
					on = false;
					prepared = false;
					stopFiring();
				} else {
					timeRemaining = getNextInterval(on) - overlap;
					on = true;
					fireLaser();
				}
			}
		}
	}

}
