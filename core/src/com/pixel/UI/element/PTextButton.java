package com.pixel.UI.element;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.pixel.game.PixelAssetManager;

public class PTextButton extends TextButton {

	Sound clickSound = PixelAssetManager.manager.get(PixelAssetManager.clickOne);

	public PTextButton(String text, Skin skin) {
		super(text, skin);
	}

	{
		addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent e, float x, float y) {
				if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {
					clickSound.play();
				}
			}
		});
	}

	public PTextButton(String text, Skin skin, String styleName) {
		super(text, skin, styleName);
	}

	public PTextButton(String text, TextButtonStyle style) {
		super(text, style);
	}
}
