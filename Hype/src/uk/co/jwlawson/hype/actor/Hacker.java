package uk.co.jwlawson.hype.actor;

import uk.co.jwlawson.hype.world.Box2dWorld;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Hacker extends RectBox2dActor {

	private static final String NAME = "char";

	public Hacker(TextureAtlas atlas, Box2dWorld world) {
		super(atlas.findRegion(NAME), world);
		setId(NAME);
		load(atlas, "stand", "walkleft", "walkright");
	}

}
