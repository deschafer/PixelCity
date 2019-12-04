package com.pixel.serialization;

import com.badlogic.gdx.math.Rectangle;
import com.pixel.map.object.Cell;
import java.util.ArrayList;

public class SpecialtyBuildingSerializable extends MapObjectSerializable {

	public Rectangle dimensions = new Rectangle();	// refers to the coordinates in the map
	public ArrayList<Cell> occupyingCells = new ArrayList<>();
}
