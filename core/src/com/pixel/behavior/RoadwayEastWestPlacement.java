package com.pixel.behavior;

import com.pixel.map.object.MapObject;
import com.pixel.map.object.roads.RoadFactory;

public class RoadwayEastWestPlacement extends RoadPlacement {

	public RoadwayEastWestPlacement(MapObject owner) {
		super(owner);
	}

	@Override
	public void act() {

		if (otherRoad == null) {
		} else if (otherRoad.getType() == RoadFactory.RoadType.ROADWAY_NS) {
			transform(RoadFactory.RoadType.INTERSECT_4);
		} else if (otherRoad.getType() == RoadFactory.RoadType.END_N) {
			transform(RoadFactory.RoadType.INTERSECT_3_S);
		} else if (otherRoad.getType() == RoadFactory.RoadType.END_S) {
			transform(RoadFactory.RoadType.INTERSECT_3_N);
		}
	}
}
