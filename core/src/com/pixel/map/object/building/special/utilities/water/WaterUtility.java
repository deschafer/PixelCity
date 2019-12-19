package com.pixel.map.object.building.special.utilities.water;

import com.pixel.city.Financials.Source;
import com.pixel.map.MapCoord;
import com.pixel.map.object.building.special.SpecialtyBuilding;
import com.pixel.map.object.building.special.utilities.UtilityManager;
import com.pixel.serialization.MapObjectSerializable;
import com.pixel.serialization.WaterUtilitySerializable;

import java.util.ArrayList;

public class WaterUtility extends SpecialtyBuilding {

	private float waterSupplied = 0;

	public WaterUtility(float x, float y, float width, float height, int widthInCells, int heightInCells, MapCoord coord, String ID, float waterSupplied) {
		super(x, y, width, height, widthInCells, heightInCells, coord, ID + "Utility");

		this.waterSupplied = waterSupplied;
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

		WaterUtilitySerializable serializable = new WaterUtilitySerializable();
		serializable.name = getName();
		serializable.mapPositionX = getMapPosition().x;
		serializable.mapPositionY = getMapPosition().y;
		serializable.x = getX();
		serializable.y = getY();
		serializable.width = getWidth();
		serializable.height = getHeight();
		serializable.waterSupplied = waterSupplied;
		serializable.dimensions = dimensions;

		// this object will have sources assoc with it
		serializable.sources = new ArrayList<>();
		for (Source source : sources) {
			serializable.sources.add(source.getSerializableObject());
		}

		return serializable;
	}

	@Override
	public void initialize() {
		// add this utility to the manager
		UtilityManager.getInstance().addWaterUtility(this);
		sources.add(new Source(this, -20));
	}
}
