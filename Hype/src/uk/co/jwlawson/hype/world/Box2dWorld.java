package uk.co.jwlawson.hype.world;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.World;

public class Box2dWorld {

	public static final float PIXELS_PER_METER = 60;
	private static float SCALE;
	private WorldMap mMap;
	private World mWorld;
	private Box2DDebugRenderer renderer;

	public Box2dWorld(WorldMap map) {
		mMap = map;
		SCALE = mMap.getScale();
		mWorld = new World(new Vector2(0f, -10f), true);
		renderer = new Box2DDebugRenderer();
	}

	public void act(float delta) {
		mWorld.step(delta, 2, 2);
	}

	/**
	 * Draw the collision boundaries
	 */
	public void debugDraw(Camera cam) {
		renderer.render(mWorld,
				cam.combined.scale(PIXELS_PER_METER, PIXELS_PER_METER, PIXELS_PER_METER));
	}

	/**
	 * Create a body in the world using the supplied body definition
	 * 
	 * @param def
	 * @return
	 */
	public Body addBody(BodyDef def) {
		return mWorld.createBody(def);
	}

	/**
	 * Safe way to remove body from the world. Remember that you cannot have any
	 * references to this body after calling this
	 * 
	 * @param body that will be removed from the physic world
	 */

	public void removeBodySafely(Body body) {
		// to prevent some obscure c assertion that happened randomly once in a blue moon
		final ArrayList<JointEdge> list = body.getJointList();
		while (list.size() > 0) {
			mWorld.destroyJoint(list.get(0).joint);
		}
		// actual remove
		mWorld.destroyBody(body);
	}

	/**
	 * Reads a file describing the collision boundaries that should be set
	 * per-tile and adds static bodies to the box2d world.
	 * 
	 * @param collisionsFile
	 */
	public void loadCollisions(String collisionsFile, AssetManager assets) {

		String[] lines = loadFile(collisionsFile, assets);

		HashMap<Integer, ArrayList<LineSegment>> tileCollisionJoints = generateCollisionJoints(lines);

		ArrayList<LineSegment> collisionLineSegments = new ArrayList<LineSegment>();
		combineSegmentsIfPossible(tileCollisionJoints, collisionLineSegments);

		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.type = BodyDef.BodyType.StaticBody;
		Body groundBody = mWorld.createBody(groundBodyDef);
		addLineSegmentsToBody(collisionLineSegments, groundBody);

		generateWorldBounds(groundBody);
	}

	private HashMap<Integer, ArrayList<LineSegment>> generateCollisionJoints(String[] lines) {

		HashMap<Integer, ArrayList<LineSegment>> tileCollisionJoints = new HashMap<Integer, ArrayList<LineSegment>>();

		/**
		 * Some locations on the map (perhaps most locations) are "undefined",
		 * empty space, and will have the tile type 0. This code adds an empty
		 * list of line segments for this "default" tile.
		 */
		tileCollisionJoints.put(Integer.valueOf(0), new ArrayList<LineSegment>());

		generateLineSegments(lines, tileCollisionJoints);
		return tileCollisionJoints;
	}

	/**
	 * Detect the tiles and dynamically create a representation of the map
	 * layout, for collision detection. Each tile has its own collision
	 * rules stored in an associated file.
	 * 
	 * The file contains lines in this format (one line per type of tile):
	 * tileNumber XxY,XxY XxY,XxY
	 * 
	 * Ex:
	 * 
	 * 3 0x0,31x0 ... 4 0x0,29x0 29x0,29x31
	 * 
	 * For a 32x32 tileset, the above describes one line segment for tile #3
	 * and two for tile #4. Tile #3 has a line segment across the top. Tile
	 * #1 has a line segment across most of the top and a line segment from
	 * the top to the bottom, 30 pixels in.
	 */
	private String[] loadFile(String collisionsFile, AssetManager assets) {
		String collisionFile = assets.get(collisionsFile);
		String lines[] = collisionFile.split("\\r?\\n");
		return lines;
	}

