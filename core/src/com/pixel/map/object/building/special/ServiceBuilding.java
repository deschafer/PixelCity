package com.pixel.map.object.building.special;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.pixel.map.BaseActor;
import com.pixel.map.Map;
import com.pixel.map.object.Cell;
import com.pixel.map.object.MapObject;
import com.pixel.map.object.building.Building;

import java.util.ArrayList;

public abstract class ServiceBuilding extends SpecialtyBuilding {

	public enum Services {
		FIRE,
		POLICE,
		HEALTH,
		EDUCATION
	}

	protected ArrayList<Cell> influencedCells = new ArrayList<>();
	private Services serviceType;
	private Polygon zoneOfInfluence;
	private float zoneWidth = 100;
	private float zoneHeight = 100;
	private final static int numSides = 8;
	private float updateTimer = 0;
	private float updateTime = 5.0f;

	public ServiceBuilding(float x, float y, float width, float height, Map.MapCoord coord, String ID) {
		super(x, y, width, height, coord, ID);


		setZoneOfInfluence();
	}

	protected void setZoneOfInfluence() {
		float w = zoneWidth;
		float h = zoneHeight;

		float[] vertices = new float[2*numSides];
		for (int i = 0; i < numSides; i++)
		{
			float angle = i * 6.28f / numSides;
			// x-coordinate
			vertices[2*i] = w/2 * MathUtils.cos(angle) + w/2;
			// y-coordinate
			vertices[2*i+1] = h/2 * MathUtils.sin(angle) + h/2;
		}
		zoneOfInfluence = new Polygon(vertices);
	}

	public boolean overlaps(MapObject other)
	{
		Polygon poly1 = this.getBoundaryPolygon();
		Polygon poly2 = other.getBoundaryPolygon();

		// initial test to improve performance
		if ( !poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()) )
			return false;

		return Intersector.overlapConvexPolygons( poly1, poly2 );
	}

	@Override
	public void act(float dt) {
		super.act(dt);

		if ((updateTime += dt) >= updateTime) {

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
}
