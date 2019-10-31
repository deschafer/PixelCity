package com.pixel.map.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.pixel.behavior.PlacementBehavior;
import com.pixel.map.Map;

import java.util.ArrayList;

public class MapObject extends Group {

	//private ArrayList<ObjectBehavior> behaviors;	// holds a list of added ObjectBehaviors

	// General map-based properties
	protected Map.MapCoord mapPosition;			// holds the current position of this object
	private float timeSpeed;
	protected boolean replaceable = false;		// flag indicating if this object can be replaced with another object
	protected boolean placeable = false;			// indicates if this object can be placed over others

	private Polygon boundaryPolygon;

	protected ArrayList<PlacementBehavior> placementBehaviors;

	// animation-based properties
	private Animation<TextureRegion> animation;
	private float elapsedTime;
	private boolean animationPaused;

	public MapObject(float x, float y, float width, float height, Map.MapCoord coord, String ID)
	{
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		setName(ID);
		mapPosition = coord;
	}

	public void setAnimation(Animation<TextureRegion> anim)
	{
		animation = anim;
		TextureRegion tr = animation.getKeyFrame(0);
		float w = tr.getRegionWidth();
		float h = tr.getRegionHeight();
		setSize( w, h );
		setOrigin( w/2, h/2 );

		if (boundaryPolygon == null)
			setBoundaryRectangle();
	}

	public Animation<TextureRegion> loadAnimationFromFiles(String[] fileNames, float frameDuration, boolean loop)
	{
		int fileCount = fileNames.length;
		Array<TextureRegion> textureArray = new Array<TextureRegion>();

		for (int n = 0; n < fileCount; n++)
		{
			String fileName = fileNames[n];
			Texture texture = new Texture( Gdx.files.internal(fileName) );
			texture.setFilter( Texture.TextureFilter.Linear, Texture.TextureFilter.Linear );
			textureArray.add( new TextureRegion( texture ) );
		}

		Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration, textureArray);

		if (loop)
			anim.setPlayMode(Animation.PlayMode.LOOP);
		else
			anim.setPlayMode(Animation.PlayMode.NORMAL);

		if (animation == null)
			setAnimation(anim);

		return anim;
	}

	public Animation<TextureRegion> loadAnimationFromSheet(String fileName, int rows, int cols, float frameDuration, boolean loop)
	{
		Texture texture = new Texture(Gdx.files.internal(fileName), true);
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

		if (animation == null)
			setAnimation(anim);

		return anim;
	}

	public Animation<TextureRegion> loadTexture(String fileName)
	{
		String[] fileNames = new String[1];
		fileNames[0] = fileName;
		return loadAnimationFromFiles(fileNames, 1, true);
	}

	public void setAnimationPaused(boolean pause)
	{
		animationPaused = pause;
	}

	public boolean isAnimationFinished()
	{
		return animation.isAnimationFinished(elapsedTime);
	}

	public void setOpacity(float opacity)
	{
		this.getColor().a = opacity;
	}

	public void setBoundaryRectangle()
	{
		float w = getWidth();
		float h = getHeight();

		float[] vertices = {0,0, w,0, w,h, 0,h};
		boundaryPolygon = new Polygon(vertices);
	}

	public Polygon getBoundaryPolygon() {
		boundaryPolygon.setPosition( getX(), getY() );
		boundaryPolygon.setOrigin( getOriginX(), getOriginY() );
		boundaryPolygon.setRotation( getRotation() );
		boundaryPolygon.setScale( getScaleX(), getScaleY() );
		return boundaryPolygon;
	}

	public boolean overlaps(MapObject other)
	{
		Polygon poly1 = this.getBoundaryPolygon();
		Polygon poly2 = other.getBoundaryPolygon();

		// initial test to improve performance
		if ( !poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()) )
			return false;

		return Intersector.overlapConvexPolygons( poly1, poly2 );
	}

	public Map.MapCoord getMapPosition() {
		return mapPosition;
	}

	public void setMapPosition(int x, int y) {
		mapPosition.x = x;
		mapPosition.y = y;
	}

	public boolean isReplaceable() {
		return replaceable;
	}

	public boolean isPlaceable() {
		return placeable;
	}

	//
	// placeOverObject
	// This function is called when an object is supposed to be placed on
	// the location of this object.
	//
	public boolean placeOverObject(MapObject object) {

		if(replaceable && !placementBehaviors.isEmpty()) {

			for(PlacementBehavior behavior : placementBehaviors) {
				behavior.setPlacement(object);
				behavior.act();
			}

			return true;
		}
		else return false;
	}

	public void act(float dt)
	{

		// TODO: Do something with time progression here

		super.act( dt );

		if (!animationPaused)
			elapsedTime += dt;
	}

	public void draw(Batch batch, float parentAlpha) {
		// We should only draw if we are in the camera of this object's scene
		Frustum frustum = getStage().getCamera().frustum;

		// Check if any corner of this given object is within the camera's view
		if (frustum.pointInFrustum(getX(), getY(), 0) ||
			   frustum.pointInFrustum(getX() + getWidth(), getY(), 0) ||
			   frustum.pointInFrustum(getX(), getY() + getHeight(), 0) ||
			   frustum.pointInFrustum(getX() + getWidth(), getY() + getHeight(), 0)) {

			// apply color tint effect
			Color c = getColor();
			batch.setColor(c.r, c.g, c.b, c.a);

			if (animation != null && isVisible())
				batch.draw(animation.getKeyFrame(elapsedTime),
					   getX(), getY(), getOriginX(), getOriginY(),
					   getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

			super.draw(batch, parentAlpha);
		}
	}

	public MapObject copy() {
		return null;
	}
}
