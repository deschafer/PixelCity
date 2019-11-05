package com.pixel.city;

public class Demand {

	private int residentialDemand;		// set by the number of incoming residents waiting to come into this city
	private int commercialDemand;		// set by the number of unemployed citizens
	private int officeDemand;			// ..

	private int projectedResidentialDemand;
	private int projectedCommericalDemand;
	private int projectedOfficeDemand;

	private int residentialDemandSupplied = 0;
	private int commercialDemandSupplied = 0;
	private int officeDemandSupplied = 0;

	private static Demand instance = new Demand();

	private Demand() {}

	public static Demand getInstance() {
		return instance;
	}

	public boolean isResidentalDemanded() {
		synchronized (this) {
			return projectedResidentialDemand > 0;
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

	public void addSuppliedResidentialDemand(int demand) {
		synchronized (this) {
			residentialDemandSupplied += demand;
		}
	}
	public void removeSuppliedResidentialDemand(int demand) {
		synchronized (this) {
			residentialDemandSupplied -= demand;
		}
	}

	public void addSuppliedCommercialDemand(int demand) {
		synchronized (this) {
			commercialDemandSupplied += demand;
		}
	}
	public void removeSuppliedCommercialDemand(int demand) {
		synchronized (this) {
			commercialDemandSupplied -= demand;
		}
	}

	public void addSuppliedOfficeDemand(int demand) {
		synchronized (this) {
			officeDemandSupplied += demand;
		}
	}
	public void removeSuppliedOfficeDemand(int demand) {
		synchronized (this) {
			officeDemandSupplied -= demand;
		}
	}

	public void update() {

		synchronized (this) {
			// update each of our demands
			residentialDemand = City.getInstance().getIncomingResidentsCount();

			projectedResidentialDemand = residentialDemand - residentialDemandSupplied;

		}
	}


}
