package com.pixel.serialization;

import com.badlogic.gdx.math.Rectangle;
import com.pixel.map.MapCoord;
import com.pixel.map.object.Cell;
import com.pixel.map.object.MapObject;
import com.pixel.map.object.building.special.SpecialtyBuilding;
import com.pixel.scene.GameScene;

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

				if (object instanceof SpecialtyBuilding) {
					// we add this object to the map's list
					GameScene.getInstance().getGameMap().addLoadedSpecBuilding((SpecialtyBuilding)object);
				}

				objectNames.add(object.getName());
			}
		}

		// NOTE: Since occupying objects refer to existing objects, we cannot instantiate new objects from these
		// serializables.
		/*
		for (MapObjectSerializable serializable : occupyingObjects) {
			if (!children.contains(serializable) && !objectNames.contains(serializable.name)) {
				//cell.addOccupyingObject(serializable.getNonSerializableObject());
			}
		}
		*/

		return cell;
	}
}
