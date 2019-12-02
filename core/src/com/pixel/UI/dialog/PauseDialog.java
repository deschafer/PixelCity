package com.pixel.UI.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.pixel.game.PixelAssetManager;
import com.pixel.game.PixelCityGame;
import com.pixel.game.styles.Styles;
import com.pixel.scene.MenuScene;

public class PauseDialog extends PDialog {

	public PauseDialog() {
		super("In-Game Menu", PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));

		TextButton saveButton =
			   new TextButton("Save", (Skin)PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		saveButton.addListener((Event e) -> {
				   if (!(e instanceof InputEvent) || !((InputEvent) e).getType().equals(InputEvent.Type.touchDown)) {
					   return false;
				   }
				   System.out.println("Save Button");
				   return true;
			   }
		);

		TextButton mainMenuButton =
			   new TextButton("Go to Main Menu", (Skin)PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		mainMenuButton.addListener((Event e) -> {
				   if (!(e instanceof InputEvent) || !((InputEvent) e).getType().equals(InputEvent.Type.touchDown)) {
					   return false;
				   }

				   PixelCityGame.setActiveScreen(MenuScene.getInstance());
				   remove();
				   return true;
			   }
		);

		TextButton quitButton =
			   new TextButton("Quit to Desktop", (Skin)PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		quitButton.addListener((Event e) -> {
				   if (!(e instanceof InputEvent) || !((InputEvent) e).getType().equals(InputEvent.Type.touchDown)) {
					   return false;
				   }
				   Gdx.app.exit();
				   return true;
			   }
		);

		getButtonTable().pad(15,15,15,15);
		getButtonTable().add(saveButton).width(200);
		getButtonTable().row();
		getButtonTable().add(mainMenuButton).width(200);
		getButtonTable().row();
		getButtonTable().add(quitButton).width(200);
	}
}
