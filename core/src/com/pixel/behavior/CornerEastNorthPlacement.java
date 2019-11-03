package com.pixel.behavior;

import com.pixel.map.object.MapObject;
import com.pixel.map.object.roads.RoadFactory;

public class CornerEastNorthPlacement extends RoadPlacement {

	public CornerEastNorthPlacement(MapObject owner) {
		super(owner);
	}

	@Override
	public void act() {

		if (otherRoad == null) {
		} else if (otherRoad.getType() == RoadFactory.RoadType.ROADWAY_NS ||
			   otherRoad.getType() == RoadFactory.RoadType.END_N) {
			transform(RoadFactory.RoadType.INTERSECT_3_W);
		} else if (otherRoad.getType() == RoadFactory.RoadType.ROADWAY_EW ||
			   otherRoad.getType() == RoadFactory.RoadType.END_W) {
			// we create an intersection
			transform(RoadFactory.RoadType.INTERSECT_3_N);
		}
	}
}
