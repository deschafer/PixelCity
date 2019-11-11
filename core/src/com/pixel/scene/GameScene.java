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
import com.pixel.tools.RoadTool;
import com.pixel.tools.Tool;
import com.pixel.tools.ZoneTool;

public class GameScene extends Scene {

	private Map gameMap;     // our actual game map
	private static GameScene instance;
	private Tool activeTool;

	private Label financeLabel;

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

		uiTable.add(financeLabel = new Label("", Styles.testFinanceLabelStyle));
		uiTable.add().expandX();
		uiTable.row();
		uiTable.add().expandY();


		// TODO: set up and init. all UI elements as well

		//activeTool = new RoadTool();
	}

	@Override
	public void update(float dt) {

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
		} else if (keycode == Input.Keys.L) {
			Demand.getInstance().print();
		}

		return false;
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

		Vector2 stageCoords = mainStage.screenToStageCoordinates(new Vector2(screenX, screenY));
		activeTool.onTouchMove(stageCoords.x, stageCoords.y);

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
