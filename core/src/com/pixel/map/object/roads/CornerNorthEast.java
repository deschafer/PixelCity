package com.pixel.map.object.roads;

import com.badlogic.gdx.graphics.Texture;
import com.pixel.behavior.CornerNorthEastPlacement;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;
import com.pixel.map.object.MapObject;

public class CornerNorthEast extends Road {

	public CornerNorthEast(float x, float y, float width, float height, Map.MapCoord coord) {
		super(x, y, width, height, coord, RoadFactory.RoadType.CORNER_NE.getName());

		type = RoadFactory.RoadType.CORNER_NE;

		// set the proper texture here as well
		Texture texture = PixelAssetManager.manager.get(PixelAssetManager.cornerNorthEast);
		loadTexture(texture, PixelAssetManager.cornerNorthEast);

		// we need to add our own placementBehavior here
		placementBehavior = new CornerNorthEastPlacement(this);
	}

	@Override
	public MapObject copy() {

		return create(this);
	}

	public static Road create(Road road) {
		return new CornerNorthEast(road.getX(), road.getY(), road.getWidth(),
			   road.getHeight(), road.getMapPosition());
	}
}
