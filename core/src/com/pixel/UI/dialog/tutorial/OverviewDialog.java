package com.pixel.UI.dialog.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.pixel.UI.dialog.PDialog;
import com.pixel.UI.element.PTextButton;
import com.pixel.game.PixelAssetManager;

public class OverviewDialog extends PDialog {

	private PTextButton nextButton;
	private Label description;

	public OverviewDialog() {
		super("Tutorial: Overview", PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));

		nextButton = new PTextButton("Next Page", (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		nextButton.addListener(new ClickListener() {
							   @Override
							   public void clicked(InputEvent e, float x, float y) {
								   if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {
									   // open a new dialog
									    Dialog dialog = new GettingStartedDialog();
									    dialog.show(getStage());
									   // close this dialog
									   close();
								   }
							   }
						   }
		);
		description = new Label("", (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));

		String descriptionText =
			   "Welcome to Pixel City, a simple City simulator. \nThese tutorial pages will" +
					 "give you everything you need to get started making cities.\n Press next to continue " +
					 "to the next page.";
		description.setText(descriptionText);
		description.setAlignment(Align.left);

		getContentTable().add(description).center().pad(0, 5, 5, 5);
		getButtonTable().add(nextButton).center().pad(5, 5, 5, 5);
	}
}
