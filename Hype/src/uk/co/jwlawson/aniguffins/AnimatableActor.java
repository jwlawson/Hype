package uk.co.jwlawson.aniguffins;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimatableActor extends AnimatedActor {

	private HashMap<String, Animation> mAnimations;

	public AnimatableActor(TextureRegion tex) {
		super(tex);
		mAnimations = new HashMap<String, Animation>();
	}

	public void addAnimation(String name, Animation ani) {
		mAnimations.put(name, ani);
	}

	protected Animation getAnimation(String name) {
		if (mAnimations.containsKey(name)) {
			return mAnimations.get(name);
		} else {
			throw new IllegalArgumentException("No animation available with name " + name);
		}
	}

	public void activateAnimation(String name, boolean looping) {
		activateAnimation(getAnimation(name), looping);
	}

}
