package com.pixel.UI.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.pixel.UI.element.PTextButton;
import com.pixel.city.City;
import com.pixel.game.PixelAssetManager;
import com.pixel.game.PixelCityGame;
import com.pixel.scene.GameScene;
import com.pixel.scene.MenuScene;
import com.pixel.serialization.SerializationManager;

public class PauseDialog extends PDialog {

	public PauseDialog() {
		super("In-Game Menu", PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));

		setModal(true);
		closeOnEscape = false;

		PTextButton saveButton =
			   new PTextButton("Save", (Skin)PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		saveButton.addListener(new ClickListener() {
								@Override
								public void clicked(InputEvent e, float x, float y) {
									if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {
										SerializationManager.getInstance().serialize(City.getInstance().getName());
									}
								}
							}
		);

		PTextButton mainMenuButton =
			   new PTextButton("Go to Main Menu", (Skin)PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		mainMenuButton.addListener(new ClickListener() {
							   @Override
							   public void clicked(InputEvent e, float x, float y) {
								   if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {
									   PixelCityGame.setActiveScreen(MenuScene.getInstance());
									   remove();
								   }
							   }
						   }
		);

		PTextButton quitButton =
			   new PTextButton("Quit to Desktop", (Skin)PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		quitButton.addListener(new ClickListener() {
								  @Override
								  public void clicked(InputEvent e, float x, float y) {
									  if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {
										  Gdx.app.exit();
									  }
								  }
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
