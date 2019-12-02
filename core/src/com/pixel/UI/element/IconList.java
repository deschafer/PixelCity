package com.pixel.UI.element;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;

public class IconList extends Group {

	private ArrayList<Icon> icons = new ArrayList<>();
	private float padding;
	private float initialOffset = 0;

	public IconList(float x, float y, float padding) {
		setPosition(x,y);
		this.padding = padding;
		setVisible(false);
		setName("IListUI");
	}

	public IconList(float x, float y, float padding, float initialOffset) {
		setPosition(x,y);
		this.padding = padding;
		this.initialOffset = initialOffset;
		setVisible(false);
	}

	public IconList getParentList() {
		// need to get the parent of this list itself
		if (hasParent()) {

			if (getParent().getName() != null && getParent().getName().contains("Icon")) {
				return ((Icon)getParent()).getParentList();
			}
		}
		return null;
	}

	public void addIcon(Icon icon) {
		icons.add(icon);
		addActor(icon);

		formatIcon(icon);

		if (icon.getWidth() > getWidth()) {
			setWidth(icon.getWidth());
		}
	}

	private void formatIcon(Icon addedIcon) {

		// If this is the first icon, then we will have a special case
		if (icons.size() == 1) {
			// then we set this icon's postion
			// move this image downward
			addedIcon.setY(-addedIcon.getHeight() - initialOffset);
		} else {

			// since this is the newly added, last icon, we can grab the second to last and use that position
			Icon iconAboveAddedIcon = icons.get(icons.size() - 2);
			addedIcon.setY(iconAboveAddedIcon.getY() - padding - addedIcon.getHeight());
		}
	}

	public float getPadding() {
		return padding;
	}
}
