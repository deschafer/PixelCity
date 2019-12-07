package com.pixel.serialization;

import com.pixel.map.MapCoord;
import com.pixel.map.object.MapObject;
import com.pixel.map.object.building.Building;
import com.pixel.map.object.zoning.ZoneCell;

public class ZoneCellSerializable extends MapObjectSerializable {
	public Building.BuildingType type;
	public boolean valid = true;

	@Override
	public MapObject getNonSerializableObject() {

		//ZoneCell cell = new ZoneCell(x, y, width, height, new MapCoord(mapPositionX, mapPositionY), type);

		return null;
	}
}
