package com.pixel.map.object.building.special.fire;

import com.pixel.city.Financials.Source;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;
import com.pixel.map.MapCoord;
import com.pixel.map.object.MapObject;
import com.pixel.map.object.building.special.ServiceBuilding;

public class FireStation extends ServiceBuilding {

	private static final int widthInCells = 3;
	private static final int heightInCells = 3;
	private static final float textureWidth = 132 * widthInCells;
	private static final float textureHeight = 83 * heightInCells;

	public FireStation(float x, float y, MapCoord coord) {
		super(x, isometricCorrection, textureWidth, textureHeight, widthInCells, heightInCells, coord, "FireStation");

		displayName = "Fire Station";

		// we need to load our texture here
		// and set the width and height of it

		loadTexture(PixelAssetManager.manager.get(PixelAssetManager.fireStation), PixelAssetManager.fireStation);
		placedownCost = 20000;
		serviceType = Services.FIRE;

		sources.add(new Source(this, -75));
	}

	@Override
	public MapObject copy() {
		return new FireStation(getX(), getY(), getMapPosition());
	}
}
