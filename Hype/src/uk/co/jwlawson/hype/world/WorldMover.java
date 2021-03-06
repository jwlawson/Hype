package uk.co.jwlawson.hype.world;

import uk.co.jwlawson.aniguffins.NinePatchActor;

import com.badlogic.gdx.graphics.g2d.NinePatch;

public class WorldMover extends NinePatchActor {

	private static final float SPEED = 240f;

	private Position position;
	private World world;

	public WorldMover(NinePatch patch, Position position, World world) {
		super(patch);
		this.position = position;
		this.world = world;
	}

	public Position getPosition() {
		return position;
	}

	protected void moveWorld() {
		float xMove = getPosition().mapX(SPEED);
		float yMove = getPosition().mapY(SPEED);

		world.moveAtSpeed(xMove, yMove);
	}

	protected void stopWorld() {
		world.stopMoveAtSpeed();
	}

	public enum Position {

		TOP(0, 1),
		TOP_LEFT(-1, 1),
		LEFT(-1, 0),
		BOTTOM_LEFT(-1, -1),
		BOTTOM(0, -1),
		BOTTOM_RIGHT(1, -1),
		RIGHT(1, 0),
		TOP_RIGHT(1, 1);

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
