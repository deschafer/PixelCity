package com.pixel.UI.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.pixel.game.PixelAssetManager;
import com.pixel.game.PixelCityGame;
import com.pixel.scene.GameScene;

public class NewGameDialog extends PDialog {

	TextField nameField;
	TextButton createButton;

	public NewGameDialog() {
		super("Create a New Game", PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));

		setModal(true);

		// we need to set up a text box for the name of the city
		nameField =
			   new TextField("", (Skin)PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		nameField.setWidth(100);

		// then we need to set up a create button
		createButton =
			   new TextButton("Create", (Skin)PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		createButton.addListener(new ClickListener() {
							   @Override
							   public void clicked(InputEvent e, float x, float y) {
								   if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {
									   acceptInput();
								   }
							   }
						   }
		);

		getContentTable().add(nameField).center().pad(20,5,5,5).width(250);
		getContentTable().row();
		getContentTable().add(createButton).center().pad(5,5,5,5).width(100);
		getContentTable().row();

	}

	@Override
	public Dialog show(Stage stage) {

		Dialog dialog = super.show(stage);

		stage.setKeyboardFocus(nameField);
		nameField.getOnscreenKeyboard().show(true);

		return dialog;
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
			acceptInput();
		}
	}

	private void acceptInput() {

		String input = nameField.getText();
		if (input != null && input.length() > 0) {

			GameScene.reset();

			// first, set the correct scene
			PixelCityGame.setActiveScreen(GameScene.getInstance());

			// There are not restrictions on the city name, so we create a new city
			GameScene.getInstance().setNewGame(input);
			remove();
		}
	}
}
