package com.pixel.map;

import java.io.Serializable;

public class MapCoord implements Serializable {

	public int x;
	public int y;

	public MapCoord(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public MapCoord(float x, float y) {
		this.x = (int)x;
		this.y = (int)y;
	}
}