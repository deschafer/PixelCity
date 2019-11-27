package com.pixel.map.object.building.special.utilities.water;

import com.pixel.map.Map;
import com.pixel.map.object.building.special.SpecialtyBuilding;
import com.pixel.map.object.building.special.utilities.UtilityManager;

public class WaterUtility extends SpecialtyBuilding {

	private float waterSupplied;

	public WaterUtility(float x, float y, float width, float height, int widthInCells, int heightInCells, Map.MapCoord coord, String ID, float powerSupplied) {
		super(x, y, width, height, widthInCells, heightInCells, coord, ID + "Utility");

		this.waterSupplied = waterSupplied;

		// add this utility to the manager
		UtilityManager.getInstance().addWaterUtility(this);
	}

	@Override
	public boolean remove() {
		UtilityManager.getInstance().removeWaterUtility(this);

		return super.remove();
	}

	public float getWaterSupplied() {
		return waterSupplied;
	}
}
