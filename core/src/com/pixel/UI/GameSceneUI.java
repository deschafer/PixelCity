package com.pixel.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.pixel.UI.dialog.*;
import com.pixel.UI.element.Icon;
import com.pixel.UI.element.IconList;
import com.pixel.city.City;
import com.pixel.city.FinancialManager;
import com.pixel.event.EventManager;
import com.pixel.game.PixelAssetManager;
import com.pixel.game.PixelCityGame;
import com.pixel.game.styles.Styles;
import com.pixel.map.object.building.Building;
import com.pixel.object.AnimatedActor;
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
	private boolean buildingsVisible = true;

	private Icon statsIcon;
	private Icon demandIcon;
	private Icon notificationIcon;

	private PDialog statsDialog;
	private PDialog demandDialog;
	private PDialog pauseDialog;
	private PDialog balanceDialog;
	private PDialog notificationDialog;

	private static final int selectedIconWidth = 48;
	private static final int selectedIconHeight = 48;

	private static SimpleActor selectedToolFire =
		   new SimpleActor(0, 0, selectedIconWidth, selectedIconHeight, "UISelectedFire", PixelAssetManager.fireIcon);
	private static SimpleActor selectedToolPolice =
		   new SimpleActor(0, 0, selectedIconWidth, selectedIconHeight, "UISelectedPolice", PixelAssetManager.policeIcon);
	private static SimpleActor selectedToolEd =
		   new SimpleActor(0, 0, selectedIconWidth, selectedIconHeight, "UISelectedEd", PixelAssetManager.educationIcon);
	private static SimpleActor selectedToolHealth =
		   new SimpleActor(0, 0, selectedIconWidth, selectedIconHeight, "UISelectedHealth", PixelAssetManager.healthIcon);
	private static SimpleActor selectedToolWater =
		   new SimpleActor(0, 0, selectedIconWidth, selectedIconHeight, "UISelectedWater", PixelAssetManager.waterUtilityIcon);
	private static SimpleActor selectedToolPower =
		   new SimpleActor(0, 0, selectedIconWidth, selectedIconHeight, "UISelectedPower", PixelAssetManager.powerPlantIcon);
	private static SimpleActor selectedToolRoad =
		   new SimpleActor(0, 0, selectedIconWidth, selectedIconHeight, "UISelectedRoad", PixelAssetManager.roadIcon);
	private static SimpleActor selectedToolDelete =
		   new SimpleActor(0, 0, selectedIconWidth, selectedIconHeight, "UISelectedDelete", PixelAssetManager.deleteIcon);
	private static SimpleActor selectedToolResZone =
		   new SimpleActor(0, 0, selectedIconWidth, selectedIconHeight, "UISelectedRZone", PixelAssetManager.greenZoningIcon);
	private static SimpleActor selectedToolCommZone =
		   new SimpleActor(0, 0, selectedIconWidth, selectedIconHeight, "UISelectedCZone", PixelAssetManager.blueZoningIcon);
	private static SimpleActor selectedToolOffZone =
		   new SimpleActor(0, 0, selectedIconWidth, selectedIconHeight, "UISelectedOZone", PixelAssetManager.amberZoningIcon);
	private static SimpleActor selectedToolBackground =
		   new SimpleActor(0, 0, selectedIconWidth + 10, selectedIconHeight + 10, "UISelectedBg", PixelAssetManager.blueBox);

	private float horizPadding = 10;
	private float verticalPadding = 10;

	private static GameSceneUI instance = new GameSceneUI();

	// contain all UI element including Icons and Labels

	private GameSceneUI() {
		super();

		float width = PixelCityGame.width;
		float height = PixelCityGame.height;

		InputMultiplexer im = (InputMultiplexer) Gdx.input.getInputProcessor();
		im.addProcessor(this);

		this.width = width;
		this.height = height;


	}

	public static GameSceneUI getInstance() {
		return instance;
	}


	public void reset() {

		if (cityNameLabel == null) {
			initLeftSideIcons();
			initRightSideIcons();
			initLabels();
		}

		resetNameLabel();
		resetPopLabel();
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
		roadIcon.addListener(new ClickListener() {
								 @Override
								 public void clicked(InputEvent e, float x, float y) {
									 if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {

										 // set the new tool
										 GameScene.getInstance().setActiveTool(GameScene.Tools.ROAD);
										 selectedToolChanged(GameScene.Tools.ROAD);
									 }
								 }
							 }
		);

		// Then we add icons to this list
		// We add the zoning icon
		Icon zoningIcon = new Icon(PixelAssetManager.zoningIcon, PixelAssetManager.blueBox, buttonSize, leftIconList);
		icons.add(zoningIcon);

		// also need to add a listener to this icon
		// this listener will open the next list within
		zoningIcon.addListener(new ClickListener() {
							 @Override
							 public void clicked(InputEvent e, float x, float y) {
								 if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {

									 clearIconLists(zoningIcon);
									 zoningIcon.setIconListVisible(!zoningIcon.isIconListVisible());
								 }
							 }
						 }
		);

		// creating the zoning icon sub list
		IconList zoningIconList =
			   new IconList(zoningIcon.getX() * 2 + zoningIcon.getWidth(), zoningIcon.getY(), 3, -zoningIcon.getHeight());

		// creating a residential zoning icon that selects the res zoning tool
		Icon resIcon = new Icon(PixelAssetManager.greenZoningIcon, PixelAssetManager.blueBox, buttonSize, zoningIconList);
		icons.add(resIcon);
		zoningIconList.addIcon(resIcon);
		resIcon.addListener(new ClickListener() {
							   @Override
							   public void clicked(InputEvent e, float x, float y) {
								   if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {
								   	   // set the new tool
									   GameScene.getInstance().setActiveTool(GameScene.Tools.RES_ZONING);
									   selectedToolChanged(GameScene.Tools.RES_ZONING);
								   }
							   }
						   }
		);
		// creating a commercial zoning icon
		Icon commIcon = new Icon(PixelAssetManager.blueZoningIcon, PixelAssetManager.blueBox, buttonSize, zoningIconList);
		icons.add(commIcon);
		zoningIconList.addIcon(commIcon);
		commIcon.addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent e, float x, float y) {
								if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {
									// set the new tool
									GameScene.getInstance().setActiveTool(GameScene.Tools.COMM_ZONING);
									selectedToolChanged(GameScene.Tools.COMM_ZONING);
								}
							}
						}
		);
		// creating an office zoning tool
		Icon offIcon = new Icon(PixelAssetManager.amberZoningIcon, PixelAssetManager.blueBox, buttonSize, zoningIconList);
		icons.add(offIcon);
		zoningIconList.addIcon(offIcon);
		offIcon.addListener(new ClickListener() {
							 @Override
							 public void clicked(InputEvent e, float x, float y) {
								 if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {
									 // set the new tool
									 GameScene.getInstance().setActiveTool(GameScene.Tools.OFF_ZONING);
									 selectedToolChanged(GameScene.Tools.OFF_ZONING);
								 }
							 }
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
		serviceIcon.addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent e, float x, float y) {
								if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {
									clearIconLists(serviceIcon);
									serviceIcon.setIconListVisible(!serviceIcon.isIconListVisible());
									System.out.println("Service Button");
								}
							}
						}
		);

		// add the additional list for this icon
		IconList serviceList =
			   new IconList(serviceIcon.getX() * 2 + serviceIcon.getWidth(), serviceIcon.getY(), 3, -serviceIcon.getHeight());
		// creating an icon for fire services
		Icon fireIcon = new Icon(PixelAssetManager.fireIcon, PixelAssetManager.blueBox, buttonSize, serviceList);
		icons.add(fireIcon);
		serviceList.addIcon(fireIcon);
		fireIcon.addListener(new ClickListener() {
							    @Override
							    public void clicked(InputEvent e, float x, float y) {
								    if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {
									    // set the new tool
									    GameScene.getInstance().setActiveTool(GameScene.Tools.FIRE);
									    selectedToolChanged(GameScene.Tools.FIRE);
								    }
							    }
						    }
		);
		// creating an icon for police services
		Icon policeIcon = new Icon(PixelAssetManager.policeIcon, PixelAssetManager.blueBox, buttonSize, serviceList);
		icons.add(policeIcon);
		serviceList.addIcon(policeIcon);
		policeIcon.addListener((Event e) -> {
				   if (!(e instanceof InputEvent) || !((InputEvent) e).getType().equals(InputEvent.Type.touchUp)) {
					   return false;
				   }
				   // set the new tool
				   GameScene.getInstance().setActiveTool(GameScene.Tools.POLICE);
				   selectedToolChanged(GameScene.Tools.POLICE);
				   return true;
			   }
		);
		policeIcon.addListener(new ClickListener() {
							 @Override
							 public void clicked(InputEvent e, float x, float y) {
								 if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {
									 // set the new tool
									 GameScene.getInstance().setActiveTool(GameScene.Tools.POLICE);
									 selectedToolChanged(GameScene.Tools.POLICE);
								 }
							 }
						 }
		);
		// creating an icon for education services
		Icon educationIcon = new Icon(PixelAssetManager.educationIcon, PixelAssetManager.blueBox, buttonSize, serviceList);
		icons.add(educationIcon);
		serviceList.addIcon(educationIcon);
		educationIcon.addListener(new ClickListener() {
							   @Override
							   public void clicked(InputEvent e, float x, float y) {
								   if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {
									   // set the new tool
									   GameScene.getInstance().setActiveTool(GameScene.Tools.ED);
									   selectedToolChanged(GameScene.Tools.ED);
								   }
							   }
						   }
		);
		// creating an icon for health services
		Icon healthIcon = new Icon(PixelAssetManager.healthIcon, PixelAssetManager.blueBox, buttonSize, serviceList);
		icons.add(healthIcon);
		serviceList.addIcon(healthIcon);
		healthIcon.addListener(new ClickListener() {
								 @Override
								 public void clicked(InputEvent e, float x, float y) {
									 if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {
										 // set the new tool
										 GameScene.getInstance().setActiveTool(GameScene.Tools.HEALTH);
										 selectedToolChanged(GameScene.Tools.HEALTH);
									 }
								 }
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
		utilityIcon.addListener(new ClickListener() {
							   @Override
							   public void clicked(InputEvent e, float x, float y) {
								   if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {
									   clearIconLists(utilityIcon);
									   utilityIcon.setIconListVisible(!utilityIcon.isIconListVisible());
								   }
							   }
						   }
		);

		// add the additional list for this icon
		IconList utilityList =
			   new IconList(utilityIcon.getX() * 2 + utilityIcon.getWidth(), utilityIcon.getY(), 3, -utilityIcon.getHeight());
		// creating an icon for water utilities
		Icon waterIcon = new Icon(PixelAssetManager.waterUtilityIcon, PixelAssetManager.blueBox, buttonSize, utilityList);
		icons.add(waterIcon);
		utilityList.addIcon(waterIcon);
		waterIcon.addListener(new ClickListener() {
							    @Override
							    public void clicked(InputEvent e, float x, float y) {
								    if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {
									    // set the new tool
									    GameScene.getInstance().setActiveTool(GameScene.Tools.WATER);
									    selectedToolChanged(GameScene.Tools.WATER);
								    }
							    }
						    }
		);

		// creating an icon for power utilities
		Icon powerIcon = new Icon(PixelAssetManager.powerPlantIcon, PixelAssetManager.blueBox, buttonSize, utilityList);
		icons.add(powerIcon);
		utilityList.addIcon(powerIcon);
		powerIcon.addListener(new ClickListener() {
							  @Override
							  public void clicked(InputEvent e, float x, float y) {
								  if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {
									  // set the new tool
									  GameScene.getInstance().setActiveTool(GameScene.Tools.POWER);
									  selectedToolChanged(GameScene.Tools.POWER);
								  }
							  }
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
		deleteIcon.addListener(new ClickListener() {
							  @Override
							  public void clicked(InputEvent e, float x, float y) {
								  if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {
									  // set the new tool
									  GameScene.getInstance().setActiveTool(GameScene.Tools.DELETE);
									  selectedToolChanged(GameScene.Tools.DELETE);
								  }
							  }
						  }
		);

		// add the icon to the left list
		leftIconList.addIcon(deleteIcon);

		// add all of our selected tool images to the same location, set them all as invisible for now
		float positionX = horizPadding;
		float positionY = 200;

		addActor(selectedToolBackground);
		selectedToolBackground.setPosition(positionX - 5, positionY - 5);
		addActor(selectedToolFire);
		selectedToolFire.setPosition(positionX, positionY);
		addActor(selectedToolPolice);
		selectedToolPolice.setPosition(positionX, positionY);
		addActor(selectedToolEd);
		selectedToolEd.setPosition(positionX, positionY);
		addActor(selectedToolHealth);
		selectedToolHealth.setPosition(positionX, positionY);
		addActor(selectedToolWater);
		selectedToolWater.setPosition(positionX, positionY);
		addActor(selectedToolPower);
		selectedToolPower.setPosition(positionX, positionY);
		addActor(selectedToolRoad);
		selectedToolRoad.setPosition(positionX, positionY);
		addActor(selectedToolDelete);
		selectedToolDelete.setPosition(positionX, positionY);
		addActor(selectedToolResZone);
		selectedToolResZone.setPosition(positionX, positionY);
		addActor(selectedToolCommZone);
		selectedToolCommZone.setPosition(positionX, positionY);
		addActor(selectedToolOffZone);
		selectedToolOffZone.setPosition(positionX, positionY);

		// set all the not visible
		clearSelectedTool();
	}

	private void initBalanceLabel() {

		// set up the background
		balanceLabelBackground =
			   new SimpleActor(horizPadding / 2, verticalPadding / 2, 250, 42, "BalanceLabelBg", PixelAssetManager.blueRectangle);
		addActor(balanceLabelBackground);

		balanceLabel = new Label("$" + FinancialManager.getInstance().getBalance(), Styles.balanceLabelStyle);
		balanceLabel.setPosition(horizPadding, verticalPadding);
		balanceLabel.addListener(new ClickListener() {
							 @Override
							 public void clicked(InputEvent e, float x, float y) {
								 if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {
									 openBalanceLabel();
								 }
							 }
						 }
		);
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
		menuIcon.addListener(new ClickListener() {
							   @Override
							   public void clicked(InputEvent e, float x, float y) {
								   if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {
									   openPauseDialog();
								   }
							   }
						   }
		);

		statsIcon = new Icon(PixelAssetManager.statIcon, PixelAssetManager.blueBox, buttonSize, rightIconList);
		icons.add(statsIcon);
		rightIconList.addIcon(statsIcon);
		statsIcon.addListener(new ClickListener() {
							 @Override
							 public void clicked(InputEvent e, float x, float y) {
								 if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {
									 openStatsDialog();
								 }
							 }
						 }
		);

		demandIcon = new Icon(PixelAssetManager.demandIcon, PixelAssetManager.blueBox, buttonSize, rightIconList);
		icons.add(demandIcon);
		rightIconList.addIcon(demandIcon);
		demandIcon.addListener(new ClickListener() {
							   @Override
							   public void clicked(InputEvent e, float x, float y) {
								   if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {
									   openDemandDialog();
								   }
							   }
						   }
		);

		Icon opacityIcon = new Icon(PixelAssetManager.buildingOpacityIcon, PixelAssetManager.blueBox, buttonSize, rightIconList);
		icons.add(opacityIcon);
		rightIconList.addIcon(opacityIcon);
		opacityIcon.addListener(new ClickListener() {
							    @Override
							    public void clicked(InputEvent e, float x, float y) {
								    if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {
									    toggleBuildingOpacity();
								    }
							    }
						    }
		);

		notificationIcon = new Icon(PixelAssetManager.notificationIcon, PixelAssetManager.blueBox, buttonSize, rightIconList);
		icons.add(notificationIcon);
		rightIconList.addIcon(notificationIcon);
		notificationIcon.addListener(new ClickListener() {
							    @Override
							    public void clicked(InputEvent e, float x, float y) {
								    if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {
									    openNotificationDialog();
								    }
							    }
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
		float width = cityNameLabel.getText().length * 19;

		// center this label on the top side
		cityNameLabel.setPosition(PixelCityGame.width / 2 - width / 2,
			   PixelCityGame.height - cityNameLabel.getHeight());

		cityNameBackground.setPosition(cityNameLabel.getX() - horizPadding, cityNameLabel.getY());
		cityNameBackground.setWidth(width + horizPadding * 2);
		cityNameBackground.setHeight(cityNameLabel.getHeight());
	}

	private void initPopLabel() {
		populationLabel = new Label(City.getInstance().getPopulation() + "", Styles.balanceLabelStyle);

		populationBackground = new SimpleActor(0, 0, 0, 0, "PopLabelBg", PixelAssetManager.blueRectangle);

		populationPersonIcon = new SimpleActor(0, 0, 32,
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
			if (icon != toggledIcon) {
				icon.setIconListVisible(false);
			}
		}
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		Rectangle rectangle = new Rectangle();
		screenY = (int) height - screenY;
		for (Icon icon : icons) {
			rectangle.x = icon.getRelativeX();
			rectangle.y = icon.getRelativeY();
			rectangle.width = icon.getWidth();
			rectangle.height = icon.getHeight();
			if (rectangle.contains(screenX, screenY) && icon.isVisible()) {
				icon.setColor(0.9f, 0.9f, 0.9f, 1);
			} else {
				icon.setColor(1, 1, 1, 1);
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

	public void clearSelectedTool() {
		selectedToolFire.addAction(Actions.fadeOut(0.2f));
		selectedToolPolice.addAction(Actions.fadeOut(0.2f));
		selectedToolEd.addAction(Actions.fadeOut(0.2f));
		selectedToolHealth.addAction(Actions.fadeOut(0.2f));
		selectedToolWater.addAction(Actions.fadeOut(0.2f));
		selectedToolPower.addAction(Actions.fadeOut(0.2f));
		selectedToolRoad.addAction(Actions.fadeOut(0.2f));
		selectedToolDelete.addAction(Actions.fadeOut(0.2f));
		selectedToolResZone.addAction(Actions.fadeOut(0.2f));
		selectedToolCommZone.addAction(Actions.fadeOut(0.2f));
		selectedToolOffZone.addAction(Actions.fadeOut(0.2f));
		selectedToolBackground.addAction(Actions.fadeOut(0.2f));

		Building.clearVisualizedCategory();
	}

	public void selectedToolChanged(GameScene.Tools tools) {

		clearSelectedTool();
		selectedToolBackground.addAction(Actions.fadeIn(0.2f));

		if (tools == GameScene.Tools.RES_ZONING) {
			selectedToolResZone.addAction(Actions.fadeIn(0.2f));

		} else if (tools == GameScene.Tools.COMM_ZONING) {
			selectedToolCommZone.addAction(Actions.fadeIn(0.2f));

		} else if (tools == GameScene.Tools.OFF_ZONING) {
			selectedToolOffZone.addAction(Actions.fadeIn(0.2f));

		} else if (tools == GameScene.Tools.POLICE) {
			selectedToolPolice.addAction(Actions.fadeIn(0.2f));
			Building.setVisualizeSelectedCategory(EventManager.Categories.POLICE);

		} else if (tools == GameScene.Tools.FIRE) {
			selectedToolFire.addAction(Actions.fadeIn(0.2f));
			Building.setVisualizeSelectedCategory(EventManager.Categories.FIRE);

		} else if (tools == GameScene.Tools.ED) {
			selectedToolEd.addAction(Actions.fadeIn(0.2f));
			Building.setVisualizeSelectedCategory(EventManager.Categories.ED);

		} else if (tools == GameScene.Tools.HEALTH) {
			selectedToolHealth.addAction(Actions.fadeIn(0.2f));
			Building.setVisualizeSelectedCategory(EventManager.Categories.HEALTH);

		} else if (tools == GameScene.Tools.WATER) {
			selectedToolWater.addAction(Actions.fadeIn(0.2f));
			Building.setVisualizeSelectedCategory(EventManager.Categories.WATER);

		} else if (tools == GameScene.Tools.POWER) {
			selectedToolPower.addAction(Actions.fadeIn(0.2f));
			Building.setVisualizeSelectedCategory(EventManager.Categories.POWER);

		} else if (tools == GameScene.Tools.ROAD) {
			selectedToolRoad.addAction(Actions.fadeIn(0.2f));

		} else if (tools == GameScene.Tools.DELETE) {
			selectedToolDelete.addAction(Actions.fadeIn(0.2f));
		}
	}

	public void openStatsDialog() {

		if (statsDialog != null) {
			statsDialog.close();
			statsDialog = null;
		} else {
			statsDialog =
				   new StatsDialog();
			statsDialog.show(this);
			statsDialog.setPosition(statsIcon.getRelativeX() - statsDialog.getWidth() - horizPadding,
				   statsIcon.getRelativeY() + statsIcon.getHeight() - statsDialog.getHeight());
		}
	}

	public void openNotificationDialog() {

		if (notificationDialog != null) {
			notificationDialog.close();
			notificationDialog = null;
		} else {
			notificationDialog = new NotificationDialog();
			notificationDialog.show(this);
			notificationDialog.setPosition(notificationIcon.getRelativeX() - notificationDialog.getWidth() - horizPadding,
				   notificationIcon.getRelativeY() + notificationIcon.getHeight() - notificationDialog.getHeight());
		}
	}

	public void openDemandDialog() {

		if (demandDialog != null) {
			demandDialog.close();
			demandDialog = null;
		} else {
			demandDialog =
				   new DemandDialog();
			demandDialog.show(this);
			demandDialog.setPosition(demandIcon.getRelativeX() - demandDialog.getWidth() - horizPadding,
				   demandIcon.getRelativeY() + demandIcon.getHeight() - demandDialog.getHeight());
		}
	}

	public void openPauseDialog() {

		pauseDialog =
			   new PauseDialog();
		pauseDialog.show(this);

	}

	public void openBalanceLabel() {
		balanceDialog = new BalanceDialog();
		balanceDialog.show(this);
	}

	private void toggleBuildingOpacity() {

		City.getInstance().toggleBuildingsVisible();
	}

	public NotificationDialog getNotificationDialog() {
		return (NotificationDialog)notificationDialog;
	}

	@Override
	public void draw() {
		super.draw();
	}
}
