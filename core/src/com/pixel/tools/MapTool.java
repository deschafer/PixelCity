package com.pixel.tools;

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

	@Override
	public boolean onTouchDown(float x, float y) {
		if (!super.onTouchDown(x, y)) {
			return false;
		}

		// clear the previous saved current cells
		currentCells.clear();

		// get the beginning cell corresponding to the first cell
		begCell = gameMap.checkPosition(x, y);

		if (begCell == null) return false;

		return true;
	}

	@Override
	public boolean onTouchMove(float x, float y) {
		if (!super.onTouchMove(x, y)) {
			return false;
		}

		// first check if we have a different coordinate
		// so we can check if we have moved more than a cell

		// get the current cell corresponding to the location
		currCell = gameMap.checkPosition(x, y);

		if (currCell == null) return false;

		currentCells.add(currCell);

		return true;
	}

	@Override
	public boolean onTouchUp(float x, float y) {
		if (!super.onTouchUp(x, y)) {
			return false;
		}

		// get the last cell corresponding to the location
		endCell = gameMap.checkPosition(x,y);

		return true;
	}
}
