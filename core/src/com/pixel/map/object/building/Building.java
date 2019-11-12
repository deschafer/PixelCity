package com.pixel.map.object.building;

import com.pixel.city.City;
import com.pixel.city.Demand;
import com.pixel.city.Financials.Source;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;
import com.pixel.map.object.Cell;
import com.pixel.map.object.MapObject;
import com.pixel.map.object.building.BuildingDisplay.BuildingDisplay;
import com.pixel.object.Resident;
import com.pixel.scene.GameScene;

import java.util.ArrayList;

public class Building extends MapObject {

	public enum BuildingType {
		RESIDENTIAL(PixelAssetManager.residentialZoning, "ResidentialZoning"),
		COMMERCIAL(PixelAssetManager.commercialZoning, "CommercialZoning"),
		OFFICE(PixelAssetManager.officeZoning, "OfficeZone");

		private String zoneTexture;
		private String zoneName;

		BuildingType(String zoneTexture, String zoneName) {
			this.zoneTexture = zoneTexture;
			this.zoneName = zoneName;
		}

		public String getZoneTexture() {
			return zoneTexture;
		}

		public String getZoneName() {
			return zoneName;
		}
	}

	public static final int residentLevelOfficeRequirement = 5;

	private BuildingType type;               // the type of this building
	private int level;                       // the level of this object
	private int numberResidents = 0;         // the number of residents
	private float happiness;				 // the happiness of the residents of this building
	private ArrayList<Resident> residents;   // list of people assoc'd with this object
	private boolean building = true;		 // the current object is being built
	private ArrayList<BuildingDisplay> drawableComponents;	// the visual components of this object
	private float buildingTimer = 0;
	private float buildTime = 1.0f;
	private boolean containsUnemployed = true;

	private float incomePerResident = 0;

	// levelling data
	private float levelUpTimer = 0;		// timer used to keep track of the time of level up time
	//private float levelUpTime = 25.0f;		// time needed to pass for this object to level up
	private float levelUpTime = 5.0f;		// time needed to pass for this object to level up
	private boolean levelTimerActive = false;	// indicates if we counting down to level up
	private float happinessRequired;		// happiness required to start level up timer

	public Building(Map.MapCoord coord, String ID, BuildingType type, int level, int numberResidents,
				 float happinessRequired, float levelUpTime, float buildTime, float incomePerResident) {
		super(0, 0, GameScene.getInstance().getGameMap().getCellWidth(),
			   GameScene.getInstance().getGameMap().getCellHeight(), coord, ID);

		this.type = type;
		this.level = level;
		this.numberResidents = numberResidents;
		this.levelUpTime = levelUpTime;
		this.buildTime = buildTime;
		this.happinessRequired = happinessRequired;
		this.incomePerResident = incomePerResident;
		residents = new ArrayList<>();
		drawableComponents = new ArrayList<>();
		happiness = 50.0f;

		replaceable = false;

		Source income = new Source(this ,residents.size() * incomePerResident * happiness / 100.0f);
		addSource(income);

		// then we need to add this building to the vacant buildings, as it can now accept residents
		if(type == BuildingType.RESIDENTIAL)
			City.getInstance().addVacantBuilding(this);
		else if (type == BuildingType.COMMERCIAL) {
			City.getInstance().addHiringCommercialBuilding(this);
			City.getInstance().addCommercialRating(numberResidents);
		}
		else if (type == BuildingType.OFFICE)
			City.getInstance().addHiringOfficeBuilding(this);

		// then also add it to the list of all buildings
		City.getInstance().addCityBuilding(this);
	}

	public void addBuildingDisplay(BuildingDisplay display) {
		drawableComponents.add(display);
		addActor(display);

		int width = (int) drawableComponents.get(0).getWidth();
		int height = (int) drawableComponents.get(0).getHeight();

		// add build time for each component
		buildTime += 1.0f;

		// if there is a story present
		if (drawableComponents.size() > 2) {

			display.setY(72 + 34 * (drawableComponents.size() - 2));
			display.setX((width - display.getWidth()) / 2);

		} else if (drawableComponents.size() > 1) {
			display.setY(72);
			display.setX((width - display.getWidth()) / 2);
		}

	}

	public boolean addResident(Resident resident) {

		synchronized (this) {

			if (residents.size() < numberResidents) {
				residents.add(resident);

				for(Source source : sources) {
					source.setChange(residents.size() * incomePerResident * happiness / 100.0f);
				}

				return true;
			}
			return false;
		}
	}

