package com.pixel.map.object.zoning;

import com.badlogic.gdx.math.Rectangle;
import com.pixel.map.object.building.Building;

public class ResidentialZone extends Zone {

	public ResidentialZone(Rectangle rectangle) {
		super(rectangle, Building.BuildingType.RESIDENTIAL);
	}

	@Override
	public void update() {

		// TODO: processes building of new res buildings

	}
}
