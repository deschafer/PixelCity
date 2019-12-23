package com.pixel.game.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pixel.game.PixelCityGame;


public class DesktopLauncher {

	static final float divider = 1.2f;

	public static void main(String[] args) {

		float width = 1920 / divider;
		float height = 1080 / divider;

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.resizable = false;
		config.width = (int)width;
		config.height = (int)height;
		config.title = "PixelCity";

		Game myGame = new PixelCityGame((int)width, (int)height);
		LwjglApplication launcher = new LwjglApplication(myGame, config);
	}
}