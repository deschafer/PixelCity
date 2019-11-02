package com.pixel.game;

import com.badlogic.gdx.graphics.Texture;
import com.pixel.scene.GameScene;

public class PixelCityGame extends AbstractGame
{

	public PixelCityGame(int width, int height) {

	}

	public void create()
	{
		// initialize all game assets

		// so far, we need the road texture and we need the grass texture
		PixelAssetManager.manager.load(PixelAssetManager.roadwayNorthSouth, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.roadwayEastWest, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.grass, Texture.class);

		PixelAssetManager.manager.finishLoading();

		GameScene gameScene = GameScene.getInstance();

		super.create();
		setActiveScreen( gameScene );
	}
}