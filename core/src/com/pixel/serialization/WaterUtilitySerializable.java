package com.pixel.serialization;

import com.pixel.map.object.MapObject;

public class WaterUtilitySerializable extends SpecialtyBuildingSerializable {

	public float waterSupplied = 0;

	@Override
	public MapObject getNonSerializableObject() {
		return null;
	}
}
