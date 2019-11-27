package com.pixel.map.object.zoning;

import com.badlogic.gdx.math.Rectangle;
import com.pixel.map.Map;
import com.pixel.map.object.Cell;
import com.pixel.map.object.MapObject;
import com.pixel.map.object.building.Building;
import com.pixel.scene.GameScene;
import javafx.collections.MapChangeListener;

import java.util.ArrayList;
import java.util.Random;

public abstract class Zone {

	protected Rectangle rectangle;		// defining rectangle for this object
	protected ArrayList<ZoneCell> availableCells = new ArrayList<>();	// cells open for construction
	protected ZoneCell[][] zoneCells;		// references to the cells of the map within this zone
	protected Building[][] buildings;	// the buildings inside this zone
	protected Building.BuildingType zoneType;	// The types of buildings that belong within this zone
	protected Map parentMap = GameScene.getInstance().getGameMap();
	protected boolean empty = true;
	protected final int distanceFromRoad = 4;

	public static float residentialZonePlacementCost = 25.0f;
	public static float commercialZonePlacementCost = 50.0f;
	public static float officeZonePlacementCost = 75.0f;

	protected Random random = new Random();
	protected boolean zoneFull = false;

	public Zone(Rectangle dimensions, Building.BuildingType type) {

		zoneType = type;

		// define our dimensions
		rectangle = dimensions;

		// create our arrays
		zoneCells = new ZoneCell[(int)dimensions.width][(int)dimensions.height];
		buildings = new Building[(int)dimensions.width][(int)dimensions.height];

		// now we create all our zone cells, the idea is for every cell within this zone we attempt to add a cell
		// to that location. However, we do not put a zone cell on a cell if there is already some object there
		for (int x = 0, mapX = (int)rectangle.x; x < (int)dimensions.width; x++, mapX++) {
			for (int y = 0, mapY = (int)rectangle.y; y < (int)dimensions.height; y++, mapY++) {
				Cell cell = parentMap.getCell(mapX, mapY);

				if(cell.getTopObject() == null && isCellNearRoad(cell)) {

					ZoneCell zoneCell = new ZoneCell(parentMap.getCellWidth(), parentMap.getCellHeight(),
						   cell.getMapPosition(), zoneType, this);
					zoneCells[x][y] = zoneCell;

					cell.addMapObject(zoneCell);
					availableCells.add(zoneCell);
					empty = false;
				}
			}
		}
	}

	private boolean isCellNearRoad(Cell cell) {

		Map.MapCoord coord = cell.getMapPosition();

		for (int i = 1; i < distanceFromRoad + 1; i++) {
			Cell checkedCell = parentMap.getCell((int) coord.x + i, (int) coord.y);
			if (checkedCell != null) {
				MapObject object = checkedCell.getTopObject();
				if (object != null && object.getName().contains("Road")) {
					return true;
				}
			}
			checkedCell = parentMap.getCell((int) coord.x - i, (int) coord.y);
			if (checkedCell != null) {
				MapObject object = checkedCell.getTopObject();
				if (object != null && object.getName().contains("Road")) {
					return true;
				}
			}
			checkedCell = parentMap.getCell((int) coord.x, (int) coord.y + i);
			if (checkedCell != null) {
				MapObject object = checkedCell.getTopObject();
				if (object != null && object.getName().contains("Road")) {
					return true;
				}
			}
			checkedCell = parentMap.getCell((int) coord.x, (int) coord.y - i);
			if (checkedCell != null) {
				MapObject object = checkedCell.getTopObject();
				if (object != null && object.getName().contains("Road")) {
					return true;
				}
			}
		}

		return false;
	}

	protected ArrayList<Map.MapCoord> findSuitableLocation(Rectangle dimensions) {

		ArrayList<Map.MapCoord> locations = new ArrayList<>();

		for (int i = 0; i < rectangle.width && i < zoneCells.length; i++) {
			for (int j = 0; j <rectangle.height && j < zoneCells[i].length; j++) {

				if (checkZoneCell(zoneCells[i][j], i, j, dimensions)) {
					locations.add(parentMap.new MapCoord(i, j));
				}
			}
		}
		return locations;
	}

	private boolean checkZoneCell(ZoneCell cell, int x, int y, Rectangle dimensions) {

		// only proceed if this cell is not null
		if (cell != null) {

			// use these outside of the loop so we can use them later
			int i = x;
			int j = y;

			// depending on the dimensions, we need to check to the left and up
			for (; i > (x - dimensions.width) && i >= 0; i--)
			{
				for (; j > (y - dimensions.height) && j >= 0; j--) {
					if(!checkCell(zoneCells[i][j])) {
						return false;
					}
				}
			}

			// if i or j is negative, then we know we have checked off the map, and therefore this is not a
			// suitable location
			if (i >= 0 && j >= 0) {
				return true;
			}
		}
		return false;
	}

	protected void placeBuilding(Map.MapCoord zoneCellLocation, Building building) {

		// since we know that the object can be placed without any issues, just place it
		Rectangle dimensions = building.getDimensions();
		ZoneCell zoneCell = zoneCells[zoneCellLocation.x][zoneCellLocation.y];
		Cell cell = parentMap.getCell(zoneCell.getMapPosition());

		// we add this building to the desired zone cell as a child actor
		// Offset the image to center the building in the cell grid
		cell.addMapObject(building);
		availableCells.remove(zoneCell);
		building.setMapPosition(cell.getMapPosition().x, cell.getMapPosition().y);
		if (building.getDimensions().width > 1 || building.getDimensions().height > 1) {


			
			// then we need to add this object as an occupying object to all cells underneath this one
			// depending on the dimensions, we need to check to the left and up
			for (int i = zoneCellLocation.x; i >= (zoneCellLocation.x - dimensions.width) && i >= 0; i--) {
				for (int j = zoneCellLocation.y; j >= (zoneCellLocation.y - dimensions.height) && j >= 0; j--) {

					if (zoneCell != zoneCells[i][j] && zoneCells[i][j] != null) {
						cell = (Cell) zoneCells[i][j].getParent();
						cell.addOccupyingObject(building);
						availableCells.remove(zoneCells[i][j]);
					}
				}
			}
		}
	}

	private boolean checkCell(ZoneCell cell) {
		return cell != null && availableCells.contains(cell);
	}

	public abstract void update();

	public void updateFromSurroundings() {

	}

	public Building[][] getBuildings() {
		return buildings;
	}

	public Building.BuildingType getZoneType() {
		return zoneType;
	}

	public boolean isEmpty() {
		return empty;
	}

	public boolean isZoneFull() {
		return zoneFull;
	}

	public void removeZoneCell(ZoneCell zoneCell) {

		availableCells.remove(zoneCell);
	}
}
