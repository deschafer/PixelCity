package com.pixel.behavior;

import com.pixel.map.object.MapObject;
import com.pixel.map.object.roads.RoadFactory;

public class RoadwayNorthSouthPlacement extends RoadPlacement {

	public RoadwayNorthSouthPlacement(MapObject owner) {
		super(owner);
	}

	@Override
	public void act() {

		if (otherRoad == null) {

		} else if (otherRoad.getType() == RoadFactory.RoadType.END_E) {
			// transform into an intersection
			transform(RoadFactory.RoadType.INTERSECT_3_W);
		} else if (otherRoad.getType() == RoadFactory.RoadType.END_W) {
			// transform into an intersection
			transform(RoadFactory.RoadType.INTERSECT_3_E);
		} else if (otherRoad.getType() == RoadFactory.RoadType.ROADWAY_EW) {
			// transform into an intersection
			transform(RoadFactory.RoadType.INTERSECT_4);
		}
	}
}
