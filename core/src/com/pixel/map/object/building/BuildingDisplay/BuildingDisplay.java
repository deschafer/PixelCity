package com.pixel.map.object.building.BuildingDisplay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;

public class BuildingDisplay extends Actor {

	// animation-based properties
	private TextureRegion textureRegion;
	private String texture;
	boolean base = false;

	public BuildingDisplay(String texture) {

		this.texture = texture;
		setSize(Map.cellExtraWidthPixels, Map.cellExtraHeightPixels);

		setName("NotBase");

		loadTexture(texture);
	}

	public BuildingDisplay(String texture, boolean base) {

		this.base = true;
		this.texture = texture;
		setSize(Map.cellWidthPixels, 127);

		setName("Base");

		loadTexture(texture);
	}

	public void loadTexture(String texture) {
		textureRegion = new TextureRegion((Texture) PixelAssetManager.manager.get(texture));
	}

	public void draw(Batch batch, float parentAlpha) {

		if (getStage() == null) return;

		if (textureRegion != null && isVisible())
			batch.draw(textureRegion,
				   getX(), getY(), getOriginX(), getOriginY(),
				   getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

		super.draw(batch, parentAlpha);
	}

	public BuildingDisplay copy() {
		if (base) return new BuildingDisplay(texture, base);
		return new BuildingDisplay(texture);
	}
}
