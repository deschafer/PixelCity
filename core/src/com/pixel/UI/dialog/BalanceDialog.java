package com.pixel.UI.dialog;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.pixel.UI.GameSceneUI;
import com.pixel.city.City;
import com.pixel.city.FinancialManager;
import com.pixel.city.Financials.Source;
import com.pixel.game.PixelAssetManager;
import com.pixel.game.styles.Styles;

import java.text.DecimalFormat;

public class BalanceDialog extends PDialog {

	private Label incomeTitleLabel;
	private Label expenseTitleLabel;
	private Table scrollPaneIncomeTable;
	private ScrollPane scrollPaneIncome;
	private Table scrollPaneExpenseTable;
	private ScrollPane scrollPaneExpense;
	private Label incomeLabel;
	private Label expensesLabel;

	private float updateSourceTimer = 0;
	private float updateSourceTime = 5.0f;
	private static DecimalFormat df = new DecimalFormat("0.00");

	public BalanceDialog() {
		super("City Finances", PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));

		incomeTitleLabel =
			   new Label("Sources of Income", (Skin)PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		getContentTable().add(incomeTitleLabel).padTop(10).align(Align.left);
		expenseTitleLabel =
			   new Label("Sources of Expenses", (Skin)PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		getContentTable().add(expenseTitleLabel).padTop(10).align(Align.left);
		getContentTable().row();

		scrollPaneIncomeTable = new Table();
		scrollPaneIncome = new ScrollPane(scrollPaneIncomeTable, (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		getContentTable().add(scrollPaneIncome).size(250, 300).pad(0,2,5,2).center();
		scrollPaneExpenseTable = new Table();
		scrollPaneExpense = new ScrollPane(scrollPaneExpenseTable, (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		getContentTable().add(scrollPaneExpense).size(250, 300).pad(0,2,5,2).center();
		getContentTable().row();

		resetScrollPane();

		incomeLabel = new Label("", Styles.greenBalanceLabelStyle);
		getContentTable().add(incomeLabel);

		expensesLabel = new Label("", Styles.redBalanceLabelStyle);
		getContentTable().add(expensesLabel);
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		updateSourceTimer += delta;

		if (updateSourceTimer >= updateSourceTime) {
			updateSourceTimer = 0;
			resetScrollPane();
		}

		// set the appropriate information in the labels
		// get each available source from the financial manager
		// update this periodically as to not be too cpu intensive
		//	so add a timer
		// then clear our list of sources, and get a new list from the manager

		// updating income/expenses is much easier, so do not worry about a timer
		incomeLabel.setText("Total Income:  $" + df.format((int)FinancialManager.getInstance().getIncome()));
		expensesLabel.setText("Total Expenses:  $"+  df.format(-(int)FinancialManager.getInstance().getExpenses()));
	}

	private void resetScrollPane() {
		// first clear our table
		scrollPaneIncomeTable.clear();
		scrollPaneExpenseTable.clear();

		// add a spacer before the list
		scrollPaneIncomeTable.add().size(10, 5);
		scrollPaneIncomeTable.row();
		scrollPaneExpenseTable.add().size(10, 5);
		scrollPaneExpenseTable.row();

		// then create new labels and store them in this table for each of the sources
		for (Source source : FinancialManager.getInstance().getSources()) {

			float change = source.act();

			if (source.isValid() && source.getSourceObject() != null) {

				String sourceSource = source.getSourceObject().getDisplayName();
				String labelMessage = sourceSource + "   $";

				Label label = new Label("", (Skin)PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));

				//  if this is an expense
				if (change > 0) {
					labelMessage += df.format(change);
					label.setText(labelMessage);
					scrollPaneIncomeTable.add(label).size(200, 20).pad(3,2,0,0);
					scrollPaneIncomeTable.add().expandX();
					scrollPaneIncomeTable.row();
				} else {
					labelMessage += df.format(-change);
					label.setText(labelMessage);
					scrollPaneExpenseTable.add(label).size(200, 20).pad(3,2,0,0);
					scrollPaneExpenseTable.add().expandX();
					scrollPaneExpenseTable.row();
				}
			}
		}
		// add a spacer after the list
		scrollPaneIncomeTable.add().size(10, 5);
		scrollPaneIncomeTable.row();
		scrollPaneExpenseTable.add().size(10, 5);
		scrollPaneExpenseTable.row();
	}

	@Override
	public void close() {
		super.close();
		GameSceneUI.getInstance().clearBalanceDialog();
	}
}
