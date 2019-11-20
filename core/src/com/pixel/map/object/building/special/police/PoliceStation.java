package com.pixel.map.object.building.special.police;

import com.pixel.city.Financials.Source;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;
import com.pixel.map.object.MapObject;
import com.pixel.map.object.building.special.ServiceBuilding;

public class PoliceStation extends ServiceBuilding {

	private static final float textureWidth = 132 * 2;
	private static final float textureHeight = 92 * 2;

	public PoliceStation(float x, float y, Map.MapCoord coord) {
		super(x, isometricCorrection, textureWidth, textureHeight, coord, "PoliceStation");

		// we need to load our texture here
		// and set the width and height of it

		loadTexture(PixelAssetManager.manager.get(PixelAssetManager.policeStation), PixelAssetManager.policeStation);
		placedownCost = 20000;

		sources.add(new Source(this, -70));
	}

	@Override
	public MapObject copy() {
		return new PoliceStation(getX(), getY(), getMapPosition());
	}
}
