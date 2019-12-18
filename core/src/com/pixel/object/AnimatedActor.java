package com.pixel.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.pixel.game.PixelAssetManager;

import java.util.ArrayList;

public class AnimatedActor extends Actor {

	private Animation<TextureRegion> animation;
	private Polygon boundaryPolygon;
	private float elapsedTime = 0;
	private boolean animationPaused = false;

	public AnimatedActor(float x, float y, float width, float height, String name, ArrayList<String> textureNames,
					 float frameDuration, boolean loop) {
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		setOrigin(width / 2, height / 2);
		setName(name);

		loadAnimationFromFiles(textureNames, frameDuration, loop);
	}

	public AnimatedActor(float x, float y, float width, float height, String name, String[] textureNames,
					 float frameDuration, boolean loop) {
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		setOrigin(width / 2, height / 2);
		setName(name);

		loadAnimationFromFiles(textureNames, frameDuration, loop);
	}

	public AnimatedActor(float x, float y, float width, float height, String name, String textureSheetName,
					 float frameDuration, boolean loop, int rows, int columns) {
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		setOrigin(width / 2, height / 2);
		setName(name);

		loadAnimationFromSheet(textureSheetName, rows, columns, frameDuration, loop);
	}

	public Animation<TextureRegion> loadAnimationFromSheet(String fileName, int rows, int cols, float frameDuration, boolean loop)
	{
		Texture texture = PixelAssetManager.manager.get(PixelAssetManager.explosion);
		texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		int frameWidth = texture.getWidth() / cols;
		int frameHeight = texture.getHeight() / rows;

		TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);

		Array<TextureRegion> textureArray = new Array<TextureRegion>();

		for (int r = 0; r < rows; r++)
			for (int c = 0; c < cols; c++)
				textureArray.add( temp[r][c] );

		Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration, textureArray);

		if (loop)
			anim.setPlayMode(Animation.PlayMode.LOOP);
		else
			anim.setPlayMode(Animation.PlayMode.NORMAL);

		if (animation == null) {

			animation = anim;
			TextureRegion tr = animation.getKeyFrame(0);

			float w = getWidth();
			float h = getHeight();
			float[] vertices = {0,0, w,0, w,h, 0,h};
			boundaryPolygon = new Polygon(vertices);
		}

		return anim;
	}


	public Animation<TextureRegion> loadAnimationFromFiles(ArrayList<String> textureNames, float frameDuration, boolean loop) {

		Array<TextureRegion> textureArray = new Array<TextureRegion>();

		for (String string  : textureNames) {
			Texture texture = PixelAssetManager.manager.get(string);
			texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			textureArray.add(new TextureRegion(texture));
		}

		Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration, textureArray);

		anim.setPlayMode(loop ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL);

		if (animation == null) {

			animation = anim;
			TextureRegion tr = animation.getKeyFrame(0);

			float w = getWidth();
			float h = getHeight();
			float[] vertices = {0,0, w,0, w,h, 0,h};
			boundaryPolygon = new Polygon(vertices);
		}

		return anim;
	}

	public Animation<TextureRegion> loadAnimationFromFiles(String[] textureNames, float frameDuration, boolean loop) {

		Array<TextureRegion> textureArray = new Array<TextureRegion>();

		for (String string  : textureNames) {
			Texture texture = PixelAssetManager.manager.get(string);
			texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			textureArray.add(new TextureRegion(texture));
		}

		Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration, textureArray);

		anim.setPlayMode(loop ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL);

		if (animation == null) {

			animation = anim;
			TextureRegion tr = animation.getKeyFrame(0);

			float w = getWidth();
			float h = getHeight();
			float[] vertices = {0,0, w,0, w,h, 0,h};
			boundaryPolygon = new Polygon(vertices);
		}

		return anim;
	}

	public void act(float dt)
	{
		super.act( dt );

		if (!animationPaused)
			elapsedTime += dt;
	}

	public void draw(Batch batch, float parentAlpha)
	{

		// apply color tint effect
		Color c = getColor();
		batch.setColor(c.r, c.g, c.b, c.a);

		if ( animation != null && isVisible() )
			batch.draw( animation.getKeyFrame(elapsedTime),
				   getX(), getY(), getOriginX(), getOriginY(),
				   getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation() );

		super.draw( batch, parentAlpha );
	}
}
