package com.pixel.city;

import com.pixel.map.object.building.Building;

public class Demand {

	private int residentialDemand;		// set by the number of incoming residents waiting to come into this city
	private int commercialDemand;		// set by the number of unemployed citizens
	private int officeDemand;			// ..

	private static Demand instance = new Demand();

	private Demand() {}

	public static Demand getInstance() {
		return instance;
	}

	public boolean isTypeDemanded(Building.BuildingType type) {
		synchronized (this) {
			if (type == Building.BuildingType.RESIDENTIAL) {
				return residentialDemand > 0;
			} else if (type == Building.BuildingType.COMMERCIAL) {
				return commercialDemand > 0;
			} else {
				return officeDemand > 0;
			}
		}
	}

	public void update() {

		synchronized (this) {
			// update each of our demands

			residentialDemand = City.getInstance().getIncomingResidentsCount();

			commercialDemand = City.getInstance().getUnemployedResidentCount();

			officeDemand = City.getInstance().getUnemployedEducatedResidentCount();
		}
	}

	public int getCommercialDemand() {
		return commercialDemand;
	}

	public int getOfficeDemand() {
		return officeDemand;
	}

	public int getResidentialDemand() {
		return residentialDemand;
	}

	public void print() {
		System.out.println("Actual Demands R C O:" + residentialDemand + " " + commercialDemand + " " + officeDemand);
	}
}
