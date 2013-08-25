package uk.co.jwlawson.hype.actor;

import java.util.ArrayList;
import java.util.List;

import uk.co.jwlawson.hype.world.Box2dWorld;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pool;

public class Laser extends Group {

	private LaserBase mBase;
	private Pool<LaserBeam> mBeamPool;
	private LaserBeam currentBeam;

	private List<LaserHitListener> hitListeners;

	public Laser(final Box2dWorld world, final TextureAtlas atlas, final Position pos) {
		mBase = new LaserBase(atlas, pos);
		mBase.setBounds(0, 0, 16, 16);
		hitListeners = new ArrayList<LaserHitListener>();

		mBeamPool = new Pool<LaserBeam>() {
			@Override
			protected LaserBeam newObject() {
				return new LaserBeam(atlas, pos, world, Laser.this);
			}
		};

		addActor(mBase);
	}

	@Override
	public void act(float delta) {
		super.act(delta);

	}

	public void prepareToFire() {
		mBase.prepareToFire();
	}

	public void fireLaser() {
		mBase.fireLaser();
		currentBeam = mBeamPool.obtain();
		currentBeam.setStart(8, 8);
		currentBeam.fireLaser();
		addActor(currentBeam);
	}

	public void stopFiring() {
		mBase.stopFiring();
		currentBeam.stopFiring();
	}

	protected void hackerHit(Hacker hacker) {
		for (LaserHitListener lis : hitListeners) {
			lis.hackerHitbyLaser(hacker);
		}
	}

	public void free(LaserBeam laserBeam) {
		mBeamPool.free(laserBeam);
		removeActor(laserBeam);
	}

	public void addLaserHitListener(LaserHitListener listener) {
		hitListeners.add(listener);
	}

	public interface LaserHitListener {
		public void hackerHitbyLaser(Hacker hacker);
	}

	public enum Position {
		BOTTOM("laser.bottom", 0, 1),
		TOP("laser.top", 0, -1),
		LEFT("laser.left", 1, 0),
		RIGHT("laser.right", -1, 0);

		private String name;
		private float xScale, yScale;

		private Position(String name, float xScale, float yScale) {
			this.name = name;
			this.xScale = xScale;
			this.yScale = yScale;
		}

		public String getName() {
			return name;
		}

		public Vector2 extend(Vector2 point, float delta) {
			point.x += xScale * delta;
			point.y += yScale * delta;
			return point;
		}

		private static Position[] allValues = values();

		public static Position fromOrdinal(int n) {
			return allValues[n];
		}

	}
}
