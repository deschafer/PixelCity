package com.pixel.event;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.pixel.UI.GameSceneUI;
import com.pixel.UI.dialog.NotificationDialog;
import com.pixel.city.City;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.object.building.Building;
import com.pixel.map.object.building.display.BuildingDisplay;
import com.pixel.object.AnimatedActor;
import com.pixel.scene.GameScene;

import java.util.ArrayList;
import java.util.Random;

public class EventManager {

	private float updateTimer = 0;
	private float updateTime = 0;
	private int updateTimerLowerBound = 1;
	private int updateTimerUpperBound = 2;
	private Random random = new Random();
	private ArrayList<Categories> categories;

	public enum Categories {
		FIRE,
		POLICE,
		HEALTH,
		ED,
		POWER,
		WATER
	}

	public enum Events {
		FIRE,
		SHOOTING,
		DOMESTIC_TERRORISM,
		HEALTH_DEATH,
		DISSATISFIED,
		DISEASE,
		POWER_DEATH
	}
	private static final Events[] fireEvents = { Events.FIRE, Events.DISSATISFIED};
	private static final Events[] policeEvents = { Events.DOMESTIC_TERRORISM, Events.SHOOTING, Events.DISSATISFIED};
	private static final Events[] healthEvents = { Events.HEALTH_DEATH, Events.DISSATISFIED, Events.DISEASE };
	private static final Events[] edEvents = { Events.DISSATISFIED};
	private static final Events[] powerEvents = { Events.DISSATISFIED, Events.POWER_DEATH};
	private static final Events[] waterEvents = { Events.DISSATISFIED};

	private static EventManager instance = new EventManager();

	public static EventManager getInstance() {
		return instance;
	}

	private EventManager() {
		updateTime = random.nextInt(updateTimerUpperBound) + updateTimerLowerBound;
		categories = new ArrayList<>();
	}

	public void update() {

		float delta = Gdx.graphics.getDeltaTime();
		updateTimer += delta;

		if (updateTimer >= updateTime && !City.getInstance().getCityBuildings().isEmpty()) {

			categories.clear();

			// reset the time
			//updateTime = random.nextInt(updateTimerUpperBound) + updateTimerLowerBound;
			updateTime = 0.25f;
			// reset the timer
			updateTimer = 0;

			// then we check what categories are available
			if (City.getInstance().buildingNeedsService(Categories.FIRE)) {
				categories.add(Categories.FIRE);
			}
			if (City.getInstance().buildingNeedsService(Categories.POLICE)) {
				categories.add(Categories.POLICE);
			}
			if (City.getInstance().buildingNeedsService(Categories.ED)) {
				categories.add(Categories.ED);
			}
			if (City.getInstance().buildingNeedsService(Categories.HEALTH)) {
				categories.add(Categories.HEALTH);
			}
			if (City.getInstance().buildingNeedsService(Categories.POWER)) {
				categories.add(Categories.POWER);
			}
			if (City.getInstance().buildingNeedsService(Categories.WATER)) {
				categories.add(Categories.WATER);
			}

			// if there is a building without a service, we need to apply an event
			if (!categories.isEmpty()) {
				Categories selectedCategory = categories.get(random.nextInt(categories.size()));

				if (selectedCategory == Categories.FIRE) {
					handleFireRandomEvent();
				} else if (selectedCategory == Categories.POLICE) {
					handlePoliceRandomEvent();
				} else if (selectedCategory == Categories.HEALTH) {
					handleHealthRandomEvent();
				} else if (selectedCategory == Categories.ED) {
					handleEdRandomEvent();
				} else if (selectedCategory == Categories.POWER) {
					handlePowerRandomEvent();
				} else if (selectedCategory == Categories.WATER) {
					handleWaterRandomEvent();
				}
			}
		}
	}

	private void handleFireRandomEvent() {

		// then we need to grab a random building and
		// pick an event to occur at that building
		int index = random.nextInt(fireEvents.length);
		Events selectedEvent = fireEvents[index];

		// we need to select a building within those available
		ArrayList<Building> buildings = City.getInstance().getBuildingsNeedFire();
		Building selectedBuilding = buildings.get(random.nextInt(buildings.size()));


		if (selectedEvent == Events.FIRE) {

			selectedBuilding.setCanLevelUp(false);
			City.getInstance().removeBuildingNeedsService(Categories.FIRE, selectedBuilding);

			// we should delete the entire building, and add some sort of fire graphic to its position

			for (Actor actor : selectedBuilding.getChildren()) {
				if (actor instanceof BuildingDisplay) {
					BuildingDisplay display = (BuildingDisplay)actor;
					AnimatedActor animatedActor = new AnimatedActor(0,10,display.getWidth(),display.getHeight(),"fire",
						   PixelAssetManager.fireAnimation, 0.08f, true);
					display.addActor(animatedActor);
				}
			}

			// play a sound effect
			Sound fireSound = PixelAssetManager.manager.get(PixelAssetManager.fireSound);
			fireSound.play(0.3f);

			// we create an explosion actor, and we place one at every child of the building
			// then we add an action to the building so it despawns after a period of time
			selectedBuilding.addAction(Actions.delay(5));
			selectedBuilding.addAction(Actions.after(Actions.removeActor()));

			NotificationDialog.getInstance().addMessage("A building is on fire");
		} else if (selectedEvent == Events.DISSATISFIED) {
			// remove a citizen from this building
			selectedBuilding.removeResident();
			NotificationDialog.getInstance().addMessage("A citizen left due to no fire protection");
		}
	}

