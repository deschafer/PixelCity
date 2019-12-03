package com.pixel.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.pixel.UI.dialog.DemandDialog;
import com.pixel.UI.dialog.PauseDialog;
import com.pixel.UI.dialog.StatsDialog;
import com.pixel.UI.element.Icon;
import com.pixel.UI.element.IconList;
import com.pixel.city.City;
import com.pixel.city.FinancialManager;
import com.pixel.game.PixelAssetManager;
import com.pixel.game.PixelCityGame;
import com.pixel.game.styles.Styles;
import com.pixel.object.SimpleActor;
import com.pixel.scene.GameScene;

import java.util.ArrayList;

public class GameSceneUI extends Stage {

	private float width;
	private float height;

	private ArrayList<Icon> icons = new ArrayList<>();
	private Label balanceLabel;
	private SimpleActor balanceLabelBackground;
	private Label cityNameLabel;
	private SimpleActor cityNameBackground;
	private Label populationLabel;
	private SimpleActor populationPersonIcon;
	private SimpleActor populationBackground;

	private float horizPadding = 10;
	private float verticalPadding = 10;

	// contain all UI element including Icons and Labels

	public GameSceneUI(float width, float height) {
		super();

		InputMultiplexer im = (InputMultiplexer) Gdx.input.getInputProcessor();
		im.addProcessor(this);

		this.width = width;
		this.height = height;

		initLeftSideIcons();
		initRightSideIcons();
		initLabels();

	}

	public void reset() {


		resetNameLabel();
	}

