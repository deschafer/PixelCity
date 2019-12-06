package com.pixel.serialization;

import java.io.Serializable;

public abstract class MapObjectSerializable implements Serializable {

	public int mapPositionX;
	public int mapPositionY;
	public float x = 0;	// lots of our map objects are added to cells and have a 0,0 position, so we set it as default
	public float y = 0; // ..
	public float width;
	public float height;
	public String name;

	public abstract MapObjectSerializable getNonSerializableObject();
}
