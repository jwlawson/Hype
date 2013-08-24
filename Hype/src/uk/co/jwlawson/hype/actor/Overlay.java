package uk.co.jwlawson.hype.actor;

import uk.co.jwlawson.hype.world.World;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Overlay extends Group implements World.MoveListener {

	public final static String NAME = "Overlay";

	public Overlay() {
		setName(NAME);
	}

	@Override
	public void worldMovingBy(float deltaX, float deltaY) {
		addAction(Actions.moveBy(deltaX, deltaY));
	}

}
