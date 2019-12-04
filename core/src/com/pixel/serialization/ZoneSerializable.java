package com.pixel.serialization;

import com.badlogic.gdx.math.Rectangle;
import com.pixel.map.object.building.Building;

import java.util.ArrayList;

public class ZoneSerializable {

	public ArrayList<ZoneCellSerializable> zoneCells;
	public ArrayList<ZoneCellSerializable> availableCells;
	public Rectangle dimensions;
	public boolean empty;
	public boolean zoneFull;
	Building.BuildingType zoneType;
}