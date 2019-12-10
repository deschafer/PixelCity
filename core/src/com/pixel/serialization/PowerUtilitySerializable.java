package com.pixel.serialization;

import com.pixel.map.object.MapObject;
import com.pixel.map.object.building.special.SpecialtyBuildingFactory;
import com.pixel.map.object.building.special.utilities.power.CoalPowerPlant;

public class PowerUtilitySerializable extends SpecialtyBuildingSerializable {

	public float powerSupplied = 0;

	@Override
	public MapObject getNonSerializableObject() {

		// since we only have the one power utility at the moment, we can go ahead
		// and create the coal power plant utility from the data in this object

		// use the SpecialtyObjectFactory to create an empty object of this type
		CoalPowerPlant powerPlant = (CoalPowerPlant)SpecialtyBuildingFactory.getInstance().createEmpty(name);

		if (powerPlant == null) {
			System.out.println("power plant was null");
			return null;
		}

		return null;
	}
}
