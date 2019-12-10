package com.pixel.map.object.roads;

public class RoadFactory {

	// All the possible road types
	public enum RoadType {
		ROADWAY_NS(0, "RoadwayNorthSouth"),
		ROADWAY_EW(1, "RoadwayEastWest"),
		INTERSECT_4(2, "RoadIntersection4"),
		INTERSECT_3_N(3, "RoadIntersection3North"),
		INTERSECT_3_E(4, "RoadIntersection3East"),
		INTERSECT_3_S(5, "RoadIntersection3South"),
		INTERSECT_3_W(6, "RoadIntersection3West"),
		END_N(7, "RoadEndNorth"),
		END_E(8, "RoadEndEast"),
		END_S(9, "RoadEndSouth"),
		END_W(10, "RoadEndWest"),
		CORNER_EN(11, "RoadCornerEastNorth"),
		CORNER_NE(12, "RoadCornerNorthEast"),
		CORNER_WN(13, "RoadCornerWestNorth"),
		CORNER_NW(14, "RoadCornerNorthWest");

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

	public RoadType getRoadTypeFromValue(int value) {
		if (RoadType.ROADWAY_NS.value == value) {
			return RoadType.ROADWAY_NS;
		} else if (RoadType.ROADWAY_EW.value == value) {
			return RoadType.ROADWAY_EW;
		} else if (RoadType.INTERSECT_4.value == value) {
			return RoadType.INTERSECT_4;
		} else if (RoadType.INTERSECT_3_N.value == value) {
			return RoadType.INTERSECT_3_N;
		} else if (RoadType.INTERSECT_3_E.value == value) {
			return RoadType.INTERSECT_3_E;
		} else if (RoadType.INTERSECT_3_S.value == value) {
			return RoadType.INTERSECT_3_S;
		} else if (RoadType.INTERSECT_3_W.value == value) {
			return RoadType.INTERSECT_3_W;
		} else if (RoadType.END_N.value == value) {
			return RoadType.END_N;
		} else if (RoadType.END_E.value == value) {
			return RoadType.END_E;
		} else if (RoadType.END_S.value == value) {
			return RoadType.END_S;
		} else if (RoadType.END_W.value == value) {
			return RoadType.END_W;
		} else if (RoadType.CORNER_EN.value == value) {
			return RoadType.CORNER_EN;
		} else if (RoadType.CORNER_NE.value == value) {
			return RoadType.CORNER_NE;
		} else if (RoadType.CORNER_WN.value == value) {
			return RoadType.CORNER_WN;
		} else if (RoadType.CORNER_NW.value == value) {
			return RoadType.CORNER_NW;
		} else return null;
	}

	public void registerRoadType(RoadType type, Road road) {
		int position = type.value;
		road.setPrototypeObject(true);

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
