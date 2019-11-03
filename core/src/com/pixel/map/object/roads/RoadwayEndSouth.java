package com.pixel.map.object.roads;

import com.badlogic.gdx.graphics.Texture;
import com.pixel.behavior.EndRoadSouthPlacement;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;
import com.pixel.map.object.MapObject;

public class RoadwayEndSouth extends Road {

	public RoadwayEndSouth(float x, float y, float width, float height, Map.MapCoord coord) {
		super(x, y, width, height, coord, RoadFactory.RoadType.END_S.getName());

		type = RoadFactory.RoadType.END_S;

		// set the proper texture here as well
		Texture texture = PixelAssetManager.manager.get(PixelAssetManager.roadwayEndSouth);
		loadTexture(texture, PixelAssetManager.roadwayEndSouth);

		// we need to add our own placementBehavior here
		placementBehavior = new EndRoadSouthPlacement(this);
	}

	@Override
	public MapObject copy() {

		return create(this);
	}

	public static Road create(Road road) {
		return new RoadwayEndSouth(road.getX(), road.getY(), road.getWidth(),
			   road.getHeight(), road.getMapPosition());
	}
}
