package com.pixel.map.object.roads;

import com.badlogic.gdx.graphics.Texture;
import com.pixel.behavior.EndRoadWestPlacement;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;
import com.pixel.map.object.MapObject;

public class RoadwayEndWest extends Road {

	public RoadwayEndWest(float x, float y, float width, float height, Map.MapCoord coord) {
		super(x, y, width, height, coord, RoadFactory.RoadType.END_W.getName());

		type = RoadFactory.RoadType.END_W;

		// set the proper texture here as well
		Texture texture = PixelAssetManager.manager.get(PixelAssetManager.roadwayEndWest);
		loadTexture(texture, PixelAssetManager.roadwayEndWest);

		// we need to add our own placementBehavior here
		placementBehavior = new EndRoadWestPlacement(this);
	}

	@Override
	public MapObject copy() {

		return create(this);
	}

	public static Road create(Road road) {
		return new RoadwayEndWest(road.getX(), road.getY(), road.getWidth(),
			   road.getHeight(), road.getMapPosition());
	}
}
