package com.pixel.map.object.building.special;

import com.badlogic.gdx.math.Rectangle;
import com.pixel.map.Map;
import com.pixel.map.object.MapObject;

public abstract class SpecialtyBuilding extends MapObject {

	protected Rectangle dimensions = new Rectangle();	// refers to the coordinates in the map
	public static final int isometricCorrection = 35;

	public SpecialtyBuilding(float x, float y, float width, float height, Map.MapCoord coord, String ID) {
		super(x, y, width, height, coord, ID);

		// this object cannot be replaced
		replaceable = false;

		dimensions.x = coord.x;
		dimensions.y = coord.y;
		dimensions.width = width / Map.cellWidth;
		dimensions.height = height / Map.cellHeight;
	}

	public Rectangle getDimensions() {
		return dimensions;
	}
}
