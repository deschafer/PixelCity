package com.pixel.tools;

import com.pixel.city.FinancialManager;
import com.pixel.map.Map;
import com.pixel.map.object.Cell;
import com.pixel.map.object.building.Building;
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

	
	public void setPlaceableObject(String buildingName) {
		this.buildingName = buildingName;

		clearVisualizers();

		visualizer = VisualizerFactory.getInstance().createVisualizer(buildingName);
		visualizer.setX(-visualizer.getWidth() + visualizer.getWidth() / 2 + cellWidth / 2);

		// create our new building
		savedBuilding = SpecialtyBuildingFactory.getInstance().create(buildingName, Map.zeroCoordinate);
		totalCost = savedBuilding.getPlacedownCost();
	}

	@Override
	public boolean onUpdate() {
		onTouchDown(0,0);

		if (!super.onUpdate()) {
			return false;
		}

		// update the position of the visualizer
		visualizer.remove();
		currCell.addActor(visualizer);

		valid = true;
		visualizer.setType(Visualizer.VisualizerType.GREEN);
		placingCells.clear();

		// also check the positions of the cell placement
		float width = visualizer.getWidth() / gameMap.getCellWidth();
		float height = visualizer.getWidth() / gameMap.getCellHeight();
		int widthInCells = (int)width;
		int heightInCells = (int)height;
		Cell cell = null;

		int currentX = currCell.getMapPosition().x;
		int currentY = currCell.getMapPosition().y;

		// checking each cell to verify that this object can be placed
		for (int i = 0; i < widthInCells; i++) {
			for (int j = 0; j < heightInCells; j++) {

				// if the cell is null, obviously cannot place this object as its off the map
				if ((cell = gameMap.getCell(currentX - i, currentY - j)) == null) {
					valid = false;
				} else {

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

		if (super.onTouchUp(x, y) && valid) {

			// create our new building
			SpecialtyBuilding building =
				   SpecialtyBuildingFactory.getInstance().create(buildingName, currCell.getMapPosition());

			// if this object was not found
			if (building == null) {
				return false;
			}

			// Offset the image to center the building in the cell grid
			building.setX(-building.getDimensions().width * cellWidth + building.getWidth() / 2 + cellWidth / 2);

			// since we know that the object can be placed without any issues, just place it
			currCell.addMapObject(building);

			// setting each cell as occupied
			for (Cell cell : placingCells) {
				cell.addOccupyingObject(building);
			}

			return true;
		}

		return false;
	}

	private boolean checkRoadRequirement() {

		System.out.println("Checking road");

		Map.MapCoord southCorner =  currCell.getMapPosition();
		Cell checkedCell;

		for (int x = 0, width = (int)savedBuilding.getDimensions().width; x < width; x++) {

			// checking bottom
			if (((checkedCell = gameMap.getCell(gameMap.new MapCoord(southCorner.x - x, southCorner.y + 1))) != null) &&
				   checkedCell.containsMapObject() &&
				   checkedCell.getTopObject().getName().contains("Road")) {

				return true;
			}
			// checking top
			if (((checkedCell = gameMap.getCell(gameMap.new MapCoord(southCorner.x - x, southCorner.y - (int)savedBuilding.getDimensions().height - 1))) != null) &&
				   checkedCell.containsMapObject() &&
				   checkedCell.getTopObject().getName().contains("Road")) {

				return true;
			}
		}

		for (int y = 0, height = (int)savedBuilding.getDimensions().width; y < height; y++) {

			// checking right
			if (((checkedCell = gameMap.getCell(gameMap.new MapCoord(southCorner.x + 1, southCorner.y - y))) != null) &&
				   checkedCell.containsMapObject() &&
				   checkedCell.getTopObject().getName().contains("Road")) {

				return true;
			}
			// checking left
			if (((checkedCell = gameMap.getCell(gameMap.new MapCoord(southCorner.x - (int)savedBuilding.getDimensions().width, southCorner.y - y))) != null) &&
				   checkedCell.containsMapObject() &&
				   checkedCell.getTopObject().getName().contains("Road")) {

				return true;
			}
		}

		return false;
	}

	@Override
	public void cancel() {

		GameScene.getInstance().clearActiveTool();
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
