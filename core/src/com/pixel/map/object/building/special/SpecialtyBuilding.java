package com.pixel.map.object.building.special;

import com.badlogic.gdx.math.Rectangle;
import com.pixel.map.Map;
import com.pixel.map.object.MapObject;
import com.pixel.scene.GameScene;

public abstract class SpecialtyBuilding extends MapObject {

	protected Rectangle dimensions = new Rectangle();	// refers to the coordinates in the map

	public SpecialtyBuilding(float x, float y, float width, float height, Map.MapCoord coord, String ID) {
		super(x, y, width, height, coord, ID);

		// this object cannot be replaced
		replaceable = false;

		dimensions.x = coord.x;
		dimensions.y = coord.y;
		dimensions.width = width / GameScene.getInstance().getGameMap().getCellWidth();
		dimensions.height = height / GameScene.getInstance().getGameMap().getCellHeight();
	}

	public Rectangle getDimensions() {
		return dimensions;
	}
}
