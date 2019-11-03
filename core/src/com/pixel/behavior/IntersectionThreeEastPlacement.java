package com.pixel.behavior;

import com.pixel.map.object.MapObject;
import com.pixel.map.object.roads.RoadFactory;

public class IntersectionThreeEastPlacement extends RoadPlacement {

	public IntersectionThreeEastPlacement(MapObject owner) {
		super(owner);
	}

	@Override
	public void act() {

		if (otherRoad == null) {
		} else if (otherRoad.getType() == RoadFactory.RoadType.ROADWAY_EW ||
			   otherRoad.getType() == RoadFactory.RoadType.END_E) {
			transform(RoadFactory.RoadType.INTERSECT_4);
		}
	}
}
