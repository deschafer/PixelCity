package com.pixel.behavior;

import com.pixel.map.object.MapObject;

public abstract class PlacementBehavior extends Behavior {

	protected MapObject other;

	public PlacementBehavior(MapObject owner) {
		super(owner);
	}

	public void setPlacement(MapObject object) {
		other = object;
	}
}
