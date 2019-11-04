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
		PixelAssetManager.manager.load(PixelAssetManager.roadwayEndWest, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.roadwayEndEast, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.roadwayEndSouth, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.roadwayEndNorth, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.cornerEastNorth, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.cornerNorthEast, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.cornerNorthWest, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.cornerWestNorth, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.intersectionFour, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.intersectionThreeSouth, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.intersectionThreeEast, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.intersectionThreeWest, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.intersectionThreeNorth, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialZoning, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialZoning, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.officeZoning, Texture.class);


		PixelAssetManager.manager.finishLoading();

		GameScene gameScene = GameScene.getInstance();

		super.create();
		setActiveScreen( gameScene );
	}
}