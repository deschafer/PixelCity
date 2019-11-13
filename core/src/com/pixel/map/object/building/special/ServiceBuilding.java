package com.pixel.map.object.building.special;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.pixel.map.Map;
import com.pixel.map.object.Cell;

import java.util.ArrayList;

public abstract class ServiceBuilding extends SpecialtyBuilding {

	public enum Services {
		FIRE,
		POLICE,
		HEALTH,
		EDUCATION
	}

	protected ArrayList<Cell> influencedCells;
	private Services serviceType;
	private Polygon zoneOfInfluence;
	private final static int numSides = 8;

	public ServiceBuilding(float x, float y, float width, float height, Map.MapCoord coord, String ID) {
		super(x, y, width, height, coord, ID);
	}

	protected void setZoneOfInfluence() {
		float w = getWidth();
		float h = getHeight();

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

	@Override
	public void act(float dt) {
		super.act(dt);

		// routinely check the zone of influence to see if a new building has appeared

	}
}
