package com.pixel.map.object.roads;

import com.badlogic.gdx.graphics.Texture;
import com.pixel.behavior.IntersectionThreeNorthPlacement;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;
import com.pixel.map.object.MapObject;

public class IntersectionThreeNorth extends Road{

	public IntersectionThreeNorth(float x, float y, float width, float height, Map.MapCoord coord) {
		super(x, y, width, height, coord, RoadFactory.RoadType.INTERSECT_3_N.getName());

		type = RoadFactory.RoadType.INTERSECT_3_N;

		// set the proper texture here as well
		Texture texture = PixelAssetManager.manager.get(PixelAssetManager.intersectionThreeNorth);
		loadTexture(texture, PixelAssetManager.intersectionThreeNorth);

		// we need to add our own placementBehavior here
		placementBehavior = new IntersectionThreeNorthPlacement(this);
	}

	@Override
	public MapObject copy() {

		return create(this);
	}

	public static Road create(Road road) {
		return new IntersectionThreeNorth(road.getX(), road.getY(), road.getWidth(),
			   road.getHeight(), road.getMapPosition());
	}
}
