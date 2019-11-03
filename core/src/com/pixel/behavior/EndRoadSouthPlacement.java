package com.pixel.behavior;

import com.pixel.map.object.MapObject;
import com.pixel.map.object.roads.RoadFactory;

public class EndRoadSouthPlacement extends RoadPlacement {

	public EndRoadSouthPlacement(MapObject owner) {
		super(owner);
	}

	@Override
	public void act() {

		if (otherRoad == null) {
		} else if (otherRoad.getType() == RoadFactory.RoadType.ROADWAY_EW) {
			transform(RoadFactory.RoadType.INTERSECT_3_S);
		} else if (otherRoad.getType() == RoadFactory.RoadType.ROADWAY_NS ||
			   otherRoad.getType() == RoadFactory.RoadType.END_N) {
			transform(RoadFactory.RoadType.ROADWAY_NS);
		} else if (otherRoad.getType() == RoadFactory.RoadType.END_E) {
			transform(RoadFactory.RoadType.CORNER_EN);
		} else if (otherRoad.getType() == RoadFactory.RoadType.END_W) {
			transform(RoadFactory.RoadType.CORNER_WN);
		}
	}
}
