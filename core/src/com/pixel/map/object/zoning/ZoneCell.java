package com.pixel.map.object.zoning;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.pixel.behavior.ReplaceBehavior;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;
import com.pixel.map.object.MapObject;
import com.pixel.map.object.building.Building;

public class ZoneCell extends MapObject {

	private Building.BuildingType type;
	private Zone parentZone;
	private boolean valid = true;

	public ZoneCell(float width, float height, Map.MapCoord coord, Building.BuildingType type, Zone parentZone) {
		super(0, 0, width, height, coord, type.getZoneName());

		Texture texture = PixelAssetManager.manager.get(type.getZoneTexture());
		loadTexture(texture, type.getZoneTexture());

		this.type = type;
		this.parentZone = parentZone;

		replaceable = true;
		placementBehaviors.add(new ReplaceBehavior(this));
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	@Override
	public boolean remove() {
		parentZone.removeZoneCell(this);

		return super.remove();
	}
}
