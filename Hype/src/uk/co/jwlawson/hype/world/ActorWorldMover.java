package uk.co.jwlawson.hype.world;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ActorWorldMover extends WorldMover {

	private Actor actor;
	private static Position controlling = null;
	private Vector2 pos1, pos2;

	public ActorWorldMover(NinePatch patch, Position position, World world) {
		super(patch, position, world);
		pos1 = new Vector2();
		pos2 = new Vector2();
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (actor == null) {
			return;
		}
		pos1.x = 8;
		pos1.y = 8;
		pos2 = actor.localToStageCoordinates(pos1);
		pos1 = stageToLocalCoordinates(pos2);
		if (hit(pos1.x, pos2.y, false) != null) {
			// actor within this mover (ish kind of)
			if (controlling != getPosition()) {
				controlling = getPosition();
				moveWorld();
			}
		} else {
			if (controlling == getPosition()) {
				controlling = null;
				stopWorld();
			}
		}
	}

}