	public float getHappiness() {
		return happiness;
	}

	public int getNumberResidents() {
		return numberResidents;
	}

	public boolean isFull() {
		return numberResidents == residents.size();
	}

	public int getSpace() {
		return numberResidents - residents.size();
	}

	@Override
	public void act(float dt) {
		super.act(dt);

		synchronized (this) {

			// If we are in the build state
			if (building) {

				buildingTimer += dt;

				// visualize that this building is being built
				setOpacity(0.50f);

				if (buildingTimer >= buildTime) {

					// we are no longer building
					building = false;

					// This building is built, so visualize it normally
					setOpacity(1);
				}
			}
			// If this building has been built
			else {
				// we need to check if any of our residents do not have a job

				if(type == BuildingType.COMMERCIAL) {
					int nop = 0;
				}

				// Level up requirements (to start the level timer)
				// We must have happiness above a certain point
				// this building must be full
				// all employees must have jobs
				if (happiness >= happinessRequired && isFull() &&
					   !containsUnemployedResidents()) {

					// then we can start our timer
					levelTimerActive = true;

					// add elapsed time to our timer
					levelUpTimer += dt;

					// if enough time has take place
					if(levelUpTimer >= levelUpTime) {

						System.out.println("Attempting to level up");

						// then we can finally level up
						levelUp();
					}

				}
				// Then we started the timer, but our requirements are no longer met
				else if (levelTimerActive) {
					levelTimerActive = false;
					levelUpTimer = 0;
				}


			}
		}
	}

	//
	// levelUp()
	//
	//
	private void levelUp() {

		// we first get the cell of this building
		Cell cell = GameScene.getInstance().getGameMap().getCell(getMapPosition());

		// then we create a new building of this type with this level + 1
		Building levelledBuilding = BuildingFactory.getInstance().create(getMapPosition(), type, level + 1);

		// then we need to transfer all residents from this object to the other building
		for(Resident resident : residents) {

			// add the resident to the new building
			levelledBuilding.addResident(resident);

			// also add to the level of this resident
			resident.levelUp();

			// If this citizen has become educated, then it needs an office job
			if(resident.getLevel() >= residentLevelOfficeRequirement &&
				type != BuildingType.OFFICE) {

				// then this resident needs a higher level job, so set it as unemployed
				resident.setEmployer(null);
				City.getInstance().addUnemployedEducatedResident(resident);
			}

			// we also need to set the reference from the resident to this building
			if(type == BuildingType.RESIDENTIAL) {
				resident.setResidence(levelledBuilding);
			} else {
				resident.setEmployer(levelledBuilding);
			}
		}
		residents.clear();

		// remove this object from the map
		cell.removeActor(this);
		remove();

		// add the new building to the map
		cell.addMapObject(levelledBuilding);

		// make changes to the city lists
		// We know this building was not vacant, and that it was full,
		// so it must only be removed from the city buildings list
		City.getInstance().removeCityBuilding(this);
		City.getInstance().addCityBuilding(levelledBuilding);

		if(type == BuildingType.COMMERCIAL) {
			City.getInstance().removeCommercialRating(numberResidents);
		}
	}

	//
	// updateHappiness()
	// Called when we have added a service, or a new employed citizen
	//
	public void updateHappiness() {

		int employed = 0;

		// first get the number of employed residents over total
		for(Resident resident : residents) {
			if(!resident.isUnemployed())
				employed++;
		}

		float citizenHappiness = employed / numberResidents;

		if(citizenHappiness == 1.0f)
			containsUnemployed = false;

		// TODO: implement services influencing happiness
		// then we determine how happy the building is
		// this will be dependent on how many services are provided
		// since this is not applicable right now, we just set it as 100%
		float buildingHappiness = 1.0f;

		// each of these two values has equal weight, so..
		happiness = (citizenHappiness + buildingHappiness) / 2;
		happiness *= 100;

		for(Source source : sources) {
			source.setChange(residents.size() * incomePerResident * happiness / 100.0f);
		}
	}

	public boolean containsUnemployedResidents() {

		if(type == BuildingType.COMMERCIAL ||
			   type == BuildingType.OFFICE)
			return !isFull();

		return containsUnemployed;
	}
}
