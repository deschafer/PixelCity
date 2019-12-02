package com.pixel.city;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.pixel.map.object.building.Building;
import com.pixel.object.Resident;

import java.util.ArrayList;

public class City {

	private String name;			// the name of this city

	private int population = 0;					// the population of this city
	private float cityHappiness = 100.0f;
	private int incomingResidents = 100;			// count of residents that are waiting to live in this city
	private ArrayList<Resident> unemployedResidents;	// residents that are waiting for a job
	private ArrayList<Resident> unemployedEducatedResidents; // residents waiting for an office job

	private ArrayList<Building> cityBuildings;
	private ArrayList<Building> vacantBuildings;			// buildings that are open for residents
	private ArrayList<Building> hiringCommercialBuildings;	// commercial buildings that have jobs available
	private ArrayList<Building> hiringOfficeBuildings;	// office buildings that have jobs available

	private int commercialRating = 0;
	private int officeRating = 0;

	private boolean commercialBoosted = true;

	private int residentNumber = 0;

	private float residentialDemandBoostTimer = 0;
	private float residentialDemandBoostTime = 15.0f;

	private float externalCommercialDemandTimer = 0;
	private float externalCommercialDemandTime = 5.0f;
	private int numberExternalWorkers = 0;
	private float externalWorkerToResidentRatio = 0.1f;

	private static City instance = new City();

	private City() {
		unemployedResidents = new ArrayList<>();
		cityBuildings = new ArrayList<>();
		vacantBuildings = new ArrayList<>();
		hiringCommercialBuildings = new ArrayList<>();
		hiringOfficeBuildings = new ArrayList<>();
		unemployedEducatedResidents = new ArrayList<>();
	}

	public static City getInstance() {
		return instance;
	}

	public static void reset() {
		instance = new City();
	}

