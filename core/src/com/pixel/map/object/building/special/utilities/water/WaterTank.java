package com.pixel.map.object.building.special.utilities.water;

import com.pixel.city.Financials.Source;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;
import com.pixel.map.object.MapObject;

public class WaterTank extends WaterUtility {

	private static final float textureWidth = 132;
	private static final float textureHeight = 118;

	public WaterTank(float x, float y, Map.MapCoord coord) {
		super(x, y, (int)textureWidth, (int)textureHeight, coord, "WaterTank", 25);

		loadTexture(PixelAssetManager.manager.get(PixelAssetManager.waterTank), PixelAssetManager.waterTank);
		placedownCost = 5000;

		sources.add(new Source(this, -20));
	}

	@Override
	public MapObject copy() {
		return new WaterTank(getX(), getY(), getMapPosition());
	}
}
