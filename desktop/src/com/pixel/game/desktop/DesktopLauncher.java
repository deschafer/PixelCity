package com.pixel.game.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.pixel.game.PixelCityGame;


public class DesktopLauncher {

	static final int divider = 2;

	public static void main(String[] args) {
		Game myGame = new PixelCityGame(1920 / divider, 1080 / divider);
		LwjglApplication launcher = new LwjglApplication(myGame, "PixelCity", 1920 / divider, 1080 / divider);
	}
}