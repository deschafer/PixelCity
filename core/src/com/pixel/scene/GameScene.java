package com.pixel.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.pixel.map.BaseActor;
import com.pixel.map.Map;
import com.pixel.map.object.Cell;

import java.lang.invoke.VolatileCallSite;

public class GameScene extends Scene {

	private Map gameMap;     // our actual game map

	@Override
	public void initialize() {

		// TODO: need to initialize our game map here
		gameMap = new Map(50, 50, mainStage);


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
	}

	public boolean keyDown(int keycode) {

		if (keycode == Input.Keys.W) {
			mainStage.getCamera().translate(0, 10, 0);
		} else if (keycode == Input.Keys.A) {
			mainStage.getCamera().translate(-10, 0, 0);
		} else if (keycode == Input.Keys.S) {
			mainStage.getCamera().translate(0, -10, 0);
		} else if (keycode == Input.Keys.D) {
			mainStage.getCamera().translate(10, 0, 0);
		}

		return false;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		Camera camera = mainStage.getCamera();

		// translate camera coordinates to the actual coordinate system
		//float x = screenX + camera.position.x - 960/2 - 32;
		//float y = Math.abs(screenY - 540) + camera.position.y - 540/2;

		Vector2 stageCoords = mainStage.screenToStageCoordinates(new Vector2(screenX, screenY));

		//System.out.println("x: " + x + " y: " + y);

		// check if this point is within the map, and if there is a cell at the point
		//Cell cell = gameMap.checkPosition(screenX, screenY);

		Cell cell = gameMap.checkPosition(stageCoords.x, stageCoords.y);

		if (cell != null) cell.setColor(0, 0, 0, 1);
		else System.out.println("No cell");


		return false;
	}
}
