package com.pixel.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.pixel.city.City;
import com.pixel.city.Demand;
import com.pixel.city.FinancialManager;
import com.pixel.game.styles.Styles;
import com.pixel.map.Map;
import com.pixel.map.object.building.Building;
import com.pixel.tools.*;

public class GameScene extends Scene {

	private Map gameMap;     // our actual game map
	private static GameScene instance = new GameScene();

	// Tools
	private Tool activeTool;
	private SpecialtyBuildingPlacementTool specialtyBuildingPlacementTool;
	private RoadTool roadTool;
	private ZoneTool residentialZoneTool;
	private ZoneTool commercialZoneTool;
	private ZoneTool officeZoneTool;
	private DeleteTool deleteTool;

	// UI elements
	private Label financeLabel;

	// TODO: for now, tools will be manually set, but in the future, it will be set by the UI system

	private GameScene() {
		super();
	}

	public static GameScene getInstance() {
		return instance;
	}

	@Override
	public void initialize() {

		// Create and initialize our game map here
		gameMap = new Map(50, 50, mainStage);
		gameMap.initialize();

		uiTable.add(financeLabel = new Label("", Styles.testFinanceLabelStyle));
		uiTable.add().expandX();
		uiTable.row();
		uiTable.add().expandY();

		mainStage.getCamera().translate(gameMap.getWidthInCells() * Map.cellWidth / 2,
			   gameMap.getHeightInCells() * Map.cellHeight / 2, 0);


		// TODO: set up and init. all UI elements as well
	}

	@Override
	public void update(float dt) {

		if (specialtyBuildingPlacementTool == null) {
			specialtyBuildingPlacementTool = new SpecialtyBuildingPlacementTool();
			roadTool = new RoadTool();
			residentialZoneTool = new ZoneTool(Building.BuildingType.RESIDENTIAL);
			commercialZoneTool = new ZoneTool(Building.BuildingType.COMMERCIAL);
			officeZoneTool = new ZoneTool(Building.BuildingType.OFFICE);
			deleteTool = new DeleteTool();
		}

		// update our finances
		FinancialManager.getInstance().update();

		// update our financial label
		financeLabel.setText("" + FinancialManager.getInstance().getBalance());

		// update our city object
		City.getInstance().update();

		// update our demand object
		Demand.getInstance().update();

		// update our map object
		gameMap.update();

		if (activeTool != null) {
			activeTool.onUpdate();
		}

		// We move around the map
		// non-discrete input, so we poll automatically
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			mainStage.getCamera().translate(0, 10, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			mainStage.getCamera().translate(-10, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			mainStage.getCamera().translate(0, -10, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			mainStage.getCamera().translate(10, 0, 0);
		}
	}

	public boolean keyDown(int keycode) {

		if (keycode == Input.Keys.ESCAPE) {
			if (activeTool != null)
				activeTool.cancel();
		} else if (keycode == Input.Keys.R) {
			System.out.println("Road tool selected");
			if (activeTool != null) activeTool.switchOut();
			activeTool = roadTool;
		} else if (keycode == Input.Keys.NUM_1) {
			System.out.println("Zone tool selected");
			if (activeTool != null) activeTool.switchOut();
			activeTool = residentialZoneTool;
		} else if (keycode == Input.Keys.NUM_2) {
			System.out.println("Zone tool selected");
			if (activeTool != null) activeTool.switchOut();
			activeTool = commercialZoneTool;
		} else if (keycode == Input.Keys.NUM_3) {
			if (activeTool != null) activeTool.switchOut();
			System.out.println("Zone tool selected");
			activeTool = officeZoneTool;
		} else if (keycode == Input.Keys.B) {
			System.out.println("Deletion tool selected");
			if (activeTool != null) activeTool.switchOut();
			activeTool = deleteTool;
		} else if (keycode == Input.Keys.L) {
			Demand.getInstance().print();
		} else if (keycode == Input.Keys.M) {
			FinancialManager.getInstance().addFunds(10000);
		} else if (keycode == Input.Keys.C) {
			System.out.println("Specialty Building tool selected");
			if (activeTool != null) activeTool.switchOut();
			activeTool = specialtyBuildingPlacementTool;
			specialtyBuildingPlacementTool.setPlaceableObject("CoalPowerPlantUtility");

		} else if (keycode == Input.Keys.V) {
			System.out.println("Specialty Building tool selected");
			if (activeTool != null) activeTool.switchOut();
			activeTool = specialtyBuildingPlacementTool;
			specialtyBuildingPlacementTool.setPlaceableObject("FireStation");
		} else if (keycode == Input.Keys.P) {
			System.out.println("Specialty Building tool selected");
			if (activeTool != null) activeTool.switchOut();
			activeTool = specialtyBuildingPlacementTool;
			specialtyBuildingPlacementTool.setPlaceableObject("PoliceStation");
		} else if (keycode == Input.Keys.H) {
			System.out.println("Specialty Building tool selected");
			if (activeTool != null) activeTool.switchOut();
			activeTool = specialtyBuildingPlacementTool;
			specialtyBuildingPlacementTool.setPlaceableObject("Hospital");
		} else if (keycode == Input.Keys.E) {
			System.out.println("Specialty Building tool selected");
			if (activeTool != null) activeTool.switchOut();
			activeTool = specialtyBuildingPlacementTool;
			specialtyBuildingPlacementTool.setPlaceableObject("SecondarySchool");
		} else if (keycode == Input.Keys.T) {
			System.out.println("Specialty Building tool selected");
			if (activeTool != null) activeTool.switchOut();
			activeTool = specialtyBuildingPlacementTool;
			specialtyBuildingPlacementTool.setPlaceableObject("WaterTankUtility");
		}


		return false;
	}

	public void clearActiveTool() {
		activeTool = null;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		if(activeTool == null) return false;

		Vector2 stageCoords = mainStage.screenToStageCoordinates(new Vector2(screenX, screenY));
		activeTool.onTouchDown(stageCoords.x, stageCoords.y);

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		if(activeTool == null) return false;

		activeTool.onUpdate();

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

		if(activeTool == null) return false;

		Vector2 stageCoords = mainStage.screenToStageCoordinates(new Vector2(screenX, screenY));
		activeTool.onTouchUp(stageCoords.x, stageCoords.y);

		return false;
	}

	public Map getGameMap() {
		return gameMap;
	}

	@Override
	public boolean scrolled(int amount) {

		if(amount > 0) {
			((OrthographicCamera) mainStage.getCamera()).zoom += 0.1f;
		}
		else if (amount < 0) {
			((OrthographicCamera) mainStage.getCamera()).zoom -= 0.1f;
		}

		return false;
	}

	public void addUIElement(Widget widget) {
		uiStage.addActor(widget);
	}

	public Stage getUIStage() {
		return uiStage;
	}

	public Stage getMainStage() {
		return mainStage;
	}
}
