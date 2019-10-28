package com.pixel.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.pixel.scene.Scene;

// singleton class structure
public abstract class AbstractGame extends Game
{

	private static AbstractGame game;

	public AbstractGame()
	{
		game = this;
	}

	/**
	 *  Called when game is initialized,
	 *  after Gdx.input and other objects have been initialized.
	 */
	public void create()
	{
		// prepare for multiple classes/stages/actors to receive discrete input
		InputMultiplexer im = new InputMultiplexer();
		Gdx.input.setInputProcessor( im );
	}

	/**
	 *  Used to switch screens while game is running.
	 *  Method is static to simplify usage.
	 */
	public static void setActiveScreen(Scene s)
	{
		game.setScreen(s);
	}
}