package com.pixel.tools;

import com.pixel.city.FinancialManager;
import com.pixel.map.Map;
import com.pixel.map.MapCoord;
import com.pixel.map.object.Cell;
import com.pixel.map.object.building.Building;
import com.pixel.map.object.building.special.ServiceBuilding;
import com.pixel.map.object.building.special.SpecialtyBuilding;
import com.pixel.map.object.building.special.SpecialtyBuildingFactory;
import com.pixel.map.visualizer.Visualizer;
import com.pixel.map.visualizer.VisualizerFactory;
import com.pixel.scene.GameScene;

import java.util.ArrayList;

public class SpecialtyBuildingPlacementTool extends MapTool {

	private String buildingName;
	private Visualizer visualizer;
	private boolean valid = false;
	private ArrayList<Cell> placingCells = new ArrayList<>();
	private SpecialtyBuilding savedBuilding;
	private Cell parentCell = null;


	public void setPlaceableObject(String buildingName) {
		this.buildingName = buildingName;

		clearVisualizers();

		// create our new building
		ServiceBuilding.placedOnMap = false;
		savedBuilding = SpecialtyBuildingFactory.getInstance().createEmpty(buildingName);
		totalCost = savedBuilding.getPlacedownCost();
		ServiceBuilding.placedOnMap = true;

		visualizer = VisualizerFactory.getInstance().createVisualizer(buildingName);
		visualizer.setX(-visualizer.getWidth() + visualizer.getWidth() / 2 + cellWidth / 2);
	}

	@Override
	public boolean onUpdate() {
		onTouchDown(0,0);

		if (!super.onUpdate()) {
			return false;
		}

		if (visualizer == null) {
			return false;
		}

		// also check the positions of the cell placement
		float width = savedBuilding.getDimensions().width;
		float height = savedBuilding.getDimensions().height;
		int widthInCells = (int)width;
		int heightInCells = (int)height;
		Cell cell;

		// get the correct cell
		parentCell = gameMap.getCell(currCell.getMapPosition().x - ((int)width - 1), currCell.getMapPosition().y);

		// verify that this cell is not null
		if (parentCell == null) {
			return false;
		}

		// update the position of the visualizer
		visualizer.remove();
		currCell.addActor(visualizer);

		valid = true;
		visualizer.setType(Visualizer.VisualizerType.GREEN);
		placingCells.clear();

		int currentX = parentCell.getMapPosition().x;
		int currentY = parentCell.getMapPosition().y;

		// checking each cell to verify that this object can be placed
		for (int i = 0; i < widthInCells; i++) {
			for (int j = 0; j < heightInCells; j++) {

				// if the cell is null, obviously cannot place this object as its off the map
				if ((cell = gameMap.getCell(currentX + i, currentY - j)) == null) {
					valid = false;
				} else if (parentCell != cell) {

					placingCells.add(cell);

					if (cell.containsMapObject()) {
						// if the cell contains an object, and if that object is replaceable, another object
						// can be placed on top
						if (cell.getTopObject().isReplaceable()) {
							visualizer.setType(Visualizer.VisualizerType.YELLOW);
						} else {
							valid = false;
						}
					}
				}
			}
		}
		if (savedBuilding.getPlacedownCost() > FinancialManager.getInstance().getBalance()) {
			valid = false;
		}
		if (!checkRoadRequirement()) {
			valid = false;
		}
		if (!valid) {
			visualizer.setType(Visualizer.VisualizerType.RED);
		}

		return true;
	}

	@Override
	public boolean onTouchUp(float x, float y) {

		if (super.onTouchUp(x, y) && valid && parentCell != null) {

			// create our new building
			SpecialtyBuilding building =
				   SpecialtyBuildingFactory.getInstance().create(buildingName, currCell.getMapPosition());

			// if this object was not found
			if (building == null) {
				return false;
			}

			// Offset the image to center the building in the cell grid
			if (building.getDimensions().width > 1) {
				building.setY(-((savedBuilding.getDimensions().height - 2) * cellHeight / 2));
			}

			// we need to call the placement of the object below
			if (parentCell.getTopObject() != null) {
				parentCell.getTopObject().placeOverObject(building);
			}

			// since we know that the object can be placed without any issues, just place it
			parentCell.addMapObject(building);

			// setting each cell as occupied
			for (Cell cell : placingCells) {
				if (cell.getTopObject() != null) {
					cell.getTopObject().placeOverObject(building);
				}
				cell.addOccupyingObject(building);
				building.addOccupyingCell(cell);
			}

			return true;
		}

		return false;
	}

	private boolean checkRoadRequirement() {
		MapCoord westCorner =  parentCell.getMapPosition();
		Cell checkedCell;

		for (int x = 0, width = (int)savedBuilding.getDimensions().width; x < width; x++) {

			// checking bottom
			if (((checkedCell = gameMap.getCell(new MapCoord(westCorner.x + x, westCorner.y + 1))) != null) &&
				   checkedCell.containsMapObject() &&
				   checkedCell.getTopObject().getName().contains("Road")) {

				return true;
			}
			// checking top
			if (((checkedCell = gameMap.getCell(new MapCoord(westCorner.x + x, westCorner.y - (int)savedBuilding.getDimensions().height))) != null) &&
				   checkedCell.containsMapObject() &&
				   checkedCell.getTopObject().getName().contains("Road")) {

				return true;
			}
		}

		for (int y = 0, height = (int)savedBuilding.getDimensions().height; y < height; y++) {

			// checking left
			if (((checkedCell = gameMap.getCell(new MapCoord(westCorner.x - 1, westCorner.y - y))) != null) &&
				   checkedCell.containsMapObject() &&
				   checkedCell.getTopObject().getName().contains("Road")) {

				return true;
			}
			// checking right
			if (((checkedCell = gameMap.getCell(new MapCoord(westCorner.x + (int)savedBuilding.getDimensions().width, westCorner.y - y))) != null) &&
				   checkedCell.containsMapObject() &&
				   checkedCell.getTopObject().getName().contains("Road")) {

				return true;
			}
		}

		return false;
	}

	@Override
	public void cancel() {

		clearVisualizers();

		super.cancel();
	}

	private void clearVisualizers() {
		// return our visualizer
		if (visualizer != null) {
			visualizer.remove();
			VisualizerFactory.getInstance().returnVisualizer(visualizer);
			visualizer = null;
		}
	}
}
