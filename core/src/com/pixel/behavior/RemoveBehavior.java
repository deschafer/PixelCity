package com.pixel.behavior;

import com.pixel.map.object.Cell;
import com.pixel.map.object.MapObject;

public class RemoveBehavior extends PlacementBehavior {

	public RemoveBehavior(MapObject owner) {
		super(owner);
	}

	@Override
	public void act() {
		if (owner.getParent() != null && owner.getParent() instanceof Cell) {
			Cell cell = (Cell)owner.getParent();
			cell.removeActor(owner);
		}

		owner.remove();
	}
}
