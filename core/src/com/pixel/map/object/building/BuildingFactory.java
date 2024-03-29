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

	private final float basePowerNeeded = 30;
	private final float powerPerLevel = 50;

	private final float baseWaterNeeded = 10;
	private final float waterPerLevel = 15;

	private final float minLevelUpTime = 5.0f;
	private final float timePerLevel = 2.0f;

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
	}

	public static BuildingFactory getInstance() {
		return instance;
	}

	public Building create(MapCoord position, Building.BuildingType type, int level) {

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
}
