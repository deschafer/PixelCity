package com.pixel.UI.dialog.tutorial;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.pixel.UI.dialog.PDialog;
import com.pixel.UI.element.PTextButton;
import com.pixel.game.PixelAssetManager;
import com.pixel.object.SimpleActor;

public class LevelUpDialog extends PDialog {
	private PTextButton nextButton;
	private Label description;

	public LevelUpDialog() {
		super("Tutorial: Building Levels", PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));

		nextButton = new PTextButton("Next Page", (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		nextButton.addListener(new ClickListener() {
							   @Override
							   public void clicked(InputEvent e, float x, float y) {
								   if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {
									   // open a new dialog
									   PDialog dialog = new FinancesTutorialDialog();
									   dialog.show(getStage());
									   // close this dialog
									   close();
								   }
							   }
						   }
		);
		description = new Label("", (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));

		String descriptionText = "Once all services have been met, all residents are employed, and the building is at \n" +
			   "max capacity, a building will level up. It increases in size by one story, and can support more residents. \n" +
			   "It is important to note that for demand to continue, you MUST allow for buildings to level up. \n" +
			   "By levelling up, the building creates demand in one of the other zone types, which fuels demand.";
		description.setText(descriptionText);
		description.setAlignment(Align.left);

		getContentTable().add(description).align(Align.left).pad(2,3,2,2);
		getButtonTable().add(nextButton).center().pad(5, 5, 5, 5);
	}
}
