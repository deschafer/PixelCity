package com.pixel.map.object.building.special;

import com.badlogic.gdx.math.Rectangle;
import com.pixel.map.Map;
import com.pixel.map.MapCoord;
import com.pixel.map.object.Cell;
import com.pixel.map.object.MapObject;
import com.pixel.serialization.MapObjectSerializable;
import com.pixel.serialization.SpecialtyBuildingSerializable;

import java.util.ArrayList;

public abstract class SpecialtyBuilding extends MapObject {

	protected Rectangle dimensions = new Rectangle();	// refers to the coordinates in the map
	public static final int isometricCorrection = 35;
	protected ArrayList<Cell> occupyingCells = new ArrayList<>();

	public SpecialtyBuilding(float x, float y, float width, float height, int widthInCells, int heightInCells, MapCoord coord, String ID) {
		super(x, y, width, height, coord, ID);

		// this object cannot be replaced
		replaceable = false;

		dimensions.x = coord.x;
		dimensions.y = coord.y;
		dimensions.width = widthInCells;
		dimensions.height = heightInCells;
	}

	public void initialize() { }

	public Rectangle getDimensions() {
		return dimensions;
	}

	public void addOccupyingCell(Cell cell) {
		occupyingCells.add(cell);
	}

	@Override
	public boolean remove() {
		for (Cell cell : occupyingCells) {
			if (cell != null)
				cell.removeOccupyingObject(this);
		}
		return super.remove();
	}
}
