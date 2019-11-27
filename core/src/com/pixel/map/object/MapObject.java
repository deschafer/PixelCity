package com.pixel.map.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.pixel.behavior.PlacementBehavior;
import com.pixel.city.FinancialManager;
import com.pixel.city.Financials.Source;
import com.pixel.map.Map;
import com.sun.org.apache.bcel.internal.generic.NOP;

import java.util.ArrayList;

public class MapObject extends Group {

	//private ArrayList<ObjectBehavior> behaviors;	// holds a list of added ObjectBehaviors

	// General map-based properties
	protected Map.MapCoord mapPosition;			// holds the current position of this object
	private float timeSpeed;
	protected boolean replaceable = false;		// flag indicating if this object can be replaced with another object

	protected Polygon boundaryPolygon;

	protected ArrayList<PlacementBehavior> placementBehaviors;

	// animation-based properties
	private Animation<TextureRegion> animation;
	private String sourceTexture;
	private float elapsedTime;
	private boolean animationPaused;

	protected boolean deleted = false;

	protected boolean financialInfluence = false;	// indicates whether this object has a source over time
	protected float placedownCost = 0.0f;		// the cost for this object to be placed
	protected ArrayList<Source> sources;			// all the sources associated with this object
	private boolean prototypeObject = false;		// this means this object is only used for copying, never actually added to the map


	public MapObject(float x, float y, float width, float height, Map.MapCoord coord, String ID)
	{
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		setName(ID);
		mapPosition = coord;
		placementBehaviors = new ArrayList<>();
		sources = new ArrayList<>();
		setTouchable(Touchable.disabled);
	}

	public void setAnimation(Animation<TextureRegion> anim) {
		animation = anim;
		TextureRegion tr = animation.getKeyFrame(0);
		setOrigin(getWidth() / 2, getHeight() / 2);

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

	public Animation<TextureRegion> loadAnimationFromFiles(Texture[] textures, float frameDuration, boolean loop) {
		int fileCount = textures.length;
		Array<TextureRegion> textureArray = new Array<TextureRegion>();

		for (int n = 0; n < fileCount; n++) {
			Texture texture = textures[n];
			texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			textureArray.add(new TextureRegion(texture));
		}

		Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration, textureArray);

		if (loop)
			anim.setPlayMode(Animation.PlayMode.LOOP);
		else
			anim.setPlayMode(Animation.PlayMode.NORMAL);

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
		sourceTexture = fileName;
		String[] fileNames = new String[1];
		fileNames[0] = fileName;
		return loadAnimationFromFiles(fileNames, 1, true);
	}

	public Animation<TextureRegion> loadTexture(Texture texture, String fileName)
	{
		sourceTexture = fileName;
		Texture[] textures = new Texture[1];
		textures[0] = texture;
		return loadAnimationFromFiles(textures, 1, true);
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

	public String getSourceTexture() { return sourceTexture; }

	public void setMapPosition(int x, int y) {
		mapPosition.x = x;
		mapPosition.y = y;
	}

	public boolean isReplaceable() {
		return replaceable;
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

		if (getStage() == null) return;

		// apply color tint effect
		Color c = getColor();
		batch.setColor(c.r, c.g, c.b, c.a);

		if (animation != null && isVisible())
			batch.draw(animation.getKeyFrame(elapsedTime),
				   getX(), getY(), getOriginX(), getOriginY(),
				   getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

		super.draw(batch, parentAlpha);

	}

	public MapObject copy() {
		return null;
	}

	@Override
	public boolean remove() {

		if(!sources.isEmpty()) {
			for(Source source : sources)
				source.setInvalid();
			sources.clear();
		}
		return super.remove();
	}

	protected void addSource(Source source) {

		// we should only add a source if this object is attached to the map
		// so if it has a parent, or a cell
		sources.add(source);
		financialInfluence = true;
	}

	//
	// validateSources()
	// Indicates that the financial sources stored in this object should be passed to the financial manager
	//
	public void validateSources() {
		for(Source source : sources) {
			FinancialManager.getInstance().addSource(source);
		}
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public void setPrototypeObject(boolean prototypeObject) {
		this.prototypeObject = prototypeObject;
	}

	public boolean isPrototypeObject() {
		return prototypeObject;
	}

	public float getPlacedownCost() {
		return placedownCost;
	}
}