	private void initLeftSideIcons() {
		// create the top left icon list
		IconList leftIconList = new IconList(10, height, 3, 80);
		// since icon lists are hidden by default
		leftIconList.setVisible(true);
		// also add to this stage to be drawn
		addActor(leftIconList);

		// set up the button size for all icons
		Vector2 buttonSize = new Vector2(50, 50);

		Icon roadIcon = new Icon(PixelAssetManager.roadIcon, PixelAssetManager.blueBox, buttonSize, leftIconList);
		icons.add(roadIcon);
		leftIconList.addIcon(roadIcon);
		roadIcon.addListener((Event e) -> {
				   if (!(e instanceof InputEvent) || !((InputEvent) e).getType().equals(InputEvent.Type.touchDown)) {
					   return false;
				   }
				   // set the new tool
				   GameScene.getInstance().setActiveTool(GameScene.Tools.ROAD);
				   return true;
			   }
		);

		// Then we add icons to this list
		// We add the zoning icon
		Icon zoningIcon = new Icon(PixelAssetManager.zoningIcon, PixelAssetManager.blueBox, buttonSize, leftIconList);
		icons.add(zoningIcon);

		// also need to add a listener to this icon
		// this listener will open the next list within
		zoningIcon.addListener((Event e) -> {
				   if (!(e instanceof InputEvent) || !((InputEvent) e).getType().equals(InputEvent.Type.touchDown)) {
					   return false;
				   }
				   clearIconLists(zoningIcon);
				   zoningIcon.setIconListVisible(!zoningIcon.isIconListVisible());
				   return true;
			   }
		);

		// creating the zoning icon sub list
		IconList zoningIconList =
			   new IconList(zoningIcon.getX() * 2 + zoningIcon.getWidth(), zoningIcon.getY(), 3, -zoningIcon.getHeight());

		// creating a residential zoning icon that selects the res zoning tool
		Icon resIcon = new Icon(PixelAssetManager.greenZoningIcon, PixelAssetManager.blueBox, buttonSize, zoningIconList);
		icons.add(resIcon);
		zoningIconList.addIcon(resIcon);
		resIcon.addListener((Event e) -> {
				   if (!(e instanceof InputEvent) || !((InputEvent) e).getType().equals(InputEvent.Type.touchDown)) {
					   return false;
				   }
				   // set the new tool
				   GameScene.getInstance().setActiveTool(GameScene.Tools.RES_ZONING);
				   return true;
			   }
		);
		// creating a commercial zoning icon
		Icon commIcon = new Icon(PixelAssetManager.blueZoningIcon, PixelAssetManager.blueBox, buttonSize, zoningIconList);
		icons.add(commIcon);
		zoningIconList.addIcon(commIcon);
		commIcon.addListener((Event e) -> {
				   if (!(e instanceof InputEvent) || !((InputEvent) e).getType().equals(InputEvent.Type.touchDown)) {
					   return false;
				   }
				   // set the new tool
				   GameScene.getInstance().setActiveTool(GameScene.Tools.COMM_ZONING);
				   return true;
			   }
		);
		// creating an office zoning tool
		Icon offIcon = new Icon(PixelAssetManager.amberZoningIcon, PixelAssetManager.blueBox, buttonSize, zoningIconList);
		icons.add(offIcon);
		zoningIconList.addIcon(offIcon);
		offIcon.addListener((Event e) -> {
				   if (!(e instanceof InputEvent) || !((InputEvent) e).getType().equals(InputEvent.Type.touchDown)) {
					   return false;
				   }
				   // set the new tool
				   GameScene.getInstance().setActiveTool(GameScene.Tools.OFF_ZONING);
				   return true;
			   }
		);
		zoningIcon.addIconList(zoningIconList);
		// add the icon to the left list
		leftIconList.addIcon(zoningIcon);


		// creating the services icon and icon lists
		Icon serviceIcon = new Icon(PixelAssetManager.serviceBuildingIcon, PixelAssetManager.blueBox, buttonSize, leftIconList);
		icons.add(serviceIcon);
		// also need to add a listener to this icon
		// this listener will open the next list within
		serviceIcon.addListener((Event e) -> {
				   if (!(e instanceof InputEvent) || !((InputEvent) e).getType().equals(InputEvent.Type.touchDown)) {
					   return false;
				   }
				   clearIconLists(serviceIcon);
				   serviceIcon.setIconListVisible(!serviceIcon.isIconListVisible());
				   System.out.println("Service Button");
				   return true;
			   }
		);

		// add the additional list for this icon
		IconList serviceList =
			   new IconList(serviceIcon.getX() * 2 + serviceIcon.getWidth(), serviceIcon.getY(), 3, -serviceIcon.getHeight());
		// creating an icon for fire services
		Icon fireIcon = new Icon(PixelAssetManager.fireIcon, PixelAssetManager.blueBox, buttonSize, serviceList);
		icons.add(fireIcon);
		serviceList.addIcon(fireIcon);
		fireIcon.addListener((Event e) -> {
				   if (!(e instanceof InputEvent) || !((InputEvent) e).getType().equals(InputEvent.Type.touchDown)) {
					   return false;
				   }
				   // set the new tool
				   GameScene.getInstance().setActiveTool(GameScene.Tools.FIRE);
				   return true;
			   }
		);
		// creating an icon for police services
		Icon policeIcon = new Icon(PixelAssetManager.policeIcon, PixelAssetManager.blueBox, buttonSize, serviceList);
		icons.add(policeIcon);
		serviceList.addIcon(policeIcon);
		policeIcon.addListener((Event e) -> {
				   if (!(e instanceof InputEvent) || !((InputEvent) e).getType().equals(InputEvent.Type.touchDown)) {
					   return false;
				   }
				   // set the new tool
				   GameScene.getInstance().setActiveTool(GameScene.Tools.POLICE);
				   return true;
			   }
		);
		// creating an icon for education services
		Icon educationIcon = new Icon(PixelAssetManager.educationIcon, PixelAssetManager.blueBox, buttonSize, serviceList);
		icons.add(educationIcon);
		serviceList.addIcon(educationIcon);
		educationIcon.addListener((Event e) -> {
				   if (!(e instanceof InputEvent) || !((InputEvent) e).getType().equals(InputEvent.Type.touchDown)) {
					   return false;
				   }
				   // set the new tool
				   GameScene.getInstance().setActiveTool(GameScene.Tools.ED);
				   return true;
			   }
		);
		// creating an icon for health services
		Icon healthIcon = new Icon(PixelAssetManager.healthIcon, PixelAssetManager.blueBox, buttonSize, serviceList);
		icons.add(healthIcon);
		serviceList.addIcon(healthIcon);
		healthIcon.addListener((Event e) -> {
				   if (!(e instanceof InputEvent) || !((InputEvent) e).getType().equals(InputEvent.Type.touchDown)) {
					   return false;
				   }
				   // set the new tool
				   GameScene.getInstance().setActiveTool(GameScene.Tools.HEALTH);
				   return true;
			   }
		);
		// adding the list to the icon
		serviceIcon.addIconList(serviceList);
		// add the icon to the left list
		leftIconList.addIcon(serviceIcon);


		Icon utilityIcon = new Icon(PixelAssetManager.utilityIcon, PixelAssetManager.blueBox, buttonSize, leftIconList);
		icons.add(utilityIcon);
		// also need to add a listener to this icon
		// this listener will open the next list within
		utilityIcon.addListener((Event e) -> {
				   if (!(e instanceof InputEvent) || !((InputEvent) e).getType().equals(InputEvent.Type.touchDown)) {
					   return false;
				   }
				   clearIconLists(utilityIcon);
				   utilityIcon.setIconListVisible(!utilityIcon.isIconListVisible());
				   System.out.println("Utility Button");
				   return true;
			   }
		);

		// add the additional list for this icon
		IconList utilityList =
			   new IconList(utilityIcon.getX() * 2 + utilityIcon.getWidth(), utilityIcon.getY(), 3, -utilityIcon.getHeight());
		// creating an icon for water utilities
		Icon waterIcon = new Icon(PixelAssetManager.waterUtilityIcon, PixelAssetManager.blueBox, buttonSize, utilityList);
		icons.add(waterIcon);
		utilityList.addIcon(waterIcon);
		waterIcon.addListener((Event e) -> {
				   if (!(e instanceof InputEvent) || !((InputEvent) e).getType().equals(InputEvent.Type.touchDown)) {
					   return false;
				   }
				   // set the new tool
				   GameScene.getInstance().setActiveTool(GameScene.Tools.WATER);
				   return true;
			   }
		);
		// creating an icon for power utilities
		Icon powerIcon = new Icon(PixelAssetManager.powerPlantIcon, PixelAssetManager.blueBox, buttonSize, utilityList);
		icons.add(powerIcon);
		utilityList.addIcon(powerIcon);
		powerIcon.addListener((Event e) -> {
				   if (!(e instanceof InputEvent) || !((InputEvent) e).getType().equals(InputEvent.Type.touchDown)) {
					   return false;
				   }
				   // set the new tool
				   GameScene.getInstance().setActiveTool(GameScene.Tools.POWER);
				   return true;
			   }
		);

		// adding the list to the icon
		utilityIcon.addIconList(utilityList);

		// add the icon to the left list
		leftIconList.addIcon(utilityIcon);

		// creating an icon for the deletion tool
		Icon deleteIcon = new Icon(PixelAssetManager.deleteIcon, PixelAssetManager.blueBox, buttonSize, leftIconList);
		icons.add(deleteIcon);
		utilityList.addIcon(deleteIcon);
		deleteIcon.addListener((Event e) -> {
				   if (!(e instanceof InputEvent) || !((InputEvent) e).getType().equals(InputEvent.Type.touchDown)) {
					   return false;
				   }
				   // set the new tool
				   GameScene.getInstance().setActiveTool(GameScene.Tools.DELETE);
				   return true;
			   }
		);
		// add the icon to the left list
		leftIconList.addIcon(deleteIcon);
	}

