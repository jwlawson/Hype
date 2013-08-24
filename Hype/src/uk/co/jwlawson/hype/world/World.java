package uk.co.jwlawson.hype.world;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * World class to hold information about the world and provide methods to move around the world.
 * Should be the first class initialised after the stage.
 * 
 * @author Administrator
 * 
 */
public class World {

	private static final String TAG = "World";

	private Stage stage;
	private float width, height;
	private float top, bottom, left, right;
	private List<MoveListener> listeners;
	private float xSpeed, ySpeed;
	private float deltaX, deltaY;
	private boolean moveOnAct;

	public World(Stage stage) {
		this.stage = stage;
		listeners = new ArrayList<World.MoveListener>();
		ActCatcher actor = new ActCatcher();
		stage.addActor(actor);
	}

	public void setBounds(float width, float height) {
		this.width = width;
		this.height = height;
		Camera cam = stage.getCamera();
		Vector3 position = cam.position;
		float x = position.x;
		float y = position.y;
		setBounds(x, height + y, width + x, y);
	}

	public void setBounds(float left, float top, float right, float bottom) {
		if (left > right) {
			float tmp = left;
			left = right;
			right = tmp;
		}
		if (bottom > top) {
			float tmp = top;
			top = bottom;
			bottom = tmp;
		}
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;

	}

	public void resize(float viewWidth, float viewHeight) {
		float x = viewWidth / 2;
		float y = viewHeight / 2;
		setBounds(x, height + y, width + x, y);
	}

	public void addMoveListener(MoveListener listener) {
		listeners.add(listener);
	}

	private void actWorld(float delta) {
		if (moveOnAct) {
			deltaX = delta * xSpeed;
			deltaY = delta * ySpeed;
			moveBy(deltaX, deltaY);
		}
	}

	/** Move the world at a rate of pixels per second. */
	public void moveAtSpeed(float xSpeed, float ySpeed) {
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		moveOnAct = true;
	}

	public void stopMoveAtSpeed() {
		moveOnAct = false;
		xSpeed = 0;
		ySpeed = 0;
	}

	/**
	 * Move the world by the specified number of pixels.
	 * If this would move the camera outside the world view, then it is moved up to the boundary.
	 */
	public void moveBy(float deltaX, float deltaY) {
		Camera cam = stage.getCamera();
		Vector3 position = cam.position;
		float x = position.x;
		float y = position.y;
		float width = cam.viewportWidth;
		float height = cam.viewportHeight;

		deltaX = validateDeltaX(deltaX, x, width);
		deltaY = validateDeltaY(deltaY, y, height);

		validateSpeeds(deltaX, deltaY);

		moveByValid(deltaX, deltaY, cam);
	}

	private void validateSpeeds(float deltaX, float deltaY) {
		if (moveOnAct) {
			if (deltaX == 0 && deltaY == 0) {
				stopMoveAtSpeed();
			}
		}
	}

	private float validateDeltaY(float deltaY, float y, float height) {
		if (deltaY < 0 && y + deltaY < bottom) {
			deltaY = (bottom - y > 0 ? 0 : bottom - y);
		} else if (deltaY > 0 && y + deltaY + height > top) {
			deltaY = (top - y - height < 0 ? 0 : top - y - height);
		}
		return deltaY;
	}

	private float validateDeltaX(float deltaX, float x, float width) {
		if (deltaX < 0 && x + deltaX < left) {
			deltaX = (left - x > 0 ? 0 : left - x);
		} else if (deltaX > 0 && x + deltaX + width > right) {
			deltaX = (right - x - width < 0 ? 0 : right - x - width);
		}
		return deltaX;
	}

	private void moveByValid(float deltaX, float deltaY, Camera cam) {
		cam.translate(deltaX, deltaY, 0);

		for (MoveListener listener : listeners) {
			listener.worldMovingBy(deltaX, deltaY);
		}
	}

	/** Point the camera at the specified point, or the closest to it while remaining in the map */
	public void lookAt(float x, float y) {
		Camera cam = stage.getCamera();
		Vector3 position = cam.position;
		float xCur = position.x;
		float yCur = position.y;

		float xMove = x - xCur;
		float yMove = y - yCur;
		moveBy(xMove, yMove);
	}

	public void lookAt(Vector2 pos) {
		lookAt(pos.x, pos.y);
	}

	public interface MoveListener {

		public void worldMovingBy(float deltaX, float deltaY);
	}

	private class ActCatcher extends Actor {

		@Override
		public void act(float delta) {
			actWorld(delta);
		}
	}
}
