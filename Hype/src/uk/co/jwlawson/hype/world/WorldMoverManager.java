package uk.co.jwlawson.hype.world;

import java.util.HashMap;

import uk.co.jwlawson.hype.actor.Overlay;
import uk.co.jwlawson.hype.world.WorldMover.Position;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public abstract class WorldMoverManager {

	private static final String TAG = "WorldMoverManager";
	private static final int MOVER_SIZE = 50;
	private static final int EXTRA = 100;

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
		bot.setBounds(MOVER_SIZE, -EXTRA, width - 2 * MOVER_SIZE, MOVER_SIZE + EXTRA);

		WorldMover bl = movers.get(Position.BOTTOM_LEFT);
		bl.setBounds(-EXTRA, -EXTRA, MOVER_SIZE + EXTRA, MOVER_SIZE + EXTRA);

		WorldMover br = movers.get(Position.BOTTOM_RIGHT);
		br.setBounds(width - MOVER_SIZE, -EXTRA, MOVER_SIZE + EXTRA, MOVER_SIZE + EXTRA);

		WorldMover l = movers.get(Position.LEFT);
		l.setBounds(-EXTRA, MOVER_SIZE, MOVER_SIZE + EXTRA, height - 2 * MOVER_SIZE);

		WorldMover tl = movers.get(Position.TOP_LEFT);
		tl.setBounds(-EXTRA, height - MOVER_SIZE, MOVER_SIZE + EXTRA, MOVER_SIZE + EXTRA);

		WorldMover t = movers.get(Position.TOP);
		t.setBounds(MOVER_SIZE, height - MOVER_SIZE, width - 2 * MOVER_SIZE, MOVER_SIZE + EXTRA);

		WorldMover tr = movers.get(Position.TOP_RIGHT);
		tr.setBounds(width - MOVER_SIZE, height - MOVER_SIZE, MOVER_SIZE + EXTRA, MOVER_SIZE
				+ EXTRA);

		WorldMover r = movers.get(Position.RIGHT);
		r.setBounds(width - MOVER_SIZE, MOVER_SIZE, MOVER_SIZE + EXTRA, height - 2 * MOVER_SIZE);
	}

	public void addToOverlay(Overlay overlay) {
		for (WorldMover mover : movers.values()) {
			overlay.addActor(mover);
		}
	}

}
