package com.pixel.map.object.building.special.health;

import com.pixel.city.Financials.Source;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;
import com.pixel.map.object.MapObject;
import com.pixel.map.object.building.special.ServiceBuilding;

public class Hospital extends ServiceBuilding {


	private static final float textureWidth = 132 * 2;
	private static final float textureHeight = 112 * 2;

	public Hospital(float x, float y, Map.MapCoord coord) {
		super(x, isometricCorrection, textureWidth, textureHeight, coord, "Hospital");

		// we need to load our texture here
		// and set the width and height of it

		loadTexture(PixelAssetManager.manager.get(PixelAssetManager.hospital), PixelAssetManager.hospital);
		placedownCost = 60000;

		sources.add(new Source(this, -70));
	}

	@Override
	public MapObject copy() {
		return new Hospital(getX(), getY(), getMapPosition());
	}
}
