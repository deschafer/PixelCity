package com.pixel.map.object.building.special;

import com.badlogic.gdx.math.Rectangle;
import com.pixel.map.Map;
import com.pixel.map.object.MapObject;

public abstract class SpecialtyBuilding extends MapObject {

	private Rectangle dimensions;	// refers to the coordinates in the map

	public SpecialtyBuilding(float x, float y, float width, float height, Map.MapCoord coord, String ID) {
		super(x, y, width, height, coord, ID);

		// this object cannot be replaced
		replaceable = false;
	}
}
