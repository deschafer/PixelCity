package com.pixel.tools;

import com.badlogic.gdx.math.Rectangle;
import com.pixel.city.FinancialManager;
import com.pixel.map.Map;
import com.pixel.map.MapCoord;
import com.pixel.map.object.Cell;
import com.pixel.map.object.MapObject;
import com.pixel.map.object.building.Building;
import com.pixel.map.object.zoning.Zone;
import com.pixel.map.visualizer.Visualizer;
import com.pixel.map.visualizer.VisualizerFactory;

import java.util.ArrayList;

public class ZoneTool extends MapTool {

	private ArrayList<Visualizer> visualizers = new ArrayList<>();
	private Rectangle dimensions = new Rectangle(0, 0, 0, 0);
	private Rectangle rectangle = new Rectangle();
	private Building.BuildingType zoneType;
	private boolean affordable = true;
	private int distanceFromRoad = 4;

	public ZoneTool(Building.BuildingType type) {
		zoneType = type;
	}

	@Override
	public boolean onUpdate() {
		if (!super.onUpdate() || begCell == null) {
			return false;
		}

		clearVisualizers();
		affordable = true;
		totalCost = 0;

		rectangle.x = 0;
		rectangle.y = 0;
		rectangle.height = 0;
		rectangle.width = 0;

		// calculate our rectangle based on the curr cell
		int width = currCell.getMapPosition().x - begCell.getMapPosition().x;
		int height = currCell.getMapPosition().y - begCell.getMapPosition().y;

		rectangle.x = begCell.getMapPosition().x;
		rectangle.y = begCell.getMapPosition().y;

		if (width < 0) {
			// then we need to flip the rectangle positions
			rectangle.x = currCell.getMapPosition().x;
			width *= -1;
		}

		// we increase this width by one to better match the cursor position
		width += 1;

		if (height < 0) {
			// then we need to flip the rectangle positions
			rectangle.y = currCell.getMapPosition().y;
			height *= -1;
		}

		// we increase this width by one to better match the cursor position
		height += 1;

		rectangle.setWidth(width);
		rectangle.setHeight(height);

		// check every cell at the edge of this rectangle to see if any of them are null
		// Checking the top left corner
		if (gameMap.getCell((int) rectangle.x, (int) rectangle.y) == null) {
			return false;
		}
		// Top right corner
		else if (gameMap.getCell((int) rectangle.x + (int) rectangle.width - 1, (int) rectangle.y) == null) {
			return false;
		}
		// Bottom left corner
		else if (gameMap.getCell((int) rectangle.x, (int) rectangle.y + (int) rectangle.height - 1) == null) {
			return false;
		}
		// Bottom right corner
		else if (gameMap.getCell((int) rectangle.x + (int) rectangle.width - 1, (int) rectangle.y + (int) rectangle.height - 1) == null) {
			return false;
		}

		// find the actual cost of these cells
		// zones can only be placed on top of blank/empty cells.
		for (int i = 0; i < rectangle.width; i++) {
			for (int j = 0; j < rectangle.height; j++) {
				// Calculate the cost of these new cells
				Cell cell = gameMap.getCell((int)rectangle.x + i, (int)rectangle.y + j);

				// only for empty cells
				if (cell != null && !cell.containsMapObject() && checkRoadRequirement(cell)) {

					if (zoneType == Building.BuildingType.RESIDENTIAL) {
						totalCost += Zone.residentialZonePlacementCost;
					} else if (zoneType == Building.BuildingType.COMMERCIAL) {
						totalCost += Zone.commercialZonePlacementCost;
					} else {
						totalCost += Zone.officeZonePlacementCost;
					}
				}
			}
		}

		// if the cells cost too much
		if(totalCost > FinancialManager.getInstance().getBalance()) {
			affordable = false;
		}

		for (int i = 0, mapX = (int) rectangle.x; i < (int) rectangle.width; i++, mapX++) {
			for (int j = 0, mapY = (int) rectangle.y; j < (int) rectangle.height; j++, mapY++) {
				Cell cell = gameMap.getCell(mapX, mapY);

				Visualizer visualizer = VisualizerFactory.getInstance().createVisualizer(zoneType.getZoneName());

				addVisualizer(cell, visualizer);
			}
		}

		return true;
	}

	@Override
	public boolean onTouchUp(float x, float y) {
		if (!super.onTouchUp(x, y)) {
			return false;
		}

		// we need to clear all visualizers
		clearVisualizers();

		Zone zone = null;

		if(!affordable)
			return false;
		else if(zoneType == Building.BuildingType.RESIDENTIAL)
			zone = new Zone(rectangle, Building.BuildingType.RESIDENTIAL);
		else if (zoneType == Building.BuildingType.COMMERCIAL)
			zone = new Zone(rectangle, Building.BuildingType.COMMERCIAL);
		else if(zoneType == Building.BuildingType.OFFICE)
			zone = new Zone(rectangle, Building.BuildingType.OFFICE);

		if (zone != null && !zone.isEmpty()) {
			gameMap.addZone(zone);
		}

		// deduct the cost from the balance
		FinancialManager.getInstance().withdrawFunds(totalCost);

		return true;
	}

	private boolean checkRoadRequirement(Cell cell) {

		MapCoord coord = cell.getMapPosition();

		for (int i = 1; i < distanceFromRoad + 1; i++) {
			Cell checkedCell = gameMap.getCell((int) coord.x + i, (int) coord.y);
			if (checkedCell != null) {
				MapObject object = checkedCell.getTopObject();
				if (object != null && object.getName().contains("Road")) {
					return true;
				}
			}
			checkedCell = gameMap.getCell((int) coord.x - i, (int) coord.y);
			if (checkedCell != null) {
				MapObject object = checkedCell.getTopObject();
				if (object != null && object.getName().contains("Road")) {
					return true;
				}
			}
			checkedCell = gameMap.getCell((int) coord.x, (int) coord.y + i);
			if (checkedCell != null) {
				MapObject object = checkedCell.getTopObject();
				if (object != null && object.getName().contains("Road")) {
					return true;
				}
			}
			checkedCell = gameMap.getCell((int) coord.x, (int) coord.y - i);
			if (checkedCell != null) {
				MapObject object = checkedCell.getTopObject();
				if (object != null && object.getName().contains("Road")) {
					return true;
				}
			}
		}
		return false;
	}

	private void addVisualizer(Cell cell, Visualizer visualizer) {

		if (cell == null) return;

		// the status of the visualizer depends on its cell
		boolean cellEmpty = !cell.containsMapObject();

		cell.addActor(visualizer);
		visualizers.add(visualizer);

		if(!affordable || !checkRoadRequirement(cell)) {
			visualizer.setType(Visualizer.VisualizerType.RED);
		}
		// if the cell is empty of MapObjects, then we can add an object here, and the visualizer is green
		else if (cellEmpty) {
			visualizer.setType(Visualizer.VisualizerType.BLANK);
		}
		// the cell contains MapObjects
		else {
			// Since a zone cannot be placed on top of any other object, visualize this as red
			visualizer.setType(Visualizer.VisualizerType.RED);
		}
	}

	private void clearVisualizers() {

		for (Visualizer visualizer : visualizers) {
			visualizer.remove();
			VisualizerFactory.getInstance().returnVisualizer(visualizer);
		}

		visualizers.clear();
	}

	@Override
	public void cancel() {
		super.cancel();

		clearVisualizers();

	}

	public void setZoneType(Building.BuildingType zoneType) {
		this.zoneType = zoneType;
	}
}
