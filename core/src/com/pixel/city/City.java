package com.pixel.city;

import com.pixel.map.object.building.Building;
import com.pixel.object.Resident;

import java.util.ArrayList;

public class City {

	private String name;			// the name of this city

	private int population = 0;					// the population of this city
	private int incomingResidents = 100;			// count of residents that are waiting to live in this city
	private ArrayList<Resident> unemployedResidents;	// residents that are waiting for a job

	private ArrayList<Building> cityBuildings;
	private ArrayList<Building> vacantBuildings;			// buildings that are open for residents
	private ArrayList<Building> hiringCommercialBuildings;	// commercial buildings that have jobs available
	private ArrayList<Building> hiringOfficeBuildings;	// office buildings that have jobs available

	private static City instance = new City();

	private City() {
		unemployedResidents = new ArrayList<>();
		cityBuildings = new ArrayList<>();
		vacantBuildings = new ArrayList<>();
		hiringCommercialBuildings = new ArrayList<>();
		hiringOfficeBuildings = new ArrayList<>();
	}

	public static City getInstance() {
		return instance;
	}

	public void update() {

	}

	public void addVacantBuilding(Building building) {
		synchronized (this) {
			vacantBuildings.add(building);
		}
	}
	public void removeVacantBuilding(Building building) {
		synchronized (this) {
			vacantBuildings.remove(building);
		}
	}

	public void addHiringCommercialBuilding(Building building) {
		synchronized (this) {
			hiringCommercialBuildings.add(building);
		}
	}
	public void removeHiringCommercialBuilding(Building building) {
		synchronized (this) {
			hiringCommercialBuildings.remove(building);
		}
	}

	public void addHiringOfficeBuilding(Building building) {
		synchronized (this) {
			hiringOfficeBuildings.add(building);
		}
	}
	public void removeHiringOfficeBuilding(Building building) {
		synchronized (this) {
			hiringOfficeBuildings.remove(building);
		}
	}

	public void addCityBuilding(Building building) {
		synchronized (this) {
			cityBuildings.add(building);
		}
	}
	public void removeCityBuilding(Building building) {
		synchronized (this) {
			cityBuildings.remove(building);
		}
	}

	public void incrementPopulation() {
		synchronized (this) {
			population++;
		}
	}
	public void decrementPopulation() {
		synchronized (this) {
			population--;
		}
	}
	public int getPopulation() {
		synchronized (this) {
			return population;
		}
	}

	public void addIncomingResident() {
		synchronized (this) {
			incomingResidents++;
		}
	}
	public void removeIncomingResident() {
		synchronized (this) {
			incomingResidents--;
		}
	}
	public int getIncomingResidentsCount() {
		synchronized (this) {
			return incomingResidents;
		}
	}

	public void addUnemployedResident(Resident resident) {
		synchronized (this) {
			unemployedResidents.add(resident);
		}
	}
	public void removeUnemployedResident(Resident resident) {
		synchronized (this) {
			unemployedResidents.remove(resident);
		}
	}
	public int getUnemployedResidentCount() {
		synchronized (this) {
			return unemployedResidents.size();
		}
	}
}
