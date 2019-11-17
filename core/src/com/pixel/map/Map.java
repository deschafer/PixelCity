package com.pixel.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.object.Cell;
import com.pixel.map.object.MapObject;
import com.pixel.map.object.building.Building;
import com.pixel.map.object.building.display.BuildingDisplay;
import com.pixel.map.object.building.BuildingFactory;
import com.pixel.map.object.roads.*;
import com.pixel.map.object.zoning.*;
import com.pixel.map.visualizer.VisualizerFactory;

import java.util.ArrayList;
import java.util.Random;

public class Map extends Group {

	public class MapCoord {

		public int x;
		public int y;

		MapCoord(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	private int width;                   	 // width of the map array in cells
	private int height;                   	 // height ...
	private float widthPixels;               // width of the map in pixels
	private float heightPixels;         	 // height of the map in pixels
	private Stage parentStage;
	private Cell[][] mapArray;
	private final int cellWidth = 132;     	// width of the cell in pixels
	private final int cellHeight = 102;     // height of the cell in pixels
	public static final int cellBuildingBaseWidth = 132;
	public static final int cellBuildingBaseHeight = 127;
	public static final int cellStoryWidth = 99;
	public static final int cellStoryHeight = 85;
	private final float cellRowWidth = 132;
	private final float cellRowHeight = 2.0f/3.0f * 101.0f;
	private Stage stage;
	private Vector2 topOfMap;
	private ArrayList<ResidentialZone> residentialZones = new ArrayList<>();
	private ArrayList<CommercialZone> commercialZones = new ArrayList<>();
	private ArrayList<OfficeZone> officeZones = new ArrayList<>();

	private float residentialTimer = 0;
	private float residentialTime = 1.0f;
	private float commericalTimer = 0;
	private float commercialTime = 1.0f;
	private float officeTimer = 0;
	private float officeTime = 1.0f;

	private Random random = new Random();

	public Map(int width, int height, Stage stage) {

		// Set our member variables
		this.width = width;
		this.height = height;
		this.stage = stage;
		parentStage = stage;
		widthPixels = width * cellRowWidth;
		heightPixels = height * cellRowHeight;

		stage.addActor(this);

		generateArray();
	}

	//
	// initialize()
	// Do all initialization for objects that require a created map here
	// otherwise, a loop of map creation will occur
	//
	public void initialize() {

		// register all of our different road types
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.ROADWAY_NS,
			   new RoadwayNorthSouth(0, 0, cellWidth, cellHeight, new MapCoord(0, 0)));
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.ROADWAY_EW,
			   new RoadwayEastWest(0, 0, cellWidth, cellHeight, new MapCoord(0, 0)));
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.END_E,
			   new RoadwayEndEast(0, 0, cellWidth, cellHeight, new MapCoord(0, 0)));
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.END_N,
			   new RoadwayEndNorth(0, 0, cellWidth, cellHeight, new MapCoord(0, 0)));
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.END_S,
			   new RoadwayEndSouth(0, 0, cellWidth, cellHeight, new MapCoord(0, 0)));
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.END_W,
			   new RoadwayEndWest(0, 0, cellWidth, cellHeight, new MapCoord(0, 0)));
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.CORNER_EN,
			   new CornerEastNorth(0, 0, cellWidth, cellHeight, new MapCoord(0, 0)));
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.CORNER_NE,
			   new CornerNorthEast(0, 0, cellWidth, cellHeight, new MapCoord(0, 0)));
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.CORNER_NW,
			   new CornerNorthWest(0, 0, cellWidth, cellHeight, new MapCoord(0, 0)));
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.CORNER_WN,
			   new CornerWestNorth(0, 0, cellWidth, cellHeight, new MapCoord(0, 0)));
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.INTERSECT_4,
			   new IntersectionFour(0, 0, cellWidth, cellHeight, new MapCoord(0, 0)));
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.INTERSECT_3_E,
			   new IntersectionThreeEast(0, 0, cellWidth, cellHeight, new MapCoord(0, 0)));
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.INTERSECT_3_N,
			   new IntersectionThreeNorth(0, 0, cellWidth, cellHeight, new MapCoord(0, 0)));
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.INTERSECT_3_S,
			   new IntersectionThreeSouth(0, 0, cellWidth, cellHeight, new MapCoord(0, 0)));
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.INTERSECT_3_W,
			   new IntersectionThreeWest(0, 0, cellWidth, cellHeight, new MapCoord(0, 0)));


		// register all of the visualizers we use in the map tools
		// We only need to placeable tiles for visualizers
		VisualizerFactory.getInstance().registerMapObject(
			   new RoadwayNorthSouth(0, 0, cellWidth, cellHeight, new MapCoord(0, 0)));
		VisualizerFactory.getInstance().registerMapObject(
			   new RoadwayEastWest(0, 0, cellWidth, cellHeight, new MapCoord(0, 0)));
		VisualizerFactory.getInstance().registerMapObject(
			   new RoadwayEndEast(0, 0, cellWidth, cellHeight, new MapCoord(0, 0)));
		VisualizerFactory.getInstance().registerMapObject(
			   new RoadwayEndNorth(0, 0, cellWidth, cellHeight, new MapCoord(0, 0)));
		VisualizerFactory.getInstance().registerMapObject(
			   new RoadwayEndSouth(0, 0, cellWidth, cellHeight, new MapCoord(0, 0)));
		VisualizerFactory.getInstance().registerMapObject(
			   new RoadwayEndWest(0, 0, cellWidth, cellHeight, new MapCoord(0, 0)));
		VisualizerFactory.getInstance().registerMapObject(
			   new ZoneCell(cellWidth, cellHeight, new MapCoord(0, 0), Building.BuildingType.RESIDENTIAL, null));
		VisualizerFactory.getInstance().registerMapObject(
			   new ZoneCell(cellWidth, cellHeight, new MapCoord(0, 0), Building.BuildingType.COMMERCIAL, null));
		VisualizerFactory.getInstance().registerMapObject(
			   new ZoneCell(cellWidth, cellHeight, new MapCoord(0, 0), Building.BuildingType.OFFICE, null));

		// set up all of our building displays
		BuildingFactory.getInstance().registerBuildingDisplayBase(
			   new BuildingDisplay(PixelAssetManager.residentialBase22, true), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayBase(
			   new BuildingDisplay(PixelAssetManager.residentialBase14, true), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayBase(
			   new BuildingDisplay(PixelAssetManager.residentialBase30, true), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayBase(
			   new BuildingDisplay(PixelAssetManager.residentialBase37, true), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayStory(
			   new BuildingDisplay(PixelAssetManager.residentialStory32), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayStory(
			   new BuildingDisplay(PixelAssetManager.residentialStory38), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayStory(
			   new BuildingDisplay(PixelAssetManager.residentialStory39), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayStory(
			   new BuildingDisplay(PixelAssetManager.residentialStory43), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayStory(
			   new BuildingDisplay(PixelAssetManager.residentialStory44), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayStory(
			   new BuildingDisplay(PixelAssetManager.residentialStory45), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayStory(
			   new BuildingDisplay(PixelAssetManager.residentialStory47), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayStory(
			   new BuildingDisplay(PixelAssetManager.residentialStory48), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayStory(
			   new BuildingDisplay(PixelAssetManager.residentialStory49), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayStory(
			   new BuildingDisplay(PixelAssetManager.residentialStory50), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayStory(
			   new BuildingDisplay(PixelAssetManager.residentialStory51), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayStory(
			   new BuildingDisplay(PixelAssetManager.residentialStory52), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayStory(
			   new BuildingDisplay(PixelAssetManager.residentialStory53), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayStory(
			   new BuildingDisplay(PixelAssetManager.residentialStory54), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayStory(
			   new BuildingDisplay(PixelAssetManager.residentialStory55), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayStory(
			   new BuildingDisplay(PixelAssetManager.residentialStory56), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof57), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof58), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof59), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof60), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof61), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof62), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof63), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof64), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof65), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof66), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof67), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof68), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof69), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof70), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof71), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof72), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof73), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof74), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof75), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof76), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof77), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof80), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof81), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof82), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof83), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof84), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof88), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof89), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof90), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof91), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof96), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof97), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof98), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof104), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof105), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.residentialRoof112), true, false, false);
		BuildingFactory.getInstance().registerBuildingDisplayBase(
			   new BuildingDisplay(PixelAssetManager.commercialBase18, true), false, true, false);
		BuildingFactory.getInstance().registerBuildingDisplayBase(
			   new BuildingDisplay(PixelAssetManager.commercialBase20, true), false, true, false);
		BuildingFactory.getInstance().registerBuildingDisplayBase(
			   new BuildingDisplay(PixelAssetManager.commercialBase04, true), false, true, false);
		BuildingFactory.getInstance().registerBuildingDisplayBase(
			   new BuildingDisplay(PixelAssetManager.commercialBase108, true), false, true, false);
		BuildingFactory.getInstance().registerBuildingDisplayBase(
			   new BuildingDisplay(PixelAssetManager.commercialBase113, true), false, true, false);
		BuildingFactory.getInstance().registerBuildingDisplayBase(
			   new BuildingDisplay(PixelAssetManager.commercialBase115, true), false, true, false);
		BuildingFactory.getInstance().registerBuildingDisplayBase(
			   new BuildingDisplay(PixelAssetManager.commercialBase109, true), false, true, false);
		BuildingFactory.getInstance().registerBuildingDisplayBase(
			   new BuildingDisplay(PixelAssetManager.commercialBase101, true), false, true, false);
		BuildingFactory.getInstance().registerBuildingDisplayStory(
			   new BuildingDisplay(PixelAssetManager.commercialStory00), false, true, false);
		BuildingFactory.getInstance().registerBuildingDisplayStory(
			   new BuildingDisplay(PixelAssetManager.commercialStory07), false, true, false);
		BuildingFactory.getInstance().registerBuildingDisplayStory(
			   new BuildingDisplay(PixelAssetManager.commercialStory08), false, true, false);
		BuildingFactory.getInstance().registerBuildingDisplayStory(
			   new BuildingDisplay(PixelAssetManager.commercialStory16), false, true, false);
		BuildingFactory.getInstance().registerBuildingDisplayStory(
			   new BuildingDisplay(PixelAssetManager.commercialStory24), false, true, false);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.commercialRoof05), false, true, true);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.commercialRoof06), false, true, true);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.commercialRoof13), false, true, true);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.commercialRoof79), false, true, true);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.commercialRoof86), false, true, true);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.commercialRoof87), false, true, true);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.commercialRoof91), false, true, true);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.commercialRoof111), false, true, true);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.commercialRoof119), false, true, true);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.commercialRoof118), false, true, true);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.commercialRoof120), false, true, true);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.commercialRoof121), false, true, true);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.commercialRoof126), false, true, true);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.commercialRoof127), false, true, true);
		BuildingFactory.getInstance().registerBuildingDisplayRoof(
			   new BuildingDisplay(PixelAssetManager.commercialRoof128), false, true, true);
		BuildingFactory.getInstance().registerBuildingDisplayBase(
			   new BuildingDisplay(PixelAssetManager.officeBase02, true), false, false, true);
		BuildingFactory.getInstance().registerBuildingDisplayBase(
			   new BuildingDisplay(PixelAssetManager.officeBase10, true), false, false, true);
		BuildingFactory.getInstance().registerBuildingDisplayBase(
			   new BuildingDisplay(PixelAssetManager.officeBase109, true), false, false, true);
		BuildingFactory.getInstance().registerBuildingDisplayBase(
			   new BuildingDisplay(PixelAssetManager.officeBase125, true), false, false, true);
		BuildingFactory.getInstance().registerBuildingDisplayBase(
			   new BuildingDisplay(PixelAssetManager.officeBase117, true), false, false, true);
		BuildingFactory.getInstance().registerBuildingDisplayBase(
			   new BuildingDisplay(PixelAssetManager.officeBase123, true), false, false, true);
		BuildingFactory.getInstance().registerBuildingDisplayStory(
			   new BuildingDisplay(PixelAssetManager.officeStory07), false, false, true);
		BuildingFactory.getInstance().registerBuildingDisplayStory(
			   new BuildingDisplay(PixelAssetManager.officeStory15), false, false, true);
		BuildingFactory.getInstance().registerBuildingDisplayStory(
			   new BuildingDisplay(PixelAssetManager.officeStory23), false, false, true);
		BuildingFactory.getInstance().registerBuildingDisplayStory(
			   new BuildingDisplay(PixelAssetManager.officeStory31), false, false, true);

	}

	public void update() {

		updateResidentialZones();
		updateCommercialZones();
		updateOfficeZones();
	}

	private void updateResidentialZones() {

		residentialTimer += Gdx.graphics.getDeltaTime();
		if(residentialTimer >= residentialTime && !residentialZones.isEmpty()) {

			int index = random.nextInt(residentialZones.size());
			Zone zone = null;

			if((zone = residentialZones.get(index)).isZoneFull()) {
				for(Zone resZone : residentialZones) {
					if(!resZone.isZoneFull()) {
						zone = resZone;
						break;
					}
				}
			}

			if(zone != null) {
				zone.update();
			}

			residentialTimer = 0;
		}
	}

	private void updateCommercialZones() {
		commericalTimer += Gdx.graphics.getDeltaTime();
		if(commericalTimer >= commercialTime && !commercialZones.isEmpty()) {

			int index = random.nextInt(commercialZones.size());
			Zone zone = null;

			if((zone = commercialZones.get(index)).isZoneFull()) {
				for(Zone resZone : commercialZones) {
					if(!resZone.isZoneFull()) {
						zone = resZone;
						break;
					}
				}
			}

			if(zone != null) {
				zone.update();
			}

			commericalTimer = 0;
		}
	}

	private void updateOfficeZones() {
		officeTimer += Gdx.graphics.getDeltaTime();
		if(officeTimer >= officeTime && !officeZones.isEmpty()) {

			int index = random.nextInt(officeZones.size());
			Zone zone = null;

			if((zone = officeZones.get(index)).isZoneFull()) {
				for(Zone offZone : officeZones) {
					if(!offZone.isZoneFull()) {
						zone = offZone;
						break;
					}
				}
			}

			if(zone != null) {
				zone.update();
			}

			officeTimer = 0;
		}
	}

	//
	// generateArray()
	// Generates our isometric array of cells
	//
	private void generateArray() {
		// initialize our array of objects
		mapArray = new Cell[width][height];

		int middleWidth = MathUtils.round(widthPixels / 2.0f);
		int middleHeight = MathUtils.round(heightPixels / 2.0f);

		// We step in halves
		float stepX = cellRowWidth / 2;
		float stepY = cellRowHeight / 2;

		// Find our initial position at the very top of the array
		float topPositionX = middleWidth;
		float topPositionY = heightPixels;
		topOfMap = new Vector2(topPositionX, topPositionY);

		for (int countX = 0; countX < width; countX++) {
			for (int countY = 0; countY < width; countY++) {
				// we move diagonally, so it makes sense we move both vertically and horizontally each instance
				float currentPositionX = topPositionX - countY * stepX;
				float currentPositionY = topPositionY - countY * stepY;

				addActor(mapArray[countX][countY] =
					   new Cell(currentPositionX, currentPositionY, cellWidth, cellHeight, new MapCoord(countX, countY)));
			}

			topPositionX += stepX;
			topPositionY -= stepY;
		}
	}

	//
	// CheckPosition()
	// If there is a cell at the position within the parent scene, then it returns that cell
	// if there is not cell that that position, then we return null
	//
	public Cell checkPosition(float x, float y) {

		Vector2 point = new Vector2(x, y);
		Actor actor = stage.hit(x, y, true);
		Cell cell = null;
		Cell otherCell = null;
		Map.MapCoord cellCoord;

		if(actor == null)
			return null;

		if(actor.getName() == "Cell") {
			cell = (Cell)actor;
		} else {
			MapObject object = (MapObject)actor.getParent();
			cell = getCell(object.getMapPosition());
		}

		if (cell != null) {

			Vector2 centerCellPosition = new Vector2(cell.getX() + cell.getWidth() / 2, cell.getY() + + 2*cell.getHeight()/3);
			cellCoord = cell.getMapPosition();

			// we need to find the next closest cell, and determine if this click is
			// within this cell or the other cell

			// then the other cell is on the right of this cell
			if (point.x > centerCellPosition.x) {
				// The point is in the upper half of this cell
				if (point.y > centerCellPosition.y) {
					otherCell = getCell(cellCoord.x, cellCoord.y - 1);
				}
				// the point is in the lower half of the cell
				else if (point.y < centerCellPosition.y) {
					otherCell = getCell(cellCoord.x + 1, cellCoord.y);
				}
			}
			// then the other cell is on the left of this cell
			else if (point.x < centerCellPosition.x) {
				// The point is in the upper half of this cell
				if (point.y > centerCellPosition.y) {
					otherCell = getCell(cellCoord.x - 1, cellCoord.y);
				}
				// the point is in the lower half of the cell
				else if (point.y < centerCellPosition.y) {
					otherCell = getCell(cellCoord.x, cellCoord.y + 1);
				}
			}

			if(otherCell != null)
			{
				// Then get center positions for each of these two cells
				Vector2 centerOtherCellPosition = new Vector2(otherCell.getX() + otherCell.getWidth() / 2, otherCell.getY() + 2*otherCell.getHeight()/3);

				// then find the distance from the center of each cell to the point of interest
				Vector2 position = new Vector2(x, y);

				// find the actual distances between the vector positions
				float distance1 = centerCellPosition.dst(position);
				float distance2 = centerOtherCellPosition.dst(position);

				// return the cell assoc with the smallest distance
				return (distance1 > distance2) ? otherCell : cell;
			}
			else return cell;
		}

		return null;
	}

	public void addZone(Zone zone) {

		if(zone.getZoneType() == Building.BuildingType.RESIDENTIAL)
			residentialZones.add((ResidentialZone)zone);
		else if(zone.getZoneType() == Building.BuildingType.COMMERCIAL)
			commercialZones.add((CommercialZone) zone);
		else if(zone.getZoneType() == Building.BuildingType.OFFICE)
			officeZones.add((OfficeZone) zone);
	}

	public Cell getCell(int x, int y) {

		if (x >= 0 && x < width &&
			   y >= 0 && y < height) {
			return mapArray[x][y];
		}
		return null;
	}

	public Cell getCell(MapCoord coord) {
		return getCell(coord.x, coord.y);
	}

	public int getCellWidth() {
		return cellWidth;
	}

	public int getCellHeight() {
		return cellHeight;
	}

	public float getCellRowHeight() {
		return cellRowHeight;
	}

	public int getWidthInCells() { return width; }

	public int getHeightInCells() { return height; }
}
