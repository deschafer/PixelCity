package com.pixel.map;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pixel.map.object.Cell;
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
	private int widthPixels;               // width of the map in pixels
	private int heightPixels;          // height of the map in pixels
	private Stage parentStage;
	private Cell[][] mapArray;
	private ArrayList<ArrayList<Cell>> rectangularBounds;     // the non iso-formatted array of cells
	private final int cellWidth = 64;     // width of the cell in pixels
	private final int cellHeight = 64;     // height of the cell in pixels
	private final int cellRowWidth = 64;
	private final int cellRowHeight = 42;
	private Stage stage;

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
		setRectangularBounds();

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

	private void setRectangularBounds() {

		rectangularBounds = new ArrayList<>();
		ArrayList<Cell> currentColumn;

		int currentCellX = 0;
		int currentCellY = height - 1;

		for (int i = 0; i < width; i++) {
			rectangularBounds.add(new ArrayList<>());
			currentColumn = rectangularBounds.get(i);

			currentCellX = i;

			// then we want to move as far down as possible
			// to move down, we add one x, then we add one y

			while(true) {

				if (currentCellY < height) {
					currentColumn.add(mapArray[currentCellX][currentCellY]);
				} else break;

				currentCellX += 1;
				currentCellY += 1;
			}
		}

		// first, get the far left position
		// then move from the furthest left all the way to the right

	}

	//
	// CheckPosition()
	// If there is a cell at the position within the parent scene, then it returns that cell
	// if there is not cell that that position, then we return null
	//
	public Cell checkPosition(float x, float y) {

		ArrayList<Cell> column1;
		ArrayList<Cell> column2;

		Cell cell1;
		Cell cell2;

		if (x < 0) return null;

		int cellX = Math.round(x) / (cellWidth / 2);     // this will get the correct column

		// We will collide within two of the columns
		// so we need to find the specific cell within either of these
		// two columns, then we check the closest cell

		// this does a preliminary check
		if (cellX < 2 * width && cellX >= 0) {
			System.out.println("Column found x: " + cellX);

			// we grab the cell denoted by cellX,
			// then we also grab cellX - 1

			column1 = rectangularBounds.get(cellX);

			// search this array for the cell
			for(Cell cell : column1)
			{
				//if(cell.overlaps())
			}

			if(cellX - 1 > 0) {
				column2 = rectangularBounds.get(cellX - 1);
			}


		}

		return null;
	}
	/*

	mouse_grid_x = floor((mouse_y / tile_height) + (mouse_x / tile_width));
	mouse_grid_y = floor((-mouse_x / tile_width) + (mouse_y / tile_height));
	 */
}
