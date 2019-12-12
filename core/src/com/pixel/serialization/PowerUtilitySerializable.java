package com.pixel.serialization;

import com.pixel.map.MapCoord;
import com.pixel.map.object.MapObject;
import com.pixel.map.object.building.special.SpecialtyBuildingFactory;
import com.pixel.map.object.building.special.utilities.power.CoalPowerPlant;
import com.pixel.map.object.building.special.utilities.water.WaterTank;

public class PowerUtilitySerializable extends SpecialtyBuildingSerializable {

	public float powerSupplied = 0;

	@Override
	public MapObject getNonSerializableObject() {

		// use the SpecialtyObjectFactory to create an empty object of this type
		CoalPowerPlant powerPlant = (CoalPowerPlant) SpecialtyBuildingFactory.getInstance().createEmpty(name);

		powerPlant.setX(x);
		powerPlant.setY(y);
		powerPlant.setMapPosition(new MapCoord(mapPositionX, mapPositionY));
		powerPlant.setName(name);

		if (powerPlant == null) {
			System.out.println("water plant was null");
		}
		return powerPlant;
	}
}
