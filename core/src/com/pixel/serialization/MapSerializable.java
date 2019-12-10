package com.pixel.serialization;

import com.badlogic.gdx.math.Vector2;
import com.pixel.map.object.zoning.Zone;

import java.io.Serializable;
import java.util.ArrayList;

public class MapSerializable implements Serializable {

	public float widthPixels;
	public float heightPixels;
	public int width;
	public int height;
	public Vector2 topOfMap;

	public ArrayList<ZoneSerializable> residentialZones;
	public ArrayList<ZoneSerializable> commercialZones;
	public ArrayList<ZoneSerializable> officeZones;

}
