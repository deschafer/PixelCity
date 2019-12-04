package com.pixel.serialization;

import com.pixel.map.Map;

import java.io.Serializable;

public class MapObjectSerializable implements Serializable {

	public Map.MapCoord position;
	public String name;
}
