package com.pixel.serialization;

import com.pixel.map.MapCoord;
import com.pixel.map.object.Cell;

import java.util.ArrayList;

public class CellSerializable extends MapObjectSerializable{

	public ArrayList<MapObjectSerializable> occupyingObjects;
	public ArrayList<MapObjectSerializable> children;

	@Override
	public MapObjectSerializable getNonSerializableObject() {

		//Cell cell = new Cell(x,y,width,height, new MapCoord(mapPositionX, mapPositionY));

		return null;

	}
}
