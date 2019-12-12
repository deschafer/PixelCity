package com.pixel.UI.dialog;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.pixel.game.styles.Styles;

public class PDialog extends Dialog {

	protected ImageButton closeButton;
	protected boolean closeOnEscape = true;

	public PDialog(String title, Skin skin) {
		super(title, skin);

		setModal(false);
		setMovable(true);
		setResizable(false);

		//skin.add("default-font", newDefaultFont, BitmapFont.class);

		// set up a close button in the top right position to allow closing this dialog
		closeButton = new ImageButton(Styles.closeButtonImageButtonStyle);
		getTitleTable().add().expandX();
		getTitleTable().add(closeButton);

		closeButton.addListener((Event e) -> {
				   if (!(e instanceof InputEvent) || !((InputEvent) e).getType().equals(InputEvent.Type.touchDown)) {
					   return false;
				   }

				   addAction(Actions.fadeOut(0.30f));
				   addAction(Actions.after(Actions.removeActor()));
				   return true;
			   }
		);
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		if (closeOnEscape) {
			if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
				addAction(Actions.fadeOut(0.30f));
				addAction(Actions.after(Actions.removeActor()));
			}
		}
	}
}
