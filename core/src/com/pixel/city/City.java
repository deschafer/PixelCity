package com.pixel.city;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.pixel.event.EventManager;
import com.pixel.map.object.building.Building;
import com.pixel.map.object.building.special.ServiceBuilding;
import com.pixel.object.Resident;
import com.pixel.serialization.CitySerializable;

import java.io.Serializable;
import java.util.ArrayList;

public class City {

	public class Stats {
		public float wealth;
		public float education;
		public float happiness;
		public float total;
	}

	private final float amountEarnedPerLevel = 0.05f;

	private String name;			// the name of this city

	private int population = 0;					// the population of this city
	private float cityHappiness = 100.0f;
	private int incomingResidents = 100;			// count of residents that are waiting to live in this city
	private ArrayList<Resident> unemployedResidents;	// residents that are waiting for a job
	private ArrayList<Resident> unemployedEducatedResidents; // residents waiting for an office job

	private ArrayList<Building> cityBuildings;
	private ArrayList<Building> buildingsNeedPower;
	private ArrayList<Building> buildingsNeedWater;
	private ArrayList<Building> buildingsNeedFire;
	private ArrayList<Building> buildingsNeedPolice;
	private ArrayList<Building> buildingsNeedEd;
	private ArrayList<Building> buildingsNeedHealth;
	private ArrayList<Building> vacantBuildings;			// buildings that are open for residents
	private ArrayList<Building> hiringCommercialBuildings;	// commercial buildings that have jobs available
	private ArrayList<Building> hiringOfficeBuildings;	// office buildings that have jobs available

	private ArrayList<Resident> loadedInResidents;

	private int commercialRating = 0;
	private int officeRating = 0;

	private boolean commercialBoosted = true;

	private int residentNumber = 0;

	private float residentialDemandBoostTimer = 0;
	private float residentialDemandBoostTime = 15.0f;

	private float externalCommercialDemandTimer = 0;
	private static final float externalCommercialDemandTime = 5.0f;
	private int numberExternalWorkers = 0;
	private static final float externalWorkerToResidentRatio = 0.1f;

	private boolean buildingsVisible = true;

	private static City instance = new City("Default");

	private City(String name) {
		this.name = name;
		unemployedResidents = new ArrayList<>();
		cityBuildings = new ArrayList<>();
		vacantBuildings = new ArrayList<>();
		hiringCommercialBuildings = new ArrayList<>();
		hiringOfficeBuildings = new ArrayList<>();
		unemployedEducatedResidents = new ArrayList<>();
		loadedInResidents = new ArrayList<>();
		buildingsNeedPower = new ArrayList<>();
		buildingsNeedWater = new ArrayList<>();
		buildingsNeedHealth = new ArrayList<>();
		buildingsNeedPolice = new ArrayList<>();
		buildingsNeedFire = new ArrayList<>();
		buildingsNeedEd = new ArrayList<>();
	}

	public static City getInstance() {
		return instance;
	}

	public static void createFromSerializable(City city) {
		instance = city;
	}

