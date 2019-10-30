package com.pixel.behavior;

import com.pixel.map.object.MapObject;

public abstract class Behavior {

	protected MapObject owner;

	public Behavior(MapObject owner) {
		this.owner = owner;
	}

	public abstract void act();
}
