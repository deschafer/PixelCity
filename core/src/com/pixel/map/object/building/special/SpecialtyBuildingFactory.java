package com.pixel.map.object.building.special;

import com.pixel.map.Map;
import java.util.HashMap;

public class SpecialtyBuildingFactory {

	private static SpecialtyBuildingFactory instance = new SpecialtyBuildingFactory();
	HashMap<String, SpecialtyBuilding> registeredObjects = new HashMap<>();

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

				// set the position of this object
				building.setMapPosition(position);

				// then we can have it initialize as needed
				building.initialize();

				return building;
			}
			return null;
		}
	}

	public SpecialtyBuilding createEmpty(String objectName) {
		synchronized (this) {
			if (registeredObjects.containsKey(objectName)) {

				// copy a new object of this type
				SpecialtyBuilding building = (SpecialtyBuilding) registeredObjects.get(objectName).copy();

				return building;
			}
			return null;
		}
	}
}