	public void update() {

		// here we put waiting residents into vacant buildings
		if(incomingResidents > 0 && !vacantBuildings.isEmpty()) {

			// get the first building in the list
			Building building = vacantBuildings.get(0);
			Resident res = null;

			// then add residents to this building
			while(building.addResident(res = new Resident("Citizen" + residentNumber++))) {
				// add to our total pop.
				population++;

				// add this building as this residents residence
				res.setResidence(building);

				// since this is a new resident, they will need a job
				unemployedResidents.add(res);

				// If we have ran out of residents to add, do not continue
				if(--incomingResidents == 0)
					break;
			}

			// added new residents, so we should update
			building.updateHappiness();

			if (building.isFull())
				vacantBuildings.remove(building);
		}

		// and we assign commercial jobs to unemployed citizens as well
		if(!hiringCommercialBuildings.isEmpty() && !unemployedResidents.isEmpty()) {

			// get the first building in the list
			Building building = hiringCommercialBuildings.get(0);
			Resident res = unemployedResidents.get(0);

			// adding residents to this building as employees until full
			while(building.addResident(res)) {

				// set this building as this residents employer
				res.setEmployer(building);

				// remove the last resident from the list
				unemployedResidents.remove(res);

				// verify we still have residents that are unemployed
				if(unemployedResidents.isEmpty())
					break;

				// tell the building of this previously unemployed resident to update its happiness
				if(res.getResidence() != null)
					res.getResidence().updateHappiness();

				// get a new resident
				res = unemployedResidents.get(0);
			}

			if (building.isFull())
				hiringCommercialBuildings.remove(building);

			// since we have added residents to this building, we should update happiness
			building.updateHappiness();
		}

		// and we assign office jobs to unemployed citizens as well
		if(!hiringOfficeBuildings.isEmpty() && !unemployedEducatedResidents.isEmpty()) {

			// get the first building in the list
			Building building = hiringOfficeBuildings.get(0);
			Resident res = unemployedEducatedResidents.get(0);

			// adding residents to this building as employees until full
			while(building.addResident(res)) {

				// set this building as this residents employer
				res.setEmployer(building);

				// remove the last resident from the list
				unemployedEducatedResidents.remove(res);

				// verify we still have residents that are unemployed
				if(unemployedEducatedResidents.isEmpty())
					break;

				// tell the building of this previously unemployed resident to update its happiness
				if(res.getResidence() != null)
					res.getResidence().updateHappiness();

				// get a new resident
				res = unemployedEducatedResidents.get(0);
			}

			if (building.isFull())
				hiringOfficeBuildings.remove(building);

			// since we have added residents to this building, we should update happiness
			building.updateHappiness();
		}

		if(!cityBuildings.isEmpty()) {
			int happiness = 0;

			// update the city's total happiness
			for (Building building : cityBuildings) {
				happiness += building.getHappiness();
			}

			// then get the mean of all of these individual happiness values
			cityHappiness = happiness /= cityBuildings.size();
		}


		// adding incoming citizens to the city
		if (incomingResidents == 0
			   && unemployedResidents.isEmpty()
			   && !hiringCommercialBuildings.isEmpty()) {

			int total = 0;

			// add up all of the available jobs
			for(Building building : hiringCommercialBuildings) {
				total += building.getSpace();
			}

			incomingResidents = total;
		}

		// adding incoming citizens to the city
		if (incomingResidents == 0
			   && unemployedEducatedResidents.isEmpty()
			   && !hiringOfficeBuildings.isEmpty()) {

			int total = 0;

			// add up all of the available jobs
			for(Building building : hiringOfficeBuildings) {
				total += building.getSpace();
			}

			incomingResidents = total;
		}

		residentialDemandBoostTimer += Gdx.graphics.getDeltaTime();

		// handle residential demand waves
		if (residentialDemandBoostTimer >= residentialDemandBoostTime && incomingResidents < 100) {

			residentialDemandBoostTimer = 0;
			residentialDemandBoostTime += 0.01f * population;

			float boost = (0.1f * population) * (cityHappiness / 100);

			incomingResidents += (int)boost;

			System.out.println("Residential boosted " + boost);
		}

		externalCommercialDemandTimer += Gdx.graphics.getDeltaTime();

		// handle external commercial demand
		if(externalCommercialDemandTimer >= externalCommercialDemandTime) {
			externalCommercialDemandTimer = 0;
			externalCommercialDemandTimer += 1.0f;

			int commercialRatio = MathUtils.round(externalWorkerToResidentRatio * commercialRating);
			int officeRatio = MathUtils.round(externalWorkerToResidentRatio * officeRating);

			// if we need more to meet the 10% ratio
			if (numberExternalWorkers < commercialRatio && commercialBoosted) {

				commercialBoosted = false;

				int difference = commercialRatio - numberExternalWorkers;

				for (int i = 0; i < difference; i++) {
					Resident resident = new Resident("ExternalWorker" + numberExternalWorkers);
					addUnemployedResident(resident);
					numberExternalWorkers++;
				}

				System.out.println(difference + " commercial workers added");
			}
		}
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
	public void decrementPopulation(int amount) {
		synchronized (this) {
			population -= amount;
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
	public void addIncomingResidents(int count) {
		synchronized (this) {
			incomingResidents += count;
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

	public int getAvailableJobs() {
		synchronized (this) {
			int total = 0;
			for(Building building : hiringCommercialBuildings) {
				total += building.getSpace();
			}

			for(Building building : hiringOfficeBuildings) {
				total += building.getSpace();
			}

			return total;
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

	public void addUnemployedEducatedResident(Resident resident) {
		synchronized (this) {
			unemployedEducatedResidents.add(resident);
		}
	}
	public void removeUnemployedEducatedResident(Resident resident) {
		synchronized (this) {
			unemployedEducatedResidents.remove(resident);
		}
	}
	public int getUnemployedEducatedResidentCount() {
		synchronized (this) {
			return unemployedEducatedResidents.size();
		}
	}

	public void addCommercialRating(int addition) {
		synchronized (this) {
			commercialRating += addition;
		}
	}
	public void removeCommercialRating(int removal) {
		synchronized (this) {
			commercialRating -= removal;
		}
	}

	public void addOfficeRating(int addition) {
		synchronized (this) {
			officeRating += addition;
		}
	}
	public void removeOfficeRating(int removal) {
		synchronized (this) {
			officeRating -= removal;
		}
	}
}
