package com.pixel.map.object.roads;

import com.pixel.map.Map;
import com.pixel.map.object.MapObject;

public class RoadwayNorthSouth extends Road {

	public RoadwayNorthSouth(float x, float y, float width, float height, Map.MapCoord coord, String ID) {
		super(x, y, width, height, coord, ID);

		type = RoadFactory.RoadType.ROADWAY_NS;

		// set the proper texture here as well
	}

	@Override
	public boolean placeOverObject(MapObject object) {
		return super.placeOverObject(object);

		// we need to add our own placementBehavior here

	}

	@Override
	public MapObject copy() {

		return create(this);
	}

	public static Road create(Road road) {
		return new RoadwayNorthSouth(road.getX(), road.getY(), road.getWidth(),
			   road.getHeight(), road.getMapPosition(), road.getName());
	}

}
