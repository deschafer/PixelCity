package com.pixel.behavior;

import com.pixel.map.object.MapObject;
import com.pixel.map.object.roads.RoadFactory;

public class EndRoadEastPlacement extends RoadPlacement {
	public EndRoadEastPlacement(MapObject owner) {
		super(owner);
	}

	@Override
	public void act() {

		if (otherRoad == null) {
		} else if (otherRoad.getType() == RoadFactory.RoadType.ROADWAY_NS) {
			transform(RoadFactory.RoadType.INTERSECT_3_W);
		} else if (otherRoad.getType() == RoadFactory.RoadType.ROADWAY_EW ||
			   otherRoad.getType() == RoadFactory.RoadType.END_W) {
			transform(RoadFactory.RoadType.ROADWAY_EW);
		} else if (otherRoad.getType() == RoadFactory.RoadType.END_N) {
			transform(RoadFactory.RoadType.CORNER_NW);
		} else if (otherRoad.getType() == RoadFactory.RoadType.END_S) {
			transform(RoadFactory.RoadType.CORNER_EN);
		}
	}
}
