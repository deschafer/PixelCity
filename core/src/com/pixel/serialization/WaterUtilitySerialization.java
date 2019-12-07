package com.pixel.serialization;

import com.pixel.map.object.MapObject;

public class WaterUtilitySerialization extends SpecialtyBuildingSerializable {

	public float waterSupplied = 0;

	@Override
	public MapObject getNonSerializableObject() {
		return null;
	}
}
