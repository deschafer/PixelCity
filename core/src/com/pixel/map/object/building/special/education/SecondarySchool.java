package com.pixel.map.object.building.special.education;

import com.pixel.city.Financials.Source;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;
import com.pixel.map.object.MapObject;
import com.pixel.map.object.building.special.ServiceBuilding;

public class SecondarySchool extends ServiceBuilding {

	private static final int widthInCells = 7;
	private static final int heightInCells = 7;
	private static final float textureWidth = 132 * widthInCells;
	private static final float textureHeight = 67.33333f * heightInCells;

	public SecondarySchool(float x, float y, Map.MapCoord coord) {
		super(x, isometricCorrection, textureWidth, textureHeight, widthInCells, heightInCells, coord, "SecondarySchool");

		// we need to load our texture here
		// and set the width and height of it

		loadTexture(PixelAssetManager.manager.get(PixelAssetManager.secondarySchool), PixelAssetManager.secondarySchool);
		placedownCost = 30000;
		serviceType = Services.EDUCATION;

		sources.add(new Source(this, -50));
	}

	@Override
	public MapObject copy() {
		return new SecondarySchool(getX(), getY(), getMapPosition());
	}

}
