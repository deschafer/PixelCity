package com.pixel.map.object.building;

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

	private BuildingType type;               // the type of this building
	private int level;                       // the level of this object
	private int numberResidents = 0;         // the number of residents
	private float happiness;				 // the happiness of the residents of this building
	private ArrayList<Resident> residents;   // list of people assoc'd with this object
	private boolean building = true;		 // the current object is being built
	private ArrayList<BuildingDisplay> drawableComponents;	// the visual components of this object

	public Building(Map.MapCoord coord, String ID, BuildingType type, int level, int numberResidents) {
		super(0, 0, GameScene.getInstance().getGameMap().getCellWidth(),
			   GameScene.getInstance().getGameMap().getCellHeight(), coord, ID);

		this.type = type;
		this.level = level;
		this.numberResidents = numberResidents;
		residents = new ArrayList<>();
		drawableComponents = new ArrayList<>();
		happiness = 50.0f;

		replaceable = false;
	}

	public void addBuildingDisplay(BuildingDisplay display) {
		drawableComponents.add(display);
		addActor(display);

		// if there is a story present
		if (drawableComponents.size() > 2) {
			display.setY((drawableComponents.size() - 1) * 34 + 76);
		} else if (drawableComponents.size() > 1) {
			display.setY(76);
		}

	}

	public boolean addResident(Resident resident) {
		if(residents.size() < numberResidents) {
			residents.add(resident);
			return true;
		}
		return false;
	}

	public float getHappiness() {
		return happiness;
	}
}
