package com.pixel.UI.element;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.pixel.game.PixelAssetManager;
import com.pixel.object.SimpleActor;

public class Icon extends Group {

	private Button iconButton;
	private SimpleActor foregroundImage;
	private IconList additionalList;
	private IconList parentList;
	private boolean fgHover = false;
	private boolean bgHover = false;

	public Icon(String fgTextureName, String bgTextureName, Vector2 buttonSize, IconList parentList) {//, Vector2 fgImageSize) {

		Icon reference = this;

		setX(0);
		setY(0);
		this.parentList = parentList;
		setName("IconUI");

		// create our button from the style provided
		Texture bgTexture = PixelAssetManager.manager.get(bgTextureName);
		Button.ButtonStyle style = new Button.ButtonStyle();
		if (bgTexture == null) {
			System.out.println("Background texture " + bgTextureName + " was not found.");
			return;
		}
		// add the texture to the button
		NinePatch buttonPatch = new NinePatch(bgTexture, 0, 0, 0, 0);
		style.up = new NinePatchDrawable(buttonPatch);

		// create the button and set its size
		iconButton = new Button(style);
		iconButton.setSize(buttonSize.x, buttonSize.y);

		// add the button to this object
		addActor(iconButton);

		// load in the foreground texture
		// the fg image should be smaller than the background, we it is set to 60% of the background's size
		foregroundImage = new SimpleActor(0, 0, buttonSize.x * 0.60f, buttonSize.y * 0.60f,
			   "IconFgImage", fgTextureName);
		foregroundImage.setSize(buttonSize.x * 0.60f, buttonSize.y * 0.60f);
		addActor(foregroundImage);

		// then finally, the foreground image must be centered on the background image
		foregroundImage.setX(iconButton.getWidth() / 2 - foregroundImage.getWidth() / 2);
		foregroundImage.setY(iconButton.getHeight() / 2 - foregroundImage.getHeight() / 2);

		setSize(buttonSize.x, buttonSize.y);
	}

	public boolean addListener (EventListener listener) {
		return iconButton.addListener(listener) && foregroundImage.addListener(listener);
	}

	public void setIconListVisible(boolean visible) {
		if (additionalList != null)
			additionalList.setVisible(visible);
	}
	public boolean isIconListVisible() {
		if (additionalList != null) {
			return additionalList.isVisible();
		}
		return false;
	}
	public void addIconList(IconList iconList) {
		additionalList = iconList;
		addActor(iconList);

		iconList.setX(iconButton.getWidth() + iconList.getPadding());
	}

	public void showAdditionalList() {
		if (additionalList != null) {
			additionalList.setVisible(true);
		}
	}

	public void hideAdditionalList() {
		if (additionalList != null) {
			additionalList.setVisible(false);
		}
	}

	public IconList getParentList() {
		return parentList;
	}

	@Override
	public void setColor(float r, float g, float b, float a) {
		super.setColor(r, g, b, a);
		foregroundImage.setColor(r, g, b, a);
		iconButton.setColor(r, g, b, a);
	}

	public float getRelativeX() {

		Vector2 coordinates = new Vector2(getX(), 0);

		return localToStageCoordinates(coordinates).x;
	}

	public float getRelativeY() {

		Vector2 coordinates = new Vector2(getX(), 0);

		return localToStageCoordinates(coordinates).y;
	}
}
