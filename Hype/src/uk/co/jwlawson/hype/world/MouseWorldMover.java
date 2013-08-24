package uk.co.jwlawson.hype.world;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class MouseWorldMover extends WorldMover {

	public MouseWorldMover(NinePatch patch, Position position, World world) {
		super(patch, position, world);
		addListener(new MouseOverListener());
	}

	private class MouseOverListener extends InputListener {

		@Override
		public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
			getColor().set(1, 0, 0, 1);
			moveWorld();
		}

		@Override
		public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
			getColor().set(1, 1, 1, 1);
			stopWorld();
		}

	}
}
