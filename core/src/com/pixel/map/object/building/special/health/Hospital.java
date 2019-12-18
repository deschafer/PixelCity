package com.pixel.map.object.building.special.health;

import com.pixel.city.Financials.Source;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;
import com.pixel.map.MapCoord;
import com.pixel.map.object.MapObject;
import com.pixel.map.object.building.special.ServiceBuilding;

public class Hospital extends ServiceBuilding {

	private static final int widthInCells = 2;
	private static final int heightInCells = 2;
	private static final float textureWidth = 132 * widthInCells;
	private static final float textureHeight = 112 * heightInCells;

	public Hospital(float x, float y, MapCoord coord) {
		super(x, isometricCorrection, textureWidth, textureHeight, widthInCells, heightInCells, coord, "Hospital");

		displayName = "Hospital";

		// we need to load our texture here
		// and set the width and height of it

		loadTexture(PixelAssetManager.manager.get(PixelAssetManager.hospital), PixelAssetManager.hospital);
		placedownCost = 60000;
		serviceType = Services.HEALTH;

		sources.add(new Source(this, -70));
	}

	@Override
	public MapObject copy() {
		return new Hospital(getX(), getY(), getMapPosition());
	}
}
