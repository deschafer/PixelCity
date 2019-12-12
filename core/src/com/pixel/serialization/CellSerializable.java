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
		ArrayList<String> objectNames = new ArrayList<>();


		if (!children.isEmpty() && !occupyingObjects.isEmpty()) {
			System.out.println("");
		}

		// we first get the children, since all children in this case will be an occupying object
		for (MapObjectSerializable serializable : children) {

			MapObject object = serializable.getNonSerializableObject();
			if (object != null) {
				// this will add to occupying and children lists
				cell.addMapObject(object);
				objectNames.add(object.getName());
			}
		}

		// then we do the same for the objects we have not already saved
		for (MapObjectSerializable serializable : occupyingObjects) {
			if (!children.contains(serializable) && !objectNames.contains(serializable.name)) {
				cell.addOccupyingObject(serializable.getNonSerializableObject());
			}
		}

		return cell;
	}
}
