package com.pixel.map.object.building.special.utilities.power;

import com.pixel.map.Map;
import com.pixel.map.object.building.special.SpecialtyBuilding;
import com.pixel.map.object.building.special.utilities.UtilityManager;

public class PowerUtility extends SpecialtyBuilding {

	private float powerSupplied;

	public PowerUtility(float x, float y, float width, float height, Map.MapCoord coord, String ID, float powerSupplied) {
		super(x, y, width, height, coord, ID + "Utility");

		this.powerSupplied = powerSupplied;

		// add this utility to the manager
		UtilityManager.getInstance().addPowerUtility(this);
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
