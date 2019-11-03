package com.pixel.map.object.roads;

import com.badlogic.gdx.graphics.Texture;
import com.pixel.behavior.ReplaceBehavior;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;
import com.pixel.map.object.MapObject;

public class IntersectionThreeWest extends Road{

	public IntersectionThreeWest(float x, float y, float width, float height, Map.MapCoord coord) {
		super(x, y, width, height, coord, RoadFactory.RoadType.INTERSECT_3_W.getName());

		type = RoadFactory.RoadType.INTERSECT_3_W;

		// set the proper texture here as well
		Texture texture = PixelAssetManager.manager.get(PixelAssetManager.intersectionThreeWest);
		loadTexture(texture, PixelAssetManager.intersectionThreeWest);

		// we need to add our own placementBehavior here
		placementBehaviors.add(new ReplaceBehavior(this));
	}

	@Override
	public MapObject copy() {

		return create(this);
	}

	public static Road create(Road road) {
		return new IntersectionThreeWest(road.getX(), road.getY(), road.getWidth(),
			   road.getHeight(), road.getMapPosition());
	}
}
