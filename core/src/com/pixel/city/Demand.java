package com.pixel.city;

public class Demand {

	private int residentialDemand;		// set by the number of incoming residents waiting to come into this city
	private int commercialDemand;		// set by the number of unemployed citizens
	private int officeDemand;			// ..

	private static Demand instance = new Demand();

	private Demand() {}

	public static Demand getInstance() {
		return instance;
	}

	public boolean isResidentalDemanded() {
		synchronized (this) {
			return residentialDemand > 0;
		}
	}

	public boolean isCommercialDemanded() {
		synchronized (this) {
			return commercialDemand > 0;
		}
	}

	public boolean isOfficeDemanded() {
		synchronized (this) {
			return officeDemand > 0;
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

	public void print() {
		System.out.println("Actual Demands R C O:" + residentialDemand + " " + commercialDemand + " " + officeDemand);
	}
}
