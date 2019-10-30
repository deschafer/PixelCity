package com.pixel.tools;

import com.badlogic.gdx.math.Vector2;

public abstract class Tool {

	private Vector2 begPoint = new Vector2();
	private Vector2 currentPoint = new Vector2();
	private Vector2 endPoint = new Vector2();

	private enum TouchState {NO_TOUCH, TOUCH}
	private TouchState currentState = TouchState.NO_TOUCH;

	//
	// onTouchDown()
	// Called when a touch is first initiated.
	//
	public boolean onTouchDown(float x, float y) {

		// set our touch state
		currentState = TouchState.TOUCH;

		// save this beginning point
		begPoint.x = x;
		begPoint.y = y;

		return true;
	}

	//
	// onTouchMove()
	// Called on each movement of the current touch
	//
	public boolean onTouchMove(float x, float y) {

		// continue only if we have already touched the screen
		if(currentState != TouchState.TOUCH) {
			return false;
		}

		currentPoint.x = x;
		currentPoint.y = y;

		return true;
	}

	//
	// onTouchUp()
	// Called when the touch is lifted, this ends a complete cycle for this tool object
	//
	public boolean onTouchUp(float x, float y) {

		if (currentState != TouchState.TOUCH) {
			return false;
		}

		// clear our touch state
		currentState = TouchState.NO_TOUCH;

		endPoint.x = x;
		endPoint.y = y;

		return true;
	}

	//
	// cancel()
	// Call to cancel this tool when it has been started by a touch
	//
	public void cancel() {
		currentState = TouchState.NO_TOUCH;
	}

}
