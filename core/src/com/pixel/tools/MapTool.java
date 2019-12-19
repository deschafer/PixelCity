package com.pixel.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.pixel.game.PixelAssetManager;
import com.pixel.game.styles.Styles;
import com.pixel.map.Map;
import com.pixel.map.object.Cell;
import com.pixel.scene.GameScene;

import java.util.ArrayList;

public class MapTool extends Tool {

	protected Cell begCell;
	protected ArrayList<Cell> currentCells = new ArrayList<>();
	protected Cell currCell;
	protected Cell endCell;
	protected Map gameMap = GameScene.getInstance().getGameMap();
	protected final float cellWidth = GameScene.getInstance().getGameMap().getCellWidth();
	protected final float cellHeight = GameScene.getInstance().getGameMap().getCellRowHeight();
	protected float totalCost = 0.0f;
	protected Label costLabel = new Label("", Styles.mapToolCostLabelStyle);

	public  MapTool() {
		GameScene.getInstance().addUIElement(costLabel);
		costLabel.setVisible(false);
	}

	@Override
	public void cancel() {
		super.cancel();

		costLabel.setVisible(false);
	}

	@Override
	public boolean onTouchDown(float x, float y) {
		if (!super.onTouchDown(x, y)) {
			return false;
		}

		// clear the previous saved current cells
		currentCells.clear();

		// reset our cost label
		costLabel.setText("$" + totalCost);
		Vector2 position = GameScene.getInstance().getUIStage().screenToStageCoordinates( new Vector2(Gdx.input.getX(), Gdx.input.getY()) );
		costLabel.setPosition(position.x, position.y + costLabel.getHeight());
		costLabel.setVisible(true);

		// get the beginning cell corresponding to the first cell
		begCell = gameMap.checkPosition(x, y);

		if (begCell == null) return false;

		return true;
	}

	@Override
	public boolean onUpdate() {
		if (!super.onUpdate()) {
			return false;
		}

		// get the current cell corresponding to the location
		Cell cell = gameMap.checkPosition(currentPoint.x, currentPoint.y);

		Vector2 position = GameScene.getInstance().getUIStage().screenToStageCoordinates( new Vector2(Gdx.input.getX(), Gdx.input.getY()) );

		// also draw the total cost of the current selection
		costLabel.setPosition(position.x, position.y + costLabel.getHeight());
		costLabel.setText("$" + totalCost);

		// If this cell is different
		if (cell != currCell && cell != null) {

			currCell = cell;

			currentCells.add(currCell);

			return true;
		}

		return false;
	}

	@Override
	public boolean onTouchUp(float x, float y) {
		if (!super.onTouchUp(x, y)) {
			return false;
		}

		// get the last cell corresponding to the location
		endCell = gameMap.checkPosition(x,y);
		costLabel.setVisible(false);

		Sound sound = PixelAssetManager.manager.get(PixelAssetManager.clickOne);
		sound.play();

		return true;
	}

	@Override
	public void switchOut() {
		cancel();
		begCell = null;
		endCell = null;
		currentCells.clear();
		currCell = null;
	}
}
