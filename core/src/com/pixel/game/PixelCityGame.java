package com.pixel.game;

import com.pixel.scene.GameScene;

public class PixelCityGame extends AbstractGame
{

	public PixelCityGame(int width, int height) {

	}

	public void create()
	{
		GameScene gameScene = GameScene.getInstance();

		super.create();
		setActiveScreen( gameScene );
	}
}