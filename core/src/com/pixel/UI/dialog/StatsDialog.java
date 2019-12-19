package com.pixel.UI.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.pixel.UI.GameSceneUI;
import com.pixel.city.City;
import com.pixel.game.PixelAssetManager;

public class StatsDialog extends PDialog {

	private float timer = 0;
	private float time = 5.0f;
	private Label wealthLabel;
	private Label educationLabel;
	private Label happinessLabel;
	private Label totalLabel;

	public StatsDialog() {
		super("City Stats", PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));

		wealthLabel =
			   new Label("", (Skin)PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		educationLabel =
			   new Label("", (Skin)PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		happinessLabel =
			   new Label("", (Skin)PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		totalLabel =
			   new Label("", (Skin)PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		setLabels();

		getContentTable().pad(15,15,15,15);
		getContentTable().add(wealthLabel);
		getContentTable().row();
		getContentTable().add(educationLabel);
		getContentTable().row();
		getContentTable().add(happinessLabel);
		getContentTable().row();
		getContentTable().add(totalLabel);
		getContentTable().row();
	}

	private void setLabels() {
		City.Stats stats = City.getInstance().calculateCityStats();

		wealthLabel.setText("Wealth: " + (int)stats.wealth);
		educationLabel.setText("Education: " + (int)stats.education);
		happinessLabel.setText("Happiness: " + (int)stats.happiness);
		totalLabel.setText("Total Score: " + (int)stats.total);
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		timer += delta;
		if (timer >= time) {
			timer = 0;
			setLabels();
		}
	}

	@Override
	public void close() {
		super.close();
		GameSceneUI.getInstance().clearStatsDialog();
	}
}
