package com.pixel.map.object.roads;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.pixel.behavior.*;
import com.pixel.city.Financials.Source;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;
import com.pixel.map.MapCoord;
import com.pixel.map.object.Cell;
import com.pixel.map.object.MapObject;
import com.pixel.scene.GameScene;
import com.pixel.serialization.MapObjectSerializable;
import com.pixel.serialization.RoadSerializable;

import java.util.ArrayList;

public class Road extends MapObject {

	protected RoadFactory.RoadType type;     // the actual type assoc with this object
	private Road northernRoad;               // references to surrounding and connected roads
	private Road easternRoad;               // references to surrounding and connected roads
	private Road southernRoad;               // references to surrounding and connected roads
	private Road westernRoad;               // references to surrounding and connected roads
	private RoadPlacement placementBehavior;

	public static boolean roadsNeedToUpdate = false;
	private static Boolean updateMutex = new Boolean(false);
	private static ArrayList<Road> activeRoads = new ArrayList<>();

	private static float upkeepCost = -0.10f;
	public static float cost = 50.0f;

	public Road(float x, float y, MapCoord coord, RoadFactory.RoadType type) {
		super(x, y, Map.cellWidth, Map.cellHeight, coord, type.getName());

		this.type = type;
		displayName = "Roadway";

		// only replaceable by other roads, however
		replaceable = true;

		// every road has an upkeep cost associated with it
		addSource(new Source(this, upkeepCost));

		// this object also has a placedown cost
		placedownCost = cost;

		String textureName = null;
		Texture texture;

		RoadPlacement behavior = null;

		// then we need to load in the correct texture for this road
		// we also find the correct placement behavior as well
		switch (type) {
			case ROADWAY_NS:
				textureName = PixelAssetManager.roadwayNorthSouth;
				behavior = new RoadwayNorthSouthPlacement(this);
				break;
			case ROADWAY_EW:
				textureName = PixelAssetManager.roadwayEastWest;
				behavior = new RoadwayEastWestPlacement(this);
				break;
			case INTERSECT_4:
				textureName = PixelAssetManager.intersectionFour;
				// we do not need a placement behavior here because there are no roads that can be added to change
				// this intersection
				break;
			case INTERSECT_3_N:
				textureName = PixelAssetManager.intersectionThreeNorth;
				behavior = new IntersectionThreeNorthPlacement(this);
				break;
			case INTERSECT_3_E:
				textureName = PixelAssetManager.intersectionThreeEast;
				behavior = new IntersectionThreeEastPlacement(this);
				break;
			case INTERSECT_3_S:
				textureName = PixelAssetManager.intersectionThreeSouth;
				behavior = new IntersectionThreeSouthPlacement(this);
				break;
			case INTERSECT_3_W:
				textureName = PixelAssetManager.intersectionThreeWest;
				behavior = new IntersectionThreeWestPlacement(this);
				break;
			case END_N:
				textureName = PixelAssetManager.roadwayEndNorth;
				behavior = new EndRoadNorthPlacement(this);
				break;
			case END_E:
				textureName = PixelAssetManager.roadwayEndEast;
				behavior = new EndRoadEastPlacement(this);
				break;
			case END_S:
				textureName = PixelAssetManager.roadwayEndSouth;
				behavior = new EndRoadSouthPlacement(this);
				break;
			case END_W:
				textureName = PixelAssetManager.roadwayEndWest;
				behavior = new EndRoadWestPlacement(this);
				break;
			case CORNER_EN:
				textureName = PixelAssetManager.cornerEastNorth;
				behavior = new CornerEastNorthPlacement(this);
				break;
			case CORNER_NE:
				textureName = PixelAssetManager.cornerNorthEast;
				behavior = new CornerNorthEastPlacement(this);
				break;
			case CORNER_WN:
				textureName = PixelAssetManager.cornerWestNorth;
				behavior = new CornerWestNorthPlacement(this);
				break;
			case CORNER_NW:
				textureName = PixelAssetManager.cornerNorthWest;
				behavior = new CornerNorthWestPlacement(this);
				break;
		}

		texture = PixelAssetManager.manager.get(textureName);
		loadTexture(texture, textureName);
		placementBehavior = behavior;

		// then we add this as an active road
		synchronized (updateMutex) {
			activeRoads.add(this);
		}
	}

