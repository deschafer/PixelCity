package com.pixel.map.object.zoning;

import com.badlogic.gdx.math.Rectangle;
import com.pixel.map.object.building.Building;

public class OfficeZoning extends Zone {

	public OfficeZoning(Rectangle dimensions) {
		super(dimensions, Building.BuildingType.OFFICE);
	}

	@Override
	public void update() {

	}
}
