package com.pixel.tools;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.pixel.map.object.Cell;
import com.pixel.map.object.MapObject;
import com.pixel.map.object.zoning.ZoneCell;


import java.util.ArrayList;

public class DeleteTool extends MapTool {

	private ArrayList<MapObject> highlightedObjects = new ArrayList<>();
	private ArrayList<Color>	highlightedColors = new ArrayList<>();
	private Rectangle rectangle = new Rectangle();

	//
	// onTouchDown()
	// Called when a touch is first initiated.
	//
	public boolean onTouchDown(float x, float y) {

		if (!super.onTouchDown(x,y)) return false;

		return true;
	}

	//
	// onUpdate()
	// Called on each movement of the current touch
	//
	public boolean onUpdate() {

		if (!super.onUpdate()) return false;

		costLabel.setVisible(false);

		for (int i = 0; i < highlightedObjects.size(); i++) {
			highlightedObjects.get(i).setColor(highlightedColors.get(i));
		}

		highlightedObjects.clear();
		highlightedColors.clear();

		rectangle.x = 0;
		rectangle.y = 0;
		rectangle.height = 0;
		rectangle.width = 0;

		if(currCell == null || begCell == null) return false;

		// calculate our rectangle based on the curr cell
		int width = currCell.getMapPosition().x - begCell.getMapPosition().x;
		int height = currCell.getMapPosition().y - begCell.getMapPosition().y;

		rectangle.x = begCell.getMapPosition().x;
		rectangle.y = begCell.getMapPosition().y;

		if (width < 0) {
			// then we need to flip the rectangle positions
			rectangle.x = currCell.getMapPosition().x;
			width *= -1;
		}

		// we increase this width by one to better match the cursor position
		width += 1;

		if (height < 0) {
			// then we need to flip the rectangle positions
			rectangle.y = currCell.getMapPosition().y;
			height *= -1;
		}

		// we increase this width by one to better match the cursor position
		height += 1;

		rectangle.setWidth(width);
		rectangle.setHeight(height);

		// check every cell at the edge of this rectangle to see if any of them are null
		// Checking the top left corner
		if (gameMap.getCell((int) rectangle.x, (int) rectangle.y) == null) {
			return false;
		}
		// Top right corner
		else if (gameMap.getCell((int) rectangle.x + (int) rectangle.width - 1, (int) rectangle.y) == null) {
			return false;
		}
		// Bottom left corner
		else if (gameMap.getCell((int) rectangle.x, (int) rectangle.y + (int) rectangle.height - 1) == null) {
			return false;
		}
		// Bottom right corner
		else if (gameMap.getCell((int) rectangle.x + (int) rectangle.width - 1, (int) rectangle.y + (int) rectangle.height - 1) == null) {
			return false;
		}

		MapObject mapObject;

		for (int i = 0, mapX = (int) rectangle.x; i < (int) rectangle.width; i++, mapX++) {
			for (int j = 0, mapY = (int) rectangle.y; j < (int) rectangle.height; j++, mapY++) {
				Cell cell = gameMap.getCell(mapX, mapY);

				// highlight the top object in red
				if ((mapObject = cell.getTopObject()) != null) {

					highlightedObjects.add(mapObject);
					highlightedColors.add(new Color(mapObject.getColor()));
					mapObject.setColor(1.0f,0,0,1.0f);
				}
			}
		}

		return true;
	}

	//
	// onTouchUp()
	// Called when the touch is lifted, this ends a complete cycle for this tool object
	//
	public boolean onTouchUp(float x, float y) {

		if (!super.onTouchUp(x,y)) return false;

		for (int i = 0; i < highlightedObjects.size(); i++) {
			highlightedObjects.get(i).setColor(highlightedColors.get(i));
		}

		for(MapObject object : highlightedObjects) {

			Cell cell = gameMap.getCell(object.getMapPosition());
			if (object.hasParent()) {
				object.getParent().removeActor(object);
			}
			cell.removeActor(object);
			object.setDeleted(true);
			object.remove();
		}

		highlightedObjects.clear();
		highlightedColors.clear();

		return true;
	}


	@Override
	public void cancel() {
		super.cancel();

		for (int i = 0; i < highlightedObjects.size(); i++) {
			highlightedObjects.get(i).setColor(highlightedColors.get(i));
		}
	}
}
