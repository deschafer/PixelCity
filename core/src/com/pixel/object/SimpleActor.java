package com.pixel.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class SimpleActor extends Actor {

	private Animation<TextureRegion> animation;

	public SimpleActor(float x, float y, float width, float height, String name, String textureName) {
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		setOrigin(width / 2, height / 2);
		setName(name);

		loadTexture(textureName);

	}

	private void loadTexture(String fileName)
	{
		Array<TextureRegion> textureArray = new Array<TextureRegion>();

		Texture texture = new Texture( Gdx.files.internal(fileName) );
		texture.setFilter( Texture.TextureFilter.Linear, Texture.TextureFilter.Linear );
		textureArray.add( new TextureRegion( texture ) );

		animation = new Animation<TextureRegion>(0, textureArray);

		TextureRegion tr = animation.getKeyFrame(0);
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{

		// apply color tint effect
		Color c = getColor();
		batch.setColor(c.r, c.g, c.b, c.a);

		if ( animation != null && isVisible() )
			batch.draw( animation.getKeyFrame(0),
				   getX(), getY(), getOriginX(), getOriginY(),
				   getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation() );

		super.draw( batch, parentAlpha );
	}

}
