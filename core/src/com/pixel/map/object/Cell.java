package com.pixel.map.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.SnapshotArray;
import com.pixel.game.PixelAssetManager;
import com.pixel.map.Map;
import com.pixel.map.MapCoord;
import com.pixel.serialization.CellSerializable;
import com.pixel.serialization.MapObjectSerializable;

import java.io.Serializable;
import java.util.ArrayList;

//
// Cell
// Each cell serves as just a container for additional map objects located at the position of this cell
// in the map.
//

public class Cell extends MapObject {

	private ArrayList<MapObject> occupyingObjects = new ArrayList<>();

	public Cell(float x, float y, float width, float height, MapCoord coord, boolean hasTexture) {
		super(x, y, width,height, coord, "Cell");

		setSize(width, height);

		// This cell has a basic texture within it
		if (hasTexture) {
			Texture texture = PixelAssetManager.manager.get(PixelAssetManager.grass);
			loadTexture(texture, PixelAssetManager.grass);
		} else {
			setBoundaryRectangle();
		}


		// since we want this to be hit with Stage.hit()
		setTouchable(Touchable.enabled);
	}

	// TODO:
	// cell should also override placeOverObject
	// contain a place on top of placementBehavior

	public MapObject getTopObject() {

		if (!occupyingObjects.isEmpty()) {
			return occupyingObjects.get(occupyingObjects.size() - 1);
		}

		if(!hasChildren()) return null;

		SnapshotArray<Actor> actors = getChildren();
		MapObject object = null;

		for(int i = actors.size - 1; i >= 0; i--) {
			Actor actor = actors.get(i);

			if (actor.getName() != "Visualizer") {
				object = (MapObject)actor;
				break;
			}
		}

		return object;
	}

	public boolean containsMapObject() {

		if (!occupyingObjects.isEmpty())
			return true;

		for (Actor actor : getChildren()) {
			if (actor.getName() != "Visualizer") {
				return true;
			}
		}
		return false;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (containsMapObject())
			setOpacity(0.0f);
		else
			setOpacity(1.0f);

		super.draw(batch, parentAlpha);
	}

	@Override
	public void addActor(Actor actor) {
		super.addActor(actor);
	}

	public void addMapObject(MapObject object) {
		addActor(object);

		occupyingObjects.add(object);

		object.validateSources();
	}

	@Override
	public boolean removeActor(Actor actor) {

		// remove from the occupying objects
		occupyingObjects.remove(actor);

		return super.removeActor(actor);
	}

	@Override
	public boolean hasChildren() {
		return super.hasChildren() || !occupyingObjects.isEmpty();
	}

	public void removeOccupyingObject(MapObject object) {
		occupyingObjects.remove(object);
	}

	public void addOccupyingObject(MapObject object) {
		occupyingObjects.add(object);
	}

	@Override
	public MapObjectSerializable getSerializableObject() {

		ArrayList<MapObjectSerializable> childrenSerializables = new ArrayList<>();

		for (Actor actor : getChildren()) {

			// if the object is a MapObject
			if (actor instanceof MapObject) {

				MapObject object = (MapObject)actor;

				childrenSerializables.add(object.getSerializableObject());
			}
		}

		CellSerializable cellSerializable = new CellSerializable();
		cellSerializable.mapPositionX = getMapPosition().x;
		cellSerializable.mapPositionY = getMapPosition().y;
		cellSerializable.x = getX();
		cellSerializable.y = getY();
		cellSerializable.width = getWidth();
		cellSerializable.height = getHeight();
		cellSerializable.children = childrenSerializables;
		cellSerializable.name = getName();

		return cellSerializable;
	}
}
