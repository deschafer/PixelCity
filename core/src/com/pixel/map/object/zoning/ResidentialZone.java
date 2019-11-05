package com.pixel.map.object.zoning;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.math.Rectangle;
import com.pixel.map.object.building.Building;
import com.pixel.map.object.building.BuildingFactory;

public class ResidentialZone extends Zone {

	public ResidentialZone(Rectangle rectangle) {
		super(rectangle, Building.BuildingType.RESIDENTIAL);
	}

	@Override
	public void update() {

		timer += Gdx.graphics.getDeltaTime();

		if(timer >= timeMax){

			// reset our timer
			timer = 0;

			// If no cells are available, then we cannot build a new building
			if(availableCells.isEmpty())
				return;

			// get our cell position from those available
			ZoneCell zoneCell = availableCells.get(random.nextInt(availableCells.size()));

			// remove this available cell
			availableCells.remove(zoneCell);

			// then we create our new building
			Building building = BuildingFactory.getInstance().create(zoneCell.getMapPosition(), zoneType, 0);

			// We add our building on top of this cell position
			parentMap.getCell(zoneCell.getMapPosition()).addActor(building);
		}
	}
}
