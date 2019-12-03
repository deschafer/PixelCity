package com.pixel.UI.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.object.building.Building;
import com.pixel.map.object.building.special.ServiceBuilding;
import com.pixel.object.SimpleActor;

public class BuildingStatDialog extends PDialog {

	private Label fireLabel;
	private Label policeLabel;
	private Label educationLabel;
	private Label healthLabel;
	private Label powerNeededLabel;
	private Label powerClaimedLabel;
	private Label waterNeededLabel;
	private Label waterClaimedLabel;
	private Label buildingTypeLabel;
	private Label happinessLabel;

	private Label servicesNeeded;

	public BuildingStatDialog() {
		super("Building Details", PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));

		// creating our fire labels and information
		fireLabel = new Label("Fire ", (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));

		// creating our police labels and information
		policeLabel = new Label("Police ", (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));

		// creating our police labels and information
		educationLabel = new Label("Education ", (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));

		// creating our police labels and information
		healthLabel = new Label("Health ", (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));

		powerNeededLabel = new Label("Power Needed ", (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		powerClaimedLabel = new Label("Power Claimed ", (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		waterNeededLabel = new Label("Water Needed ", (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		waterClaimedLabel = new Label("Water Claimed ", (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));

		buildingTypeLabel = new Label("", (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		happinessLabel = new Label("Happiness ", (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));

		servicesNeeded = new Label("dfgdf", (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));

		getContentTable().add(new Label("Services Needed", (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin))).align(Align.left);
		getContentTable().add().expandX();
		getContentTable().row();
		getContentTable().add(servicesNeeded).padLeft(10).align(Align.left);
		getContentTable().add().expandX();
		getContentTable().row();
		getContentTable().add(new Label("Power", (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin))).align(Align.left);
		getContentTable().add().expandX();
		getContentTable().row();
		getContentTable().add(powerNeededLabel).padLeft(10).align(Align.left);
		getContentTable().add().expandX();
		getContentTable().row();
		getContentTable().add(powerClaimedLabel).padLeft(10).align(Align.left);
		getContentTable().add().expandX();
		getContentTable().row();
		getContentTable().add(new Label("Water", (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin))).align(Align.left);
		getContentTable().add().expandX();
		getContentTable().row();
		getContentTable().add(waterNeededLabel).padLeft(10).align(Align.left);
		getContentTable().add().expandX();
		getContentTable().row();
		getContentTable().add(waterClaimedLabel).padLeft(10).align(Align.left);
		getContentTable().add().expandX();
		getContentTable().row();
		getContentTable().add(happinessLabel).align(Align.left);
		getContentTable().add().expandX();

	}

	public void setBuilding(Building building) {

		Building.BuildingType type = building.getType();
		if (type == Building.BuildingType.RESIDENTIAL) {
			getTitleLabel().setText("Level " + building.getLevel() + " Residential");
		} else if (type == Building.BuildingType.COMMERCIAL) {
			getTitleLabel().setText("Level " + building.getLevel() + " Commercial");
		} else if (type == Building.BuildingType.OFFICE) {
			getTitleLabel().setText("Level " + building.getLevel() + " Office");
		}

		boolean allServices = true;

		String servicesNeededString = "";

		// now we set the labels appropriately
		if (!building.hasService(ServiceBuilding.Services.FIRE)) {
			servicesNeededString += "Fire";
			allServices = false;
		}
		if (!building.hasService(ServiceBuilding.Services.POLICE)) {
			if (!allServices)
				servicesNeededString += '\n';
			servicesNeededString += "Police";
			allServices = false;
		}
		if (!building.hasService(ServiceBuilding.Services.EDUCATION)) {
			if (!allServices)
				servicesNeededString += '\n';
			servicesNeededString += "Education";
			allServices = false;
		}
		if (!building.hasService(ServiceBuilding.Services.HEALTH)) {
			if (!allServices)
				servicesNeededString += '\n';
			servicesNeededString += "Health";
			allServices = false;
		}

		if (allServices) {
			servicesNeeded.setText("All Provided");
		} else {
			servicesNeeded.setText(servicesNeededString);
		}

		powerNeededLabel.setText("Power needed: " + (int)building.getPowerNeeded() + "kW");
		powerClaimedLabel.setText("Power claimed " + (int)building.getPowerClaimed() + "kW");
		waterNeededLabel.setText("Water needed " + (int)building.getWaterNeeded() + "CCF");
		waterClaimedLabel.setText("Water claimed: " + (int)building.getWaterClaimed() + "CCF");
		happinessLabel.setText("Happiness " + (int)building.getHappiness() +"%");
	}
}