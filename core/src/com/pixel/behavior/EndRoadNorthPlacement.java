package com.pixel.behavior;

import com.pixel.map.object.MapObject;
import com.pixel.map.object.roads.RoadFactory;

public class EndRoadNorthPlacement extends RoadPlacement {

	public EndRoadNorthPlacement(MapObject owner) {
		super(owner);
	}

	@Override
	public void act() {

		if (otherRoad == null) {

		} else if (otherRoad.getType() == RoadFactory.RoadType.ROADWAY_NS ||
			   otherRoad.getType() == RoadFactory.RoadType.END_S) {
			// transform into a roadwayNorthSouth
			transform(RoadFactory.RoadType.ROADWAY_NS);
		}
	}
}