	public static void reset(String name) {
		instance = new City(name);
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
			if (population - amount >= 0)
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

	public Stats calculateCityStats() {

		Stats stats = new Stats();
		stats.education = 0;
		stats.happiness = 0;
		stats.total = 0;
		stats.wealth = 0;
		int numberBuildingsEducated = 0;
		int totalEducatedCitizens = 0;
		float grossResidentWealth = 0;
		float cityWealth = FinancialManager.getInstance().getBalance() + FinancialManager.getInstance().getRevenue();
		float happiness = 0;

		if (cityBuildings.isEmpty()) {
			return stats;
		}

		// we have to visit every building and get information from it
		for (Building building : cityBuildings) {

			stats.happiness += building.getHappiness();

			// education score calculation
			// if this building has an education service provided,
			if (building.hasService(ServiceBuilding.Services.EDUCATION)) {
				numberBuildingsEducated++;
			}
			if (building.getType() == Building.BuildingType.COMMERCIAL) {
				// then this building only has educated residents, get the number
				totalEducatedCitizens += building.getActualNumberResidents();
			}

			// wealth score calculation
			grossResidentWealth += building.getLevel() * building.getActualNumberResidents();

			// happiness calculation
			happiness += building.getHappiness();
		}

		// get the average happiness
		stats.happiness = happiness / cityBuildings.size();

		// calculate the final educ. score
		stats.education = totalEducatedCitizens + numberBuildingsEducated / cityBuildings.size();

		// calculate the wealth of the city
		stats.wealth = cityWealth + grossResidentWealth;

		// calculate our total score
		stats.total = (stats.happiness / 100) * (stats.education + stats.wealth);

		return stats;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addLoadedInResident(Resident resident) {
		loadedInResidents.add(resident);
	}

	public CitySerializable getSerializableObject() {

		CitySerializable serializable = new CitySerializable();
		serializable.cityHappiness = cityHappiness;
		serializable.commercialRating = commercialRating;
		serializable.incomingResidents = incomingResidents;
		serializable.name = name;
		serializable.officeRating = officeRating;
		serializable.residentNumber = residentNumber;
		serializable.residentialDemandBoostTime = residentialDemandBoostTime;
		serializable.population = population;
		serializable.numberExternalWorkers = numberExternalWorkers;

		return serializable;
	}

	public void createFromSerializable(CitySerializable serializable) {

		name = serializable.name;
		cityHappiness = serializable.cityHappiness;
		commercialRating = serializable.commercialRating;
		incomingResidents = serializable.incomingResidents;
		officeRating = serializable.officeRating;
		residentialDemandBoostTime = serializable.residentNumber;
		residentialDemandBoostTime = serializable.residentialDemandBoostTime;
		population = serializable.population;
		numberExternalWorkers = serializable.numberExternalWorkers;
	}

	public ArrayList<Building> getCityBuildings() {
		return cityBuildings;
	}

	public ArrayList<Resident> getLoadedInResidents() {
		return loadedInResidents;
	}

	public void toggleBuildingsVisible() {

		buildingsVisible = !buildingsVisible;
		for (Building building : cityBuildings) {
			building.setVisible(buildingsVisible);
		}
	}

	public boolean isBuildingsVisible() {
		return buildingsVisible;
	}

	public void addBuildingNeedsService(EventManager.Categories category, Building building) {

		if (category == EventManager.Categories.FIRE) {
			buildingsNeedFire.add(building);
		} else if (category == EventManager.Categories.POLICE) {
			buildingsNeedPolice.add(building);
		} else if (category == EventManager.Categories.HEALTH) {
			buildingsNeedHealth.add(building);
		} else if (category == EventManager.Categories.ED) {
			buildingsNeedEd.add(building);
		} else if (category == EventManager.Categories.POWER) {
			buildingsNeedPower.add(building);
		} else if (category == EventManager.Categories.WATER) {
			buildingsNeedWater.add(building);
		}
	}

	public void removeBuildingNeedsService(EventManager.Categories category, Building building) {

		if (category == EventManager.Categories.FIRE) {
			buildingsNeedFire.remove(building);
		} else if (category == EventManager.Categories.POLICE) {
			buildingsNeedPolice.remove(building);
		} else if (category == EventManager.Categories.HEALTH) {
			buildingsNeedHealth.remove(building);
		} else if (category == EventManager.Categories.ED) {
			buildingsNeedEd.remove(building);
		} else if (category == EventManager.Categories.POWER) {
			buildingsNeedPower.remove(building);
		} else if (category == EventManager.Categories.WATER) {
			buildingsNeedWater.remove(building);
		}
	}

	public boolean buildingNeedsService(EventManager.Categories category) {

		if (category == EventManager.Categories.FIRE) {
			return !buildingsNeedFire.isEmpty();
		} else if (category == EventManager.Categories.POLICE) {
			return !buildingsNeedPolice.isEmpty();
		} else if (category == EventManager.Categories.HEALTH) {
			return !buildingsNeedHealth.isEmpty();
		} else if (category == EventManager.Categories.ED) {
			return !buildingsNeedEd.isEmpty();
		} else if (category == EventManager.Categories.POWER) {
			return !buildingsNeedPower.isEmpty();
		} else if (category == EventManager.Categories.WATER) {
			return !buildingsNeedWater.isEmpty();
		} else {
			return false;
		}
	}

	public ArrayList<Building> getBuildingsNeedFire() {
		return buildingsNeedFire;
	}

	public ArrayList<Building> getBuildingsNeedPolice() {
		return buildingsNeedPolice;
	}

	public ArrayList<Building> getBuildingsNeedEd() {
		return buildingsNeedEd;
	}

	public ArrayList<Building> getBuildingsNeedHealth() {
		return buildingsNeedHealth;
	}

	public ArrayList<Building> getBuildingsNeedPower() {
		return buildingsNeedPower;
	}

	public ArrayList<Building> getBuildingsNeedWater() {
		return buildingsNeedWater;
	}
}
