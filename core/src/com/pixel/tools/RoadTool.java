package com.pixel.tools;

public class RoadTool extends MapTool {
	@Override
	public boolean onTouchDown(float x, float y) {
		if(!super.onTouchDown(x, y)) {
			return false;
		}

		// for now, just mark this cell as black
		begCell.setColor(0,0,0,1);

		return true;
	}

	@Override
	public boolean onTouchMove(float x, float y) {
		if (!super.onTouchMove(x, y)) {
			return false;
		}

		currCell.setColor(0,0,0,1);

		return true;
	}

	@Override
	public boolean onTouchUp(float x, float y) {
		if (!super.onTouchUp(x, y)) {
			return false;
		}

		return true;
	}
}
