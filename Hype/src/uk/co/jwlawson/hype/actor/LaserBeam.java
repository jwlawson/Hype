package uk.co.jwlawson.hype.actor;

import uk.co.jwlawson.aniguffins.NinePatchActor;
import uk.co.jwlawson.hype.actor.Laser.Position;
import uk.co.jwlawson.hype.world.Box2dWorld;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

public class LaserBeam extends NinePatchActor {

	private static final String NAME = "laserbeam";
	private static final int BEAM_WIDTH = 2;
	private static final float SPEED = 70f;
	private Position pos;
	private Box2dWorld world;
	private Laser laser;
	private Callback callback;
	private Vector2 start, end, scaledStart, scaledEnd;
	private float timeBuffer = 0.5f;
	private boolean movingEnd = false;
	private boolean movingStart = false;
	private boolean invalidBounds = false;
	private float lastXDiff, lastYDiff, currXDiff, currYDiff;

	public LaserBeam(TextureAtlas atlas, Position pos, Box2dWorld world, Laser laser) {
		super(atlas.createPatch(NAME));
		this.pos = pos;
		this.world = world;
		this.laser = laser;
		setVisible(false);
		callback = new Callback();
		start = new Vector2();
		end = new Vector2();
		scaledStart = new Vector2();
		scaledEnd = new Vector2();
	}

	public void setStart(float x, float y) {
		start.set(x, y);
		end.set(x, y);
		pos.extend(start, 3);
		pos.extend(end, 5);
	}

	public void setStart(Vector2 start) {
		pos.extend(start, 3);
		this.start.set(start);
		this.end.set(start);
		pos.extend(this.end, 2);
	}

	protected void fireLaser() {
		setVisible(true);
		setColor(1f, 1f, 1f, 1f);
		movingEnd = true;
		movingStart = false;
	}

	protected void stopFiring() {
		movingStart = true;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		timeBuffer -= delta;
		if (timeBuffer < 0 && start.dst(end) > 2) {
			scaledStart.set(start);
			getParent().localToStageCoordinates(scaledStart);
			scaledStart.scl(1 / Box2dWorld.PIXELS_PER_METER);
			scaledEnd.set(end);
			getParent().localToStageCoordinates(scaledEnd);
			scaledEnd.scl(1 / Box2dWorld.PIXELS_PER_METER);
			world.rayCast(callback, scaledStart, scaledEnd);
		}
		if (invalidBounds) {
			updateBounds(delta);
		}
		if (movingEnd) {
			pos.extend(end, delta * SPEED);
			invalidBounds = true;
		}
		if (movingStart) {
			pos.extend(start, delta * SPEED);
			currXDiff = start.x - end.x;
			currYDiff = start.y - end.y;

			if (pos.getxScale() == 0) {
				if ((0 < Math.max(currYDiff, lastYDiff)) && (0 > Math.min(currYDiff, lastYDiff))) {
					movingStart = false;
					laser.free(this);
				}
			} else if ((0 < Math.max(currXDiff, lastXDiff) && (0 > Math.min(currXDiff, lastXDiff)))) {
				// start has just gone past end
				movingStart = false;
				laser.free(this);
			}

			lastXDiff = currXDiff;
			lastYDiff = currYDiff;
			invalidBounds = true;
		}

	}

	private void updateBounds(float delta) {
		if (equal(start.x, end.x)) {
			if (equal(start.y, end.y)) {
				// Beam has run out of space
				laser.free(this);
			}
			// laser firing up or down
			setBounds(start.x, Math.min(start.y, end.y), BEAM_WIDTH, Math.abs(start.y - end.y));
		} else {
			// laser firing left or right
			setBounds(Math.min(start.x, end.x), start.y, Math.abs(start.x - end.x), BEAM_WIDTH);
		}
		invalidBounds = false;
	}

	private boolean equal(double a, double b) {
		return Math.abs(a - b) < 1e-2;
	}

	private class Callback implements RayCastCallback {

		@Override
		public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
			Object obj = fixture.getUserData();
			if (obj != null && obj instanceof Hacker) {
				laser.hackerHit((Hacker) obj);
			} else if (movingEnd) {
				end.set(point);
				end.scl(Box2dWorld.PIXELS_PER_METER);
				getParent().stageToLocalCoordinates(end);
				pos.extend(end, -1);
				movingEnd = false;
				invalidBounds = true;
			} else {
				movingStart = false;
			}
			return 0;
		}

	}
}
