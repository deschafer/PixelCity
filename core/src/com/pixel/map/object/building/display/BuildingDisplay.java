package com.pixel.map.object.building.display;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;

import java.io.Serializable;

public class BuildingDisplay extends Group {

	// animation-based properties
	private TextureRegion textureRegion;
	private String texture;
	boolean base = false;

	public BuildingDisplay(String texture) {

		this.texture = texture;
		setSize(Map.cellStoryWidth, Map.cellStoryHeight);

		setName("BuildingStory");
		loadTexture(texture);
	}

	public BuildingDisplay(String texture, boolean base) {

		this.base = true;
		this.texture = texture;
		setSize(Map.cellBuildingBaseWidth, Map.cellBuildingBaseHeight);

		setName("BuildingBase");
		loadTexture(texture);
	}

	public void loadTexture(String texture) {
		textureRegion = new TextureRegion((Texture) PixelAssetManager.manager.get(texture));
		setSize(textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
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
