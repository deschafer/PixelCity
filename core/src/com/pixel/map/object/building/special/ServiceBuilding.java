package com.pixel.map.object.building.special;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.pixel.city.Financials.Source;
import com.pixel.map.Map;
import com.pixel.map.MapCoord;
import com.pixel.map.object.Cell;
import com.pixel.map.object.MapObject;
import com.pixel.map.object.building.Building;
import com.pixel.scene.GameScene;
import com.pixel.serialization.MapObjectSerializable;
import com.pixel.serialization.ServiceBuildingSerializable;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class ServiceBuilding extends SpecialtyBuilding {

	public enum Services implements Serializable {
		FIRE,
		POLICE,
		HEALTH,
		EDUCATION
	}

	protected ArrayList<Cell> influencedCells = new ArrayList<>();

	protected Services serviceType;
	private Polygon zoneOfInfluence;
	private static float zoneWidth = 2000;
	private static float zoneHeight = 1200;
	private final static int numSides = 16;
	private float updateTimer = 5.0f;
	private float updateTime = 2.0f;

	public static boolean placedOnMap = true;

	public ServiceBuilding(float x, float y, float width, float height, int widthInCells, int heightInCells, MapCoord coord, String ID) {
		super(x, y, width, height, widthInCells, heightInCells, coord, ID);
	}

	@Override
	public void initialize() {
		float w = zoneWidth;
		float h = zoneHeight;

		float[] vertices = new float[2 * numSides];
		for (int i = 0; i < numSides; i++) {
			float angle = i * 6.28f / numSides;
			// x-coordinate
			vertices[2 * i] = w / 2 * MathUtils.cos(angle) + w / 2;
			// y-coordinate
			vertices[2 * i + 1] = h / 2 * MathUtils.sin(angle) + h / 2;
		}
		zoneOfInfluence = new Polygon(vertices);
		boundaryPolygon = zoneOfInfluence;

		// then we add all of our influenced cells
		Map map = GameScene.getInstance().getGameMap();
		int mapWidth = map.getWidthInCells();
		int mapHeight = map.getHeightInCells();

		Cell occupiedCell = map.getCell(getMapPosition());
		if (getMapPosition() == Map.zeroCoordinate) {
			System.out.println("Zero coord. issue");
		}

		boundaryPolygon.setPosition(occupiedCell.getX() - boundaryPolygon.getBoundingRectangle().width / 2.0f + Map.cellWidth / 2.0f,
			   occupiedCell.getY() - boundaryPolygon.getBoundingRectangle().height / 2.0f + getHeight() / 2);

		for (int x = 0; x < mapWidth; x++) {
			for (int y = 0; y < mapHeight; y++) {
				Cell cell = map.getCell(x, y);
				if (cell != null && overlaps(cell)) {
					influencedCells.add(cell);
				}
			}
		}
	}

	@Override
	public boolean overlaps(MapObject other)
	{
		// TODO: need to set the origin and we need to set the position correctly
		// or just set position correctly

		Polygon poly1 = boundaryPolygon;
		Polygon poly2 = other.getBoundaryPolygon();

		// initial test to improve performance
		if ( !poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()) )
			return false;

		return Intersector.overlapConvexPolygons( poly1, poly2 );
	}

	@Override
	public void act(float dt) {
		super.act(dt);

		if ((updateTimer += dt) >= updateTime) {

			updateTimer = 0;

			// routinely check the zone of influence to see if a new building has appeared
			for (Cell cell : influencedCells) {

				MapObject object = cell.getTopObject();

				// if this object is a building
				if(object != null && object.getName().contains("Building")) {
					// then we add this service
					Building building = (Building)object;
					building.addService(serviceType);
				}
			}
		}
	}

	@Override
	public boolean remove() {

		// routinely check the zone of influence to see if a new building has appeared
		for (Cell cell : influencedCells) {

			MapObject object = cell.getTopObject();

			// if this object is a building
			if(object != null && object.getName().contains("Building")) {
				// then we add this service
				Building building = (Building)object;
				building.removeService(serviceType);
			}
		}
		return super.remove();
	}

	@Override
	public MapObjectSerializable getSerializableObject() {
		ServiceBuildingSerializable serializable = new ServiceBuildingSerializable();
		serializable.name = getName();
		serializable.mapPositionX = getMapPosition().x;
		serializable.mapPositionY = getMapPosition().y;
		serializable.x = getX();
		serializable.y = getY();
		serializable.width = getWidth();
		serializable.height = getHeight();
		serializable.serviceType = serviceType;
		serializable.dimensions = dimensions;

		serializable.sources = new ArrayList<>();
		for (Source source : sources) {
			serializable.sources.add(source.getSerializableObject());
		}

		return serializable;
	}
}
