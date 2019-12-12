package com.pixel.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.pixel.UI.GameSceneUI;
import com.pixel.UI.dialog.BuildingStatDialog;
import com.pixel.city.City;
import com.pixel.city.Demand;
import com.pixel.city.FinancialManager;
import com.pixel.game.PixelCityGame;
import com.pixel.map.Map;
import com.pixel.map.object.building.Building;
import com.pixel.map.object.building.display.BuildingDisplay;
import com.pixel.tools.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class GameScene extends Scene {

	private Map gameMap;     // our actual game map
	private static GameScene instance = new GameScene();
	private Vector2 placementVector = new Vector2(0,0);

	// Tools
	private Tool activeTool;
	private SpecialtyBuildingPlacementTool specialtyBuildingPlacementTool;
	private RoadTool roadTool;
	private ZoneTool residentialZoneTool;
	private ZoneTool commercialZoneTool;
	private ZoneTool officeZoneTool;
	private DeleteTool deleteTool;
	private GameSceneUI gameSceneUI;
	public static final int mapWidth = 50;
	public static final int mapHeight = 50;
	public static final String savedGamesAtlasPath = "assets/savedGames/atlas.txt";
	public static final String savedGamesDirPath = "assets/savedGames/";

	public enum Tools {
		RES_ZONING,
		COMM_ZONING,
		OFF_ZONING,
		POLICE,
		FIRE,
		ED,
		HEALTH,
		WATER,
		POWER,
		DELETE,
		ROAD
	}

	private GameScene() {
		super();
	}

	public static GameScene getInstance() {
		return instance;
	}

	public static void reset() {
		instance = new GameScene();
	}

	@Override
	public void initialize() {

		uiStage = gameSceneUI = GameSceneUI.getInstance();

		if (gameMap == null) {
			setNewGame("Default");
		}
	}

	public void setNewGame(String cityName) {

		// in this section, we need to...
		// create a new map, and save this as the current game map
		// Create and initialize our game map here

		// reset the financials
		FinancialManager.reset();

		// reset the city class
		City.reset(cityName);

		// reset the main stage
		mainStage = new Stage();

		gameMap = new Map(mapWidth, mapHeight, mainStage);
		gameMap.initialize();

		mainStage.getCamera().translate(gameMap.getWidthInCells() * Map.cellWidth / 2,
			   gameMap.getHeightInCells() * Map.cellHeight / 2, 0);

		gameSceneUI.reset();
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
			// if there is a tool selected, we deselect it
			if (activeTool != null) {
				activeTool.cancel();
				activeTool.switchOut();
				activeTool = null;
				GameSceneUI.getInstance().clearSelectedTool();
			}
			// otherwise open the in game menu dialog from the UI
			else {
				gameSceneUI.openInGameDialog();
			}
		} else if (keycode == Input.Keys.M) {
			FinancialManager.getInstance().addFunds(1000000);
		}

		return false;
	}

	public void clearActiveTool() {
		if (activeTool != null) activeTool.switchOut();
		activeTool = null;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		Vector2 stageCoords = mainStage.screenToStageCoordinates(new Vector2(screenX, screenY));

		if(activeTool == null) {
			// then we handle mouse clicks as selecting a building from the view
			handleBuildingSelection(stageCoords);
		} else {

			activeTool.onTouchDown(stageCoords.x, stageCoords.y);
		}

		return false;
	}

	private void handleBuildingSelection(Vector2 coords) {



		// get the actor at this position
		Actor actor = mainStage.hit(coords.x, coords.y, false);
		if (actor != null && (actor.getName().contains("Base") || actor.getName().contains("Story") || actor.getName().contains("Roof"))) {
			// since buildings use bases, we need to get the parent building
			BuildingDisplay display = (BuildingDisplay)actor;
			if (display.hasParent()) {
				Building selectedBuilding = (Building)display.getParent();
				System.out.println("X: " + selectedBuilding.getMapPosition().x + " Y: " + selectedBuilding.getMapPosition().y);



				// now we need to create a new dialog to look at this building
				// this will be a non-modal dialog
				BuildingStatDialog dialog = new BuildingStatDialog();
				dialog.setBuilding(selectedBuilding);
				dialog.show(gameSceneUI);
				placementVector.x = Gdx.input.getX();
				placementVector.y = Gdx.input.getY();
				dialog.setX(Gdx.input.getX());
				dialog.setY(PixelCityGame.height - Gdx.input.getY());
			}
		}
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

	public void setActiveTool(Tools tool) {
		clearActiveTool();

		switch (tool) {
			case RES_ZONING:
				activeTool = residentialZoneTool;
				break;
			case COMM_ZONING:
				activeTool = commercialZoneTool;
				break;
			case OFF_ZONING:
				activeTool = officeZoneTool;
				break;
			case POLICE:
				activeTool = specialtyBuildingPlacementTool;
				specialtyBuildingPlacementTool.setPlaceableObject("PoliceStation");
				break;
			case FIRE:
				activeTool = specialtyBuildingPlacementTool;
				specialtyBuildingPlacementTool.setPlaceableObject("FireStation");
				break;
			case ED:
				activeTool = specialtyBuildingPlacementTool;
				specialtyBuildingPlacementTool.setPlaceableObject("SecondarySchool");
				break;
			case HEALTH:
				activeTool = specialtyBuildingPlacementTool;
				specialtyBuildingPlacementTool.setPlaceableObject("Hospital");
				break;
			case WATER:
				activeTool = specialtyBuildingPlacementTool;
				specialtyBuildingPlacementTool.setPlaceableObject("WaterTankUtility");
				break;
			case POWER:
				activeTool = specialtyBuildingPlacementTool;
				specialtyBuildingPlacementTool.setPlaceableObject("CoalPowerPlantUtility");
				break;
			case DELETE:
				activeTool = deleteTool;
				break;
			case ROAD:
				activeTool = roadTool;
				break;
		}
	}
}
