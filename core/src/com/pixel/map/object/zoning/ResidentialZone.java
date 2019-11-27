package com.pixel.map.object.zoning;

import com.badlogic.gdx.math.Rectangle;
import com.pixel.city.Demand;
import com.pixel.map.Map;
import com.pixel.map.object.building.Building;
import com.pixel.map.object.building.BuildingFactory;

import java.util.ArrayList;

public class ResidentialZone extends Zone {

	public ResidentialZone(Rectangle rectangle) {
		super(rectangle, Building.BuildingType.RESIDENTIAL);
	}

	@Override
	public void update() {

		// If no cells are available, then we cannot build a new building
		if (availableCells.isEmpty()) {
			zoneFull = true;
			return;
		}

		// then we need to verify that we have demand prior to building
		if (Demand.getInstance().isResidentalDemanded()) {

			// get a building from the BuildingFactory
			Building building =
				   BuildingFactory.getInstance().create(parentMap.new MapCoord(0,0), Building.BuildingType.RESIDENTIAL, 0);

			ZoneCell suitableCell;
			ArrayList<Map.MapCoord> suitableCellLocations;

			// now we need to find a position for this building if there is one available
			// use our findSuitableLocation function from zone to get a placement for this building

			//  if there is a suitable location
			if (!(suitableCellLocations = findSuitableLocation(building.getDimensions())).isEmpty()) {

				// then we found a suitableCell, and we need to place this object
				placeBuilding(suitableCellLocations.get(random.nextInt(suitableCellLocations.size())), building);
				System.out.println("X: " + building.getMapPosition().x + "\nY:" + building.getMapPosition().y);
			}
			else {
				System.out.println("Position not found for building");
			}
		}
	}
}
