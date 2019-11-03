package com.pixel.behavior;

import com.pixel.map.object.MapObject;
import com.pixel.map.object.roads.RoadFactory;

public class CornerNorthEastPlacement extends RoadPlacement {

	public CornerNorthEastPlacement(MapObject owner) {
		super(owner);
	}

	@Override
	public void act() {

		if (otherRoad == null) {
		} else if (otherRoad.getType() == RoadFactory.RoadType.ROADWAY_NS ||
			   otherRoad.getType() == RoadFactory.RoadType.END_S) {
			transform(RoadFactory.RoadType.INTERSECT_3_E);
		} else if (otherRoad.getType() == RoadFactory.RoadType.ROADWAY_EW ||
			   otherRoad.getType() == RoadFactory.RoadType.END_E) {
			// we create an intersection
			transform(RoadFactory.RoadType.INTERSECT_3_S);
		}
	}
}
