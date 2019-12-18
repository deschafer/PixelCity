package com.pixel.map.object.building.special.police;

import com.pixel.city.Financials.Source;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;
import com.pixel.map.MapCoord;
import com.pixel.map.object.MapObject;
import com.pixel.map.object.building.special.ServiceBuilding;

public class PoliceStation extends ServiceBuilding {

	private static final int widthInCells = 2;
	private static final int heightInCells = 2;
	private static final float textureWidth = 132 * widthInCells;
	private static final float textureHeight = 92 * heightInCells;

	public PoliceStation(float x, float y, MapCoord coord) {
		super(x, isometricCorrection, textureWidth, textureHeight, widthInCells, heightInCells, coord, "PoliceStation");

		displayName = "Police Station";

		// we need to load our texture here
		// and set the width and height of it

		loadTexture(PixelAssetManager.manager.get(PixelAssetManager.policeStation), PixelAssetManager.policeStation);
		placedownCost = 20000;
		serviceType = Services.POLICE;

		sources.add(new Source(this, -70));
	}

	@Override
	public MapObject copy() {
		return new PoliceStation(getX(), getY(), getMapPosition());
	}
}
