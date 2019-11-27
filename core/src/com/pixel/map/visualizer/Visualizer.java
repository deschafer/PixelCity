package com.pixel.map.visualizer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.object.MapObject;

public class Visualizer extends Actor {

	private TextureRegion textureRegion;
	private VisualizerType type;
	private String textureString;

	public enum VisualizerType {GREEN, YELLOW, RED, BLANK}

	public Visualizer(float x, float y, MapObject copiedType) {

		setX(copiedType.getX());
		setY(copiedType.getY());
		loadTexture(copiedType.getSourceTexture());
		setWidth(copiedType.getWidth());
		setHeight(copiedType.getHeight());

		textureString = copiedType.getSourceTexture();

		setName("Visualizer");
	}

	public void loadTexture(String texture) {
		textureRegion = new TextureRegion((Texture)PixelAssetManager.manager.get(texture));

		// all visualizers are partly transparent
		getColor().a = 0.75f;
	}

	public void draw(Batch batch, float parentAlpha) {

		if (getStage() == null) return;

		// apply color tint effect
		Color c = getColor();
		batch.setColor(c.r, c.g, c.b, c.a);

		if (textureRegion != null && isVisible())
			batch.draw(textureRegion,
				   getX(), getY(), getOriginX(), getOriginY(),
				   getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

		super.draw(batch, parentAlpha);
	}

	public void setType(VisualizerType type) {
		this.type = type;

		switch (type) {
			case GREEN:
				setColor(0,1,0,getColor().a);
				break;
			case RED:
				setColor(1,0,0,getColor().a);
				break;
			case YELLOW:
				setColor(1,1,0,getColor().a);
				break;
			case BLANK:
				setColor(1,1,1,getColor().a);
				break;
		}
	}

	public String getTextureString() {
		return textureString;
	}
}
