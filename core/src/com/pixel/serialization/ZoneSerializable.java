package com.pixel.serialization;

import com.badlogic.gdx.math.Rectangle;
import com.pixel.map.object.building.Building;

import java.io.Serializable;
import java.util.ArrayList;

public class ZoneSerializable implements Serializable {

	public Rectangle dimensions;
	public boolean empty;
	public boolean zoneFull;
	public Building.BuildingType zoneType;
}
