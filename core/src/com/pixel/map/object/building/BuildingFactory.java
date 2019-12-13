package com.pixel.map.object.building;

import com.badlogic.gdx.math.Rectangle;
import com.pixel.city.City;
import com.pixel.map.Map;
import com.pixel.map.MapCoord;
import com.pixel.map.object.building.display.BuildingDisplay;
import com.pixel.scene.GameScene;
import com.pixel.serialization.BuildingSerializable;
import com.pixel.serialization.ResidentSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class BuildingFactory {

	public enum SpecificBuildingTypes {
		TENEMENT("Tenement"),
		BLUE_SKYSCRAPER("BlueSkyscraper");

		private String name;

		SpecificBuildingTypes(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	private static BuildingFactory instance = new BuildingFactory();

	private final int numberResidentsPerLevel = 10;
	private final int minimumResidents = 10;

	private final float minHappinessRequired = 80.0f;
	private final float happinessRequiredPerLevel = 2.5f;
	private final float maxHappinessRequired = 97.5f;

	private final float buildTimePerLevel = 1.5f;
	private final float buildTime = 1.0f;

	private final float residentialBaseIncomePerResident = 0.05f;
	private final float residentialLevelIncomeBonusPerResident = 0.025f;
	private final float commercialBaseIncomePerResident = 0.10f;
	private final float commercialLevelIncomeBonusPerResident = 0.05f;
	private final float officeBaseIncomePerResident = 0.15f;
	private final float officeLevelIncomeBonusPerResident = 0.10f;

	//private final float percentageSpecificBuildings = 0.30f;
	private final float percentageSpecificBuildings = 0.0f;

	private final float basePowerNeeded = 30;
	private final float powerPerLevel = 50;

	private final float baseWaterNeeded = 10;
	private final float waterPerLevel = 15;

	//private final float minLevelUpTime = 5.0f;
	//private final float timePerLevel = 2.0f;
	private final float minLevelUpTime = 1.0f;
	private final float timePerLevel = .0f;

	private Random random = new Random();

	// lists for residential types
	private ArrayList<BuildingDisplay> residentialBases;
	private ArrayList<BuildingDisplay> residentialStories;
	private ArrayList<BuildingDisplay> residentialRoofs;

	// lists for commercial types
	private ArrayList<BuildingDisplay> commercialBases;
	private ArrayList<BuildingDisplay> commercialStories;
	private ArrayList<BuildingDisplay> commercialRoofs;

	// lists for office types
	private ArrayList<BuildingDisplay> officeBases;
	private ArrayList<BuildingDisplay> officeStories;
	private ArrayList<BuildingDisplay> officeRoofs;

	// lists for specific residential buildings
	private ArrayList<String> specificResidentialNames;
	private HashMap<String, Rectangle> specificResidentialDimensions;
	private HashMap<String, BuildingDisplay> specificResidentialBases;
	private HashMap<String, BuildingDisplay> specificResidentialStories;
	private HashMap<String, BuildingDisplay> specificResidentialRoofs;

	// lists for specific commercial buildings
	private ArrayList<String> specificCommercialNames;
	private HashMap<String, Rectangle> specificCommercialDimensions;
	private HashMap<String, BuildingDisplay> specificCommercialBases;
	private HashMap<String, BuildingDisplay> specificCommercialStories;
	private HashMap<String, BuildingDisplay> specificCommercialRoofs;

	// lists for specific office buildings
	private ArrayList<String> specificOfficeNames;
	private HashMap<String, Rectangle> specificOfficeDimensions;
	private HashMap<String, BuildingDisplay> specificOfficeBases;
	private HashMap<String, BuildingDisplay> specificOfficeStories;
	private HashMap<String, BuildingDisplay> specificOfficeRoofs;


	private BuildingFactory() {
		residentialBases = new ArrayList<>();
		residentialStories = new ArrayList<>();
		residentialRoofs = new ArrayList<>();

		commercialBases = new ArrayList<>();
		commercialStories = new ArrayList<>();
		commercialRoofs = new ArrayList<>();

		officeBases = new ArrayList<>();
		officeStories = new ArrayList<>();
		officeRoofs = new ArrayList<>();

		specificResidentialNames = new ArrayList<>();
		specificResidentialDimensions = new HashMap<>();
		specificResidentialBases = new HashMap<>();
		specificResidentialStories = new HashMap<>();
		specificResidentialRoofs = new HashMap<>();

		specificCommercialNames = new ArrayList<>();
		specificCommercialDimensions = new HashMap<>();
		specificCommercialBases = new HashMap<>();
		specificCommercialStories = new HashMap<>();
		specificCommercialRoofs = new HashMap<>();

		specificOfficeNames = new ArrayList<>();
		specificOfficeDimensions = new HashMap<>();
		specificOfficeBases = new HashMap<>();
		specificOfficeStories = new HashMap<>();
		specificOfficeRoofs = new HashMap<>();
	}

	public static BuildingFactory getInstance() {
		return instance;
	}

	public Building create(MapCoord position, Building.BuildingType type, int level) {

		// needs to return a specific or generic building
		int chance = (int)(random.nextInt(100));
		boolean specificBuildingCreated = false;

		if (chance <= percentageSpecificBuildings * 100) {
			specificBuildingCreated = true;
		}

		if (specificBuildingCreated) {

			String buildingName = "empty";

			// handle all of our building type specific operations here
			if (type == Building.BuildingType.RESIDENTIAL && !specificResidentialNames.isEmpty()) {
				buildingName = specificResidentialNames.get(random.nextInt(specificResidentialNames.size()));
			} else if (type == Building.BuildingType.COMMERCIAL && !specificCommercialNames.isEmpty()) {
				buildingName = specificCommercialNames.get(random.nextInt(specificCommercialNames.size()));
			} else if (type == Building.BuildingType.OFFICE && !specificOfficeNames.isEmpty()) {
				buildingName = specificOfficeNames.get(random.nextInt(specificOfficeNames.size()));
			}

			Building building = createSpecificBuilding(buildingName, position, type, level);

			// only return this if a building was created
			if (building != null) {
				return building;
			}
		}

		return createGenericBuilding(position, type, level);
	}

	public void registerBuildingDisplayBase(BuildingDisplay display, boolean residential, boolean commercial, boolean office) {

		if(residential) {
			residentialBases.add(display);
		}
		if(commercial) {
			commercialBases.add(display);
		}
		if(office) {
			officeBases.add(display);
		}
	}

	public void registerBuildingDisplayStory(BuildingDisplay display, boolean residential, boolean commercial, boolean office) {

		if(residential) {
			residentialStories.add(display);
		}
		if(commercial) {
			commercialStories.add(display);
		}
		if(office) {
			officeStories.add(display);
		}
	}

	public void registerBuildingDisplayRoof(BuildingDisplay display, boolean residential, boolean commercial, boolean office) {

		if(residential) {
			residentialRoofs.add(display);
		}
		if(commercial) {
			commercialRoofs.add(display);
		}
		if(office) {
			officeRoofs.add(display);
		}
	}

	public void registerSpeceficBuildingDisplayBase(BuildingDisplay display, String name, Building.BuildingType type, Rectangle dimensions) {

		if (type == Building.BuildingType.RESIDENTIAL) {
			specificResidentialNames.add(name);
			specificResidentialDimensions.put(name, dimensions);
			specificResidentialBases.put(name, display);
		} else if (type == Building.BuildingType.COMMERCIAL) {
			specificCommercialNames.add(name);
			specificCommercialDimensions.put(name, dimensions);
			specificCommercialBases.put(name, display);
		} else if (type == Building.BuildingType.OFFICE) {
			specificOfficeNames.add(name);
			specificOfficeDimensions.put(name, dimensions);
			specificOfficeBases.put(name, display);
		}
	}

	public void registerSpeceficBuildingDisplayStory(BuildingDisplay display, String name, Building.BuildingType type) {

		if (type == Building.BuildingType.RESIDENTIAL) {
			specificResidentialStories.put(name, display);
		} else if (type == Building.BuildingType.COMMERCIAL) {
			specificCommercialStories.put(name, display);
		} else if (type == Building.BuildingType.OFFICE) {
			specificOfficeStories.put(name, display);
		}
	}

	public void registerSpeceficBuildingDisplayRoof(BuildingDisplay display, String name, Building.BuildingType type) {

		if (type == Building.BuildingType.RESIDENTIAL) {
			specificResidentialRoofs.put(name, display);
		} else if (type == Building.BuildingType.COMMERCIAL) {
			specificCommercialRoofs.put(name, display);
		} else if (type == Building.BuildingType.OFFICE) {
			specificOfficeRoofs.put(name, display);
		}
	}

	private ArrayList<BuildingDisplay> getBases(Building.BuildingType type) {
		switch (type) {
			case RESIDENTIAL:
				return residentialBases;
			case OFFICE:
				return officeBases;
			case COMMERCIAL:
				return commercialBases;
		}
		return residentialBases;
	}

	private ArrayList<BuildingDisplay> getStories(Building.BuildingType type) {
		switch (type) {
			case RESIDENTIAL:
				return residentialStories;
			case OFFICE:
				return officeStories;
			case COMMERCIAL:
				return commercialStories;
		}
		return residentialStories;
	}

	private ArrayList<BuildingDisplay> getRoofs(Building.BuildingType type) {
		switch (type) {
			case RESIDENTIAL:
				return residentialRoofs;
			case OFFICE:
				return officeRoofs;
			case COMMERCIAL:
				return commercialRoofs;
		}
		return residentialRoofs;
	}

	private BuildingDisplay getSpecificBase(String name, Building.BuildingType type) {
		switch (type) {
			case RESIDENTIAL:
				return specificResidentialBases.get(name);
			case OFFICE:
				return specificCommercialBases.get(name);
			case COMMERCIAL:
				return specificOfficeBases.get(name);
		}
		return null;
	}

	private BuildingDisplay getSpecificStory(String name, Building.BuildingType type) {
		switch (type) {
			case RESIDENTIAL:
				return specificResidentialStories.get(name);
			case OFFICE:
				return specificCommercialStories.get(name);
			case COMMERCIAL:
				return specificOfficeStories.get(name);
		}
		return null;
	}

	private BuildingDisplay getSpecificRoof(String name, Building.BuildingType type) {
		switch (type) {
			case RESIDENTIAL:
				return specificResidentialRoofs.get(name);
			case OFFICE:
				return specificCommercialRoofs.get(name);
			case COMMERCIAL:
				return specificOfficeRoofs.get(name);
		}
		return null;
	}

	private Building createGenericBuilding(MapCoord position, Building.BuildingType type, int level) {
		float happiness = minHappinessRequired + level * happinessRequiredPerLevel;
		if (happiness > maxHappinessRequired)
			happiness = maxHappinessRequired;

		float incomePerResident = 0;
		if (type == Building.BuildingType.RESIDENTIAL) {
			incomePerResident = residentialBaseIncomePerResident + level * residentialLevelIncomeBonusPerResident;
		} else if (type == Building.BuildingType.COMMERCIAL) {
			incomePerResident = commercialBaseIncomePerResident + level * commercialLevelIncomeBonusPerResident;
		} else {
			incomePerResident = officeBaseIncomePerResident + level * officeLevelIncomeBonusPerResident;
		}

		// we create a new building
		Building building = new Building(
			   position,
			   type.name() + "Building",
			   type,
			   level,
			   level * numberResidentsPerLevel + minimumResidents,
			   happiness,
			   minLevelUpTime + level * level * timePerLevel,
			   buildTime + buildTimePerLevel * level,
			   incomePerResident,
			   basePowerNeeded + powerPerLevel * level,
			   baseWaterNeeded + waterPerLevel * level,
			   new Rectangle(position.x, position.y, 1, 1),
			   false);

		// then we need to add the displays to this object
		ArrayList<BuildingDisplay> bases = getBases(type);
		ArrayList<BuildingDisplay> stories = getStories(type);
		ArrayList<BuildingDisplay> roofs = getRoofs(type);

		// we add a base, let's get a random base
		BuildingDisplay base = bases.get(random.nextInt(bases.size())).copy();
		building.addBuildingDisplay(base);

		// then we will get all of our stories for each of these levels
		ArrayList<BuildingDisplay> buildingStories = new ArrayList<>();

		for (int i = 0; i < level; i++) {
			buildingStories.add(stories.get(random.nextInt(stories.size())).copy());
			building.addBuildingDisplay(buildingStories.get(i));
		}

		// then we can get our roof
		BuildingDisplay roof = roofs.get(random.nextInt(roofs.size())).copy();
		building.addBuildingDisplay(roof);

		building.setVisible(City.getInstance().isBuildingsVisible());

		return building;
	}

	private Building createSpecificBuilding(MapCoord position, Building.BuildingType type, int level) {

		String buildingName;
		Rectangle dimensions;
		float incomePerResident;

		// handle all of our building type specific operations here
		if (type == Building.BuildingType.RESIDENTIAL && !specificResidentialNames.isEmpty()) {
			buildingName = specificResidentialNames.get(random.nextInt(specificResidentialNames.size()));
			dimensions = specificResidentialDimensions.get(buildingName);
			incomePerResident = residentialBaseIncomePerResident + level * residentialLevelIncomeBonusPerResident;
		} else if (type == Building.BuildingType.COMMERCIAL && !specificCommercialNames.isEmpty()) {
			buildingName = specificCommercialNames.get(random.nextInt(specificCommercialNames.size()));
			dimensions = specificCommercialDimensions.get(buildingName);
			incomePerResident = commercialBaseIncomePerResident + level * commercialLevelIncomeBonusPerResident;
		} else if (type == Building.BuildingType.OFFICE && !specificOfficeNames.isEmpty()) {
			buildingName = specificOfficeNames.get(random.nextInt(specificOfficeNames.size()));
			dimensions = specificOfficeDimensions.get(buildingName);
			incomePerResident = officeBaseIncomePerResident + level * officeLevelIncomeBonusPerResident;
		} else {
			return null;
		}

		int multiplier = (int) (dimensions.width > dimensions.height ? dimensions.width : dimensions.height);

		// generate all of our data about this object
		float happiness = minHappinessRequired + level * happinessRequiredPerLevel;
		if (happiness > maxHappinessRequired) {
			happiness = maxHappinessRequired;
		}

		float powerNeeded = (basePowerNeeded + powerPerLevel * level) * multiplier;
		float waterNeeded = (baseWaterNeeded + waterPerLevel * level) * multiplier;
		int numberResidents = (level * numberResidentsPerLevel + minimumResidents) * multiplier;

		// we create a new building
		Building building = new Building(
			   position,
			   type.name() + "SpecificBuilding",
			   type,
			   level,
			   numberResidents,
			   happiness,
			   minLevelUpTime + level * level * timePerLevel,
			   buildTime + buildTimePerLevel * level,
			   incomePerResident,
			   powerNeeded,
			   waterNeeded,
			   dimensions,
			   true);

		// then we need to find the specific displays for this object
		BuildingDisplay base = getSpecificBase(buildingName, type);
		BuildingDisplay story = getSpecificStory(buildingName, type);
		BuildingDisplay roof = getSpecificRoof(buildingName, type);

		if (base == null || story == null || roof == null) {
			System.out.println("Building displays for " + buildingName + " could not all be found");
			return null;
		}

		// add the specific base to this object
		building.addBuildingDisplay(base.copy());

		// then we will get all of our stories for each of these levels
		ArrayList<BuildingDisplay> buildingStories = new ArrayList<>();

		for (int i = 0; i < level; i++) {
			buildingStories.add(story.copy());
			building.addBuildingDisplay(buildingStories.get(i));
		}

		// then we can get our roof
		building.addBuildingDisplay(roof.copy());


		return building;
	}

	private Building createSpecificBuilding(String buildingName, MapCoord position, Building.BuildingType type, int level) {

		Rectangle dimensions;
		float incomePerResident;

		// handle all of our building type specific operations here
		if (type == Building.BuildingType.RESIDENTIAL && !specificResidentialNames.isEmpty()) {
			dimensions = specificResidentialDimensions.get(buildingName);
			incomePerResident = residentialBaseIncomePerResident + level * residentialLevelIncomeBonusPerResident;
		} else if (type == Building.BuildingType.COMMERCIAL && !specificCommercialNames.isEmpty()) {
			dimensions = specificCommercialDimensions.get(buildingName);
			incomePerResident = commercialBaseIncomePerResident + level * commercialLevelIncomeBonusPerResident;
		} else if (type == Building.BuildingType.OFFICE && !specificOfficeNames.isEmpty()) {
			dimensions = specificOfficeDimensions.get(buildingName);
			incomePerResident = officeBaseIncomePerResident + level * officeLevelIncomeBonusPerResident;
		} else {
			return null;
		}

		int multiplier = (int) (dimensions.width > dimensions.height ? dimensions.width : dimensions.height);

		// generate all of our data about this object
		float happiness = minHappinessRequired + level * happinessRequiredPerLevel;
		if (happiness > maxHappinessRequired) {
			happiness = maxHappinessRequired;
		}

		float powerNeeded = (basePowerNeeded + powerPerLevel * level) * multiplier;
		float waterNeeded = (baseWaterNeeded + waterPerLevel * level) * multiplier;
		int numberResidents = (level * numberResidentsPerLevel + minimumResidents) * multiplier;

		// we create a new building
		Building building = new Building(
			   position,
			   type.name() + "SpecificBuilding",
			   type,
			   level,
			   numberResidents,
			   happiness,
			   minLevelUpTime + level * level * timePerLevel,
			   buildTime + buildTimePerLevel * level,
			   incomePerResident,
			   powerNeeded,
			   waterNeeded,
			   dimensions,
			   true);

		// then we need to find the specific displays for this object
		BuildingDisplay base = getSpecificBase(buildingName, type);
		BuildingDisplay story = getSpecificStory(buildingName, type);
		BuildingDisplay roof = getSpecificRoof(buildingName, type);

		if (base == null || story == null || roof == null) {
			System.out.println("Building displays for " + buildingName + " could not all be found");
			return null;
		}

		// add the specific base to this object
		building.addBuildingDisplay(base.copy());

		// then we will get all of our stories for each of these levels
		ArrayList<BuildingDisplay> buildingStories = new ArrayList<>();

		for (int i = 0; i < level; i++) {
			buildingStories.add(story.copy());
			building.addBuildingDisplay(buildingStories.get(i));
		}

		// then we can get our roof
		building.addBuildingDisplay(roof.copy());


		return building;
	}
}
