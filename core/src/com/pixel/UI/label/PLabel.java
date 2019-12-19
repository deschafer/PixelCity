package com.pixel.UI.label;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.StringBuilder;
import com.pixel.game.PixelAssetManager;


public class PLabel extends Group {

	private Label highlightedLabel;
	private Label normalLabel;
	private Sound clickSound = PixelAssetManager.manager.get(PixelAssetManager.clickOne);

	public PLabel(String text, LabelStyle normalStyle, LabelStyle highlightedStyle) {

		setX(0);
		setY(0);

		addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent e, float x, float y) {
				if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {
					clickSound.play();
				}
			}
		});

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
