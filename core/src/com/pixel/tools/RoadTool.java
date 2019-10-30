package com.pixel.tools;

import com.badlogic.gdx.math.Vector2;
import com.pixel.map.Map;
import com.pixel.map.object.Cell;

import java.util.ArrayList;

public class RoadTool extends MapTool {

	@Override
	public boolean onTouchDown(float x, float y) {
		if(!super.onTouchDown(x, y)) {
			return false;
		}

		// for now, just mark this cell as black
		begCell.setColor(0,0,0,1);

		return true;
	}

	@Override
	public boolean onTouchMove(float x, float y) {
		if (!super.onTouchMove(x, y)) {
			return false;
		}

		boolean horiz = false;
		int numberCells = 0;
		int xChange = 0;
		int yChange = 0;
		ArrayList<Cell> proposedCells = new ArrayList<>();

		// we get the loc of the beginning point and the current point
		// we know that currCell is not null due to overridden method

		Map.MapCoord begCoord = begCell.getMapPosition();
		float currCellX = currCell.getX();
		float currCellY = currCell.getY();
		float begCellX = begCell.getX();
		float begCellY = begCell.getY();

		// get the distance
		Vector2 distance = new Vector2(currCellX - begCellX, currCellY - begCellY);

		// The top right corner
		if(distance.x >= 0 && distance.y >= 0) {
			System.out.println("Quad 0");
			yChange = -1;
		}
		// The top left corner
		else if(distance.x < 0 && distance.y >= 0) {
			System.out.println("Quad 1");
			xChange = -1;
		}
		// The bottom left corner
		else if(distance.x < 0 && distance.y < 0) {
			System.out.println("Quad 2");
			yChange = 1;
		}
		// The bottom right corner
		else if(distance.x >= 0 && distance.y < 0) {
			System.out.println("Quad 3");
			xChange = 1;
		}

		//		get the number of cells we place, since x and y distances are equal, use the greatest one
		//		numberCells = (int)((distance.x > distance.y) ? distance.x : distance.y / (cellWidth / 2));

		numberCells = (int)Math.abs(distance.x / (cellWidth / 2));

		for(int i = 0; i < numberCells; i++) {

			Cell checkedCell = gameMap.getCell(begCoord.x + i * xChange, begCoord.y + i * yChange);
			if(checkedCell == null)
				return false;

			checkedCell.setColor(0,0,0,1);
		}


		//currCell.setColor(0, 0, 0, 1);

		return true;
	}

	@Override
	public boolean onTouchUp(float x, float y) {
		if (!super.onTouchUp(x, y)) {
			return false;
		}

		return true;
	}
}
