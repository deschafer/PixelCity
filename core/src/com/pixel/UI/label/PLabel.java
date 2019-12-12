package com.pixel.UI.label;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.StringBuilder;


public class PLabel extends Group {

	private Label highlightedLabel;
	private Label normalLabel;

	public PLabel(String text, LabelStyle normalStyle, LabelStyle highlightedStyle) {

		setX(0);
		setY(0);

		// then we create our labels
		normalLabel = new Label(text, normalStyle);
		highlightedLabel = new Label(text, highlightedStyle);

		addActor(normalLabel);
		addActor(highlightedLabel);
		highlightedLabel.setVisible(false);
	}

	public void highlight() {
		normalLabel.setVisible(false);
		highlightedLabel.setVisible(true);
	}

	public void clearHighlight() {
		normalLabel.setVisible(true);
		highlightedLabel.setVisible(false);
	}

	public void setText(String string) {
		normalLabel.setText(string);
		highlightedLabel.setText(string);
	}

	public StringBuilder getText() {
		return normalLabel.getText();
	}
}
