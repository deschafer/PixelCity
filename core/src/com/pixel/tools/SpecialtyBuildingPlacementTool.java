package com.pixel.tools;

import com.pixel.map.object.building.special.SpecialtyBuilding;
import com.pixel.map.object.building.special.utilities.power.CoalPowerPlant;
import com.pixel.map.object.building.special.utilities.water.WaterTank;

public class SpecialtyBuildingPlacementTool extends MapTool {

	// TODO: this is only implemented so we can test water and power utilities quickly
	private int number = 0;

	public SpecialtyBuildingPlacementTool(int number) {
		this.number = number;
	}

	@Override
	public boolean onTouchDown(float x, float y) {
		if(!super.onTouchDown(x, y))
			return false;

		if(begCell != null) {

			SpecialtyBuilding building;

			if(number == 1)
				building = new WaterTank(0,0, begCell.getMapPosition());
			else
				building = new CoalPowerPlant(0,0, begCell.getMapPosition());

			building.setX((-building.getWidth() / 4) * (building.getDimensions().width - 1));
			building.setY((cellHeight / 2) * (building.getDimensions().width - 1));

			begCell.addMapObject(building);
		}

		return true;
	}

	@Override
	public boolean onTouchMove(float x, float y) {
		return super.onTouchMove(x, y);
	}

	@Override
	public boolean onTouchUp(float x, float y) {
		return super.onTouchUp(x, y);
	}
}
