package com.pixel.serialization;

import com.badlogic.gdx.math.Rectangle;
import com.pixel.map.object.MapObject;
import com.pixel.map.object.building.Building;

import java.util.ArrayList;

public class BuildingSerializable extends MapObjectSerializable {

	public Rectangle dimensions;	// refers to the coordinates in the map
	public Building.BuildingType type;               // the type of this building
	public int level;                       // the level of this object
	public ArrayList<ResidentSerializable> residents;   // list of people assoc'd with this object

	@Override
	public MapObject getNonSerializableObject() {

		// we need to get the object
		return null;
	}
}
