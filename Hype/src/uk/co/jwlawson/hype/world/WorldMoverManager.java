package uk.co.jwlawson.hype.world;

import java.util.HashMap;

import uk.co.jwlawson.hype.actor.Overlay;
import uk.co.jwlawson.hype.world.WorldMover.Position;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public abstract class WorldMoverManager {

	private static final String TAG = "WorldMoverManager";
	private static final int MOVER_SIZE_X = 130;
	private static final int MOVER_SIZE_Y = 70;
	private static final int EXTRA = 300;

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
		bot.setBounds(MOVER_SIZE_X, -EXTRA, width - 2 * MOVER_SIZE_X, MOVER_SIZE_Y + EXTRA);

		WorldMover bl = movers.get(Position.BOTTOM_LEFT);
		bl.setBounds(-EXTRA, -EXTRA, MOVER_SIZE_X + EXTRA, MOVER_SIZE_Y + EXTRA);

		WorldMover br = movers.get(Position.BOTTOM_RIGHT);
		br.setBounds(width - MOVER_SIZE_X, -EXTRA, MOVER_SIZE_X + EXTRA, MOVER_SIZE_Y + EXTRA);

		WorldMover l = movers.get(Position.LEFT);
		l.setBounds(-EXTRA, MOVER_SIZE_Y, MOVER_SIZE_X + EXTRA, height - 2 * MOVER_SIZE_Y);

		WorldMover tl = movers.get(Position.TOP_LEFT);
		tl.setBounds(-EXTRA, height - MOVER_SIZE_Y, MOVER_SIZE_X + EXTRA, MOVER_SIZE_Y + EXTRA);

		WorldMover t = movers.get(Position.TOP);
		t.setBounds(MOVER_SIZE_X, height - MOVER_SIZE_Y, width - 2 * MOVER_SIZE_X, MOVER_SIZE_Y
				+ EXTRA);

		WorldMover tr = movers.get(Position.TOP_RIGHT);
		tr.setBounds(width - MOVER_SIZE_X, height - MOVER_SIZE_Y, MOVER_SIZE_X + EXTRA,
				MOVER_SIZE_Y + EXTRA);

		WorldMover r = movers.get(Position.RIGHT);
		r.setBounds(width - MOVER_SIZE_X, MOVER_SIZE_Y, MOVER_SIZE_X + EXTRA, height - 2
				* MOVER_SIZE_Y);
	}

	public void addToOverlay(Overlay overlay) {
		for (WorldMover mover : movers.values()) {
			overlay.addActor(mover);
		}
	}

}
