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

public class UtilityTutorialDialog extends PDialog {
	private PTextButton nextButton;
	private Label description;
	private SimpleActor image;

	public UtilityTutorialDialog() {
		super("Tutorial: City Utilities", PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));

		nextButton = new PTextButton("Next Page", (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		nextButton.addListener(new ClickListener() {
							   @Override
							   public void clicked(InputEvent e, float x, float y) {
								   if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {
									   // open a new dialog
									    PDialog dialog = new LevelUpDialog();
									    dialog.show(getStage());
									   // close this dialog
									   close();
								   }
							   }
						   }
		);
		description = new Label("", (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		image = new SimpleActor(0,0,131,285, "Image", PixelAssetManager.utilityImage);

		String descriptionText = "Similar to services, residents must also be provided with power and water utilities.\n" +
			   "Unlike services, each of these contributes to a total pool of available resources.\n" +
			   "To Check if enough power or water is being supplied, selecting one of the tool icons\n " +
			   "shown left will show all supplied buildings in green.";
		description.setText(descriptionText);
		description.setAlignment(Align.left);

		getContentTable().add(image).align(Align.left).pad(5,5,2,0);
		getContentTable().add(description).align(Align.left).pad(2,3,2,2);
		getButtonTable().add(nextButton).center().pad(5, 5, 5, 5);
	}
}
