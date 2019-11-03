package com.pixel.map.object.roads;

import com.badlogic.gdx.graphics.Texture;
import com.pixel.behavior.ReplaceBehavior;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;
import com.pixel.map.object.MapObject;

public class RoadwayEndEast extends Road {

	public RoadwayEndEast(float x, float y, float width, float height, Map.MapCoord coord) {
		super(x, y, width, height, coord, RoadFactory.RoadType.END_E.getName());

		type = RoadFactory.RoadType.END_E;

		// set the proper texture here as well
		Texture texture = PixelAssetManager.manager.get(PixelAssetManager.roadwayEndEast);
		loadTexture(texture, PixelAssetManager.roadwayEndEast);

		// we need to add our own placementBehavior here
		placementBehaviors.add(new ReplaceBehavior(this));
	}

	@Override
	public MapObject copy() {

		return create(this);
	}

	public static Road create(Road road) {
		return new RoadwayEndEast(road.getX(), road.getY(), road.getWidth(),
			   road.getHeight(), road.getMapPosition());
	}
}
