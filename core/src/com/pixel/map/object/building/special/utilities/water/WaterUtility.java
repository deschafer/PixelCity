package com.pixel.map.object.building.special.utilities.water;

import com.pixel.map.Map;
import com.pixel.map.MapCoord;
import com.pixel.map.object.building.special.SpecialtyBuilding;
import com.pixel.map.object.building.special.utilities.UtilityManager;
import com.pixel.serialization.MapObjectSerializable;
import com.pixel.serialization.PowerUtilitySerializable;
import com.pixel.serialization.WaterUtilitySerialization;

public class WaterUtility extends SpecialtyBuilding {

	private float waterSupplied = 0;

	public WaterUtility(float x, float y, float width, float height, int widthInCells, int heightInCells, MapCoord coord, String ID, float waterSupplied, boolean placedOnMap) {
		super(x, y, width, height, widthInCells, heightInCells, coord, ID + "Utility");

		if (placedOnMap) {

			this.waterSupplied = waterSupplied;

			// add this utility to the manager
			UtilityManager.getInstance().addWaterUtility(this);
		}
	}

	@Override
	public boolean remove() {
		UtilityManager.getInstance().removeWaterUtility(this);

		return super.remove();
	}

	public float getWaterSupplied() {
		return waterSupplied;
	}

	@Override
	public MapObjectSerializable getSerializableObject() {

		WaterUtilitySerialization serializable = new WaterUtilitySerialization();
		serializable.name = getName();
		serializable.mapPositionX = getMapPosition().x;
		serializable.mapPositionY = getMapPosition().y;
		serializable.waterSupplied = waterSupplied;
		serializable.dimensions = dimensions;
		serializable.occupyingCells = occupyingCells;

		return serializable;
	}
}
