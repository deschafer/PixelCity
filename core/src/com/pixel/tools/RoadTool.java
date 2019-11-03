package com.pixel.tools;

import com.badlogic.gdx.math.Vector2;
import com.pixel.map.Map;
import com.pixel.map.object.Cell;
import com.pixel.map.object.MapObject;
import com.pixel.map.object.roads.Road;
import com.pixel.map.object.roads.RoadFactory;
import com.pixel.map.visualizer.Visualizer;
import com.pixel.map.visualizer.VisualizerFactory;

import java.util.ArrayList;

public class RoadTool extends MapTool {

	private enum Direction {NORTH, SOUTH, EAST, WEST}
	private ArrayList<RoadFactory.RoadType> proposedRoadTypes = new ArrayList<>();
	private ArrayList<Cell> proposedCells = new ArrayList<>();
	private ArrayList<Visualizer> visualizers = new ArrayList<>();

	@Override
	public boolean onTouchDown(float x, float y) {
		if(!super.onTouchDown(x, y)) {
			return false;
		}

		return true;
	}

	@Override
	public boolean onTouchMove(float x, float y) {
		if (!super.onTouchMove(x, y)) {
			return false;
		}

		clearVisualizers();
		proposedCells.clear();
		proposedRoadTypes.clear();

		boolean horiz = false;
		int numberCells = 0;
		int xChange = 0;
		int yChange = 0;
		Direction roadDirection = Direction.NORTH;

		// we get the loc of the beginning point and the current point
		// we know that currCell is not null due to overridden method

		if(begCell == null)
			return false;

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
			roadDirection = Direction.NORTH;
		}
		// The top left corner
		else if(distance.x < 0 && distance.y >= 0) {
			System.out.println("Quad 1");
			xChange = -1;
			roadDirection = Direction.WEST;
		}
		// The bottom left corner
		else if(distance.x < 0 && distance.y < 0) {
			System.out.println("Quad 2");
			yChange = 1;
			roadDirection = Direction.SOUTH;
		}
		// The bottom right corner
		else if(distance.x >= 0 && distance.y < 0) {
			System.out.println("Quad 3");
			xChange = 1;
			roadDirection = Direction.EAST;
		}

		numberCells = (int)Math.abs(distance.x / (cellWidth / 2)) + 1;

		for(int i = 0; i < numberCells; i++) {

			Cell checkedCell = gameMap.getCell(begCoord.x + i * xChange, begCoord.y + i * yChange);
			if (checkedCell == null) {

				// clear all of our lists of objects
				proposedCells.clear();
				proposedRoadTypes.clear();

				return false;
			}

			// add this cell to the end of this list of proposed cell
			proposedCells.add(checkedCell);

			// determine which cell to add, return a road type
			proposedRoadTypes.add(findRoadType(roadDirection, i == (numberCells - 1), i == 0, checkedCell));
		}

		// then we create our visualizers
		for(int i = 0; i < numberCells; i++) {

			// for each of our proposed cells, we need to create a new visualizer for this cell
			Visualizer visualizer = VisualizerFactory.getInstance().createVisualizer(proposedRoadTypes.get(i).getName());
			visualizers.add(visualizer);

			// then we need to add this visualizer to the cell
			addVisualizerToCell(proposedCells.get(i), visualizer);
		}

		return true;
	}

	private RoadFactory.RoadType findRoadType(Direction direction, boolean lastRoad, boolean firstRoad, Cell cell) {

		// TODO: will also need to check surrounding cells

		// set to a default road type to handle edge cases
		RoadFactory.RoadType roadType = RoadFactory.RoadType.ROADWAY_NS;

		// check the direction first
		if(direction == Direction.NORTH || direction == Direction.SOUTH) {

			if(lastRoad) {

				roadType = (direction == Direction.NORTH) ?
					   RoadFactory.RoadType.END_N :
					   RoadFactory.RoadType.END_S;
			}
			else if (firstRoad) {
				roadType = (direction == Direction.NORTH) ?
					   RoadFactory.RoadType.END_S :
					   RoadFactory.RoadType.END_N;
			}
			else {
				// Just a normal road in this direction will be placed
				roadType = RoadFactory.RoadType.ROADWAY_NS;
			}
		}
		else if (direction == Direction.WEST || direction == Direction.EAST) {

			if(lastRoad) {
				roadType = (direction == Direction.WEST) ?
					   RoadFactory.RoadType.END_W :
					   RoadFactory.RoadType.END_E;
			}
			else if (firstRoad) {
				roadType = (direction == Direction.WEST) ?
					   RoadFactory.RoadType.END_E :
					   RoadFactory.RoadType.END_W;
			}
			else {
				// Just a normal road in this direction will be placed
				roadType = RoadFactory.RoadType.ROADWAY_EW;
			}
		}

		return roadType;
	}

	private void addVisualizerToCell(Cell cell, Visualizer visualizer) {

		// the status of the visualizer depends on its cell
		boolean cellEmpty = !cell.containsMapObject();

		cell.addActor(visualizer);

		// if the cell is empty of MapObjects, then we can add an object here, and the visualizer is green
		if (cellEmpty) {
			visualizer.setType(Visualizer.VisualizerType.GREEN);
		}
		// the cell contains MapObjects
		else {
			// get the top MapObject of the cell
			MapObject object = cell.getTopObject();

			if(object == null)
			{
				int g = 9;
			}

			// the top object can be placed on, the visualizer is yellow
			if(object.isReplaceable()) {
				visualizer.setType(Visualizer.VisualizerType.YELLOW);
			}
			// the top object cannot be placed over, the visualizer is red
			else {
				visualizer.setType(Visualizer.VisualizerType.RED);
			}
		}
	}

	private void clearVisualizers() {

		for(Visualizer visualizer : visualizers) {
			visualizer.remove();
			VisualizerFactory.getInstance().returnVisualizer(visualizer);
		}

		visualizers.clear();
	}

	@Override
	public boolean onTouchUp(float x, float y) {

		// lets clear all of our present visualizers
		clearVisualizers();

		if (!super.onTouchUp(x, y)) {
			return false;
		}

		for (int i = 0; i < proposedCells.size(); i++) {

			Cell cell = proposedCells.get(i);
			RoadFactory.RoadType type = proposedRoadTypes.get(i);

			// create our new map object
			MapObject object = RoadFactory.getInstance().getRoad(type);

			// We need to set the object's position as well
			object.setMapPosition(cell.getMapPosition().x, cell.getMapPosition().y);

			// Then we attempt to place this new object
			if (cell.containsMapObject() && cell.getTopObject().isReplaceable()) {

				cell.getTopObject().placeOverObject(object);
			} else {
				cell.addActor(object);
			}
		}

		proposedCells.clear();
		proposedRoadTypes.clear();

		return true;
	}

	@Override
	public void cancel() {
		super.cancel();

		clearVisualizers();
		proposedRoadTypes.clear();
		proposedCells.clear();
	}
}
