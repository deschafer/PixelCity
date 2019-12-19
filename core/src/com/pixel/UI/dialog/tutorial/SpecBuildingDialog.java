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

public class SpecBuildingDialog extends PDialog {
	private PTextButton nextButton;
	private Label description;
	private SimpleActor image;

	public SpecBuildingDialog() {
		super("Tutorial: City Services", PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));

		nextButton = new PTextButton("Next Page", (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		nextButton.addListener(new ClickListener() {
							   @Override
							   public void clicked(InputEvent e, float x, float y) {
								   if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {
									   // open a new dialog
									    PDialog dialog = new UtilityTutorialDialog();
									    dialog.show(getStage());
									   // close this dialog
									   close();
								   }
							   }
						   }
		);
		description = new Label("", (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		image = new SimpleActor(0,0,119,335, "Image", PixelAssetManager.serviceImage);

		String descriptionText = "In order for residents of buildings to be happy and content, you \n" +
			   "must provide services.\n These include Fire Protection, \n" +
			   "Police, Education, and Health services. Each of these have a limited area of \n" +
			   "influence. To see this, click on one of the service tool icons shown left, \n" +
			   "and all covered buildings will be shown in green. If you fail to provide \n" +
			   "services to all buildings, citizens may leave, or buildings may burn down or \n" +
			   "even explode. Use the bell icon to see what is happening.";
		description.setText(descriptionText);
		description.setAlignment(Align.left);

		getContentTable().add(image).align(Align.left).pad(5,5,2,0);
		getContentTable().add(description).align(Align.left).pad(2,3,2,2);
		getButtonTable().add(nextButton).center().pad(5, 5, 5, 5);
	}
}
