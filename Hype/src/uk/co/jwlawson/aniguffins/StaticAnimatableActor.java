package uk.co.jwlawson.aniguffins;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class StaticAnimatableActor extends AnimatedActor {

	private static final String TAG = "StaticAnimatableActor";
	private static HashMap<String, Animation> animations;

	public StaticAnimatableActor(TextureRegion tex) {
		super(tex);
	}

	public static void addAnimation(String name, Animation ani) {
		if (animations.containsKey(name)) {
			Gdx.app.log(TAG, "Overwriting animation " + name);
		}
		animations.put(name, ani);
	}

	protected Animation getAnimation(String name) {
		if (animations.containsKey(name)) {
			return animations.get(name);
		} else {
			throw new IllegalArgumentException("No animation available with name " + name);
		}
	}

	public void activateAnimation(String name, boolean looping) {
		activateAnimation(getAnimation(name), looping);
	}

}
