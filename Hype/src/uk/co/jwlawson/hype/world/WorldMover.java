package uk.co.jwlawson.hype.world;

import uk.co.jwlawson.aniguffins.NinePatchActor;

import com.badlogic.gdx.graphics.g2d.NinePatch;

public class WorldMover extends NinePatchActor {

	private static final float DISTANCE = 75f;

	private Position position;
	private World world;

	public WorldMover(NinePatch patch, Position position, World world) {
		super(patch);
		this.position = position;
		this.world = world;
	}

	protected void moveWorld() {
		float xMove = position.mapX(DISTANCE);
		float yMove = position.mapY(DISTANCE);

		world.moveAtSpeed(xMove, yMove);
	}

	protected void stopWorld() {
		world.stopMoveAtSpeed();
	}

	public enum Position {

		TOP(0, 1),
		TOP_LEFT(-1 / Math.sqrt(2), 1 / Math.sqrt(2)),
		LEFT(-1, 0),
		BOTTOM_LEFT(-1 / Math.sqrt(2), -1 / Math.sqrt(2)),
		BOTTOM(0, -1),
		BOTTOM_RIGHT(1 / Math.sqrt(2), -1 / Math.sqrt(2)),
		RIGHT(1, 0),
		TOP_RIGHT(1 / Math.sqrt(2), 1 / Math.sqrt(2));

		private double x, y;

		Position(double x, double y) {
			this.x = x;
			this.y = y;
		}

		float mapX(float x) {
			return (float) (x * this.x);
		}

		float mapY(float y) {
			return (float) (y * this.y);
		}

	}
}
