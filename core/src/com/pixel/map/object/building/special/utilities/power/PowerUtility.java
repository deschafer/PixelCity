package com.pixel.map.object.building.special.utilities.power;

import com.pixel.city.Financials.Source;
import com.pixel.map.Map;
import com.pixel.map.MapCoord;
import com.pixel.map.object.building.special.SpecialtyBuilding;
import com.pixel.map.object.building.special.utilities.UtilityManager;
import com.pixel.serialization.MapObjectSerializable;
import com.pixel.serialization.PowerUtilitySerializable;

import java.util.ArrayList;

public class PowerUtility extends SpecialtyBuilding {

	private float powerSupplied = 0;

	public PowerUtility(float x, float y, float width, float height, int widthInCells, int heightInCells,
					MapCoord coord, String ID, float powerSupplied, boolean placedOnMap) {
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

	@Override
	public MapObjectSerializable getSerializableObject() {

		PowerUtilitySerializable serializable = new PowerUtilitySerializable();
		serializable.name = getName();
		serializable.x = getX();
		serializable.y = getY();
		serializable.width = getWidth();
		serializable.height = getHeight();
		serializable.mapPositionX = getMapPosition().x;
		serializable.mapPositionY = getMapPosition().y;
		serializable.powerSupplied = powerSupplied;
		serializable.dimensions = dimensions;

		// this object will have sources assoc with it
		serializable.sources = new ArrayList<>();
		for (Source source : sources) {
			serializable.sources.add(source.getSerializableObject());
		}

		return serializable;
	}

}
