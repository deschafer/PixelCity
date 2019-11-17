package com.pixel.map.object.building.special.utilities.fire;

import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;
import com.pixel.map.object.building.special.ServiceBuilding;

public class FireStation extends ServiceBuilding {

	private static final float textureWidth = 132 * 3;
	private static final float textureHeight = 83 * 3;

	public FireStation(float x, float y, Map.MapCoord coord) {
		super(x, y + 10, textureWidth, textureHeight, coord, "FireStation");

		// we need to load our texture here
		// and set the width and height of it

		loadTexture(PixelAssetManager.manager.get(PixelAssetManager.fireStation), PixelAssetManager.fireStation);
	}
}
