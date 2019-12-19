package com.pixel.UI.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.pixel.UI.GameSceneUI;
import com.pixel.city.Demand;
import com.pixel.game.PixelAssetManager;

public class DemandDialog extends PDialog {

	private Label resDemandLabel;
	private Label commDemandLabel;
	private Label offDemandLabel;

	public DemandDialog() {
		super("City Demand", PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));

		resDemandLabel =
			   new Label("Residential: " + Demand.getInstance().getResidentialDemand(), (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		commDemandLabel =
			   new Label("Commercial: " + Demand.getInstance().getCommercialDemand(), (Skin)PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		offDemandLabel =
			   new Label("Office: " + Demand.getInstance().getOfficeDemand(), (Skin)PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));

		getContentTable().pad(15,15,15,15);

		getContentTable().add(resDemandLabel);
		getContentTable().row();
		getContentTable().add(commDemandLabel);
		getContentTable().row();
		getContentTable().add(offDemandLabel);
		getContentTable().row();
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		// need to update changing demand
		resDemandLabel.setText("Residential: " + Demand.getInstance().getResidentialDemand());
		commDemandLabel.setText("Commercial: " + Demand.getInstance().getCommercialDemand());
		offDemandLabel.setText("Office: " + Demand.getInstance().getOfficeDemand());
	}

	@Override
	public void close() {
		super.close();
		GameSceneUI.getInstance().clearDemandDialog();
	}
}
