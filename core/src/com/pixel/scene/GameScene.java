package com.pixel.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.pixel.map.Map;
import com.pixel.map.object.building.Building;
import com.pixel.tools.RoadTool;
import com.pixel.tools.Tool;
import com.pixel.tools.ZoneTool;

public class GameScene extends Scene {

	private Map gameMap;     // our actual game map
	private static GameScene instance;
	private Tool activeTool;

	// TODO: for now, tools will be manually set, but in the future, it will be set by the UI system

	private GameScene() {
		super();
	}

	public static GameScene getInstance() {
		if(instance == null) instance = new GameScene();
		return instance;
	}

	@Override
	public void initialize() {

		// Create and initialize our game map here
		gameMap = new Map(50, 50, mainStage);
		gameMap.initialize();


		// TODO: set up and init. all UI elements as well
	}

	@Override
	public void update(float dt) {
		// TODO: Update our game map

		// TODO: Update any user input-related modifications made to the ui elements

		// TODO: handle any related tools for the user

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

		if(activeTool == null)
			activeTool = new ZoneTool(Building.BuildingType.RESIDENTIAL);
	}

	public boolean keyDown(int keycode) {

		if (keycode == Input.Keys.ESCAPE) {
			if (activeTool != null)
				activeTool.cancel();
		} else if (keycode == Input.Keys.R) {
			System.out.println("Road tool selected");
			activeTool = new RoadTool();
		} else if (keycode == Input.Keys.NUM_1) {
			System.out.println("Zone tool selected");
			activeTool = new ZoneTool(Building.BuildingType.RESIDENTIAL);
		} else if (keycode == Input.Keys.NUM_2) {
			System.out.println("Zone tool selected");
			activeTool = new ZoneTool(Building.BuildingType.COMMERCIAL);
		} else if (keycode == Input.Keys.NUM_3) {
			System.out.println("Zone tool selected");
			activeTool = new ZoneTool(Building.BuildingType.OFFICE);
		}


		return false;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		Vector2 stageCoords = mainStage.screenToStageCoordinates(new Vector2(screenX, screenY));
		activeTool.onTouchDown(stageCoords.x, stageCoords.y);

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		Vector2 stageCoords = mainStage.screenToStageCoordinates(new Vector2(screenX, screenY));
		activeTool.onTouchMove(stageCoords.x, stageCoords.y);

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

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
}
