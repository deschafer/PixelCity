package com.pixel.serialization;

public class WaterUtilitySerialization extends SpecialtyBuildingSerializable {

	public float waterSupplied = 0;

	@Override
	public MapObjectSerializable getNonSerializableObject() {
		return null;
	}
}
