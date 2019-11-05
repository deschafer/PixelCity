package com.pixel.map.object.zoning;

import com.badlogic.gdx.math.Rectangle;
import com.pixel.map.Map;
import com.pixel.map.object.Cell;
import com.pixel.map.object.MapObject;
import com.pixel.map.object.building.Building;
import com.pixel.scene.GameScene;

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

	protected float timer = 0;
	protected float timeMax = 1.0f;
	protected Random random = new Random();

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

					cell.addActor(zoneCell);
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
}
