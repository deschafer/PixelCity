package com.pixel.map.object.zoning;

import com.badlogic.gdx.math.Rectangle;
import com.pixel.map.object.building.Building;

public class OfficeZone extends Zone {

	public OfficeZone(Rectangle dimensions) {
		super(dimensions, Building.BuildingType.OFFICE);
	}

	@Override
	public void update() {

	}
}
