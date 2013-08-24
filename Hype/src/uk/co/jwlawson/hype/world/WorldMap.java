package uk.co.jwlawson.hype.world;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class WorldMap {

	private static final String TAG = "WorldMap";
	private static final float SCALE = 1f;

	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private Stage stage;

	private int pixWidth, pixHeight;
	private int width, height;
	private int tileHeight, tileWidth;

	public WorldMap(String mapName, AssetManager assets, Stage stage) {
		map = assets.get(mapName);
		this.stage = stage;
		renderer = new OrthogonalTiledMapRenderer(map, SCALE);

		calcPixelDimensions();
	}

	private void calcPixelDimensions() {
		MapProperties props = map.getProperties();
		tileWidth = (Integer) props.get("tilewidth");
		tileHeight = (Integer) props.get("tileheight");
		width = (Integer) props.get("width");
		height = (Integer) props.get("height");
		pixWidth = (int) (width * tileWidth * SCALE);
		pixHeight = (int) (height * tileHeight * SCALE);
	}

	public int getPixWidth() {
		return pixWidth;
	}

	public int getPixHeight() {
		return pixHeight;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public float getScale() {
		return SCALE;
	}

	public Vector2 findEntrance() {
		Vector2 pos = new Vector2();

		TiledMapTileLayer mapLayer = (TiledMapTileLayer) map.getLayers().get(0);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Cell cell = mapLayer.getCell(i, j);
				if (cell != null) {
					TiledMapTile tile = cell.getTile();
					if (tile.getProperties().containsKey("start")) {
						pos.x = i * tileWidth * SCALE;
						pos.y = j * tileHeight * SCALE;
						break;
					}
				}
			}
		}

		return pos;
	}

	public int getTileId(int layer, int x, int y) {
		int id = 0;
		TiledMapTileLayer mapLayer = (TiledMapTileLayer) map.getLayers().get(layer);
		Cell cell = mapLayer.getCell(x, y);
		if (cell != null) {
			TiledMapTile tile = cell.getTile();
			id = tile.getId();
		}

		return id;
	}

	public void draw() {
		renderer.setView((OrthographicCamera) stage.getCamera());
		renderer.render();
	}

}
