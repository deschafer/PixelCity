package com.pixel.behavior;

import com.pixel.map.object.Cell;
import com.pixel.map.object.MapObject;
import com.pixel.map.object.roads.Road;
import com.pixel.map.object.roads.RoadFactory;
import com.pixel.scene.GameScene;

public abstract class RoadPlacement extends PlacementBehavior {

	protected Road otherRoad;

	public RoadPlacement(MapObject owner) {
		super(owner);
	}

	public void setOtherRoad(Road otherRoad) {
		this.otherRoad = otherRoad;
	}

	protected void transform(RoadFactory.RoadType newType) {

		Cell cell1 = GameScene.getInstance().getGameMap().getCell(owner.getMapPosition());
		Cell cell = (Cell)owner.getParent();

		// Remove the current object located at this cell
		cell.removeActor(owner);
		owner.remove();

		Road newRoad = RoadFactory.getInstance().getRoad(newType);
		newRoad.setMapPosition(owner.getMapPosition().x, owner.getMapPosition().y);
		newRoad.setSize(owner.getWidth(), owner.getHeight());
		cell.addActor(newRoad);

		// finally, remove our owner cell
		owner = null;
	}
}