	private void generateLineSegments(String[] lines,
			HashMap<Integer, ArrayList<LineSegment>> tileCollisionJoints) {
		for (int n = 0; n < lines.length; n++) {
			String cols[] = lines[n].split(" ");
			int tileNo = Integer.parseInt(cols[0]);

			ArrayList<LineSegment> tmp = new ArrayList<LineSegment>();

			for (int m = 1; m < cols.length; m++) {
				String coords[] = cols[m].split(",");

				String start[] = coords[0].split("x");
				String end[] = coords[1].split("x");

				tmp.add(new LineSegment(Integer.parseInt(start[0]), Integer.parseInt(start[1]),
						Integer.parseInt(end[0]), Integer.parseInt(end[1])));
			}

			tileCollisionJoints.put(Integer.valueOf(tileNo), tmp);
		}
	}

	private void combineSegmentsIfPossible(
			HashMap<Integer, ArrayList<LineSegment>> tileCollisionJoints,
			ArrayList<LineSegment> collisionLineSegments) {
		for (int y = 0; y < mMap.getHeight(); y++) {
			for (int x = 0; x < mMap.getWidth(); x++) {
				int tileType = mMap.getTileId(0, x, y);

				for (int n = 0; n < tileCollisionJoints.get(Integer.valueOf(tileType)).size(); n++) {
					LineSegment lineSeg = tileCollisionJoints.get(Integer.valueOf(tileType)).get(n);

					addOrExtendCollisionLineSegment(x * mMap.getTileWidth() + lineSeg.start().x, y
							* mMap.getTileHeight() - lineSeg.start().y + mMap.getTileHeight(), x
							* mMap.getTileWidth() + lineSeg.end().x, y * mMap.getTileHeight()
							- lineSeg.end().y + mMap.getTileHeight(), collisionLineSegments);
				}
			}
		}
	}

	/**
	 * This is a helper function that makes calls that will attempt to extend
	 * one of the line segments already tracked by TiledMapHelper, if possible.
	 * The goal is to have as few line segments as possible.
	 * 
	 * Ex: If you have a line segment in the system that is from 1x1 to 3x3 and
	 * this function is called for a line that is 4x4 to 9x9, rather than add a
	 * whole new line segment to the list, the 1x1,3x3 line will be extended to
	 * 1x1,9x9. See also: LineSegment.extendIfPossible.
	 * 
	 * @param lsx1
	 *            starting x of the new line segment
	 * @param lsy1
	 *            starting y of the new line segment
	 * @param lsx2
	 *            ending x of the new line segment
	 * @param lsy2
	 *            ending y of the new line segment
	 * @param collisionLineSegments
	 *            the current list of line segments
	 */
	private void addOrExtendCollisionLineSegment(float lsx1, float lsy1, float lsx2, float lsy2,
			ArrayList<LineSegment> collisionLineSegments) {
		LineSegment line = new LineSegment(lsx1, lsy1, lsx2, lsy2);

		boolean didextend = false;

		for (LineSegment test : collisionLineSegments) {
			if (test.extendIfPossible(line)) {
				didextend = true;
				break;
			}
		}

		if (!didextend) {
			collisionLineSegments.add(line);
		}
	}

	private void addLineSegmentsToBody(ArrayList<LineSegment> collisionLineSegments, Body groundBody) {

		for (LineSegment lineSegment : collisionLineSegments) {
			EdgeShape environmentShape = new EdgeShape();

			environmentShape.set(lineSegment.start().scl(1 / PIXELS_PER_METER), lineSegment.end()
					.scl(1 / PIXELS_PER_METER));
			groundBody.createFixture(environmentShape, 0);

			environmentShape.dispose();
		}
	}

