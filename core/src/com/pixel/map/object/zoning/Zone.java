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
import com.pixel.serialization.ZoneSerializable;
import javafx.collections.MapChangeListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Zone {

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

	private static Random random = new Random();
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

	public Zone(ZoneSerializable serializable) {

		rectangle = serializable.dimensions;
		empty = serializable.empty;
		zoneFull = serializable.zoneFull;
		zoneType = serializable.zoneType;

		// create our arrays
		zoneCells = new ZoneCell[(int)rectangle.width][(int)rectangle.height];

		// we will not load in zone cells yet
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

				if (checkCell(zoneCells[i][j])) {
					locations.add(new MapCoord(i, j));
				}
			}
		}
		return locations;
	}

	protected void placeBuilding(ZoneCell zoneCell, Building building) {

		// since we know that the object can be placed without any issues, just place it
		Rectangle dimensions = building.getDimensions();
		Cell cell = parentMap.getCell(zoneCell.getMapPosition());

		// we add this building to the desired zone cell as a child actor
		// Offset the image to center the building in the cell grid
		cell.addMapObject(building);
		building.setMapPosition(cell.getMapPosition().x, cell.getMapPosition().y);

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
			if (!availableCells.isEmpty()) {
				ZoneCell zoneCell = availableCells.get(random.nextInt(availableCells.size()));
				if (zoneCell.hasParent() && zoneCell.getParent() instanceof Cell) {
					Cell cell = (Cell)zoneCell.getParent();
					if (cell.getTopObject() instanceof ZoneCell) {
						placeBuilding(zoneCell, building);
					}
				}
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

	public Rectangle getRectangle() {
		return rectangle;
	}

	public void removeZoneCell(ZoneCell zoneCell) {

		availableCells.remove(zoneCell);
	}

	public ZoneSerializable getSerializableObject() {
		ZoneSerializable serializable = new ZoneSerializable();
		serializable.dimensions = rectangle;
		serializable.empty = empty;
		serializable.zoneFull = zoneFull;
		serializable.zoneType = zoneType;

		return serializable;
	}

	public void addAvailableZoneCell(ZoneCell zoneCell) {
		availableCells.add(zoneCell);
	}

	public void addZoneCell(ZoneCell zoneCell) {

		int x = zoneCell.getMapPosition().x;
		int y = zoneCell.getMapPosition().y;

		if (rectangle.contains(x, y)) {
			// translate to our array
			x -= rectangle.x;
			y -= rectangle.y;

			try {
				zoneCells[x][y] = zoneCell;
			} catch (ArrayIndexOutOfBoundsException ex) {
				System.out.println("Exception");
			}
		}
	}
}
