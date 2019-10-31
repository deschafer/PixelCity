package com.pixel.map.object.roads;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.pixel.map.Map;
import com.pixel.map.object.Cell;
import com.pixel.map.object.MapObject;
import com.pixel.scene.GameScene;

public class Road extends MapObject {

	protected RoadFactory.RoadType type;     // the actual type assoc with this object
	protected Road northernRoad;               // references to surrounding and connected roads
	protected Road easternRoad;               // references to surrounding and connected roads
	protected Road southernRoad;               // references to surrounding and connected roads
	protected Road westernRoad;               // references to surrounding and connected roads

	public enum Axis {HORIZ, VERTI}		// the axis types for this object

	public Road(float x, float y, float width, float height, Map.MapCoord coord, String ID) {
		super(x, y, width, height, coord, ID);

		// For each derived object, we will need to set neighbors if they exist,
		// and we will need to set the type correctly

		setName("Road");	// used to identify the general road type

	}

	public void updateSurroundings() {
		Cell cell;
		Map map = GameScene.getInstance().getGameMap();

		// check the northern direction
		if (northernRoad == null) {
			cell = map.getCell(getMapPosition().x, getMapPosition().y - 1);
			northernRoad = checkForRoad(cell);
		}

		// check the eastern direction
		if (easternRoad == null) {
			cell = map.getCell(getMapPosition().x + 1, getMapPosition().y);
			easternRoad = checkForRoad(cell);
		}

		// check the southern direction
		if (southernRoad == null) {
			cell = map.getCell(getMapPosition().x, getMapPosition().y + 1);
			southernRoad = checkForRoad(cell);
		}

		// check the western direction
		if (westernRoad == null) {
			cell = map.getCell(getMapPosition().x - 1, getMapPosition().y);
			westernRoad = checkForRoad(cell);
		}

	}

	private Road checkForRoad(Cell cell) {
		if(cell != null) {
			for(Actor actor : cell.getChildren()) {
				if(actor.getName() == "Road") {
					// save this reference
					Road road = (Road)actor;
					return road;
				}
			}
		}
		return null;
	}

}
