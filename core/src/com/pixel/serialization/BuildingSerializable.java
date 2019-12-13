package com.pixel.serialization;

import com.badlogic.gdx.math.Rectangle;
import com.pixel.city.City;
import com.pixel.city.FinancialManager;
import com.pixel.city.Financials.Source;
import com.pixel.map.MapCoord;
import com.pixel.map.object.MapObject;
import com.pixel.map.object.building.Building;
import com.pixel.map.object.building.BuildingFactory;
import com.pixel.object.Resident;

import java.util.ArrayList;

public class BuildingSerializable extends MapObjectSerializable {

	public Rectangle dimensions;	// refers to the coordinates in the map
	public Building.BuildingType type;               // the type of this building
	public int level;                       // the level of this object
	public ArrayList<ResidentSerializable> residents;   // list of people assoc'd with this object

	@Override
	public MapObject getNonSerializableObject() {

		// create a new building of the desired level and type
		Building building = BuildingFactory.getInstance().create(new MapCoord(mapPositionX, mapPositionY), type, level);

		for (SourceSerializable sourceSerializable : sources) {
			Source source = sourceSerializable.getNonSerializableObject(building);
			FinancialManager.getInstance().addSource(source);
		}

		// then we add the residents to this object
		for (ResidentSerializable residentSerializable : residents) {

			// if this is a residential building, then we create the resident. We don't want to create multiple residents,
			// so this is limited to only residential buildings
			if (type == Building.BuildingType.RESIDENTIAL) {

				// create a resident
				Resident resident = residentSerializable.getNonSerializableObject();
				building.addResident(resident);
				resident.setResidence(building);

				if (resident.isUnemployed()) {
					if (resident.isEducated()) {
						City.getInstance().addUnemployedEducatedResident(resident);
					} else {
						City.getInstance().addUnemployedResident(resident);
					}
				} else {
					City.getInstance().addLoadedInResident(resident);
				}
			}
		}

		City.getInstance().addCityBuilding(building);

		// adding this object to city building classes
		if (type == Building.BuildingType.RESIDENTIAL && !building.isFull()) {
			City.getInstance().addVacantBuilding(building);
		} else if (type == Building.BuildingType.COMMERCIAL && !building.isFull()) {
			City.getInstance().addHiringCommercialBuilding(building);
		} else if (type == Building.BuildingType.OFFICE && !building.isFull()) {
			City.getInstance().addHiringOfficeBuilding(building);
		}

		return building;
	}
}
