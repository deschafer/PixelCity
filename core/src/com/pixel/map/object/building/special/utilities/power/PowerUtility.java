package com.pixel.map.object.building.special.utilities.power;

import com.pixel.map.Map;
import com.pixel.map.object.building.special.SpecialtyBuilding;
import com.pixel.map.object.building.special.utilities.UtilityManager;

public class PowerUtility extends SpecialtyBuilding {

	private float powerSupplied = 0;

	public PowerUtility(float x, float y, float width, float height, int widthInCells, int heightInCells,
					Map.MapCoord coord, String ID, float powerSupplied, boolean placedOnMap) {
		super(x, y, width, height, widthInCells, heightInCells, coord, ID + "Utility");

		if (placedOnMap) {
			this.powerSupplied = powerSupplied;

			// add this utility to the manager
			UtilityManager.getInstance().addPowerUtility(this);
		}
	}

	@Override
	public boolean remove() {
		UtilityManager.getInstance().removePowerUtility(this);

		return super.remove();
	}

	public float getPowerSupplied() {
		return powerSupplied;
	}
}
