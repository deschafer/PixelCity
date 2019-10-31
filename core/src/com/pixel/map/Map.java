package com.pixel.map;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pixel.map.object.Cell;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Map extends Group {

	public class MapCoord {

		public int x;
		public int y;

		MapCoord(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	private int width;                    // width of the map array in cells
	private int height;                    // height ...
	private float widthPixels;               // width of the map in pixels
	private float heightPixels;          // height of the map in pixels
	private Stage parentStage;
	private Cell[][] mapArray;
	private final int cellWidth = 64;     // width of the cell in pixels
	private final int cellHeight = 64;     // height of the cell in pixels
	private final float cellRowWidth = 64;
	private final float cellRowHeight = 2.0f/3.0f * 64.0f;
	private Stage stage;
	private Vector2 topOfMap;

	public Map(int width, int height, Stage stage) {

		// Set our member variables
		this.width = width;
		this.height = height;
		this.stage = stage;
		parentStage = stage;
		widthPixels = width * cellRowWidth;
		heightPixels = height * cellRowHeight;

		stage.addActor(this);

		generateArray();
	}

	public void update() {

	}

	//
	// generateArray()
	// Generates our isometric array of cells
	//
	private void generateArray() {
		// initialize our array of objects
		mapArray = new Cell[width][height];

		int middleWidth = MathUtils.round(widthPixels / 2.0f);
		int middleHeight = MathUtils.round(heightPixels / 2.0f);

		// We step in halves
		float stepX = cellRowWidth / 2;
		float stepY = cellRowHeight / 2;

		// Find our initial position at the very top of the array
		float topPositionX = middleWidth;
		float topPositionY = heightPixels;
		topOfMap = new Vector2(topPositionX, topPositionY);

		for (int countX = 0; countX < width; countX++) {
			for (int countY = 0; countY < width; countY++) {
				// we move diagonally, so it makes sense we move both vertically and horizontally each instance
				float currentPositionX = topPositionX - countY * stepX;
				float currentPositionY = topPositionY - countY * stepY;

				addActor(mapArray[countX][countY] =
					   new Cell(currentPositionX, currentPositionY, cellWidth, cellHeight, new MapCoord(countX, countY)));
			}

			topPositionX += stepX;
			topPositionY -= stepY;
		}
	}

	//
	// CheckPosition()
	// If there is a cell at the position within the parent scene, then it returns that cell
	// if there is not cell that that position, then we return null
	//
	public Cell checkPosition(float x, float y) {

		Vector2 point = new Vector2(x, y);
		Actor actor = stage.hit(x, y, true);
		Cell cell = (Cell) actor;
		Cell otherCell = null;
		Map.MapCoord cellCoord;

		if (cell != null) {

			Vector2 centerCellPosition = new Vector2(cell.getX() + cell.getWidth() / 2, cell.getY() + + 2*cell.getHeight()/3);
			cellCoord = cell.getMapPosition();

			// we need to find the next closest cell, and determine if this click is
			// within this cell or the other cell

			// then the other cell is on the right of this cell
			if (point.x > centerCellPosition.x) {
				// The point is in the upper half of this cell
				if (point.y > centerCellPosition.y) {
					otherCell = getCell(cellCoord.x, cellCoord.y - 1);
				}
				// the point is in the lower half of the cell
				else if (point.y < centerCellPosition.y) {
					otherCell = getCell(cellCoord.x + 1, cellCoord.y);
				}
			}
			// then the other cell is on the left of this cell
			else if (point.x < centerCellPosition.x) {
				// The point is in the upper half of this cell
				if (point.y > centerCellPosition.y) {
					otherCell = getCell(cellCoord.x - 1, cellCoord.y);
				}
				// the point is in the lower half of the cell
				else if (point.y < centerCellPosition.y) {
					otherCell = getCell(cellCoord.x, cellCoord.y + 1);
				}
			}

			if(otherCell != null)
			{
				// Then get center positions for each of these two cells
				Vector2 centerOtherCellPosition = new Vector2(otherCell.getX() + otherCell.getWidth() / 2, otherCell.getY() + 2*otherCell.getHeight()/3);

				// then find the distance from the center of each cell to the point of interest
				Vector2 position = new Vector2(x, y);

				// find the actual distances between the vector positions
				float distance1 = centerCellPosition.dst(position);
				float distance2 = centerOtherCellPosition.dst(position);

				// return the cell assoc with the smallest distance
				return (distance1 > distance2) ? otherCell : cell;
			}
			else return cell;
		}

		return null;
	}

	public Cell getCell(int x, int y) {

		if (x >= 0 && x < width &&
			   y >= 0 && y < height) {
			return mapArray[x][y];
		}
		return null;
	}

	public Cell getCell(MapCoord coord) {
		return getCell(coord.x, coord.y);
	}

	public int getCellWidth() {
		return cellWidth;
	}

	public int getCellHeight() {
		return cellHeight;
	}

	public float getCellRowHeight() {
		return cellRowHeight;
	}
}
