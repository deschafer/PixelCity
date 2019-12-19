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

public class DemandTutorialDialog extends PDialog {

	private PTextButton nextButton;
	private Label description;
	private SimpleActor image;

	public DemandTutorialDialog() {
		super("Tutorial: Demand", PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));

		nextButton = new PTextButton("Next Page", (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		nextButton.addListener(new ClickListener() {
							   @Override
							   public void clicked(InputEvent e, float x, float y) {
								   if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {
									   // open a new dialog
									   PDialog dialog = new SpecBuildingDialog();
									   dialog.show(getStage());
									   // close this dialog
									   close();
								   }
							   }
						   }
		);
		description = new Label("", (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		image = new SimpleActor(0,0,185,252, "Image", PixelAssetManager.demandImage);

		String descriptionText = "In Pixel City, all buildings are \n " +
			   "automatically created in zoned sections. There are a total \n" +
			   "of three zone types: Residential (Green), Commercial (Blue), and Office (Amber)\n " +
			   "Zones can only occupy within 4 cells of the nearest road.\n" +
			   "Once zones have been created, and given there is demand \n" +
			   "(discussed in the next page), buildings will automatically develop. \n" +
			   "To zone a section, use the Zone tools shown left. R for residential,\n" +
			   " O for office, and C for commercial";
		description.setText(descriptionText);
		description.setAlignment(Align.left);

		getContentTable().add(image).align(Align.left).pad(5,5,2,0);
		getContentTable().add(description).align(Align.left).pad(2,3,2,2);
		getButtonTable().add(nextButton).center().pad(5, 5, 5, 5);
	}
}