	/**
	 * Drawing a boundary around the entire map. We can't use a box because
	 * then the world objects would be inside and the physics engine would
	 * try to push them out.
	 */
	private void generateWorldBounds(Body groundBody) {
		EdgeShape mapBounds = new EdgeShape();
		mapBounds.set(new Vector2(0.0f, 0.0f), new Vector2(mMap.getPixWidth() / PIXELS_PER_METER,
				0.0f));
		groundBody.createFixture(mapBounds, 0);

		mapBounds.set(new Vector2(0.0f, mMap.getPixHeight() / PIXELS_PER_METER),
				new Vector2(mMap.getPixWidth() / PIXELS_PER_METER, mMap.getPixHeight()
						/ PIXELS_PER_METER));
		groundBody.createFixture(mapBounds, 0);

		mapBounds.set(new Vector2(0.0f, 0.0f), new Vector2(0.0f, mMap.getPixHeight()
				/ PIXELS_PER_METER));
		groundBody.createFixture(mapBounds, 0);

		mapBounds.set(new Vector2(mMap.getPixWidth() / PIXELS_PER_METER, 0.0f),
				new Vector2(mMap.getPixWidth() / PIXELS_PER_METER, mMap.getPixHeight()
						/ PIXELS_PER_METER));
		groundBody.createFixture(mapBounds, 0);

		mapBounds.dispose();
	}

	/**
	 * Describes the start and end points of a line segment and contains a
	 * helper method useful for extending line segments.
	 */
	private class LineSegment {
		private Vector2 start = new Vector2();
		private Vector2 end = new Vector2();

		/**
		 * Construct a new LineSegment with the specified coordinates.
		 * 
		 * @param x1
		 * @param y1
		 * @param x2
		 * @param y2
		 */
		public LineSegment(float x1, float y1, float x2, float y2) {
			start = new Vector2(x1, y1);
			end = new Vector2(x2, y2);
		}

		/**
		 * The "start" of the line. Start and end are misnomers, this is just
		 * one end of the line.
		 * 
		 * @return Vector2
		 */
		public Vector2 start() {
			return start;
		}

		/**
		 * The "end" of the line. Start and end are misnomers, this is just one
		 * end of the line.
		 * 
		 * @return Vector2
		 */
		public Vector2 end() {
			return end;
		}

		/**
		 * Determine if the requested line could be tacked on to the end of this
		 * line with no kinks or gaps. If it can, the current LineSegment will
		 * be extended by the length of the passed LineSegment.
		 * 
		 * @param lineSegment
		 * @return boolean true if line was extended, false if not.
		 */
		public boolean extendIfPossible(LineSegment lineSegment) {
			/**
			 * First, let's see if the slopes of the two segments are the same.
			 */
			double slope1 = Math.atan2(end.y - start.y, end.x - start.x);
			double slope2 = Math.atan2(lineSegment.end.y - lineSegment.start.y, lineSegment.end.x
					- lineSegment.start.x);

			if (Math.abs(slope1 - slope2) > 1e-9) {
				return false;
			}

			/**
			 * Second, check if either end of this line segment is adjacent to
			 * the requested line segment. So, 1 pixel away up through sqrt(2)
			 * away.
			 * 
			 * Whichever two points are within the right range will be "merged"
			 * so that the two outer points will describe the line segment.
			 */
			if (start.dst(lineSegment.start) <= Math.sqrt(2) + 1e-9) {
				start.set(lineSegment.end);
				return true;
			} else if (end.dst(lineSegment.start) <= Math.sqrt(2) + 1e-9) {
				end.set(lineSegment.end);
				return true;
			} else if (end.dst(lineSegment.end) <= Math.sqrt(2) + 1e-9) {
				end.set(lineSegment.start);
				return true;
			} else if (start.dst(lineSegment.end) <= Math.sqrt(2) + 1e-9) {
				start.set(lineSegment.start);
				return true;
			}

			return false;
		}

		/**
		 * Returns a pretty description of the LineSegment.
		 * 
		 * @return String
		 */
		@Override
		public String toString() {
			return "[" + start.x + "x" + start.y + "] -> [" + end.x + "x" + end.y + "]";
		}
	}
}
