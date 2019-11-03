package com.pixel.map.object.roads;

import com.badlogic.gdx.graphics.Texture;
import com.pixel.behavior.ReplaceBehavior;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;
import com.pixel.map.object.MapObject;

public class IntersectionFour extends Road{

	public IntersectionFour(float x, float y, float width, float height, Map.MapCoord coord) {
		super(x, y, width, height, coord, RoadFactory.RoadType.INTERSECT_4.getName());

		type = RoadFactory.RoadType.INTERSECT_4;

		// set the proper texture here as well
		Texture texture = PixelAssetManager.manager.get(PixelAssetManager.intersectionFour);
		loadTexture(texture, PixelAssetManager.intersectionFour);

		// we need to add our own placementBehavior here
		placementBehaviors.add(new ReplaceBehavior(this));
	}

	@Override
	public MapObject copy() {

		return create(this);
	}

	public static Road create(Road road) {
		return new IntersectionFour(road.getX(), road.getY(), road.getWidth(),
			   road.getHeight(), road.getMapPosition());
	}
}
