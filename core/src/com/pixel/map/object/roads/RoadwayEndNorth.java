package com.pixel.map.object.roads;

import com.badlogic.gdx.graphics.Texture;
import com.pixel.behavior.EndRoadNorthPlacement;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;
import com.pixel.map.object.MapObject;

public class RoadwayEndNorth extends Road {

	public RoadwayEndNorth(float x, float y, float width, float height, Map.MapCoord coord) {
		super(x, y, width, height, coord, RoadFactory.RoadType.END_N.getName());

		type = RoadFactory.RoadType.END_N;

		// set the proper texture here as well
		Texture texture = PixelAssetManager.manager.get(PixelAssetManager.roadwayEndNorth);
		loadTexture(texture, PixelAssetManager.roadwayEndNorth);

		// we need to add our own placementBehavior here
		placementBehavior = new EndRoadNorthPlacement(this);
	}

	@Override
	public MapObject copy() {

		return create(this);
	}

	public static Road create(Road road) {
		return new RoadwayEndNorth(road.getX(), road.getY(), road.getWidth(),
			   road.getHeight(), road.getMapPosition());
	}
}
