package com.pixel.map.object.building.special.utilities;

import com.pixel.map.object.building.special.utilities.power.PowerUtility;
import com.pixel.map.object.building.special.utilities.water.WaterUtility;

public class UtilityManager {

	private static UtilityManager instance = new UtilityManager();

	private float powerAvailable = 0;
	private float waterAvailable = 0;

	private UtilityManager() {}

	public static UtilityManager getInstance() {
		return instance;
	}

	public boolean claimWater(float waterNeeded) {

		if (waterAvailable - waterNeeded >= 0) {
			waterAvailable -= waterNeeded;
			return true;
		} else {
			return false;
		}
	}

	public boolean claimPower(float powerNeeded) {

		if (powerAvailable - powerNeeded >= 0) {
			powerAvailable -= powerNeeded;
			return true;
		} else {
			return false;
		}
	}

	public void returnWaterClaimed(float water) {
		waterAvailable += water;
	}

	public void returnPowerClaimed(float power) {
		powerAvailable += power;
	}

	public void addPowerUtility(PowerUtility utility) {

		powerAvailable += utility.getPowerSupplied();
	}

	public void addWaterUtility(WaterUtility utility) {

		waterAvailable += utility.getWaterSupplied();
	}

	public void removePowerUtility(PowerUtility utility) {

		powerAvailable -= utility.getPowerSupplied();
	}

	public void removeWaterUtility(WaterUtility utility) {

		waterAvailable -= utility.getWaterSupplied();
	}
}
