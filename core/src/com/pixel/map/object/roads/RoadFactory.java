package com.pixel.map.object.roads;

public class RoadFactory {

	// All the possible road types
	public enum RoadType {
		ROADWAY_NS(0),
		ROADWAY_EW(1),
		INTERSECT_4(2),
		INTERSECT_3_N(3),
		INTERSECT_3_E(4),
		INTERSECT_3_S(5),
		INTERSECT_3_W(6),
		END_N(7),
		END_E(8),
		END_S(9),
		END_W(10),
		CORNER_EN(11),
		CORNER_NE(12),
		CORNER_WN(13),
		CORNER_NW(14);

		private int value;

		RoadType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
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
