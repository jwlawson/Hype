package uk.co.jwlawson.hype.world;

import java.util.HashMap;

import uk.co.jwlawson.hype.actor.Overlay;
import uk.co.jwlawson.hype.world.WorldMover.Position;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public abstract class WorldMoverManager {

	private static final String TAG = "WorldMoverManager";
	private static final int MOVER_SIZE = 100;

	protected HashMap<Position, WorldMover> movers = new HashMap<Position, WorldMover>();

	public WorldMoverManager(World world, TextureAtlas atlas) {
		NinePatch p1 = atlas.createPatch("blue");
		NinePatch p2 = atlas.createPatch("green");

		int i = 0;
		for (Position pos : Position.values()) {
			WorldMover mover = getMover(i % 2 == 0 ? p1 : p2, pos, world);
			mover.setName(pos.toString());
			i++;
			movers.put(pos, mover);
		}
	}

	protected abstract WorldMover getMover(NinePatch patch, Position position, World world);

	public void setSize(int width, int height) {

		WorldMover bot = movers.get(Position.BOTTOM);
		bot.setBounds(MOVER_SIZE, 0, width - 2 * MOVER_SIZE, MOVER_SIZE);

		WorldMover bl = movers.get(Position.BOTTOM_LEFT);
		bl.setBounds(0, 0, MOVER_SIZE, MOVER_SIZE);

		WorldMover br = movers.get(Position.BOTTOM_RIGHT);
		br.setBounds(width - MOVER_SIZE, 0, MOVER_SIZE, MOVER_SIZE);

		WorldMover l = movers.get(Position.LEFT);
		l.setBounds(0, MOVER_SIZE, MOVER_SIZE, height - 2 * MOVER_SIZE);

		WorldMover tl = movers.get(Position.TOP_LEFT);
		tl.setBounds(0, height - MOVER_SIZE, MOVER_SIZE, MOVER_SIZE);

		WorldMover t = movers.get(Position.TOP);
		t.setBounds(MOVER_SIZE, height - MOVER_SIZE, width - 2 * MOVER_SIZE, MOVER_SIZE);

		WorldMover tr = movers.get(Position.TOP_RIGHT);
		tr.setBounds(width - MOVER_SIZE, height - MOVER_SIZE, MOVER_SIZE, MOVER_SIZE);

		WorldMover r = movers.get(Position.RIGHT);
		r.setBounds(width - MOVER_SIZE, MOVER_SIZE, MOVER_SIZE, height - 2 * MOVER_SIZE);
	}

	public void addToOverlay(Overlay overlay) {
		for (WorldMover mover : movers.values()) {
			overlay.addActor(mover);
		}
	}

}
