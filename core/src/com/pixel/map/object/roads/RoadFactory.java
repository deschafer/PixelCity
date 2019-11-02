package com.pixel.map.object.roads;

public class RoadFactory {

	// All the possible road types
	public enum RoadType {
		ROADWAY_NS(0, "RoadwayNorthSouth"),
		ROADWAY_EW(1, "RoadwayEastWest"),
		INTERSECT_4(2, "Intersection4"),
		INTERSECT_3_N(3, "Intersection3North"),
		INTERSECT_3_E(4, "Intersection3East"),
		INTERSECT_3_S(5, "Intersection3South"),
		INTERSECT_3_W(6, "Intersection3West"),
		END_N(7, "EndNorth"),
		END_E(8, "EndEast"),
		END_S(9, "EndSouth"),
		END_W(10, "EndWest"),
		CORNER_EN(11, "CornerEastNorth"),
		CORNER_NE(12, "CornerNorthEast"),
		CORNER_WN(13, "CornerWestNorth"),
		CORNER_NW(14, "CornerNorthWest");

		private int value;
		private String name;

		RoadType(int value, String name) {
			this.value = value;
			this.name = name;
		}

		public int getValue() {
			return value;
		}
		public String getName() { return name;}
	}

	private int numberRoadTypes = 15;
	private Road[] roadTypes = new Road[numberRoadTypes];
	private static RoadFactory instance = new RoadFactory();

	private RoadFactory() {}

	public static RoadFactory getInstance() {
		return instance;
	}

	public void registerRoadType(RoadType type, Road road) {
		int position = type.value;

		// save the association between this road type and an actual road class
		roadTypes[position] = road;
	}

	//
	// getRoad()
	// This is where construction of a road takes place, it copies the base road type and returns it
	//
	public Road getRoad(RoadType type) {
		return (Road)roadTypes[type.value].copy();
	}
}
