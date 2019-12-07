package com.pixel.serialization;

import com.pixel.map.MapCoord;
import com.pixel.map.object.Cell;
import com.pixel.map.object.MapObject;

import java.util.ArrayList;

public class CellSerializable extends MapObjectSerializable{

	public ArrayList<MapObjectSerializable> occupyingObjects;
	public ArrayList<MapObjectSerializable> children;

	@Override
	public MapObject getNonSerializableObject() {

		// create a new cell from this serializable
		Cell cell = new Cell(x, y, width, height, new MapCoord(mapPositionX, mapPositionY));

		// then we need to get non serializables from all the serializables stored in this object
		for (MapObjectSerializable serializable : occupyingObjects) {
			cell.addOccupyingObject(serializable.getNonSerializableObject());
		}

		// then we do the same for the children objects
		for (MapObjectSerializable serializable : children) {
			cell.addActor(serializable.getNonSerializableObject());
		}

		return null;
	}
}