	private void initBalanceLabel() {

		// set up the background
		balanceLabelBackground =
			   new SimpleActor(horizPadding / 2,verticalPadding / 2, 250, 42, "BalanceLabelBg", PixelAssetManager.blueRectangle);
		addActor(balanceLabelBackground);

		balanceLabel = new Label("$" + FinancialManager.getInstance().getBalance(), Styles.balanceLabelStyle);
		balanceLabel.setPosition(horizPadding,verticalPadding);
		addActor(balanceLabel);
	}

	private void initRightSideIcons() {

		Vector2 buttonSize = new Vector2(50, 50);
		IconList rightIconList = new IconList(width - buttonSize.x - 10, height, 3, 80);
		rightIconList.setVisible(true);
		addActor(rightIconList);

		Icon menuIcon = new Icon(PixelAssetManager.pauseIcon, PixelAssetManager.blueBox, buttonSize, rightIconList);
		icons.add(menuIcon);
		rightIconList.addIcon(menuIcon);
		menuIcon.addListener((Event e) -> {
				   if (!(e instanceof InputEvent) || !((InputEvent) e).getType().equals(InputEvent.Type.touchDown)) {
					   return false;
				   }
				   openInGameDialog();
				   return true;
			   }
		);

		Icon statsIcon = new Icon(PixelAssetManager.statIcon, PixelAssetManager.blueBox, buttonSize, rightIconList);
		icons.add(statsIcon);
		rightIconList.addIcon(statsIcon);
		statsIcon.addListener((Event e) -> {
				   if (!(e instanceof InputEvent) || !((InputEvent) e).getType().equals(InputEvent.Type.touchDown)) {
					   return false;
				   }
				   Dialog dialog =
						 new StatsDialog();
				   dialog.show(this);
				   return true;
			   }
		);

		Icon demandIcon = new Icon(PixelAssetManager.demandIcon, PixelAssetManager.blueBox, buttonSize, rightIconList);
		icons.add(demandIcon);
		rightIconList.addIcon(demandIcon);
		demandIcon.addListener((Event e) -> {
				   if (!(e instanceof InputEvent) || !((InputEvent) e).getType().equals(InputEvent.Type.touchDown)) {
					   return false;
				   }
				   Dialog dialog =
						 new DemandDialog();
				   dialog.show(this);
				   return true;
			   }
		);
	}

