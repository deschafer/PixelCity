package com.pixel.UI.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.pixel.city.FinancialManager;
import com.pixel.game.PixelAssetManager;
import com.pixel.game.styles.Styles;

public class BalanceDialog extends PDialog {

	private Table scrollPaneTable;
	private ScrollPane scrollPane;
	private Label incomeLabel;
	private Label expensesLabel;

	public BalanceDialog() {
		super("City Finances", PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));

		scrollPaneTable = new Table();
		scrollPane =
			   new ScrollPane(scrollPaneTable, (Skin)PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		getContentTable().add(scrollPane).expandX();
		getContentTable().row();

		incomeLabel = new Label("", Styles.greenBalanceLabelStyle);
		getContentTable().add(incomeLabel);

		expensesLabel = new Label("", Styles.redBalanceLabelStyle);
		getContentTable().add(expensesLabel);
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		// set the appropriate information in the labels
		// get each available source from the financial manager
		// update this periodically as to not be too cpu intensive
		//	so add a timer
		// then clear our list of sources, and get a new list from the manager

		// updating income/expenses is much easier, so do not worry about a timer
		incomeLabel.setText("" + (int)FinancialManager.getInstance().getIncome());
		expensesLabel.setText("" + (int)FinancialManager.getInstance().getExpenses());
	}
}
