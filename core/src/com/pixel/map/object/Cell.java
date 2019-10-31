package com.pixel.map.object;

import com.pixel.map.Map;

//
// Cell
// Each cell serves as just a container for additional map objects located at the position of this cell
// in the map.
//

public class Cell extends MapObject {

	public Cell(float x, float y, float width, float height, Map.MapCoord coord) {
		super(x, y, width,height, coord, "com.pixel.map.object.Cell");

		// This cell has a basic texture within it
		loadTexture("assets/landscape/PNG/landscapeTiles_075.png");
		setSize(width, height);
	}

	// TODO:
	// cell should also override placeOverObject
	// contain a place on top of placementBehavior
}
