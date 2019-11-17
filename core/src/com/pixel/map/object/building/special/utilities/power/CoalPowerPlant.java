package com.pixel.map.object.building.special.utilities.power;

import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;

public class CoalPowerPlant extends PowerUtility {

	private static final float textureWidth = 132 * 3;
	private static final float textureHeight = 96 * 3;

	public CoalPowerPlant(float x, float y, Map.MapCoord coord) {
		super(x, y, (int) textureWidth, (int) textureHeight, coord, "CoalPowerPlant", 100);

		loadTexture(PixelAssetManager.manager.get(PixelAssetManager.coalPowerPlant), PixelAssetManager.coalPowerPlant);

	}
}
