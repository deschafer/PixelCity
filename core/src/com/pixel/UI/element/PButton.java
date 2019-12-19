package com.pixel.UI.element;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.pixel.game.PixelAssetManager;

public class PButton extends Button {

	Sound clickSound = PixelAssetManager.manager.get(PixelAssetManager.clickOne);

	public PButton(Skin skin) {
		super(skin);
	}

	public PButton(ButtonStyle style) {
		super(style);
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
}
