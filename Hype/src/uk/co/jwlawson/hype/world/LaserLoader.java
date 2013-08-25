package uk.co.jwlawson.hype.world;

import java.util.ArrayList;
import java.util.List;

import uk.co.jwlawson.hype.actor.IrregularLaser;
import uk.co.jwlawson.hype.actor.Laser;
import uk.co.jwlawson.hype.actor.Laser.LaserHitListener;
import uk.co.jwlawson.hype.actor.Laser.Position;
import uk.co.jwlawson.hype.actor.RegularLaser;
import uk.co.jwlawson.hype.actor.TimedLaser;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class LaserLoader {

	private static final int IRREGULAR = 0;
	private static final int REGULAR = 1;
	private List<TimedLaser> lasers;
	private WorldMap mWorldMap;

	public LaserLoader(WorldMap world) {
		lasers = new ArrayList<TimedLaser>();
		mWorldMap = world;
	}

	public void addToStage(Stage stage) {
		for (Laser laser : lasers) {
			stage.addActor(laser);
		}
	}

	public void startFiring() {
		for (TimedLaser laser : lasers) {
			laser.start();
		}
	}

	public void addLaserHitListener(LaserHitListener lis) {
		for (Laser laser : lasers) {
			laser.addLaserHitListener(lis);
		}
	}

	public void load(String laserFileName, AssetManager assets, TextureAtlas atlas, Box2dWorld world) {
		String file = assets.get(laserFileName, String.class);
		String[] lines = file.split("\\r?\\n");

		for (String line : lines) {
			if (line.startsWith("%")) {
				continue;
			}
			String[] split = line.split(" ");
			System.out.println(line + " " + split.length);
			int xTile = Integer.parseInt(split[0]);
			int yTile = Integer.parseInt(split[1]);
			int type = Integer.parseInt(split[2]);
			int position = Integer.parseInt(split[3]);
			float[] floats = new float[split.length - 4];
			for (int i = 0; i < split.length - 4; i++) {
				floats[i] = Float.parseFloat(split[i + 4]);
			}
			lasers.add(getLaser(atlas, world, xTile, yTile, type, position, floats));
		}
	}

	private TimedLaser getLaser(TextureAtlas atlas, Box2dWorld world, int xTile, int yTile,
			int type, int position, float... intervals) {
		Position pos = Position.fromOrdinal(position);
		if (type == REGULAR) {
			RegularLaser laser = new RegularLaser(world, atlas, pos);
			laser.setInterval(intervals[0], intervals[1]);
			laser.setBounds(xTile * mWorldMap.getTileWidth(),
					(mWorldMap.getMapHeight() - yTile - 1) * mWorldMap.getTileHeight(), 16, 16);
			return laser;
		} else if (type == IRREGULAR) {
			IrregularLaser laser = new IrregularLaser(world, atlas, pos);
			laser.setBounds(xTile * mWorldMap.getTileWidth(),
					(mWorldMap.getMapHeight() - yTile - 1) * mWorldMap.getTileHeight(), 16, 16);
			laser.setIntervals(intervals);
			return laser;
		} else {
			throw new IllegalArgumentException("Type must be either 1 or 0. Not " + type);
		}

	}
}