	public Road(float x, float y, float width, float height, MapCoord coord, String ID) {
		super(x, y, width, height, coord, ID);

		replaceable = true;

		// every road has an upkeep cost associated with it
		addSource(new Source(this, upkeepCost));

		// this object also has a placedown cost
		placedownCost = cost;

		synchronized (updateMutex) {
			activeRoads.add(this);
		}
	}

	public Road(float x, float y, float width, float height, MapCoord coord, String ID, boolean prototype) {
		super(x, y, width, height, coord, ID);

		setPrototypeObject(prototype);
		replaceable = true;

		// every road has an upkeep cost associated with it
		addSource(new Source(this, upkeepCost));

		// this object also has a placedown cost
		placedownCost = 50.0f;

		// For each derived object, we will need to set neighbors if they exist,
		// and we will need to set the type correctly
	}

	private static void setRoadsToBeUpdated() {
		synchronized (updateMutex) {
			roadsNeedToUpdate = true;
		}
	}

	public static void updateRoads() {
		synchronized (updateMutex) {
			for (int i = 0; i < activeRoads.size(); i++) {
				activeRoads.get(i).updateSurroundings();
			}
			roadsNeedToUpdate = false;
		}
	}

	public void updateSurroundings() {

		synchronized (this) {

			Cell cell;
			Map map = GameScene.getInstance().getGameMap();

			// reset our last roads, as we do not know if they have changed
			northernRoad = null;
			easternRoad = null;
			westernRoad = null;
			southernRoad = null;

			if (hasParent()) {
				MapCoord position = ((Cell) getParent()).getMapPosition();

				// check the northern direction
				if (northernRoad == null) {
					cell = map.getCell(position.x, position.y - 1);
					if (cell != null && cell.containsMapObject() && cell.getTopObject().getName().contains("Road")) {
						northernRoad = (Road) cell.getTopObject();
					}
				}

				// check the eastern direction
				if (easternRoad == null) {
					cell = map.getCell(position.x + 1, position.y);

					if (cell != null && cell.containsMapObject() && cell.getTopObject().getName().contains("Road")) {
						easternRoad = (Road) cell.getTopObject();
					}
				}

				// check the southern direction
				if (southernRoad == null) {
					cell = map.getCell(position.x, position.y + 1);
					if (cell != null && cell.containsMapObject() && cell.getTopObject().getName().contains("Road")) {
						southernRoad = (Road) cell.getTopObject();
					}
				}

				// check the western direction
				if (westernRoad == null) {
					cell = map.getCell(position.x - 1, position.y);
					if (cell != null && cell.containsMapObject() && cell.getTopObject().getName().contains("Road")) {
						westernRoad = (Road) cell.getTopObject();
					}
				}

				// by using the information gathered above, we can now make a decision about how the cell will change
				handleRoadChange();
			} else {
				synchronized (updateMutex) {
					activeRoads.remove(this);
				}
			}
		}
	}

