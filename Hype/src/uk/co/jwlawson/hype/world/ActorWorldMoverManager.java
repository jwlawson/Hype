package uk.co.jwlawson.hype.world;

import uk.co.jwlawson.hype.world.WorldMover.Position;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ActorWorldMoverManager extends WorldMoverManager {

	private Actor actor = null;

	public ActorWorldMoverManager(World world, TextureAtlas atlas) {
		super(world, atlas);
	}

	public void setActor(Actor actor) {
		this.actor = actor;
		for (WorldMover mover : movers.values()) {
			if (mover instanceof ActorWorldMover) {
				((ActorWorldMover) mover).setActor(actor);
			}
		}
	}

	@Override
	protected WorldMover getMover(NinePatch patch, Position position, World world) {
		ActorWorldMover mover = new ActorWorldMover(patch, position, world);
		mover.setActor(actor);
		return mover;
	}

}
