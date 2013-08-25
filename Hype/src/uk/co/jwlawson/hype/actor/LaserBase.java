package uk.co.jwlawson.hype.actor;

import uk.co.jwlawson.aniguffins.LoadAnimatableActor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class LaserBase extends LoadAnimatableActor {

	public LaserBase(TextureAtlas atlas, Laser.Position pos) {
		super(atlas.findRegion(pos.getName()));
		System.out.println(atlas.findRegion(pos.getName()));
		setId(pos.getName());
		load(atlas, "prepare", "fire");
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}

	@Override
	protected void load(TextureAtlas atlas, String... strings) {
		super.load(atlas, strings);
	}

	protected void prepareToFire() {
		activateAnimation("prepare", true);
	}

	protected void fireLaser() {
		activateAnimation("fire", true);
	}

	protected void stopFiring() {
		forceStopAnimation();
	}

}
