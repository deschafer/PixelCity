package com.pixel.UI.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.pixel.game.PixelAssetManager;

public class NotificationDialog extends PDialog {

	public Label entryOneLabel;
	public Label entryTwoLabel;
	public Label entryThreeLabel;
	public Label entryFourLabel;
	public Label entryFiveLabel;

	public NotificationDialog() {
		super("Notification Log", PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));

		// create our labels
		entryOneLabel = new Label("", (Skin)PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		entryTwoLabel = new Label("", (Skin)PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		entryThreeLabel = new Label("", (Skin)PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		entryFourLabel = new Label("", (Skin)PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		entryFiveLabel = new Label("", (Skin)PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));

		// add them to the table
		getContentTable().add(entryOneLabel).size(400,20).pad(3,3,3,3);
		getContentTable().row();
		getContentTable().add(entryTwoLabel).size(400,20).pad(3,3,3,3);
		getContentTable().row();
		getContentTable().add(entryThreeLabel).size(400,20).pad(3,3,3,3);
		getContentTable().row();
		getContentTable().add(entryFourLabel).size(400,20).pad(3,3,3,3);
		getContentTable().row();
		getContentTable().add(entryFiveLabel).size(400,20).pad(3,3,3,3);
		getContentTable().row();
	}

	public void addMessage(String message) {

		// then we add the new message
		// move all the strings down, then add the string to the first label
		entryFiveLabel.setText(entryFourLabel.getText());
		entryFourLabel.setText(entryThreeLabel.getText());
		entryThreeLabel.setText(entryTwoLabel.getText());
		entryTwoLabel.setText(entryOneLabel.getText());
		entryOneLabel.setText(message);

	}
}
