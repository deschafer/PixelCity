package com.pixel.map.object.zoning;

import com.badlogic.gdx.math.Rectangle;
import com.pixel.city.Demand;
import com.pixel.map.Map;
import com.pixel.map.MapCoord;
import com.pixel.map.object.Cell;
import com.pixel.map.object.MapObject;
import com.pixel.map.object.building.Building;
import com.pixel.map.object.building.BuildingFactory;
import com.pixel.scene.GameScene;
import javafx.collections.MapChangeListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Zone implements Serializable {

	private Rectangle rectangle;		// defining rectangle for this object
	private ArrayList<ZoneCell> availableCells = new ArrayList<>();	// cells open for construction
	private ZoneCell[][] zoneCells;		// references to the cells of the map within this zone
	private Building.BuildingType zoneType;	// The types of buildings that belong within this zone
	private Map parentMap = GameScene.getInstance().getGameMap();
	private boolean empty = true;
	private static final int distanceFromRoad = 4;

	public static float residentialZonePlacementCost = 25.0f;
	public static float commercialZonePlacementCost = 50.0f;
	public static float officeZonePlacementCost = 75.0f;

	private Random random = new Random();
	private boolean zoneFull = false;

	public Zone(Rectangle dimensions, Building.BuildingType type) {

		zoneType = type;

		// define our dimensions
		rectangle = dimensions;

		// create our arrays
		zoneCells = new ZoneCell[(int)dimensions.width][(int)dimensions.height];

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

		MapCoord coord = cell.getMapPosition();

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

	protected ArrayList<MapCoord> findSuitableLocation(Rectangle dimensions) {

		ArrayList<MapCoord> locations = new ArrayList<>();

		for (int i = 0; i < rectangle.width && i < zoneCells.length; i++) {
			for (int j = 0; j <rectangle.height && j < zoneCells[i].length; j++) {

				if (checkZoneCell(zoneCells[i][j], i, j, dimensions)) {
					locations.add(new MapCoord(i, j));
				}
			}
		}
		return locations;
	}

	private boolean checkZoneCell(ZoneCell zoneCell, int x, int y, Rectangle dimensions) {

		// only proceed if this cell is not null
		if (zoneCell != null) {

			if (dimensions.height == 1 && dimensions.width == 1) {
				return checkCell(zoneCell);
			} else {

				// TODO: this section below does not work and has been removed.

				/*

				// use these outside of the loop so we can use them later
				int i = x;
				int j = y;

				// depending on the dimensions, we need to check to the left and up
				for (; i > (x - dimensions.width) && i >= 0; i--) {
					for (j = y; j > (y - dimensions.height) && j >= 0; j--) {
						if (!checkCell(zoneCells[i][j])) {
							return false;
						}
					}
				}

				// if i or j is negative, then we know we have checked off the map, and therefore this is not a
				// suitable location
				if (i >= 0 && j >= 0) {
					return true;
				}*/
			}
		}
		return false;
	}

	protected void placeBuilding(MapCoord zoneCellLocation, Building building) {

		// since we know that the object can be placed without any issues, just place it
		Rectangle dimensions = building.getDimensions();
		ZoneCell zoneCell = zoneCells[zoneCellLocation.x][zoneCellLocation.y];
		Cell cell = parentMap.getCell(zoneCell.getMapPosition());

		// we add this building to the desired zone cell as a child actor
		// Offset the image to center the building in the cell grid
		cell.addMapObject(building);
		building.setMapPosition(cell.getMapPosition().x, cell.getMapPosition().y);
		//zoneCell.setColor(1,0,0,1);

		// TODO: this section below is not working, needs to be redone
		/*
		if (building.getDimensions().width > 1 || building.getDimensions().height > 1) {

			// TODO: follow our sheet, calc the size variable, then offset it with those equations
			float sizeInCells = building.getDimensions().width * 0.5f + building.getDimensions().height * 0.5f;
			float widthPixels = sizeInCells * Map.cellWidth;
			float heightPixels = sizeInCells * Map.cellRowHeight;

			//float xOffset = zoneCellLocation.x + widthPixels / 2 - building.getWidth() / 2;
			//float yOffset = zoneCellLocation.y + heightPixels / 2 - building.getHeight() / 2;
			float xOffset = Map.cellWidth / 2.0f - building.getWidth() / 2;
			float yOffset = building.getHeight();

			building.setX(xOffset);
			//building.setY();

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
		 */

		availableCells.remove(zoneCell);
	}

	private boolean checkCell(ZoneCell zoneCell) {

		if (zoneCell != null && availableCells.contains(zoneCell) && zoneCell.hasParent()) {
			Cell cell = (Cell)zoneCell.getParent();

			// then this cell contains the zoneCell as a child, but this must be the top object
			if (cell.getTopObject().getName().contains("Zoning")) {
				return true;
			}
		}
		return false;
	}

	public void update() {
		// If no cells are available, then we cannot build a new building
		if (availableCells.isEmpty()) {
			zoneFull = true;
			return;
		}

		// then we need to verify that we have demand prior to building
		if (Demand.getInstance().isTypeDemanded(zoneType)) {

			// get a building from the BuildingFactory
			Building building =
				   BuildingFactory.getInstance().create(new MapCoord(0,0), zoneType, 0);

			ArrayList<MapCoord> suitableCellLocations;

			// now we need to find a position for this building if there is one available
			// use our findSuitableLocation function from zone to get a placement for this building

			//  if there is a suitable location
			if (!(suitableCellLocations = findSuitableLocation(building.getDimensions())).isEmpty()) {

				// then we found a suitableCell, and we need to place this object
				placeBuilding(suitableCellLocations.get(random.nextInt(suitableCellLocations.size())), building);
			}
			else {
				System.out.println("Position not found for building");
				building.remove();
			}
		}
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
