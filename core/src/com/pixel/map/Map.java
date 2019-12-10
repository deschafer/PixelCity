package com.pixel.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pixel.game.PixelAssetManager;
import com.pixel.game.PixelCityGame;
import com.pixel.map.object.Cell;
import com.pixel.map.object.MapObject;
import com.pixel.map.object.building.Building;
import com.pixel.map.object.building.display.BuildingDisplay;
import com.pixel.map.object.building.BuildingFactory;
import com.pixel.map.object.building.special.ServiceBuilding;
import com.pixel.map.object.building.special.SpecialtyBuildingFactory;
import com.pixel.map.object.building.special.education.SecondarySchool;
import com.pixel.map.object.building.special.fire.FireStation;
import com.pixel.map.object.building.special.health.Hospital;
import com.pixel.map.object.building.special.police.PoliceStation;
import com.pixel.map.object.building.special.utilities.power.CoalPowerPlant;
import com.pixel.map.object.building.special.utilities.water.WaterTank;
import com.pixel.map.object.roads.*;
import com.pixel.map.object.zoning.*;
import com.pixel.map.visualizer.VisualizerFactory;
import com.pixel.object.Resident;
import com.pixel.object.SimpleActor;
import com.pixel.serialization.CellSerializable;
import com.pixel.serialization.MapObjectSerializable;
import com.pixel.serialization.MapSerializable;
import com.pixel.serialization.ZoneSerializable;
import sun.nio.ch.IOStatus;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;



public class Map extends Group implements Serializable {

	public static MapCoord zeroCoordinate;

	private int width;                     // width of the map array in cells
	private int height;                     // height ...
	private float widthPixels;               // width of the map in pixels
	private float heightPixels;           // height of the map in pixels
	private Cell[][] mapArray;
	public static final int cellWidth = 132;          // width of the cell in pixels
	public static final int cellHeight = 102;     // height of the cell in pixels
	public static final int cellBuildingBaseWidth = 132;
	public static final int cellBuildingBaseHeight = 127;
	public static final int cellStoryWidth = 99;
	public static final int cellStoryHeight = 85;
	public static final float cellRowWidth = 132;
	public static final float cellRowHeight = 2.0f / 3.0f * 101.0f;
	private Vector2 topOfMap;
	private ArrayList<Zone> residentialZones = new ArrayList<>();
	private ArrayList<Zone> commercialZones = new ArrayList<>();
	private ArrayList<Zone> officeZones = new ArrayList<>();

	private static float residentialTimer = 0;
	private static float residentialTime = 1.0f;
	private static float commericalTimer = 0;
	private static float commercialTime = 1.0f;
	private static float officeTimer = 0;
	private static float officeTime = 1.0f;

	private Random random = new Random();

