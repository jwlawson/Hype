package uk.co.jwlawson.aniguffins;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class LoadStaticAnimatableActor extends StaticAnimatableActor {

	protected static final String name = "name";

	public LoadStaticAnimatableActor(TextureRegion tex) {
		super(tex);
	}

	public static void load(TextureAtlas atlas) {
		String[] anis = { "moveup", "movedown", "moveleft", "moveright", "attack", "gethit", "die" };

		for (String ani : anis) {
			loadAnimation(ani, atlas);
		}
	}

	private static void loadAnimation(String animation, TextureAtlas atlas) {
		TextureRegion reg = atlas.findRegion(name + animation);
		Animation ani = getAnimation(reg, 1, 32);
		addAnimation(animation, ani);
	}

}
