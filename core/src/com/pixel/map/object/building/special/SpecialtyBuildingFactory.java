package com.pixel.map.object.building.special;

import com.pixel.map.Map;

import java.util.ArrayList;
import java.util.HashMap;

public class SpecialtyBuildingFactory {

	private static SpecialtyBuildingFactory instance = new SpecialtyBuildingFactory();
	HashMap<String, SpecialtyBuilding> registeredObjects;

	private SpecialtyBuildingFactory() {}

	public static SpecialtyBuildingFactory getInstance() {
		return instance;
	}

	public void registerObject(SpecialtyBuilding building, String objectName) {

		synchronized (this) {

			registeredObjects.put(objectName, building);
		}
	}

	public SpecialtyBuilding create(String objectName, Map.MapCoord position) {

		synchronized (this) {
			if (registeredObjects.containsKey(objectName)) {

				// copy a new object of this type
				SpecialtyBuilding building = (SpecialtyBuilding) registeredObjects.get(objectName).copy();

				building.setMapPosition(position.x, position.y);
				return building;

			}
			return null;
		}
	}
}
