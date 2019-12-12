package com.pixel.serialization;

import com.pixel.city.FinancialManager;
import com.pixel.city.Financials.Source;
import com.pixel.map.MapCoord;
import com.pixel.map.object.MapObject;
import com.pixel.map.object.building.special.ServiceBuilding;
import com.pixel.map.object.building.special.SpecialtyBuilding;
import com.pixel.map.object.building.special.SpecialtyBuildingFactory;
import com.pixel.scene.GameScene;

public class ServiceBuildingSerializable extends SpecialtyBuildingSerializable {

	public ServiceBuilding.Services serviceType;

	@Override
	public MapObject getNonSerializableObject() {

		SpecialtyBuilding building = null;

		// depending on the service type, we create a new service building
		if (serviceType == ServiceBuilding.Services.FIRE) {
			building = SpecialtyBuildingFactory.getInstance().create("FireStation", new MapCoord(mapPositionX, mapPositionY));
		} else if (serviceType == ServiceBuilding.Services.HEALTH) {
			building = SpecialtyBuildingFactory.getInstance().create("Hospital", new MapCoord(mapPositionX, mapPositionY));
		} else if (serviceType == ServiceBuilding.Services.EDUCATION) {
			building = SpecialtyBuildingFactory.getInstance().create("SecondarySchool", new MapCoord(mapPositionX, mapPositionY));
		} else if (serviceType == ServiceBuilding.Services.POLICE) {
			building = SpecialtyBuildingFactory.getInstance().create("PoliceStation", new MapCoord(mapPositionX, mapPositionY));
		}

		building.setX(x);
		building.setY(y);
		building.setMapPosition(mapPositionX, mapPositionY);
		building.setWidth(width);
		building.setHeight(height);
		building.setName(name);

		for (SourceSerializable sourceSerializable : sources) {
			Source source = sourceSerializable.getNonSerializableObject(building);
			FinancialManager.getInstance().addSource(source);
		}

		GameScene.getInstance().getGameMap().addLoadedServiceBuilding((ServiceBuilding)building);

		return building;
	}
}
