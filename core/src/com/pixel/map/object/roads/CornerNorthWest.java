package com.pixel.map.object.roads;

import com.badlogic.gdx.graphics.Texture;
import com.pixel.behavior.CornerNorthWestPlacement;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;
import com.pixel.map.object.MapObject;

public class CornerNorthWest extends Road {

	public CornerNorthWest(float x, float y, float width, float height, Map.MapCoord coord) {
		super(x, y, width, height, coord, RoadFactory.RoadType.CORNER_NW.getName());

		type = RoadFactory.RoadType.CORNER_NW;

		// set the proper texture here as well
		Texture texture = PixelAssetManager.manager.get(PixelAssetManager.cornerNorthWest);
		loadTexture(texture, PixelAssetManager.cornerNorthWest);

		// we need to add our own placementBehavior here
		placementBehavior = new CornerNorthWestPlacement(this);
	}

	@Override
	public MapObject copy() {

		return create(this);
	}

	public static Road create(Road road) {
		return new CornerNorthWest(road.getX(), road.getY(), road.getWidth(),
			   road.getHeight(), road.getMapPosition());
	}
}
