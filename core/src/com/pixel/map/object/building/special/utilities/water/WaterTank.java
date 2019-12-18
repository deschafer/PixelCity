package com.pixel.map.object.building.special.utilities.water;

import com.pixel.city.Financials.Source;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;
import com.pixel.map.MapCoord;
import com.pixel.map.object.MapObject;

public class WaterTank extends WaterUtility {

	private static final int widthInCells = 1;
	private static final int heightInCells = 1;
	private static final float textureWidth = 132;
	private static final float textureHeight = 118;
	private static final float waterSupplied = 490000000;

	public WaterTank(float x, float y, MapCoord coord, boolean placedOnMap) {
		super(x, y, (int)textureWidth, (int)textureHeight, widthInCells, heightInCells, coord, "WaterTank", placedOnMap ? waterSupplied : 0 , placedOnMap);
		if (placedOnMap) {
			placedownCost = 5000;

			sources.add(new Source(this, -20));
		}
	}

	public WaterTank(float x, float y, MapCoord coord) {
		super(x, y, (int)textureWidth, (int)textureHeight, widthInCells, heightInCells, coord, "WaterTank", 0, false);
	}
	{
		displayName = "Water Tank";
		loadTexture(PixelAssetManager.manager.get(PixelAssetManager.waterTank), PixelAssetManager.waterTank);
	}

	@Override
	public MapObject copy() {
		return new WaterTank(getX(), getY(), getMapPosition(), true);
	}

	@Override
	public void initialize() {

	}
}
