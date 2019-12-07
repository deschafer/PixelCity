package com.pixel.serialization;

import com.pixel.map.object.MapObject;
import com.pixel.map.object.roads.RoadFactory;

public class RoadSerializable extends MapObjectSerializable {

	public RoadFactory.RoadType type;     // the actual type assoc with this object

	@Override
	public MapObject getNonSerializableObject() {
		return null;
	}
}
