package com.pixel.serialization;

import com.pixel.map.object.MapObject;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class MapObjectSerializable implements Serializable {

	public int mapPositionX;
	public int mapPositionY;
	public float x = 0;	// lots of our map objects are added to cells and have a 0,0 position, so we set it as default
	public float y = 0; // ..
	public float width;
	public float height;
	public String name;
	public ArrayList<SourceSerializable> sources;

	// TODO: we should probably store sources in the map object class and then add them to the financial class once
	// serialization is complete.

	public abstract MapObject getNonSerializableObject();
}
