package com.pixel.map.object.zoning;

import com.badlogic.gdx.math.Rectangle;
import com.pixel.map.object.building.Building;

public class CommercialZone extends Zone {

	public CommercialZone(Rectangle dimensions) {
		super(dimensions, Building.BuildingType.COMMERCIAL);
	}

	@Override
	public void update() {

	}
}
