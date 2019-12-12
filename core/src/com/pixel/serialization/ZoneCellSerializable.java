package com.pixel.serialization;

import com.badlogic.gdx.math.Rectangle;
import com.pixel.map.MapCoord;
import com.pixel.map.object.MapObject;
import com.pixel.map.object.building.Building;
import com.pixel.map.object.zoning.ZoneCell;
import com.pixel.scene.GameScene;

public class ZoneCellSerializable extends MapObjectSerializable {

	public Building.BuildingType type;
	public boolean valid = true;
	public Rectangle parentZone;

	@Override
	public MapObject getNonSerializableObject() {

		ZoneCell cell = new ZoneCell(width, height, new MapCoord(mapPositionX, mapPositionY), type, null);
		cell.setValid(valid);
		cell.setName(name);
		cell.setParentZoneDimensions(parentZone);

		GameScene.getInstance().getGameMap().addLoadedInZoneCell(cell, type);

		return cell;
	}
}
