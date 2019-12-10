package com.pixel.serialization;

import com.pixel.map.object.MapObject;
import com.pixel.map.object.roads.Road;
import com.pixel.map.object.roads.RoadFactory;

public class RoadSerializable extends MapObjectSerializable {

	public int type;     // the actual type assoc with this object

	@Override
	public MapObject getNonSerializableObject() {

		Road road;

		RoadFactory.RoadType roadType = RoadFactory.getInstance().getRoadTypeFromValue(type);
		road = RoadFactory.getInstance().getRoad(roadType);
		return road;
	}
}
