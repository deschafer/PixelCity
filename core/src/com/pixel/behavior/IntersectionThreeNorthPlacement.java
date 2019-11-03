package com.pixel.behavior;

import com.pixel.map.object.MapObject;
import com.pixel.map.object.roads.RoadFactory;

public class IntersectionThreeNorthPlacement extends RoadPlacement {

	public IntersectionThreeNorthPlacement(MapObject owner) {
		super(owner);
	}

	@Override
	public void act() {

		if (otherRoad == null) {
		} else if (otherRoad.getType() == RoadFactory.RoadType.ROADWAY_NS ||
			   otherRoad.getType() == RoadFactory.RoadType.END_N) {
			transform(RoadFactory.RoadType.INTERSECT_4);
		}
	}
}