	private void handleRoadChange() {

		synchronized (this) {

			int numberNeighbors = 0;

			if (northernRoad != null) {
				numberNeighbors++;
			}
			if (easternRoad != null) {
				numberNeighbors++;
			}
			if (southernRoad != null) {
				numberNeighbors++;
			}
			if (westernRoad != null) {
				numberNeighbors++;
			}

			// this means somehow this method was called without having this object be removed
			if (numberNeighbors == 4) {
				setNewRoadTexture(RoadFactory.RoadType.INTERSECT_4);
			}
			// we now change to a three way intersections
			else if (numberNeighbors == 3) {

				// this depends on the direction as well
				if (northernRoad == null) {
					setNewRoadTexture(RoadFactory.RoadType.INTERSECT_3_N);
				} else if (westernRoad == null) {
					setNewRoadTexture(RoadFactory.RoadType.INTERSECT_3_W);
				} else if (southernRoad == null) {
					setNewRoadTexture(RoadFactory.RoadType.INTERSECT_3_S);
				} else if (easternRoad == null) {
					setNewRoadTexture(RoadFactory.RoadType.INTERSECT_3_E);
				}
			}
			// then we need to consider a normal, non-intersection roadway
			else if (numberNeighbors == 2) {
				// two normal road options

				// if both the north and south are null, this has to be a normal roadway going horizontal
				if (northernRoad == null && southernRoad == null) {
					// then a horizontal road
					setNewRoadTexture(RoadFactory.RoadType.ROADWAY_EW);
				}
				// the opposite of the case above
				else if (westernRoad == null && easternRoad == null) {
					// then a vertical road
					setNewRoadTexture(RoadFactory.RoadType.ROADWAY_NS);
				}
				// then we need to consider the corner roads as well
				else if (northernRoad == null) {
					// there are an additional two cases down this branch
					if (westernRoad == null) {
						// then its east and south, so a corner heading north (From south) and going east
						setNewRoadTexture(RoadFactory.RoadType.CORNER_NE);
					} else if (easternRoad == null) {
						// then its west and south, so a corner heading north (From south) and going west
						setNewRoadTexture(RoadFactory.RoadType.CORNER_NW);
					}
				} else if (westernRoad == null) {
					// there are an additional two cases down this branch
					if (southernRoad == null) {
						// then its west and south, so a corner heading north (From south) and going west
						setNewRoadTexture(RoadFactory.RoadType.CORNER_NW);
					} else {
						setNewRoadTexture(RoadFactory.RoadType.CORNER_NE);
					}
				} else if (easternRoad == null) {
					// there are an additional two cases down this branch
					if (southernRoad == null) {
						// then its west and south, so a corner heading north (From south) and going west
						setNewRoadTexture(RoadFactory.RoadType.CORNER_EN);
					} else {
						setNewRoadTexture(RoadFactory.RoadType.CORNER_NW);
					}
				} else if (southernRoad == null) {
					// then its west and south, so a corner heading north (From south) and going west
					setNewRoadTexture(RoadFactory.RoadType.CORNER_EN);
				}
			}

			// the end of a road
			else if (numberNeighbors == 1) {
				// there are a total of 4 options then
				// this depends on the direction as well
				if (northernRoad == null && easternRoad == null && westernRoad ==null) {
					setNewRoadTexture(RoadFactory.RoadType.END_N);
				} else if (westernRoad == null && northernRoad == null && southernRoad == null) {
					setNewRoadTexture(RoadFactory.RoadType.END_W);
				} else if (southernRoad == null && westernRoad == null && easternRoad == null) {
					setNewRoadTexture(RoadFactory.RoadType.END_S);
				} else if (easternRoad == null && northernRoad == null && southernRoad == null) {
					setNewRoadTexture(RoadFactory.RoadType.END_E);
				}
			}
		}
	}

	public RoadFactory.RoadType getType() {
		return type;
	}

	@Override
	public boolean placeOverObject(MapObject object) {

		synchronized (this) {
			if (object.getName().contains("Road") && placementBehavior != null) {

				placementBehavior.setOtherRoad((Road) object);
				placementBehavior.act();
			} else {
				System.out.println("Non-road object attempted to be added on top of a road object");
			}

			return true;
		}
	}

	@Override
	public boolean remove() {

		synchronized (this) {

			boolean result = super.remove();

			// if this object was deleted, then roads need to be updated to have the correct texture
			if (deleted) {
				setRoadsToBeUpdated();
			}

			synchronized (updateMutex) {
				activeRoads.remove(this);
			}
			return result;
		}
	}

