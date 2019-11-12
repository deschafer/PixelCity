package com.pixel.map.object.zoning;

import com.badlogic.gdx.graphics.Texture;
import com.pixel.behavior.ReplaceBehavior;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;
import com.pixel.map.object.MapObject;
import com.pixel.map.object.building.Building;

public class ZoneCell extends MapObject {

	private Building.BuildingType type;
	private Zone parentZone;

	public ZoneCell(float width, float height, Map.MapCoord coord, Building.BuildingType type, Zone parentZone) {
		super(0, 0, width, height, coord, type.getZoneName());

		Texture texture = PixelAssetManager.manager.get(type.getZoneTexture());
		loadTexture(texture, type.getZoneTexture());

		this.type = type;
		this.parentZone = parentZone;

		replaceable = true;
		placementBehaviors.add(new ReplaceBehavior(this));
	}


	@Override
	public boolean remove() {
		parentZone.removeZoneCell(getMapPosition());

		return super.remove();
	}
}
