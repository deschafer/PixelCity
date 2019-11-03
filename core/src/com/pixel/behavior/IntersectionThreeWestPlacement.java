package com.pixel.behavior;

import com.pixel.map.object.MapObject;
import com.pixel.map.object.roads.RoadFactory;

public class IntersectionThreeWestPlacement extends RoadPlacement {

	public IntersectionThreeWestPlacement(MapObject owner) {
		super(owner);
	}

	@Override
	public void act() {

		if (otherRoad == null) {
		} else if (otherRoad.getType() == RoadFactory.RoadType.ROADWAY_EW ||
			   otherRoad.getType() == RoadFactory.RoadType.END_W) {
			transform(RoadFactory.RoadType.INTERSECT_4);
		}
	}
}
