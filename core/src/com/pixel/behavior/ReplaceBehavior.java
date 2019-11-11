package com.pixel.behavior;

import com.pixel.map.object.Cell;
import com.pixel.map.object.MapObject;
import com.pixel.scene.GameScene;

//
// This is a simple replacement behavior
//
//

public class ReplaceBehavior extends PlacementBehavior {

	public ReplaceBehavior(MapObject owner) {
		super(owner);
	}

	@Override
	public void act() {

		if(other != null) {

			// get the cell assoc with this map object
			Cell cell = GameScene.getInstance().getGameMap().getCell(owner.getMapPosition());

			// remove this object from this cell and stage
			cell.removeActor(owner);
			owner.remove();

			// then add the new object to this same cell
			cell.addMapObject(other);

			other = null;
		}
	}
}
