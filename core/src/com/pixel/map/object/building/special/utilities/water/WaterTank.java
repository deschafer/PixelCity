package com.pixel.map.object.building.special.utilities.water;

import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;

public class WaterTank extends WaterUtility {

	private static final float textureWidth = 132;
	private static final float textureHeight = 118;

	public WaterTank(float x, float y, Map.MapCoord coord) {
		super(x, y, (int)textureWidth, (int)textureHeight, coord, "CoalPowerPlant", 25);

		loadTexture(PixelAssetManager.manager.get(PixelAssetManager.waterTank), PixelAssetManager.waterTank);
	}
}