	private void setNewRoadTexture(RoadFactory.RoadType type) {
		Texture texture;

		// we set the new texture of the object based on its type
		switch (type) {
			case ROADWAY_NS:
				texture = PixelAssetManager.manager.get(PixelAssetManager.roadwayNorthSouth);
				loadTexture(texture, PixelAssetManager.roadwayNorthSouth);
				break;
			case ROADWAY_EW:
				texture = PixelAssetManager.manager.get(PixelAssetManager.roadwayEastWest);
				loadTexture(texture, PixelAssetManager.roadwayEastWest);
				break;
			case INTERSECT_4:
				texture = PixelAssetManager.manager.get(PixelAssetManager.intersectionFour);
				loadTexture(texture, PixelAssetManager.intersectionFour);
				break;
			case INTERSECT_3_N:
				texture = PixelAssetManager.manager.get(PixelAssetManager.intersectionThreeNorth);
				loadTexture(texture, PixelAssetManager.intersectionThreeNorth);
				break;
			case INTERSECT_3_E:
				texture = PixelAssetManager.manager.get(PixelAssetManager.intersectionThreeEast);
				loadTexture(texture, PixelAssetManager.intersectionThreeEast);
				break;
			case INTERSECT_3_S:
				texture = PixelAssetManager.manager.get(PixelAssetManager.intersectionThreeSouth);
				loadTexture(texture, PixelAssetManager.intersectionThreeSouth);
				break;
			case INTERSECT_3_W:
				texture = PixelAssetManager.manager.get(PixelAssetManager.intersectionThreeWest);
				loadTexture(texture, PixelAssetManager.intersectionThreeWest);
				break;
			case END_N:
				texture = PixelAssetManager.manager.get(PixelAssetManager.roadwayEndNorth);
				loadTexture(texture, PixelAssetManager.roadwayEndNorth);
				break;
			case END_E:
				texture = PixelAssetManager.manager.get(PixelAssetManager.roadwayEndEast);
				loadTexture(texture, PixelAssetManager.roadwayEndEast);
				break;
			case END_S:
				texture = PixelAssetManager.manager.get(PixelAssetManager.roadwayEndSouth);
				loadTexture(texture, PixelAssetManager.roadwayEndSouth);
				break;
			case END_W:
				texture = PixelAssetManager.manager.get(PixelAssetManager.roadwayEndWest);
				loadTexture(texture, PixelAssetManager.roadwayEndWest);
				break;
			case CORNER_EN:
				texture = PixelAssetManager.manager.get(PixelAssetManager.cornerEastNorth);
				loadTexture(texture, PixelAssetManager.cornerEastNorth);
				break;
			case CORNER_NE:
				texture = PixelAssetManager.manager.get(PixelAssetManager.cornerNorthEast);
				loadTexture(texture, PixelAssetManager.cornerNorthEast);
				break;
			case CORNER_WN:
				texture = PixelAssetManager.manager.get(PixelAssetManager.cornerWestNorth);
				loadTexture(texture, PixelAssetManager.cornerWestNorth);
				break;
			case CORNER_NW:
				texture = PixelAssetManager.manager.get(PixelAssetManager.cornerNorthWest);
				loadTexture(texture, PixelAssetManager.cornerNorthWest);
				break;
		}
	}

	@Override
	public void setPrototypeObject(boolean prototypeObject) {

		if (prototypeObject) {
			synchronized (updateMutex) {
				activeRoads.remove(this);
			}
		}
		super.setPrototypeObject(prototypeObject);
	}

	@Override
	public MapObject copy() {
		return new Road(getX(), getY(), getMapPosition(), type);
	}

	@Override
	public MapObjectSerializable getSerializableObject() {
		RoadSerializable serializable = new RoadSerializable();
		serializable.mapPositionX = getMapPosition().x;
		serializable.mapPositionY = getMapPosition().y;
		serializable.x = getX();
		serializable.y = getY();
		serializable.width = getWidth();
		serializable.height = getHeight();
		serializable.name = getName();
		serializable.type = type.getValue();

		// since there are expenses associated with this object
		serializable.sources = new ArrayList<>();
		for (Source source : sources) {
			serializable.sources.add(source.getSerializableObject());
		}

		return serializable;
	}
}
