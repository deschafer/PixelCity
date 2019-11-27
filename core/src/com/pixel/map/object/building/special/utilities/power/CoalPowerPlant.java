package com.pixel.map.object.building.special.utilities.power;

import com.pixel.city.Financials.Source;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;
import com.pixel.map.object.MapObject;

public class CoalPowerPlant extends PowerUtility {

	private static final int widthInCells = 3;
	private static final int heightInCells = 3;
	private static final float textureWidth = 132 * widthInCells;
	private static final float textureHeight = 96 * heightInCells;

	public CoalPowerPlant(float x, float y, Map.MapCoord coord) {
		super(x, isometricCorrection, (int) textureWidth, (int) textureHeight, widthInCells, heightInCells, coord, "CoalPowerPlant", 100);

		loadTexture(PixelAssetManager.manager.get(PixelAssetManager.coalPowerPlant), PixelAssetManager.coalPowerPlant);
		placedownCost = 50000;

		sources.add(new Source(this, -100));
	}

	@Override
	public MapObject copy() {
		return new CoalPowerPlant(getX(), getY(), getMapPosition());
	}
}
