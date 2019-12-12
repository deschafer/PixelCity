package com.pixel.serialization;

import com.pixel.map.MapCoord;
import com.pixel.map.object.MapObject;
import com.pixel.map.object.building.special.SpecialtyBuildingFactory;
import com.pixel.map.object.building.special.utilities.power.CoalPowerPlant;
import com.pixel.map.object.building.special.utilities.water.WaterTank;

public class WaterUtilitySerializable extends SpecialtyBuildingSerializable {

	public float waterSupplied = 0;

	@Override
	public MapObject getNonSerializableObject() {

		// use the SpecialtyObjectFactory to create an empty object of this type
		WaterTank waterTank = (WaterTank) SpecialtyBuildingFactory.getInstance().createEmpty(name);

		waterTank.setX(x);
		waterTank.setY(y);
		waterTank.setMapPosition(new MapCoord(mapPositionX, mapPositionY));
		waterTank.setName(name);

		if (waterTank == null) {
			System.out.println("water plant was null");
		}
		return waterTank;
	}
}
