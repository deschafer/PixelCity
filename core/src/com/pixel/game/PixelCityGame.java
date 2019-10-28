package com.pixel.game;

import com.pixel.scene.GameScene;

public class PixelCityGame extends AbstractGame
{

	public PixelCityGame(int width, int height) {

	}

	public void create()
	{


		super.create();
		setActiveScreen( new GameScene() );
	}
}