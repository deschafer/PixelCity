package com.pixel.UI.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.pixel.game.PixelAssetManager;

public class StatsDialog extends PDialog {

	public StatsDialog() {
		super("City Stats", PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));


		Label wealthLabel =
			   new Label("Wealth: ", (Skin)PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		Label educationLabel =
			   new Label("Education: ", (Skin)PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		Label happinessLabel =
			   new Label("Happiness: ", (Skin)PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));

		getContentTable().pad(15,15,15,15);

		getContentTable().add(wealthLabel);
		getContentTable().row();
		getContentTable().add(educationLabel);
		getContentTable().row();
		getContentTable().add(happinessLabel);
		getContentTable().row();
	}
}