	private void initLabels() {

		initBalanceLabel();
		initNameLabel();
		initPopLabel();
	}

	private void initNameLabel() {
		cityNameLabel = new Label(City.getInstance().getName(), Styles.balanceLabelStyle);
		cityNameBackground = new SimpleActor(0, 0, 0, 0,
			   "CityNameBg", PixelAssetManager.blueRectangle);
		addActor(cityNameBackground);
		addActor(cityNameLabel);
		resetNameLabel();
	}

	private void resetNameLabel() {
		cityNameLabel.setText(City.getInstance().getName());
		float width = cityNameLabel.getText().length * cityNameLabel.getStyle().font.getXHeight();

		// center this label on the top side
		cityNameLabel.setPosition(PixelCityGame.width / 2 - width / 2,
			   PixelCityGame.height - cityNameLabel.getHeight());

		cityNameBackground.setPosition(cityNameLabel.getX() - horizPadding, cityNameLabel.getY());
		cityNameBackground.setWidth(width + horizPadding * 2);
		cityNameBackground.setHeight(cityNameLabel.getHeight());
	}

	private void initPopLabel() {
		populationLabel = new Label(City.getInstance().getPopulation() + "", Styles.balanceLabelStyle);

		populationBackground = new SimpleActor(0,0,0, 0, "PopLabelBg", PixelAssetManager.blueRectangle);

		populationPersonIcon = new SimpleActor(0,0, 32,
			   32, "PopLabelPersonIcon", PixelAssetManager.personIcon);

		addActor(populationBackground);
		addActor(populationPersonIcon);
		addActor(populationLabel);

		resetPopLabel();
	}

	private void resetPopLabel() {
		populationLabel.setText(City.getInstance().getPopulation() + "");

		float width = populationLabel.getText().length * populationLabel.getStyle().font.getXHeight();

		populationLabel.setPosition(PixelCityGame.width - width - horizPadding * 2, verticalPadding);

		populationBackground.setPosition(populationLabel.getX() - horizPadding - 30, verticalPadding / 2);
		populationBackground.setWidth(width + horizPadding * 2 + 30);
		populationBackground.setHeight(42);

		populationPersonIcon.setPosition(populationBackground.getX(), populationBackground.getY() + verticalPadding);
	}

	private void clearIconLists(Icon toggledIcon) {
		for (Icon icon : icons) {
			if(icon != toggledIcon) {
				icon.setIconListVisible(false);
			}
		}
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		Rectangle rectangle = new Rectangle();
		screenY = (int)height - screenY;
		for (Icon icon : icons) {
			rectangle.x = icon.getRelativeX();
			rectangle.y = icon.getRelativeY();
			rectangle.width = icon.getWidth();
			rectangle.height = icon.getHeight();
			if (rectangle.contains(screenX, screenY) && icon.isVisible()) {
				icon.setColor(0.9f,0.9f,0.9f,1);
			} else {
				icon.setColor(1,1,1,1);
			}
		}
		return super.mouseMoved(screenX, screenY);
	}

	@Override
	public void act(float dt) {
		super.act(dt);

		String string = "$" + FinancialManager.getInstance().getBalance();
		int count = string.length();

		balanceLabel.setText(string);

		// then we set the size of the label background based off this count
		float width = count * balanceLabel.getStyle().font.getXHeight();
		balanceLabelBackground.setWidth(width);

		resetPopLabel();
	}

	public void openInGameDialog() {
		Dialog dialog =
			   new PauseDialog();
		dialog.show(this);
	}
}
