package com.pixel.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.pixel.UI.dialog.LoadDialog;
import com.pixel.UI.dialog.NewGameDialog;
import com.pixel.UI.dialog.PDialog;
import com.pixel.UI.dialog.tutorial.OverviewDialog;
import com.pixel.UI.element.PTextButton;
import com.pixel.game.PixelAssetManager;
import com.pixel.game.PixelCityGame;
import com.pixel.game.styles.Styles;
import com.pixel.serialization.SerializationManager;

import java.util.ArrayList;

public class MenuScene extends Scene {

	private PTextButton newGameButton;
	private PTextButton loadGameButton;
	private PTextButton tutorialButton;
	private PTextButton quitButton;
	private Label titleLabel;
	private ArrayList<Actor> elements;
	private Music menuMusic;

	private static MenuScene instance = new MenuScene();

	public static MenuScene getInstance() {
		return instance;
	}

	private MenuScene() {}

	@Override
	public void initialize() {

		menuMusic = PixelAssetManager.manager.get(PixelAssetManager.menuMusic);

		titleLabel = new Label("Pixel City", Styles.mainMenuTitleLabelStyle);
		newGameButton = new PTextButton("New Game", Styles.mainMenuTextButtonStyle);
		newGameButton.addListener(new ClickListener() {
								 @Override
								 public void clicked(InputEvent e, float x, float y) {
									 if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {

										 // create our new game dialog
										 Dialog dialog = new NewGameDialog();
										 dialog.show(uiStage);
									 }
								 }
							 }
		);

		loadGameButton = new PTextButton("Load Game", Styles.mainMenuTextButtonStyle);
		loadGameButton.addListener(new ClickListener() {
								 @Override
								 public void clicked(InputEvent e, float x, float y) {
									 if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {

										 // create our load game dialog
										 Dialog dialog = new LoadDialog();
										 dialog.show(uiStage);
									 }
								 }
							 }
		);

		tutorialButton = new PTextButton("Tutorials", Styles.mainMenuTextButtonStyle);
		tutorialButton.addListener(new ClickListener() {
								  @Override
								  public void clicked(InputEvent e, float x, float y) {
									  if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {

										  PDialog dialog = new OverviewDialog();
										  dialog.show(uiStage);
									  }
								  }
							  }
		);

		quitButton = new PTextButton("Quit", Styles.mainMenuTextButtonStyle);
		quitButton.addListener(new ClickListener() {
								  @Override
								  public void clicked(InputEvent e, float x, float y) {
									  if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {

										  Gdx.app.exit();
									  }
								  }
							  }
		);

		elements = new ArrayList<>();

		uiTable.add(titleLabel).center().padBottom(96);
		uiTable.row();
		uiTable.add(newGameButton).center().pad(5,5,5,5);;
		uiTable.row();
		elements.add(newGameButton);
		uiTable.add(loadGameButton).center().pad(5,5,5,5);;
		uiTable.row();
		elements.add(loadGameButton);
		uiTable.add(tutorialButton).center().pad(5,5,5,5);;
		uiTable.row();
		elements.add(tutorialButton);
		uiTable.add(quitButton).center().pad(5,5,5,5);;
		uiTable.row();
		elements.add(quitButton);
	}

	@Override
	public void update(float dt) {

		Rectangle rectangle = new Rectangle();
		Vector2 coord = new Vector2();

		// handle all hovering for labels and buttons
		for (Actor actor : elements) {
			rectangle.width = actor.getWidth();
			rectangle.height = actor.getHeight();
			coord.x = 0;
			coord. y = 0;
			coord = actor.localToStageCoordinates(coord);
			rectangle.x = coord.x;
			rectangle.y = coord.y;

			float y = PixelCityGame.height - Gdx.input.getY();

			if (rectangle.contains(Gdx.input.getX(), y)) {

				actor.setColor(0.9f, 0.9f, 0.9f, 1.0f);
			} else {
				actor.setColor(1,1,1,1);
			}
		}
	}

	@Override
	public void show() {
		super.show();
		menuMusic.play();
		menuMusic.setLooping(true);
	}

	@Override
	public void hide() {
		super.hide();
		menuMusic.stop();
	}
}
