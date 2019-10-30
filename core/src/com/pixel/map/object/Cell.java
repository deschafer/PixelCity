package com.pixel.map.object;

import com.pixel.map.Map;

public class Cell extends MapObject {

	public Cell(float x, float y, float width, float height, Map.MapCoord coord) {
		super(x, y, width,height, coord, "grass");

		// This cell has a basic texture within it
		loadTexture("assets/landscape/PNG/landscapeTiles_075.png");
		setSize(width, height);
	}
}