	private void handlePoliceRandomEvent() {
		// then we need to grab a random building and
		// pick an event to occur at that building
		int index = random.nextInt(policeEvents.length);
		Events selectedEvent = policeEvents[index];

		// we need to select a building within those available
		ArrayList<Building> buildings = City.getInstance().getBuildingsNeedPolice();
		Building selectedBuilding = buildings.get(random.nextInt(buildings.size()));

		if (selectedEvent == Events.DOMESTIC_TERRORISM) {

			selectedBuilding.setCanLevelUp(false);
			City.getInstance().removeBuildingNeedsService(Categories.POLICE, selectedBuilding);

			// we should delete the entire building, and add some sort of fire graphic to its position

			for (Actor actor : selectedBuilding.getChildren()) {
				if (actor instanceof BuildingDisplay) {
					BuildingDisplay display = (BuildingDisplay)actor;
					AnimatedActor animatedActor = new AnimatedActor(0,10,display.getWidth(),display.getHeight(),"explosion",
						   PixelAssetManager.explosion, 0.02f, false, 6, 6);
					display.addActor(animatedActor);
				}
			}

			// play a sound effect
			Sound explosionSound = PixelAssetManager.manager.get(PixelAssetManager.explosionSound);
			explosionSound.play();

			// we create an explosion actor, and we place one at every child of the building
			// then we add an action to the building so it despawns after a period of time
			selectedBuilding.addAction(Actions.delay(0.72f));
			selectedBuilding.addAction(Actions.after(Actions.removeActor()));

			NotificationDialog.getInstance().addMessage("Domestic terrorists destroyed a building");
		} else if (selectedEvent == Events.SHOOTING) {
			// remove a citizen from one of the buildings
			selectedBuilding.removeResident();

			NotificationDialog.getInstance().addMessage("A shooting resulted in a fatality");
		} else if (selectedEvent == Events.DISSATISFIED) {
			// remove a citizen from this building
			selectedBuilding.removeResident();

			NotificationDialog.getInstance().addMessage("A citizen left due to no police service");
		}
	}

	private void handleHealthRandomEvent() {
		// then we need to grab a random building and
		// pick an event to occur at that building
		int index = random.nextInt(healthEvents.length);
		Events selectedEvent = healthEvents[index];

		// we need to select a building within those available
		ArrayList<Building> buildings = City.getInstance().getBuildingsNeedHealth();
		Building selectedBuilding = buildings.get(random.nextInt(buildings.size()));

		if (selectedBuilding.getActualNumberResidents() == 0) {
			return;
		}

		if (selectedEvent == Events.DISEASE) {

			// remove multiple citizens based on the population of the building
			int numberRemoved = random.nextInt(selectedBuilding.getActualNumberResidents());

			// remove each of these citizens
			for (int i = 0; i < numberRemoved; i++) {
				selectedBuilding.removeResident();
			}

			NotificationDialog.getInstance().addMessage("A disease broke out killing " + numberRemoved + " citizens");
		} else if (selectedEvent == Events.HEALTH_DEATH) {
			// remove a citizen from this building
			selectedBuilding.removeResident();

			NotificationDialog.getInstance().addMessage("A citizen died due to poor health");
		} else if (selectedEvent == Events.DISSATISFIED) {
			// remove a citizen from this building
			selectedBuilding.removeResident();

			NotificationDialog.getInstance().addMessage("A citizen left due to no health services");
		}
	}

	private void handleEdRandomEvent() {
		// then we need to grab a random building and
		// pick an event to occur at that building
		int index = random.nextInt(edEvents.length);
		Events selectedEvent = edEvents[index];

		// we need to select a building within those available
		ArrayList<Building> buildings = City.getInstance().getBuildingsNeedEd();
		Building selectedBuilding = buildings.get(random.nextInt(buildings.size()));

		if (selectedEvent == Events.DISSATISFIED) {
			// remove a citizen from this building
			selectedBuilding.removeResident();

			NotificationDialog.getInstance().addMessage("A citizen left due to no education services");
		}
	}

	private void handlePowerRandomEvent() {
		// then we need to grab a random building and
		// pick an event to occur at that building
		int index = random.nextInt(powerEvents.length);
		Events selectedEvent = powerEvents[index];

		// we need to select a building within those available
		ArrayList<Building> buildings = City.getInstance().getBuildingsNeedPower();
		Building selectedBuilding = buildings.get(random.nextInt(buildings.size()));

		if (selectedEvent == Events.DISSATISFIED) {
			// remove a citizen from this building
			selectedBuilding.removeResident();

			NotificationDialog.getInstance().addMessage("A citizen left due to no power services");
		} else if (selectedEvent == Events.POWER_DEATH) {
			// remove a citizen from this building
			selectedBuilding.removeResident();

			NotificationDialog.getInstance().addMessage("A citizen died by falling in the dark");
		}
	}

	private void handleWaterRandomEvent() {
		// then we need to grab a random building and
		// pick an event to occur at that building
		int index = random.nextInt(waterEvents.length);
		Events selectedEvent = waterEvents[index];

		// we need to select a building within those available
		ArrayList<Building> buildings = City.getInstance().getBuildingsNeedWater();
		Building selectedBuilding = buildings.get(random.nextInt(buildings.size()));

		if (selectedEvent == Events.DISSATISFIED) {
			// remove a citizen from this building
			selectedBuilding.removeResident();

			NotificationDialog.getInstance().addMessage("A citizen left due to no water services");
		}
	}
}
