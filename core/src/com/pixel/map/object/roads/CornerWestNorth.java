package com.pixel.map.object.roads;

import com.badlogic.gdx.graphics.Texture;
import com.pixel.behavior.CornerWestNorthPlacement;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;
import com.pixel.map.object.MapObject;

public class CornerWestNorth extends Road {

	public CornerWestNorth(float x, float y, float width, float height, Map.MapCoord coord) {
		super(x, y, width, height, coord, RoadFactory.RoadType.CORNER_WN.getName());

		type = RoadFactory.RoadType.CORNER_WN;

		// set the proper texture here as well
		Texture texture = PixelAssetManager.manager.get(PixelAssetManager.cornerWestNorth);
		loadTexture(texture, PixelAssetManager.cornerWestNorth);

		// we need to add our own placementBehavior here
		placementBehavior = new CornerWestNorthPlacement(this);
	}

	@Override
	public MapObject copy() {

		return create(this);
	}

	public static Road create(Road road) {
		return new CornerWestNorth(road.getX(), road.getY(), road.getWidth(),
			   road.getHeight(), road.getMapPosition());
	}
}
