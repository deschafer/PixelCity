package com.pixel.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.pixel.UI.dialog.NewGameDialog;
import com.pixel.game.PixelCityGame;
import com.pixel.game.styles.Styles;
import java.util.ArrayList;

public class MenuScene extends Scene {

	private TextButton newGameButton;
	private TextButton loadGameButton;
	private TextButton tutorialButton;
	private TextButton quitButton;
	private Label titleLabel;
	private ArrayList<Actor> elements;

	private static MenuScene instance = new MenuScene();

	public static MenuScene getInstance() {
		return instance;
	}

	private MenuScene() {}

	@Override
	public void initialize() {

		titleLabel = new Label("Pixel City", Styles.mainMenuTitleLabelStyle);
		newGameButton = new TextButton("New Game", Styles.mainMenuTextButtonStyle);
		newGameButton.addListener((Event e) -> {
				   if (!(e instanceof InputEvent) || !((InputEvent) e).getType().equals(InputEvent.Type.touchDown)) {
					   return false;
				   }
				   // create our new game dialog
			        Dialog dialog = new NewGameDialog();
				   dialog.show(uiStage);
				   return true;
			   }
		);
		loadGameButton = new TextButton("Load Game", Styles.mainMenuTextButtonStyle);
		loadGameButton.addListener((Event e) -> {
				   if (!(e instanceof InputEvent) || !((InputEvent) e).getType().equals(InputEvent.Type.touchDown)) {
					   return false;
				   }
				   System.out.println("Load Game Button");
				   return true;
			   }
		);
		tutorialButton = new TextButton("Tutorials", Styles.mainMenuTextButtonStyle);
		tutorialButton.addListener((Event e) -> {
				   if (!(e instanceof InputEvent) || !((InputEvent) e).getType().equals(InputEvent.Type.touchDown)) {
					   return false;
				   }
				   System.out.println("Tutorial Button");
				   return true;
			   }
		);
		quitButton = new TextButton("Quit", Styles.mainMenuTextButtonStyle);
		quitButton.addListener((Event e) -> {
				   if (!(e instanceof InputEvent) || !((InputEvent) e).getType().equals(InputEvent.Type.touchDown)) {
					   return false;
				   }
				   Gdx.app.exit();
				   return true;
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
}
