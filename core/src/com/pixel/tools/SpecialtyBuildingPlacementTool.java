package com.pixel.tools;

import com.pixel.map.object.MapObject;
import com.pixel.map.object.building.special.SpecialtyBuilding;
import com.pixel.map.object.building.special.SpecialtyBuildingFactory;
import com.pixel.map.object.building.special.utilities.fire.FireStation;
import com.pixel.map.object.building.special.utilities.power.CoalPowerPlant;
import com.pixel.map.object.building.special.utilities.water.WaterTank;

public class SpecialtyBuildingPlacementTool extends MapTool {

	// TODO: this is only implemented so we can test water and power utilities quickly
	private int number = 0;
	private String buildingName;

	public SpecialtyBuildingPlacementTool(int number) {
		this.number = number;
	}

	public void setPlaceableObject(String buildingName) {
		this.buildingName = buildingName;
	}

	@Override
	public boolean onTouchDown(float x, float y) {
		if(!super.onTouchDown(x, y))
			return false;


		
		if(begCell != null) {

			// create our new building
			SpecialtyBuilding building =
				   SpecialtyBuildingFactory.getInstance().create(buildingName, begCell.getMapPosition());

			// if this object was not found
			if (building == null) {
				return false;
			}

			// Offset the image to center the building in the cell grid
			building.setX(-building.getDimensions().width * cellWidth + building.getWidth() / 2 + cellWidth / 2);
			building.setY(35);	// accounts for the vertical section of the tiles

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
