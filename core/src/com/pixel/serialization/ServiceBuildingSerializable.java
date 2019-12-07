package com.pixel.serialization;

import com.pixel.map.object.MapObject;
import com.pixel.map.object.building.special.ServiceBuilding;

public class ServiceBuildingSerializable extends SpecialtyBuildingSerializable {

	public ServiceBuilding.Services serviceType;

	@Override
	public MapObject getNonSerializableObject() {
		return null;
	}
}
