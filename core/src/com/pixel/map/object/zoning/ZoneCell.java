package com.pixel.map.object.zoning;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.pixel.behavior.ReplaceBehavior;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;
import com.pixel.map.MapCoord;
import com.pixel.map.object.MapObject;
import com.pixel.map.object.building.Building;
import com.pixel.serialization.MapObjectSerializable;
import com.pixel.serialization.ZoneCellSerializable;

public class ZoneCell extends MapObject {

	private Building.BuildingType type;
	private Zone parentZone;
	private boolean valid = true;

	public ZoneCell(float width, float height, MapCoord coord, Building.BuildingType type, Zone parentZone) {
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
		valid = false;

		return super.remove();
	}

	@Override
	public MapObjectSerializable getSerializableObject() {

		ZoneCellSerializable serializable = new ZoneCellSerializable();
		serializable.name = getName();
		serializable.mapPositionX = mapPosition.x;
		serializable.mapPositionY = mapPosition.y;
		serializable.x = getX();
		serializable.y = getY();
		serializable.width = getWidth();
		serializable.height = getHeight();
		serializable.type = type;
		serializable.valid = valid;

		return serializable;
	}
}