	public Map(int width, int height, Stage stage) {

		// Set our member variables
		this.width = width;
		this.height = height;
		setStage(stage);
		widthPixels = width * cellRowWidth;
		heightPixels = height * cellRowHeight;
		zeroCoordinate = new MapCoord(0, 0);
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

		// TODO: change all of the road creation below to be a road class only
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.ROADWAY_NS,
			   new Road(0, 0, new MapCoord(0, 0), RoadFactory.RoadType.ROADWAY_NS));
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.ROADWAY_EW,
			   new Road(0, 0, new MapCoord(0, 0), RoadFactory.RoadType.ROADWAY_EW));
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.END_E,
			   new Road(0, 0, new MapCoord(0, 0), RoadFactory.RoadType.END_E));
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.END_N,
			   new Road(0, 0, new MapCoord(0, 0), RoadFactory.RoadType.END_N));
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.END_S,
			   new Road(0, 0, new MapCoord(0, 0), RoadFactory.RoadType.END_S));
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.END_W,
			   new Road(0, 0, new MapCoord(0, 0), RoadFactory.RoadType.END_W));
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.CORNER_EN,
			   new Road(0, 0, new MapCoord(0, 0), RoadFactory.RoadType.CORNER_EN));
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.CORNER_NE,
			   new Road(0, 0, new MapCoord(0, 0), RoadFactory.RoadType.CORNER_NE));
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.CORNER_NW,
			   new Road(0, 0, new MapCoord(0, 0), RoadFactory.RoadType.CORNER_NW));
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.CORNER_WN,
			   new Road(0, 0, new MapCoord(0, 0), RoadFactory.RoadType.CORNER_WN));
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.INTERSECT_4,
			   new Road(0, 0, new MapCoord(0, 0), RoadFactory.RoadType.INTERSECT_4));
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.INTERSECT_3_E,
			   new Road(0, 0, new MapCoord(0, 0), RoadFactory.RoadType.INTERSECT_3_E));
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.INTERSECT_3_N,
			   new Road(0, 0, new MapCoord(0, 0), RoadFactory.RoadType.INTERSECT_3_N));
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.INTERSECT_3_S,
			   new Road(0, 0, new MapCoord(0, 0), RoadFactory.RoadType.INTERSECT_3_S));
		RoadFactory.getInstance().registerRoadType(
			   RoadFactory.RoadType.INTERSECT_3_W,
			   new Road(0, 0, new MapCoord(0, 0), RoadFactory.RoadType.INTERSECT_3_W));


		// register all of the visualizers we use in the map tools
		// We only need to placeable tiles for visualizers
		VisualizerFactory.getInstance().registerMapObject(
			   new Road(0, 0, new MapCoord(0, 0), RoadFactory.RoadType.ROADWAY_NS));
		VisualizerFactory.getInstance().registerMapObject(
			   new Road(0, 0, new MapCoord(0, 0), RoadFactory.RoadType.ROADWAY_EW));
		VisualizerFactory.getInstance().registerMapObject(
			   new Road(0, 0, new MapCoord(0, 0), RoadFactory.RoadType.END_E));
		VisualizerFactory.getInstance().registerMapObject(
			   new Road(0, 0, new MapCoord(0, 0), RoadFactory.RoadType.END_N));
		VisualizerFactory.getInstance().registerMapObject(
			   new Road(0, 0, new MapCoord(0, 0), RoadFactory.RoadType.END_S));
		VisualizerFactory.getInstance().registerMapObject(
			   new Road(0, 0, new MapCoord(0, 0), RoadFactory.RoadType.END_W));
		VisualizerFactory.getInstance().registerMapObject(
			   new ZoneCell(cellWidth, cellHeight, new MapCoord(0, 0), Building.BuildingType.RESIDENTIAL, null));
		VisualizerFactory.getInstance().registerMapObject(
			   new ZoneCell(cellWidth, cellHeight, new MapCoord(0, 0), Building.BuildingType.COMMERCIAL, null));
		VisualizerFactory.getInstance().registerMapObject(
			   new ZoneCell(cellWidth, cellHeight, new MapCoord(0, 0), Building.BuildingType.OFFICE, null));
		VisualizerFactory.getInstance().registerMapObject(
			   new CoalPowerPlant(0, 0, new MapCoord(0, 0), false));
		VisualizerFactory.getInstance().registerMapObject(
			   new WaterTank(0, 0, new MapCoord(0, 0), false));

		// since these are service buildings, they attempt to connect to the map. Prevent this in the class
		ServiceBuilding.placedOnMap = false;
		VisualizerFactory.getInstance().registerMapObject(
			   new FireStation(0, 0, new MapCoord(0, 0)));
		VisualizerFactory.getInstance().registerMapObject(
			   new PoliceStation(0, 0, new MapCoord(0, 0)));
		VisualizerFactory.getInstance().registerMapObject(
			   new Hospital(0, 0, new MapCoord(0, 0)));
		VisualizerFactory.getInstance().registerMapObject(
			   new SecondarySchool(0, 0, new MapCoord(0, 0)));


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

		/*
		// register our specific, RCO buildings
		BuildingFactory.getInstance().registerSpeceficBuildingDisplayBase(
			   new BuildingDisplay((PixelAssetManager.blueSkyscraperBase), true),
			   BuildingFactory.SpecificBuildingTypes.BLUE_SKYSCRAPER.getName(),
			   Building.BuildingType.RESIDENTIAL, new Rectangle(0,0,2,2));
		BuildingFactory.getInstance().registerSpeceficBuildingDisplayStory(
			   new BuildingDisplay((PixelAssetManager.blueSkyscraperStory)),
			   BuildingFactory.SpecificBuildingTypes.BLUE_SKYSCRAPER.getName(),
			   Building.BuildingType.RESIDENTIAL);
		BuildingFactory.getInstance().registerSpeceficBuildingDisplayRoof(
			   new BuildingDisplay((PixelAssetManager.blueSkyscraperRoof)),
			   BuildingFactory.SpecificBuildingTypes.BLUE_SKYSCRAPER.getName(),
			   Building.BuildingType.RESIDENTIAL);
		 */

		// register our specialty buildings
		CoalPowerPlant coalPowerPlant;
		SpecialtyBuildingFactory.getInstance().registerObject(coalPowerPlant =
			   new CoalPowerPlant(0, 0, new MapCoord(0, 0), false), coalPowerPlant.getName());
		WaterTank waterTank;
		SpecialtyBuildingFactory.getInstance().registerObject(waterTank =
			   new WaterTank(0, 0, new MapCoord(0, 0), false), waterTank.getName());
		FireStation fireStation;
		SpecialtyBuildingFactory.getInstance().registerObject(fireStation =
			   new FireStation(0, 0, new MapCoord(0, 0)), fireStation.getName());
		PoliceStation policeStation;
		SpecialtyBuildingFactory.getInstance().registerObject(policeStation =
			   new PoliceStation(0, 0, new MapCoord(0, 0)), policeStation.getName());
		Hospital hospital;
		SpecialtyBuildingFactory.getInstance().registerObject(hospital =
			   new Hospital(0, 0, new MapCoord(0, 0)), hospital.getName());
		SecondarySchool school;
		SpecialtyBuildingFactory.getInstance().registerObject(school =
			   new SecondarySchool(0, 0, new MapCoord(0, 0)), school.getName());

		// Reset it back to normal
		ServiceBuilding.placedOnMap = true;
	}

	public void update() {

		updateResidentialZones();
		updateCommercialZones();
		updateOfficeZones();
		if (Road.roadsNeedToUpdate) {
			Road.updateRoads();
		}
	}

	private void updateResidentialZones() {

		residentialTimer += Gdx.graphics.getDeltaTime();
		if (residentialTimer >= residentialTime && !residentialZones.isEmpty()) {

			int index = random.nextInt(residentialZones.size());
			Zone zone = null;

			if ((zone = residentialZones.get(index)).isZoneFull()) {
				for (Zone resZone : residentialZones) {
					if (!resZone.isZoneFull()) {
						zone = resZone;
						break;
					}
				}
			}

			if (zone != null) {
				zone.update();
			}

			residentialTimer = 0;
		}
	}

	private void updateCommercialZones() {
		commericalTimer += Gdx.graphics.getDeltaTime();
		if (commericalTimer >= commercialTime && !commercialZones.isEmpty()) {

			int index = random.nextInt(commercialZones.size());
			Zone zone = null;

			if ((zone = commercialZones.get(index)).isZoneFull()) {
				for (Zone resZone : commercialZones) {
					if (!resZone.isZoneFull()) {
						zone = resZone;
						break;
					}
				}
			}

			if (zone != null) {
				zone.update();
			}

			commericalTimer = 0;
		}
	}

	private void updateOfficeZones() {
		officeTimer += Gdx.graphics.getDeltaTime();
		if (officeTimer >= officeTime && !officeZones.isEmpty()) {

			int index = random.nextInt(officeZones.size());
			Zone zone = null;

			if ((zone = officeZones.get(index)).isZoneFull()) {
				for (Zone offZone : officeZones) {
					if (!offZone.isZoneFull()) {
						zone = offZone;
						break;
					}
				}
			}

			if (zone != null) {
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
		Actor actor = getStage().hit(x, y, true);
		Cell cell = null;
		Cell otherCell = null;
		MapCoord cellCoord;

		if (actor == null)
			return null;

		if (actor.getName() == "Cell") {
			cell = (Cell) actor;
		} else {
			MapObject object = (MapObject) actor.getParent();
			cell = getCell(object.getMapPosition());
		}

		if (cell != null) {

			Vector2 centerCellPosition = new Vector2(cell.getX() + cell.getWidth() / 2, cell.getY() + +2 * cell.getHeight() / 3);
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

			if (otherCell != null) {
				// Then get center positions for each of these two cells
				Vector2 centerOtherCellPosition = new Vector2(otherCell.getX() + otherCell.getWidth() / 2, otherCell.getY() + 2 * otherCell.getHeight() / 3);

				// then find the distance from the center of each cell to the point of interest
				Vector2 position = new Vector2(x, y);

				// find the actual distances between the vector positions
				float distance1 = centerCellPosition.dst(position);
				float distance2 = centerOtherCellPosition.dst(position);

				// return the cell assoc with the smallest distance
				return (distance1 > distance2) ? otherCell : cell;
			} else return cell;
		}

		return null;
	}

	public void addZone(Zone zone) {

		if (zone.getZoneType() == Building.BuildingType.RESIDENTIAL)
			residentialZones.add(zone);
		else if (zone.getZoneType() == Building.BuildingType.COMMERCIAL)
			commercialZones.add(zone);
		else if (zone.getZoneType() == Building.BuildingType.OFFICE)
			officeZones.add(zone);
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

	public int getWidthInCells() {
		return width;
	}

	public int getHeightInCells() {
		return height;
	}

	public void serialize(ObjectOutputStream out) {

		// we handle all of our cells and serializable map objects
		ArrayList<Serializable> serializables = new ArrayList<>();

		// we get all of our serializables first
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {

				serializables.add(mapArray[i][j].getSerializableObject());
			}
		}

		// then we need to serialize all of our zones, and our width and height of this map
		// create a map serializable for this
		MapSerializable serializable = new MapSerializable();
		serializable.height = height;
		serializable.width = width;
		serializable.topOfMap = topOfMap;
		serializable.heightPixels = heightPixels;
		serializable.widthPixels = widthPixels;

		if (!residentialZones.isEmpty()) {
			serializable.residentialZones = new ArrayList<>();
			for (Zone zone : residentialZones) {
				serializable.residentialZones.add(zone.getSerializableObject());
			}
		}
		if (!commercialZones.isEmpty()) {
			serializable.commercialZones = new ArrayList<>();
			for (Zone zone : commercialZones) {
				serializable.commercialZones.add(zone.getSerializableObject());
			}
		}
		if (!officeZones.isEmpty()) {
			serializable.officeZones = new ArrayList<>();
			for (Zone zone : officeZones) {
				serializable.officeZones.add(zone.getSerializableObject());
			}
		}

		// then we write out this object
		try {
			out.writeObject(serializable);
		} catch (IOException ex) {
			System.out.println("IOException during game map MapSerializable serialization " + ex.getMessage());
		}

		// then since each of these are serializable, we write them using the ObjectOutputStream
		for (Serializable serial : serializables) {
			try {
				out.writeObject(serial);
			} catch (IOException ex) {
				System.out.println("IOException during game map serialization " + ex.getMessage());
			}
		}
	}

	public void deserialize(ObjectInputStream input) {

		MapSerializable mapSerializable = null;

		try {
			mapSerializable = (MapSerializable)input.readObject();
		} catch (IOException ex) {
			System.out.println("IOException during game map serialization " + ex.getMessage());

		} catch (ClassNotFoundException ex) {
			System.out.println("ClassNotFoundException during game map serialization " + ex.getMessage());
		}

		if (mapSerializable != null) {

			// transfer data from this file to this map
			width = mapSerializable.width;
			height = mapSerializable.height;
			widthPixels = mapSerializable.widthPixels;
			heightPixels = mapSerializable.heightPixels;
			topOfMap = mapSerializable.topOfMap;

			if (!mapSerializable.residentialZones.isEmpty()) {
				for (ZoneSerializable zone : mapSerializable.residentialZones) {
					residentialZones.add(new Zone(zone));
				}
			}
			if (!mapSerializable.commercialZones.isEmpty()) {
				for (ZoneSerializable zone : mapSerializable.commercialZones) {
					commercialZones.add(new Zone(zone));
				}
			}
			if (!mapSerializable.officeZones.isEmpty()) {
				for (ZoneSerializable zone : mapSerializable.officeZones) {
					officeZones.add(new Zone(zone));
				}
			}
		}

		// then based off the data we just received, we can go ahead and load in the cells as well

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				try {
					CellSerializable serializable = (CellSerializable)input.readObject();

					// create a cell from this cell
					mapArray[i][j] = (Cell)serializable.getNonSerializableObject();

				} catch (IOException ex) {
					System.out.println("IOException during game map serialization " + ex.getMessage());

				} catch (ClassNotFoundException ex) {
					System.out.println("ClassNotFoundException during game map serialization " + ex.getMessage());
				}
			}
		}
	}
}

